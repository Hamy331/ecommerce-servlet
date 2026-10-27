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
    Details     TEXT,
    Image       MEDIUMBLOB,
    ImageType   VARCHAR(50),
    PRIMARY KEY (ProductID)
);

INSERT INTO product (ProductCode, Description, Price, Details) VALUES
    ('p01', 'Tai nghe chống ồn Sony WH-1000XM5', 6490000,
     'Tai nghe chụp tai không dây, chống ồn chủ động với 8 micro và bộ xử lý QN1 + V1.\nPin đến 30 giờ (bật chống ồn), sạc nhanh 3 phút dùng 3 giờ.\nKết nối Bluetooth 5.2, hỗ trợ LDAC, kết nối cùng lúc 2 thiết bị.\nTrọng lượng khoảng 250 g, cổng sạc USB-C.\nBảo hành chính hãng 12 tháng.'),
    ('p02', 'Apple AirPods Pro 2 (USB-C)', 5790000,
     'Tai nghe nhét tai không dây dùng chip Apple H2.\nChống ồn chủ động, chế độ Xuyên âm thích ứng, âm thanh không gian.\nPin tai nghe đến 6 giờ, tổng 30 giờ khi dùng kèm hộp sạc.\nHộp sạc USB-C, sạc không dây MagSafe, có loa tìm thiết bị.\nKháng bụi và nước IP54. Bảo hành chính hãng 12 tháng.'),
    ('p03', 'Chuột không dây Logitech MX Master 3S', 2290000,
     'Chuột công thái học cho dân văn phòng và thiết kế.\nCảm biến 8000 DPI, dùng được cả trên mặt kính.\nNút bấm giảm ồn, con lăn MagSpeed cuộn 1000 dòng mỗi giây.\nKết nối Bluetooth hoặc đầu thu Logi Bolt, dùng cho 3 máy.\nPin sạc USB-C dùng đến 70 ngày. Bảo hành 12 tháng.'),
    ('p04', 'Đồng hồ Casio G-Shock GA-2100-1A1', 3290000,
     'Đồng hồ nam kim và số, mặt bát giác, vỏ lõi carbon.\nChống va đập, chống nước 200 m.\nĐèn LED, giờ thế giới, bấm giờ, hẹn giờ, báo thức.\nDây nhựa resin màu đen, đường kính mặt 45,4 mm, nặng 51 g.\nPin dùng khoảng 3 năm. Bảo hành chính hãng 12 tháng.'),
    ('p05', 'Máy chơi game Nintendo Switch OLED', 7290000,
     'Máy chơi game cầm tay và cắm TV, màn hình OLED 7 inch.\nBộ nhớ trong 64 GB, khe thẻ microSD.\nĐế cắm có cổng LAN, chân đế điều chỉnh được góc.\nTrọn bộ gồm máy, 2 tay cầm Joy-Con, đế cắm, dây HDMI và sạc.\nPin dùng từ 4,5 đến 9 giờ. Bảo hành 12 tháng.');

ALTER TABLE invoice AUTO_INCREMENT = 1001;

-- user riêng cho app (slide dùng murach_user / sesame)
DROP USER IF EXISTS 'minishop_user'@'localhost';
CREATE USER 'minishop_user'@'localhost' IDENTIFIED BY 'sesame';
GRANT SELECT, INSERT, UPDATE, DELETE ON minishop.* TO 'minishop_user'@'localhost';
FLUSH PRIVILEGES;

-- cấp quyền admin cho tài khoản của bạn (sau khi đã đăng ký trên web):
-- UPDATE user SET Role = 'admin' WHERE Email = 'email_cua_ban@gmail.com';
