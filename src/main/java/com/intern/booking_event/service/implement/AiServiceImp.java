package com.intern.booking_event.service.implement;

import com.intern.booking_event.model.dto.request.AiRequest;
import com.intern.booking_event.model.dto.response.AiResponse;
import com.intern.booking_event.model.dto.response.EventResponse;
import com.intern.booking_event.model.entity.Customer;
import com.intern.booking_event.model.entity.Event;
import com.intern.booking_event.repository.CustomerRepository;
import com.intern.booking_event.repository.EventRepository;
import com.intern.booking_event.service.AIService;
import com.intern.booking_event.service.EventService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AiServiceImp implements AIService {

    private final ChatClient chatClient;
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final EventService eventService;

    public AiServiceImp(ChatClient.Builder chatClientBuilder, EventRepository eventRepository, CustomerRepository customerRepository, CustomerRepository customerRepository1, EventService eventService) {
        this.chatClient = chatClientBuilder.build();
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository1;
        this.eventService = eventService;
    }

    @Override
    @Transactional(readOnly = true)
    public AiResponse searchEventWithAi(AiRequest request) {
        String eventContext = buildEventContext();
        String customerInfo = "";
        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
            if (customer != null) {
                customerInfo = String.format("%s", customer.getName());
            }
        }

        String sysPrompt = """
                Bạn là Trợ lý AI chính thức của dự án "Online Event Booking" (Hệ thống Đặt vé Sự kiện Trực tuyến).
                Tên dự án của bạn là: Online Event Booking.

                THÔNG TIN NGƯỜI DÙNG: 
                %s 
                Dưới đây là DỮ LIỆU THỰC TẾ các sự kiện hiện có trong cơ sở dữ liệu:
                %s
                
                QUY TẮC PHẢN HỒI NGHIÊM NGẶT:
                1. GIỚI HẠN PHẠM VI (QUAN TRỌNG NHẤT): Bạn CHỈ ĐƯỢC PHÉP trả lời các câu hỏi liên quan đến dự án Online Event Booking, tìm kiếm sự kiện và đặt vé. Nếu người dùng hỏi các câu hỏi không liên quan đến dự án (ví dụ: công thức nấu ăn, thời tiết, giải toán, lập trình ngoài, chuyện phiếm ngoài đời sống...), hãy từ chối lịch sự: "Xin lỗi, tôi là trợ lý AI của dự án Online Event Booking nên chỉ có thể hỗ trợ các thông tin liên quan đến sự kiện và đặt vé."
                2. Nếu người dùng hỏi về tên dự án, tên hệ thống hoặc bạn là ai: Hãy khẳng định rõ ràng tên dự án là "Online Event Booking".
                3. Nếu người dùng hỏi tìm kiếm sự kiện: Hãy thông báo số lượng sự kiện phù hợp tìm thấy và liệt kê từng sự kiện (Tên sự kiện, Địa điểm/Nơi tổ chức, Thời gian).
                4. Nếu có tên khách hàng ở phần THÔNG TIN NGƯỜI DÙNG, hãy gửi lời chào kèm theo tên của họ
                5. Nếu không tìm thấy sự kiện phù hợp, hãy thông báo lịch sự.
                """.formatted(customerInfo,eventContext);

        String aiResult = chatClient.prompt()
                .system(sysPrompt)
                .user(request.getMessage())
                .call()
                .content();


        return AiResponse.builder()
                .response(aiResult)
                .build();
    }

    @Override
    public AiResponse askEventIdWithAi(Long eventId, AiRequest request) {
        EventResponse event = eventService.getEventById(eventId);

        String eventContext = String.format("""
                Tên sự kiện: %s
                Thể loại: %s
                Địa điểm tổ chức: %s
                Thời gian bắt đầu: %s
                Nhà tổ chức: %s
                Mô tả: %s
                """,
                event.getTitle(),
                event.getCategory(),
                event.getVenue(),
                event.getStartTime(),
                event.getOrganizer(),
                event.getDescription() != null ? event.getDescription() : "Không có"
                );
        String sysPrompt = """
                Bạn là Trợ lý AI thông minh phụ trách giải đáp thắc mắc cho sự kiện cụ thể "%s" (thuộc hệ thống Online Event Booking).
                            THÔNG TIN CHI TIẾT VỀ SỰ KIỆN NÀY:
                            %s
                            HƯỚNG DẪN PHẢN HỒI:
                            1. Dựa vào THÔNG TIN CHI TIẾT trên để giải đáp thắc mắc của người dùng về sự kiện này.
                            2. Trả lời thân thiện, chính xác, ngắn gọn và xuống dòng rõ ràng.
                            3. Nếu câu hỏi của người dùng không liên quan đến sự kiện này, hãy nhắc nhở lịch sự.
                """.formatted(event.getTitle(), eventContext);
        String aiResult = chatClient.prompt()
                .system(sysPrompt)
                .user(request.getMessage())
                .call()
                .content();
        return AiResponse.builder()
                .response(aiResult)
                .build();
    }

    private String buildEventContext() {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            return "Hiện tại chưa có sự kiện nào trong hệ thống.";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Event event : events) {
            stringBuilder.append(String.format("- [ID: %d] Tên sự kiện: %s\n, Thể loại: %s\n, Địa điểm: %s\n, Thời gian: %s\n, Ban tổ chức: %s\n, Mô tả: %s\n\n",
                    event.getId(), event.getTitle(), event.getCategory(),
                    event.getVenue(), event.getStartTime(), event.getOrganizer(),
                    event.getDescription() != null ? event.getDescription() : "Không có"));
        }
        return stringBuilder.toString();
    }
}
