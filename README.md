# Ecommerce Servlet: Đăng nhập & Đăng ký

Dự án Servlet/JSP đơn giản, viết theo slide Murach (chapter 2, 5, 7, 8, 9, 17).

## Cấu trúc

```
src/main/java/murach/
├── business/User.java        JavaBean (ch 2, 9)
├── data/UserDB.java          Lưu user (cùng tên method với UserDB ch 12)
├── util/PasswordUtil.java    Hash SHA-256 + salt (ch 17)
└── user/UserServlet.java     Controller: action ẩn, default action, url, 1 forward, doGet gọi doPost
src/main/webapp/
├── index.jsp                 Chuyển sang login.jsp
├── login.jsp                 Trang đăng nhập
├── register.jsp              Trang đăng ký
├── welcome.jsp               Trang sau khi đăng nhập (cần session)
├── includes/header.jsp, footer.jsp   (ch 8: c:import)
├── styles/main.css
└── WEB-INF/web.xml           Map UserServlet -> /user
```

## Luồng xử lý (UserServlet, URL `/user`)

| action          | Việc làm                                         | Trang trả về |
|-----------------|--------------------------------------------------|--------------|
| (không có)      | mặc định = showLogin                             | login.jsp    |
| showRegister    | mở form đăng ký                                  | register.jsp |
| register        | kiểm tra dữ liệu, hash mật khẩu, UserDB.insert   | login.jsp (lỗi thì register.jsp) |
| login           | tìm user theo email, so sánh hash                | welcome.jsp (sai thì login.jsp) |
| logout          | session.invalidate()                             | login.jsp    |

## Lưu trữ

`UserDB` hiện lưu user trong một `ArrayList` trong bộ nhớ, nên khi restart Tomcat thì mất dữ liệu.
Chọn cách này để chạy được ngay, không cần cài MySQL. Khi học tới chapter 12, chỉ cần thay phần thân
của `insert`, `emailExists`, `selectUser` bằng JDBC + `ConnectionPool` (thêm `META-INF/context.xml`),
`UserServlet` và các JSP giữ nguyên.

## Chạy

Cần JDK 11+ và Maven, Tomcat 9 (javax.servlet, **không** dùng Tomcat 10).

```
mvn package
```

Copy `target/ecommerce.war` vào thư mục `webapps` của Tomcat 9, chạy Tomcat rồi mở
http://localhost:8080/ecommerce/

Hoặc mở thư mục này bằng NetBeans (File > Open Project, nhận dạng là Maven project) và bấm Run.
