-- Tạo database cho MiniShop (theo Ch 12: database + user riêng cho app)
DROP DATABASE IF EXISTS minishop;
CREATE DATABASE minishop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE minishop;
-- tên bảng viết thường để chạy giống nhau trên Windows và Linux (Render)

CREATE TABLE user (
    UserID       INT NOT NULL AUTO_INCREMENT,
    Email        VARCHAR(100) NOT NULL UNIQUE,
    FirstName    VARCHAR(50)  NOT NULL,
    LastName     VARCHAR(50)  NOT NULL,
    PasswordHash VARCHAR(64)  NOT NULL,
    Salt         VARCHAR(64)  NOT NULL,
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

-- mã đơn bắt đầu từ 1001 như bản lưu trong bộ nhớ
ALTER TABLE invoice AUTO_INCREMENT = 1001;

-- user riêng cho app (slide dùng murach_user / sesame)
DROP USER IF EXISTS 'minishop_user'@'localhost';
CREATE USER 'minishop_user'@'localhost' IDENTIFIED BY 'sesame';
GRANT SELECT, INSERT, UPDATE, DELETE ON minishop.* TO 'minishop_user'@'localhost';
FLUSH PRIVILEGES;
