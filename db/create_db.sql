-- Tạo database cho MiniShop
DROP DATABASE IF EXISTS minishop;
CREATE DATABASE minishop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE minishop;
SET NAMES utf8mb4;

CREATE TABLE user (
    UserID       INT NOT NULL AUTO_INCREMENT,
    Email        VARCHAR(100) NOT NULL UNIQUE,
    FirstName    VARCHAR(50)  NOT NULL,
    LastName     VARCHAR(50)  NOT NULL,
    PasswordHash VARCHAR(64)  NOT NULL,
    Salt         VARCHAR(64)  NOT NULL,
    Role         VARCHAR(10)  NOT NULL DEFAULT 'customer',
    PRIMARY KEY (UserID)
);

CREATE TABLE remembertoken (
    Token   VARCHAR(64) NOT NULL,
    UserID  INT NOT NULL,
    PRIMARY KEY (Token),
    FOREIGN KEY (UserID) REFERENCES user (UserID) ON DELETE CASCADE
);

CREATE TABLE invoice (
    InvoiceID     INT NOT NULL AUTO_INCREMENT,
    UserID        INT NOT NULL,
    InvoiceDate   DATETIME NOT NULL,
    FullName      VARCHAR(100) NOT NULL,
    Phone         VARCHAR(20)  NOT NULL,
    Address       VARCHAR(255) NOT NULL,
    PaymentMethod VARCHAR(10)  NOT NULL,
    IsProcessed   TINYINT(1)   NOT NULL DEFAULT 0,
    PRIMARY KEY (InvoiceID),
    FOREIGN KEY (UserID) REFERENCES user (UserID)
);

CREATE TABLE lineitem (
    LineItemID  INT NOT NULL AUTO_INCREMENT,
    InvoiceID   INT NOT NULL,
    ProductCode VARCHAR(10)  NOT NULL,
    Description VARCHAR(100) NOT NULL,
    Price       DECIMAL(12,0) NOT NULL,
    Quantity    INT NOT NULL,
    PRIMARY KEY (LineItemID),
    FOREIGN KEY (InvoiceID) REFERENCES invoice (InvoiceID) ON DELETE CASCADE
);

CREATE TABLE product (
    ProductID   INT NOT NULL AUTO_INCREMENT,
    ProductCode VARCHAR(10)  NOT NULL UNIQUE,
    Description VARCHAR(100) NOT NULL,
    Price       DECIMAL(12,0) NOT NULL,
    PRIMARY KEY (ProductID)
);

INSERT INTO product (ProductCode, Description, Price) VALUES
    ('p01', 'iPhone 17 Pro Max 256GB', 34990000),
    ('p02', 'Tủ lạnh Side by Side 635L', 25990000),
    ('p03', 'Bộ đồ thể thao nam', 350000),
    ('p04', 'Balo laptop chống nước', 350000),
    ('p05', 'Giày sneaker trắng', 590000),
    ('p06', 'Mũ lưỡi trai', 99000);

ALTER TABLE invoice AUTO_INCREMENT = 1001;

-- user riêng cho app (slide dùng murach_user / sesame)
DROP USER IF EXISTS 'minishop_user'@'localhost';
CREATE USER 'minishop_user'@'localhost' IDENTIFIED BY 'sesame';
GRANT SELECT, INSERT, UPDATE, DELETE ON minishop.* TO 'minishop_user'@'localhost';
FLUSH PRIVILEGES;

-- cấp quyền admin cho tài khoản của bạn (sau khi đã đăng ký trên web):
-- UPDATE user SET Role = 'admin' WHERE Email = 'email_cua_ban@gmail.com';
