package com.intern.booking_event;

import com.intern.booking_event.model.entity.Event;
import com.intern.booking_event.model.entity.TicketType;
import com.intern.booking_event.repository.EventRepository;
import com.intern.booking_event.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@SpringBootTest
class BookingEventApplicationTests {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private TicketRepository ticketRepository;

	@Test
	void insertTestData() {
		// Clear old data to prevent duplication issues
		ticketRepository.deleteAll();
		eventRepository.deleteAll();

		// Event 1: Music Concert 2026
		Event e1 = Event.builder()
				.title("Music Concert 2026")
				.description("Summer grand pop concert")
				.category("MUSIC")
				.venue("My Dinh Stadium")
				.startTime(Instant.now().plus(30, ChronoUnit.DAYS))
				.organizer("V-Pop Center")
				.build();
		e1 = eventRepository.save(e1);

		TicketType t1_1 = TicketType.builder()
				.event(e1)
				.name("VIP Ticket")
				.price(new BigDecimal("1500000.00"))
				.totalQuantity(100)
				.soldQuantity(0)
				.build();
		TicketType t1_2 = TicketType.builder()
				.event(e1)
				.name("Standard Ticket")
				.price(new BigDecimal("500000.00"))
				.totalQuantity(500)
				.soldQuantity(0)
				.build();
		ticketRepository.saveAll(Arrays.asList(t1_1, t1_2));

		// Event 2: Tech Seminar 2026
		Event e2 = Event.builder()
				.title("Tech Seminar 2026")
				.description("Latest AI and Spring Boot technology trends")
				.category("TECH")
				.venue("Keangnam Landmark")
				.startTime(Instant.now().plus(45, ChronoUnit.DAYS))
				.organizer("FPT Software")
				.build();
		e2 = eventRepository.save(e2);

		TicketType t2_1 = TicketType.builder()
				.event(e2)
				.name("Free Ticket")
				.price(BigDecimal.ZERO)
				.totalQuantity(200)
				.soldQuantity(0)
				.build();
		ticketRepository.save(t2_1);

		// Event 3: Food Festival 2026
		Event e3 = Event.builder()
				.title("Food Festival 2026")
				.description("Enjoy international cuisines")
				.category("FOOD")
				.venue("Quang truong 2-4")
				.startTime(Instant.now().plus(60, ChronoUnit.DAYS))
				.organizer("Nha Trang Tourism")
				.build();
		e3 = eventRepository.save(e3);

		TicketType t3_1 = TicketType.builder()
				.event(e3)
				.name("Entry Ticket")
				.price(new BigDecimal("100000.00"))
				.totalQuantity(1000)
				.soldQuantity(0)
				.build();
		ticketRepository.save(t3_1);

		// Event 4: Art Exhibition 2026
		Event e4 = Event.builder()
				.title("Art Exhibition 2026")
				.description("Contemporary art gallery")
				.category("ART")
				.venue("Hanoi Opera House")
				.startTime(Instant.now().plus(90, ChronoUnit.DAYS))
				.organizer("Art Society")
				.build();
		e4 = eventRepository.save(e4);

		TicketType t4_1 = TicketType.builder()
				.event(e4)
				.name("General Admission")
				.price(new BigDecimal("200000.00"))
				.totalQuantity(150)
				.soldQuantity(0)
				.build();
		ticketRepository.save(t4_1);

		System.out.println(">>> INSERTED 4 TEST EVENTS AND TICKET TYPES SUCCESSFULLY!");
	}

}
