package murach.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.business.User;
import murach.data.UserDB;
import murach.util.CookieUtil;
import murach.util.PasswordUtil;

public class UserServlet extends HttpServlet {

    private static final String REMEMBER_COOKIE = "rememberToken";

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String url = "/login.jsp";

        String action = request.getParameter("action");
        if (action == null) {
            action = "showLogin"; 
        }

        if (action.equals("showLogin")) {
            url = autoLogin(request) ? "/home" : "/login.jsp";
        } else if (action.equals("showRegister")) {
            url = "/register.jsp";
        } else if (action.equals("register")) {
            url = register(request);
        } else if (action.equals("login")) {
            url = login(request, response);
        } else if (action.equals("logout")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // xoá token và cookie ghi nhớ đăng nhập
            String token = CookieUtil.getCookieValue(
                    request.getCookies(), REMEMBER_COOKIE);
            UserDB.deleteRememberToken(token);
            Cookie c = new Cookie(REMEMBER_COOKIE, "");
            c.setMaxAge(0);  // delete the cookie
            c.setPath(request.getContextPath() + "/");
            response.addCookie(c);
            request.setAttribute("message", "Bạn đã đăng xuất.");
            request.setAttribute("messageType", "success");
            url = "/login.jsp";
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    private String register(HttpServletRequest request)
            throws ServletException {
        // get parameters from the request
        String firstName = trim(request.getParameter("firstName"));
        String lastName = trim(request.getParameter("lastName"));
        String email = trim(request.getParameter("email"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        User user = new User(firstName, lastName, email);
        request.setAttribute("user", user);

        String message;
        String url;
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || password == null || password.isEmpty()) {
            message = "Vui lòng điền đầy đủ tất cả các ô.";
            url = "/register.jsp";
        } else if (password.length() < 6) {
            message = "Mật khẩu phải có ít nhất 6 ký tự.";
            url = "/register.jsp";
        } else if (!password.equals(confirmPassword)) {
            message = "Mật khẩu nhập lại không khớp.";
            url = "/register.jsp";
        } else if (UserDB.emailExists(email)) {
            message = "Email này đã được đăng ký. Hãy dùng email khác.";
            url = "/register.jsp";
        } else {
            try {
                String salt = PasswordUtil.getSalt();
                user.setSalt(salt);
                user.setPasswordHash(
                        PasswordUtil.hashAndSaltPassword(password, salt));
            } catch (NoSuchAlgorithmException e) {
                throw new ServletException(e);
            }
            if (UserDB.insert(user) > 0) {
                message = "Đăng ký thành công! Hãy đăng nhập.";
                request.setAttribute("messageType", "success");
                request.setAttribute("email", email);
                url = "/login.jsp";
            } else {
                message = "Không lưu được tài khoản. Vui lòng thử lại sau.";
                url = "/register.jsp";
            }
        }
        request.setAttribute("message", message);
        return url;
    }

    private String login(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {
        String email = trim(request.getParameter("email"));
        String password = request.getParameter("password");
        if (password == null) {
            password = "";
        }

        User user = UserDB.selectUser(email);
        boolean ok = false;
        if (user != null) {
            try {
                String hash = PasswordUtil.hashAndSaltPassword(
                        password, user.getSalt());
                ok = hash.equals(user.getPasswordHash());
            } catch (NoSuchAlgorithmException e) {
                throw new ServletException(e);
            }
        }

        if (ok) {
            HttpSession session = request.getSession();
            request.changeSessionId();  
            session.setAttribute("user", user);

            // tick "Ghi nhớ đăng nhập" thì lưu cookie 30 ngày
            if (request.getParameter("remember") != null) {
                String token = PasswordUtil.getSalt();  // chuỗi ngẫu nhiên
                UserDB.insertRememberToken(token, user.getEmail());
                Cookie c = new Cookie(REMEMBER_COOKIE, token);
                c.setMaxAge(60 * 60 * 24 * 30);  // set age to 30 days
                c.setPath(request.getContextPath() + "/");
                c.setHttpOnly(true);
                response.addCookie(c);
            }
            return "/home"; 
        } else {
            request.setAttribute("email", email);
            request.setAttribute("message", "Email hoặc mật khẩu không đúng.");
            return "/login.jsp";
        }
    }

    // đã đăng nhập, hoặc có cookie ghi nhớ hợp lệ thì vào thẳng trang chủ
    private boolean autoLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            return true;
        }
        String token = CookieUtil.getCookieValue(
                request.getCookies(), REMEMBER_COOKIE);
        User user = UserDB.selectUserByRememberToken(token);
        if (user == null) {
            return false;
        }
        request.changeSessionId();
        session.setAttribute("user", user);
        return true;
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
