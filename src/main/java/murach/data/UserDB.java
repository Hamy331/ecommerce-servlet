package murach.data;

import java.util.ArrayList;
import java.util.List;

import murach.business.User;

/**
 * Lưu user trong bộ nhớ (mất khi restart Tomcat).
 * Các method có cùng tên với UserDB ở chapter 12 (insert, emailExists,
 * selectUser) nên sau này chỉ cần thay phần thân bằng JDBC + ConnectionPool,
 * servlet không phải sửa gì.
 */
public class UserDB {

    private static final List<User> users = new ArrayList<>();

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
