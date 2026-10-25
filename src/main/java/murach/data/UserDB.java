package murach.data;

import java.util.ArrayList;
import java.util.List;

import murach.business.User;
import murach.util.PasswordUtil;

/**
 * Lưu user trong bộ nhớ (mất khi restart Tomcat).
 * Các method có cùng tên với UserDB ở chapter 12 (insert, emailExists,
 * selectUser) nên sau này chỉ cần thay phần thân bằng JDBC + ConnectionPool,
 * servlet không phải sửa gì.
 */
public class UserDB {

    private static final List<User> users = new ArrayList<>();

    // mock data: tài khoản mẫu để thử đăng nhập (demo@minishop.vn / 123456)
    static {
        try {
            User demo = new User("An", "Nguyễn", "demo@minishop.vn");
            String salt = PasswordUtil.getSalt();
            demo.setSalt(salt);
            demo.setPasswordHash(
                    PasswordUtil.hashAndSaltPassword("123456", salt));
            users.add(demo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static synchronized int insert(User user) {
        if (emailExists(user.getEmail())) {
            return 0;
        }
        users.add(user);
        return 1;
    }

    public static synchronized boolean emailExists(String email) {
        return selectUser(email) != null;
    }

    public static synchronized User selectUser(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
}
