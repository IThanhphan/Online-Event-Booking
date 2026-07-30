package com.intern.booking_event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.intern.booking_event", 
    "org.springdoc"             
})
public class BookingEventApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookingEventApplication.class, args);
	}

}
