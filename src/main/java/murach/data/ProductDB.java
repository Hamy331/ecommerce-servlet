package murach.data;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import murach.business.Product;
import murach.business.ProductImage;

public class ProductDB {

    // không lấy cột Image, chỉ biết sản phẩm có ảnh hay không
    private static final String COLUMNS = "ProductCode, Description, Price, "
            + "Details, Image IS NOT NULL AS HasImage, "
            + "COALESCE(CRC32(Image), 0) AS ImageVersion";

    public static List<Product> selectProducts() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT " + COLUMNS + " FROM product ORDER BY ProductCode";
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

        String query = "SELECT " + COLUMNS + " FROM product "
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

        String query = "SELECT " + COLUMNS + " FROM product WHERE ProductCode = ?";
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
                "INSERT INTO product (Description, Price, Details, ProductCode) "
                + "VALUES (?, ?, ?, ?)", product);
    }

    public static int update(Product product) {
        return executeUpdate(
                "UPDATE product SET Description = ?, Price = ?, Details = ? "
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

    public static int updateImage(String code, InputStream image,
            long size, String contentType) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "UPDATE product SET Image = ?, ImageType = ? "
                + "WHERE ProductCode = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setBinaryStream(1, image, size);
            ps.setString(2, contentType);
            ps.setString(3, code);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static ProductImage selectImage(String code) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT Image, ImageType FROM product "
                + "WHERE ProductCode = ? AND Image IS NOT NULL";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new ProductImage(rs.getBytes("Image"),
                        rs.getString("ImageType"));
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // insert và update dùng chung 4 tham số: Description, Price, Details, ProductCode
    private static int executeUpdate(String query, Product product) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, product.getDescription());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getDetails());
            ps.setString(4, product.getCode());
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
        p.setDetails(rs.getString("Details") == null ? "" : rs.getString("Details"));
        p.setHasImage(rs.getBoolean("HasImage"));
        p.setImageVersion(rs.getLong("ImageVersion"));
        return p;
    }
}
