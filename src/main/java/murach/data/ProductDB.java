package murach.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import murach.business.Product;

public class ProductDB {

    public static List<Product> selectProducts() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM product ORDER BY ProductCode";
        List<Product> products = new ArrayList<>();
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                products.add(toProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            System.out.println(e);
            return products;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static List<Product> selectProducts(String keyword) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM product "
                + "WHERE Description LIKE ? OR ProductCode LIKE ? "
                + "ORDER BY ProductCode";
        List<Product> products = new ArrayList<>();
        try {
            // thoát ký tự % và _ để người dùng gõ vào không thành ký tự đại diện
            String pattern = "%" + keyword.replace("\\", "\\\\")
                    .replace("%", "\\%").replace("_", "\\_") + "%";
            ps = connection.prepareStatement(query);
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            rs = ps.executeQuery();
            while (rs.next()) {
                products.add(toProduct(rs));
            }
            return products;
        } catch (SQLException e) {
            System.out.println(e);
            return products;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static Product selectProduct(String code) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM product WHERE ProductCode = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, code);
            rs = ps.executeQuery();
            return rs.next() ? toProduct(rs) : null;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static int insert(Product product) {
        return executeUpdate(
                "INSERT INTO product (Description, Price, ProductCode) "
                + "VALUES (?, ?, ?)", product);
    }

    public static int update(Product product) {
        return executeUpdate(
                "UPDATE product SET Description = ?, Price = ? "
                + "WHERE ProductCode = ?", product);
    }

    public static int delete(String code) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "DELETE FROM product WHERE ProductCode = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, code);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // insert và update dùng chung 3 tham số: Description, Price, ProductCode
    private static int executeUpdate(String query, Product product) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, product.getDescription());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getCode());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    private static Product toProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setCode(rs.getString("ProductCode"));
        p.setDescription(rs.getString("Description"));
        p.setPrice(rs.getDouble("Price"));
        return p;
    }
}
