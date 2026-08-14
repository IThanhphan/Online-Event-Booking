package com.intern.booking_event.service.implement;

import com.intern.booking_event.model.dto.request.AiRequest;
import com.intern.booking_event.model.dto.response.AiResponse;
import com.intern.booking_event.model.dto.response.AiSearchEventResponse;
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
                customerInfo = customer.getName();
            }
        }

        String sysPrompt = """
                Bạn là Trợ lý AI chính thức của "Online Event Booking".
                NGƯỜI DÙNG: %s | DỮ LIỆU CƠ SỞ DỮ LIỆU: %s

                QUY TẮC PHẢN HỒI (BẮT BUỘC CHỈ TRẢ VỀ CÚ PHÁP JSON HỢP LỆ):
                1. LỌC THEO THỂ LOẠI (CATEGORY):
                   - "âm nhạc/music": CHỈ lấy sự kiện có category là MUSIC. (KHÔNG lấy ART/FOOD dù địa điểm ghi Opera House).
                   - "đồ ăn/food": CHỈ lấy sự kiện có category là FOOD.
                   - "tranh/nghệ thuật/art": CHỈ lấy sự kiện có category là ART.

                2. TÌM THẤY SỰ KIỆN:
                   type: "EVENT_SEARCH", message: "Xin chào [Tên], tìm thấy X sự kiện phù hợp.", events: [{id, title, category, venue, startTime, organizer, description}]

                3. KHÔNG TÌM THẤY SỰ KIỆN KHỚP:
                   type: "EVENT_SEARCH", message: "Xin lỗi, không tìm thấy sự kiện nào phù hợp.", events: []

                4. HỎI BẠN LÀ AI:
                   type: "SYSTEM_INFO", message: "Tôi là trợ lý AI của dự án Online Event Booking.", events: []
                """.formatted(customerInfo.isEmpty() ? "Khách hàng" : customerInfo, eventContext);

        AiSearchEventResponse aiResult = chatClient.prompt()
                .system(sysPrompt)
                .user(request.getMessage())
                .call()
                .entity(AiSearchEventResponse.class);

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
                2. Hãy trả lời bằng văn bản tự nhiên, thân thiện, ngắn gọn và chính xác. Không cần trả về JSON.
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
            String categoryFriendly = event.getCategory();
            if ("FOOD".equalsIgnoreCase(event.getCategory())) {
                categoryFriendly = "FOOD (Ẩm thực, Đồ ăn, Món ăn)";
            } else if ("MUSIC".equalsIgnoreCase(event.getCategory())) {
                categoryFriendly = "MUSIC (Âm nhạc, Nhạc, Ca hát, Concert)";
            } else if ("ART".equalsIgnoreCase(event.getCategory())) {
                categoryFriendly = "ART (Triển lãm Nghệ thuật, Tranh vẽ, Hội họa)";
            }

            stringBuilder.append(String.format("- [ID: %d] Tên sự kiện: %s\n  + Thể loại: %s\n  + Địa điểm: %s\n  + Thời gian: %s\n  + Ban tổ chức: %s\n  + Mô tả: %s\n\n",
                    event.getId(), event.getTitle(), categoryFriendly,
                    event.getVenue(), event.getStartTime(), event.getOrganizer(),
                    event.getDescription() != null ? event.getDescription() : "Không có"));
        }
        return stringBuilder.toString();
    }
}
