package com.intern.booking_event.model.dto.request;

import com.intern.booking_event.model.dto.response.EventResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {

    @NotBlank(message = "Tiêu đề sự kiện (title) là bắt buộc và không được để trống")
    private String title;

    private String description; 

    @NotBlank(message = "Danh mục sự kiện (category) là bắt buộc và không được để trống")
    private String category; 

    @NotBlank(message = "Địa điểm tổ chức (venue) là bắt buộc và không được để trống")
    private String venue;

    @NotNull(message = "Thời gian bắt đầu (startTime) không được để null")
    @Future(message = "Thời gian bắt đầu sự kiện phải là một thời điểm trong tương lai")
    private Instant startTime;

    @NotBlank(message = "Tên nhà tổ chức (organizer) là bắt buộc và không được để trống")
    private String organizer;

    @NotEmpty(message = "Danh sách loại vé (ticketTypes) không được để trống")
    @Valid
    private List<TicketRequest> ticketTypes;
}