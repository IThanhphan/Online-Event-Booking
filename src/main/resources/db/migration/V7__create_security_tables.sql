-- 1. Thêm cột password vào bảng customers
ALTER TABLE customers
    ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT '';

-- 2. Tạo bảng permission
CREATE TABLE IF NOT EXISTS permission (
    name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. Tạo bảng roles
CREATE TABLE IF NOT EXISTS roles (
    name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 4. Tạo bảng liên kết roles và permission
CREATE TABLE IF NOT EXISTS roles_permissions (
    role_name VARCHAR(255) NOT NULL,
    permissions_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_name, permissions_name),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_name) REFERENCES roles(name) ON DELETE CASCADE,
    CONSTRAINT fk_rp_permission FOREIGN KEY (permissions_name) REFERENCES permission(name) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 5. Tạo bảng liên kết customers và roles
CREATE TABLE IF NOT EXISTS customers_roles (
    customer_id BIGINT NOT NULL,
    roles_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (customer_id, roles_name),
    CONSTRAINT fk_cr_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    CONSTRAINT fk_cr_role FOREIGN KEY (roles_name) REFERENCES roles(name) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 6. Tạo bảng invalidated_token (lưu token đã logout/refresh)
CREATE TABLE IF NOT EXISTS invalidated_token (
    id VARCHAR(255) PRIMARY KEY,
    expiry_time DATETIME
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
