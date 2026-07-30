package com.intern.booking_event.service.implement;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.intern.booking_event.model.entity.Booking;
import com.intern.booking_event.model.entity.BookingItem;
import com.intern.booking_event.service.PdfGeneratorService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfGeneratorServiceImp implements PdfGeneratorService {

    @Override
    public ByteArrayInputStream generateBookingInvoice(Booking booking) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Khởi tạo các Font chữ cần thiết
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.BLACK);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(0, 102, 204));
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);

            // ==================== TRANG 1: HÓA ĐƠN THANH TOÁN (INVOICE) ====================
            Paragraph title = new Paragraph("INVOICE / RECEIPT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(25);
            document.add(title);

            // Thông tin chung của Đơn hàng và Khách hàng
            document.add(new Paragraph("Customer Name: " + booking.getCustomer().getName(), boldFont));
            document.add(new Paragraph("Booking Reference: " + booking.getReference(), normalFont));
            document.add(new Paragraph("Created At: " + booking.getCreatedAt().toString(), normalFont));
            document.add(new Paragraph("Status: " + booking.getStatus().name(), boldFont));
            document.add(new Paragraph(" ", normalFont)); // Tạo dòng trống

            // Tạo bảng chi tiết danh sách vé đặt (4 cột)
            PdfPTable table = new PdfPTable(4); 
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setWidths(new int[]{4, 2, 2, 2}); // Tỉ lệ độ rộng các cột

            // Thêm Headers cho bảng
            String[] headers = {"Ticket Type", "Price", "Quantity", "Total"};
            for (String headerText : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(headerText, headerFont));
                cell.setBackgroundColor(new Color(0, 102, 204));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                table.addCell(cell);
            }

            // Đổ dữ liệu Booking Items vào từng hàng của bảng
            for (BookingItem item : booking.getItems()) {
                table.addCell(new PdfPCell(new Phrase(item.getTicketType().getName(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(item.getUnitPrice().toString() + " VND", normalFont)));
                
                PdfPCell qtyCell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), normalFont));
                qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(qtyCell);

                double lineTotal = item.getUnitPrice().doubleValue() * item.getQuantity();
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f VND", lineTotal), normalFont)));
            }
            document.add(table);

            // Hiển thị tổng thanh toán
            Paragraph totalAmount = new Paragraph("Total Amount Paid: " + booking.getTotalAmount().toString() + " VND", boldFont);
            totalAmount.setAlignment(Element.ALIGN_RIGHT);
            totalAmount.setSpacingBefore(15);
            document.add(totalAmount);

            // ==================== NGẮT TRANG SANG TRANG 2 ====================
            document.newPage();

            // ==================== TRANG 2: THÔNG TIN CHI TIẾT SỰ KIỆN ====================
            Paragraph eventHeader = new Paragraph("EVENT INFORMATION & GUIDE", titleFont);
            eventHeader.setAlignment(Element.ALIGN_CENTER);
            eventHeader.setSpacingAfter(25);
            document.add(eventHeader);

            // Lấy thông tin Event từ mặt vé đầu tiên nằm trong Booking
            if (booking.getItems() != null && !booking.getItems().isEmpty()) {
                var firstItem = booking.getItems().get(0);
                var event = firstItem.getTicketType().getEvent(); 

                if (event != null) {
                    document.add(new Paragraph("Event Details", sectionFont));
                    document.add(new Paragraph("--------------------------------------------------------------------------------------------------------", normalFont));
                    document.add(new Paragraph(" ", normalFont));

                    // Hiển thị thông tin Event (Tương tự như cấu trúc EventResponse)
                    document.add(new Paragraph("Event Name: " + event.getTitle(), boldFont));
                    document.add(new Paragraph("Category: " + event.getCategory(), normalFont));
                    document.add(new Paragraph("Organizer: " + event.getOrganizer(), normalFont));
                    document.add(new Paragraph("Venue / Location: " + event.getVenue(), normalFont));
                    document.add(new Paragraph("Start Time: " + event.getStartTime().toString(), boldFont));
                    document.add(new Paragraph(" ", normalFont));

                    document.add(new Paragraph("Description:", boldFont));
                    Paragraph desc = new Paragraph(event.getDescription(), normalFont);
                    desc.setSpacingBefore(5);
                    desc.setLeading(14f); // Giãn dòng cho mô tả dài dễ đọc
                    document.add(desc);
                    
                    document.add(new Paragraph(" ", normalFont));
                    document.add(new Paragraph("--------------------------------------------------------------------------------------------------------", normalFont));
                    document.add(new Paragraph("Please note: Present this confirmation document (or the digital QR code) at the event entrance for validation.", boldFont));
                } else {
                    document.add(new Paragraph("Event details could not be loaded.", normalFont));
                }
            } else {
                document.add(new Paragraph("No booked items found associated with this booking.", normalFont));
            }

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Lỗi trong quá trình tạo tài liệu PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}