-- Chạy file này MỘT LẦN trên database minishop đã có
-- Máy mới cài từ đầu thì chỉ cần chạy create_db.sql.
USE minishop;
SET NAMES utf8mb4; 

-- quyền của tài khoản: 'customer' hoặc 'admin'
ALTER TABLE user ADD COLUMN Role VARCHAR(10) NOT NULL DEFAULT 'customer';

-- đơn hàng đã được admin xử lý chưa
ALTER TABLE invoice ADD COLUMN IsProcessed TINYINT(1) NOT NULL DEFAULT 0;

-- sản phẩm chuyển từ products.txt vào database để admin sửa được
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

-- cấp quyền admin cho tài khoản của bạn (đổi email cho đúng):
UPDATE user SET Role = 'admin' WHERE Email = 'admin@gmail.com';
