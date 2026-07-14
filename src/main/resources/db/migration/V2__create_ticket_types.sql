CREATE TABLE ticket_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    total_quantity INT NOT NULL,
    sold_quantity INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_ticket_type_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;