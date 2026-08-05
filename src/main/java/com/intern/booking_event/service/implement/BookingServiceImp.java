package com.intern.booking_event.service.implement;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.booking_event.constant.BookingStatus;
import com.intern.booking_event.exception.ResourceNotFoundException;
import com.intern.booking_event.mapper.BookingMapper;
import com.intern.booking_event.model.dto.request.BookingRequest;
import com.intern.booking_event.model.dto.response.BookingResponse;
import com.intern.booking_event.model.entity.Booking;
import com.intern.booking_event.model.entity.BookingItem;
import com.intern.booking_event.model.entity.Customer;
import com.intern.booking_event.model.entity.TicketType;
import com.intern.booking_event.repository.BookingRepository;
import com.intern.booking_event.repository.CustomerRepository;
import com.intern.booking_event.repository.TicketRepository;
import com.intern.booking_event.service.BookingService;
import com.intern.booking_event.service.RefundService;
import com.intern.booking_event.service.PdfGeneratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final RefundService refundService;
    private final BookingMapper bookingMapper;
    private final PdfGeneratorService pdfGeneratorService;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Kiểm tra sự tồn tại của Customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + request.getCustomerId()));

        // 2. Tạo Booking mới ở trạng thái PENDING
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.PENDING);

        List<BookingItem> bookingItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Kiểm tra kho vé và trừ kho (ItemRequest từ BookingRequest)
        for (BookingRequest.ItemRequest itemReq : request.getItems()) {
            TicketType ticketType = ticketRepository.findById(itemReq.getTicketTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại vé với ID: " + itemReq.getTicketTypeId()));

            int availableQuantity = ticketType.getTotalQuantity() - ticketType.getSoldQuantity();
            if (availableQuantity < itemReq.getQuantity()) {
                throw new RuntimeException("Loại vé '" + ticketType.getName() + "' không đủ số lượng khả dụng! Còn lại: " + availableQuantity);
            }

            // Trừ kho vé
            ticketType.setSoldQuantity(ticketType.getSoldQuantity() + itemReq.getQuantity());
            ticketRepository.save(ticketType);

            // Khởi tạo BookingItem
            BookingItem bookingItem = new BookingItem();
            bookingItem.setBooking(booking);
            bookingItem.setTicketType(ticketType);
            bookingItem.setQuantity(itemReq.getQuantity());
            bookingItem.setUnitPrice(ticketType.getPrice());

            bookingItems.add(bookingItem);

            // Tính toán tổng tiền
            BigDecimal itemTotal = ticketType.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        booking.setItems(bookingItems);
        booking.setTotalAmount(totalAmount);

        // 4. Lưu đơn hàng
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponse(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponse payBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + id));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Đơn hàng phải ở trạng thái PENDING mới có thể tiến hành thanh toán.");
        }

        // Thực hiện cập nhật an toàn PENDING -> PAID
        int updated = bookingRepository.updateStatus(id, BookingStatus.PENDING, BookingStatus.PAID);
        if (updated == 0) {
            throw new IllegalStateException("Thanh toán thất bại: Trạng thái đơn hàng đã bị thay đổi bởi luồng khác.");
        }

        log.info("[Pay-Booking] Đơn hàng ID: {} đã chuyển trạng thái thành công sang PAID.", id);

        Booking paidBooking = bookingRepository.findById(id).get();
        return bookingMapper.toBookingResponse(paidBooking);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + id));

        // Nếu trạng thái đã là CONFIRMED -> Chặn đứng hoàn toàn, khóa cứng không cho hủy
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Không thể hủy hoặc hoàn tiền cho đơn hàng đã được CONFIRMED.");
        }

        BookingStatus originalStatus = booking.getStatus();

        // Cập nhật trạng thái sang CANCELLED (Chỉ cho phép nếu trạng thái gốc là PENDING hoặc PAID)
        List<BookingStatus> allowedStatuses = Arrays.asList(BookingStatus.PENDING, BookingStatus.PAID);
        int updated = bookingRepository.updateStatusFromAllowed(id, allowedStatuses, BookingStatus.CANCELLED);

        if (updated == 0) {
            throw new IllegalStateException("Hủy đơn thất bại: Trạng thái đơn hàng không hợp lệ để hủy.");
        }

        // Hoàn trả số lượng vé của từng item về kho cho sự kiện
        for (BookingItem item : booking.getItems()) {
            Long ticketTypeId = item.getTicketType().getId();
            int quantityToRefund = item.getQuantity();
            ticketRepository.refundTicketQuantity(ticketTypeId, quantityToRefund);
        }

        // Chỉ kích hoạt lệnh hoàn tiền nếu trạng thái trước khi hủy của khách là PAID
        if (originalStatus == BookingStatus.PAID) {
            log.info("[Cancel-Refund] Kích hoạt tiến trình hoàn tiền cho đơn hàng đã thanh toán (PAID) ID: {}", id);
            refundService.processRefund(booking);
        }

        log.info("[Cancel] Đơn hàng ID: {} đã được hủy thành công.", id);

        Booking cancelledBooking = bookingRepository.findById(id).get();
        return bookingMapper.toBookingResponse(cancelledBooking);
    }

    
    @Override
    @Transactional
    public ByteArrayInputStream confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + id));

        // KHÓA CỨNG: Chỉ cho phép xác nhận khi trạng thái hiện tại là PAID (Đã hoàn thành thanh toán ở bước trước)
        if (booking.getStatus() != BookingStatus.PAID) {
            throw new IllegalStateException("Không thể xác nhận đơn hàng. Đơn hàng bắt buộc phải ở trạng thái PAID. Trạng thái hiện tại: " + booking.getStatus());
        }

        // Cập nhật trạng thái từ PAID sang CONFIRMED
        int updated = bookingRepository.updateStatus(id, BookingStatus.PAID, BookingStatus.CONFIRMED);
        if (updated == 0) {
            throw new IllegalStateException("Xác nhận thất bại: Trạng thái đơn hàng đã thay đổi.");
        }

        log.info("[Confirmation] Đơn hàng ID: {} đã chuyển sang trạng thái khóa cứng CONFIRMED.", id);

        // Đọc dữ liệu mới nhất đã CONFIRMED để xuất hóa đơn PDF 2 trang
        Booking confirmedBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể nạp dữ liệu đơn hàng sau khi CONFIRMED. ID: " + id));

        // Sinh file PDF hóa đơn
        return pdfGeneratorService.generateBookingInvoice(confirmedBooking);
    }
}