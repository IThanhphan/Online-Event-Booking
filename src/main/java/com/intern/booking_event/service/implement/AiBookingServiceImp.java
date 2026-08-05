package com.intern.booking_event.service.implement;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import com.intern.booking_event.exception.ResourceNotFoundException;
import com.intern.booking_event.model.dto.request.EventSearchRequest;
import com.intern.booking_event.model.entity.Event;
import com.intern.booking_event.repository.EventRepository;
import com.intern.booking_event.service.AiBookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiBookingServiceImp implements AiBookingService {

    private final ChatClient.Builder chatClientBuilder;
    private final EventRepository eventRepository;

    @Override
    public EventSearchRequest parseSearchPrompt(String userPrompt) {
        BeanOutputConverter<EventSearchRequest> parser = 
                new BeanOutputConverter<>(EventSearchRequest.class);

        String systemPrompt = """
            Bạn là trợ lý AI tìm kiếm sự kiện thông minh cho hệ thống Online Event Booking.
            Nhiệm vụ của bạn là đọc yêu cầu bằng ngôn ngữ tự nhiên của khách hàng và trích xuất thành đối tượng EventSearchRequest.

            QUY TẮC MÁP DỮ LIỆU CHUẨN:
            1. title: Tên sự kiện, tên nghệ sĩ/ca sĩ, hoặc từ khóa chính của tên sự kiện (VD: "concert Rap Việt" -> title = "Rap Việt"). Nếu khách chỉ tìm chung chung, để null.
            2. category: Chỉ chọn 1 trong các giá trị chuẩn sau: MUSIC, SPORTS, THEATRE, CONFERENCE. Nếu không rõ, để null.
            3. venue: Tên rạp, sân vận động, thành phố hoặc địa điểm tổ chức.
            4. maxPrice: Mức giá tối đa khách sẵn sàng trả (dạng số BigDecimal VND, VD: 500k -> 500000).

            Định dạng JSON trả về bắt buộc tuân theo schema:
            {format}
            """;

        ChatClient chatClient = chatClientBuilder.build();

        return chatClient.prompt()
                .system(sp -> sp.text(systemPrompt).param("format", parser.getFormat()))
                .user(userPrompt)
                .call()
                .entity(parser);
    }

    @Override
    public String answerEventQuestion(Long eventId, String customerQuestion) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sự kiện với ID: " + eventId));

        String systemPrompt = """
            Bạn là trợ lý giải đáp thắc mắc về sự kiện chuyên nghiệp.
            Dưới đây là ngữ cảnh thông tin chi tiết về sự kiện mà khách hàng đang hỏi:
            
            - Tên sự kiện: {title}
            - Địa điểm: {venue}
            - Ban tổ chức: {organizer}
            - Mô tả chi tiết: {description}

            Nhiệm vụ của bạn:
            Hãy trả lời câu hỏi của khách hàng dựa HOÀN TOÀN vào các thông tin ngữ cảnh trên. 
            Nếu thông tin khách hỏi không xuất hiện trong phần mô tả, hãy lịch sự trả lời rằng bạn chưa có thông tin này và khuyên khách liên hệ trực tiếp Ban tổ chức.
            """;

        ChatClient chatClient = chatClientBuilder.build();

        return chatClient.prompt()
                .system(sp -> sp.text(systemPrompt)
                        .param("title", event.getTitle() != null ? event.getTitle() : "")
                        .param("venue", event.getVenue() != null ? event.getVenue() : "")
                        .param("organizer", event.getOrganizer() != null ? event.getOrganizer() : "")
                        .param("description", event.getDescription() != null ? event.getDescription() : ""))
                .user(customerQuestion)
                .call()
                .content();
    }
}