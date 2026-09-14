package com.intern.booking_event.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    CUSTOMER_NOT_FOUND(1001, "customer not found", HttpStatus.NOT_FOUND),
    TICKET_TYPE_NOT_FOUND(1002, "ticket type not found", HttpStatus.NOT_FOUND),
    OUT_OF_STOCK(1003, "out of stock", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You do not have permission", HttpStatus.FORBIDDEN),
    ROLE_NOT_FOUND(1006, "Role not found", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(1007, "Permission not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(1008, "User already existed", HttpStatus.BAD_REQUEST),
    ;

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;

    }
}
