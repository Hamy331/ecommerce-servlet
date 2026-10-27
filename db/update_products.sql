-- thêm mô tả, ảnh và thay sản phẩm mẫu bằng 5 sản phẩm thật
-- tên bảng ghi kèm minishop. để chạy được cả khi công cụ không giữ lệnh USE
USE minishop;
SET NAMES utf8mb4;

ALTER TABLE minishop.product
    ADD COLUMN Details   TEXT,
    ADD COLUMN Image     MEDIUMBLOB,
    ADD COLUMN ImageType VARCHAR(50);

DELETE FROM minishop.product;

INSERT INTO minishop.product (ProductCode, Description, Price, Details) VALUES
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
