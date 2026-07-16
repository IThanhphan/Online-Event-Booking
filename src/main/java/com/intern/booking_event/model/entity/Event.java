package com.intern.booking_event.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category; 

    @Column(nullable = false)
    private String venue;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private String organizer;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<TicketType> ticketTypes;
}