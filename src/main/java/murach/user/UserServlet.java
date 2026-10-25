package murach.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.business.User;
import murach.data.UserDB;
import murach.util.PasswordUtil;

public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String url = "/login.jsp";

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "showLogin";  // default action
        }

        // perform action and set URL to appropriate page
        if (action.equals("showLogin")) {
            url = "/login.jsp";
        } else if (action.equals("showRegister")) {
            url = "/register.jsp";
        } else if (action.equals("register")) {
            url = register(request);
        } else if (action.equals("login")) {
            url = login(request);
        } else if (action.equals("logout")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
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

        // store data in User object (giữ lại để form điền sẵn khi có lỗi)
        User user = new User(firstName, lastName, email);
        request.setAttribute("user", user);

        // validate the parameters
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
            UserDB.insert(user);
            message = "Đăng ký thành công! Hãy đăng nhập.";
            request.setAttribute("messageType", "success");
            request.setAttribute("email", email);
            url = "/login.jsp";
        }
        request.setAttribute("message", message);
        return url;
    }

    private String login(HttpServletRequest request)
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
            request.changeSessionId();  // chống session fixation
            session.setAttribute("user", user);
            return "/welcome.jsp";
        } else {
            request.setAttribute("email", email);
            request.setAttribute("message", "Email hoặc mật khẩu không đúng.");
            return "/login.jsp";
        }
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
