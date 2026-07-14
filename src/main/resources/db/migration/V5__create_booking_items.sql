CREATE TABLE booking_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    ticket_type_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_booking_item_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_item_ticket_type FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(id),

    -- Thêm UNIQUE constraint và CHECK constraint cho quantity
    --  Đảm bảo một loại vé không bị lặp lại dòng trong cùng một đơn hàng
    CONSTRAINT uq_booking_ticket_type UNIQUE (booking_id, ticket_type_id),
    --Khách hàng phải chọn ít nhất 1 vé khi đặt mua
    CONSTRAINT chk_booking_item_quantity CHECK (quantity > 0),
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;