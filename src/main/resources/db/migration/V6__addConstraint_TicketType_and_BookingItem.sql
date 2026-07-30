-- 1. Bổ sung các ràng buộc cho bảng ticket_types (Bảo vệ kho vé)
ALTER TABLE ticket_types
    ADD CONSTRAINT chk_ticket_price CHECK (price >= 0),
    ADD CONSTRAINT chk_sold_quantity CHECK (sold_quantity <= total_quantity);

-- 2. Bổ sung các ràng buộc cho bảng booking_items (Ngăn chặn trùng lặp loại vé trong một đơn hàng)
ALTER TABLE booking_items
    ADD CONSTRAINT uq_booking_ticket_type UNIQUE (booking_id, ticket_type_id),
    ADD CONSTRAINT chk_booking_item_quantity CHECK (quantity > 0);