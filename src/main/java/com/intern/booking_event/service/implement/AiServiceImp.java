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
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class AiServiceImp implements AIService {

    @Value("${spring.ai.prompt.search-events}")
    private Resource searchPrompt;
    @Value("${spring.ai.prompt.ask-events}")
    private Resource askPrompt;

    private final ChatClient chatClient;
    private final EventRepository eventRepository;
    private final CustomerRepository customerRepository;
    private final EventService eventService;

    public AiServiceImp(ChatClient.Builder chatClientBuilder, EventRepository eventRepository, CustomerRepository customerRepository, EventService eventService) {
        this.chatClient = chatClientBuilder.build();
        this.eventRepository = eventRepository;
        this.customerRepository = customerRepository;
        this.eventService = eventService;
    }

    @SneakyThrows
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

        String promptTemplate= searchPrompt.getContentAsString(StandardCharsets.UTF_8);

        String sysPrompt = promptTemplate.formatted(customerInfo.isEmpty() ? "Khách Hàng" : customerInfo,eventContext);

        AiSearchEventResponse aiResult = chatClient.prompt()
                .system(sysPrompt)
                .user(request.getMessage())
                .call()
                .entity(AiSearchEventResponse.class);

        return AiResponse.builder()
                .response(aiResult)
                .build();
    }

    @SneakyThrows
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

        String promptTemplate= askPrompt.getContentAsString(StandardCharsets.UTF_8);

        String sysPrompt = promptTemplate.formatted(event.getTitle(), eventContext);

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
            stringBuilder.append(String.format(
                    "- [ID: %d] Tên sự kiện: %s\n  + Thể loại (Categories): %s\n  + Địa điểm: %s\n  + Thời gian: %s\n  + Ban tổ chức: %s\n  + Mô tả: %s\n\n",
                    event.getId(), event.getTitle(), event.getCategory(),
                    event.getVenue(), event.getStartTime(), event.getOrganizer(),
                    event.getDescription() != null ? event.getDescription() : "Không có"
            ));
        }
        return stringBuilder.toString();
    }
}
