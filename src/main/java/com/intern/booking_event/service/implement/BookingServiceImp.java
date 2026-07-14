package com.intern.booking_event.service.implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.booking_event.constant.BookingStatus;
import com.intern.booking_event.exception.ResourceNotFoundException;
import com.intern.booking_event.mapper.BookingMapper;
import com.intern.booking_event.model.dto.response.BookingResponse;
import com.intern.booking_event.model.entity.Booking;
import com.intern.booking_event.repository.BookingRepository;
import com.intern.booking_event.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse payBooking(Long id){
        int updatedToPaid = bookingRepository.updateStatus(id, BookingStatus.PENDING, BookingStatus.PAID);
        if (updatedToPaid == 0) {
            throw new IllegalStateException("Đơn hàng không tồn tại hoặc đã thanh toán thành công");
        }
        int updatedToConfirmed = bookingRepository.updateStatus(id, BookingStatus.PAID, BookingStatus.CONFIRMED);
        if (updatedToConfirmed == 0) {
            throw new IllegalStateException("Xác nhận thất bại: Có lỗi xảy ra trong quá trình hoàn tất đơn hàng.");
        }

        Booking confirmedBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng sau khi cập nhật. ID: " + id));

        return bookingMapper.toBookingResponse(confirmedBooking);
    }


}
