package murach.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import murach.business.Invoice;
import murach.business.LineItem;
import murach.business.Product;
import murach.business.User;

public class InvoiceDB {

    // lưu đơn hàng và các dòng sản phẩm trong cùng một transaction
    public static long insert(Invoice invoice) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String invoiceQuery
                = "INSERT INTO invoice (UserID, InvoiceDate, FullName, Phone, "
                + "Address, PaymentMethod) VALUES (?, ?, ?, ?, ?, ?)";
        String lineItemQuery
                = "INSERT INTO lineitem (InvoiceID, ProductCode, Description, "
                + "Price, Quantity) VALUES (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(invoiceQuery,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, invoice.getUser().getUserId());
            ps.setTimestamp(2, new Timestamp(invoice.getInvoiceDate().getTime()));
            ps.setString(3, invoice.getFullName());
            ps.setString(4, invoice.getPhone());
            ps.setString(5, invoice.getAddress());
            ps.setString(6, invoice.getPaymentMethod());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            long invoiceId = rs.getLong(1);
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);

            ps = connection.prepareStatement(lineItemQuery);
            for (LineItem item : invoice.getLineItems()) {
                ps.setLong(1, invoiceId);
                ps.setString(2, item.getProduct().getCode());
                ps.setString(3, item.getProduct().getDescription());
                ps.setDouble(4, item.getProduct().getPrice());
                ps.setInt(5, item.getQuantity());
                ps.addBatch();
            }
            ps.executeBatch();

            connection.commit();
            invoice.setInvoiceNumber(invoiceId);
            return invoiceId;
        } catch (SQLException e) {
            System.out.println(e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            return 0;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e);
            }
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // admin: tất cả đơn hàng, mới nhất trước
    public static List<Invoice> selectInvoices() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT i.*, u.Email FROM invoice i "
                + "JOIN user u ON u.UserID = i.UserID "
                + "ORDER BY i.InvoiceID DESC";
        List<Invoice> invoices = new ArrayList<>();
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(toInvoice(rs));
            }
            for (Invoice invoice : invoices) {
                invoice.setLineItems(
                        selectLineItems(connection, invoice.getInvoiceNumber()));
            }
            return invoices;
        } catch (SQLException e) {
            System.out.println(e);
            return invoices;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    // khách hàng: đơn của một tài khoản, mới nhất trước
    public static List<Invoice> selectInvoices(long userId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT i.*, u.Email FROM invoice i "
                + "JOIN user u ON u.UserID = i.UserID "
                + "WHERE i.UserID = ? "
                + "ORDER BY i.InvoiceID DESC";
        List<Invoice> invoices = new ArrayList<>();
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                invoices.add(toInvoice(rs));
            }
            for (Invoice invoice : invoices) {
                invoice.setLineItems(
                        selectLineItems(connection, invoice.getInvoiceNumber()));
            }
            return invoices;
        } catch (SQLException e) {
            System.out.println(e);
            return invoices;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static Invoice selectInvoice(long invoiceId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT i.*, u.Email FROM invoice i "
                + "JOIN user u ON u.UserID = i.UserID "
                + "WHERE i.InvoiceID = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setLong(1, invoiceId);
            rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Invoice invoice = toInvoice(rs);
            invoice.setLineItems(selectLineItems(connection, invoiceId));
            return invoice;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    public static int updateProcessed(long invoiceId, boolean processed) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "UPDATE invoice SET IsProcessed = ? WHERE InvoiceID = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setBoolean(1, processed);
            ps.setLong(2, invoiceId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }

    private static Invoice toInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(rs.getLong("InvoiceID"));
        invoice.setInvoiceDate(rs.getTimestamp("InvoiceDate"));
        invoice.setFullName(rs.getString("FullName"));
        invoice.setPhone(rs.getString("Phone"));
        invoice.setAddress(rs.getString("Address"));
        invoice.setPaymentMethod(rs.getString("PaymentMethod"));
        invoice.setProcessed(rs.getBoolean("IsProcessed"));
        User user = new User();
        user.setUserId(rs.getLong("UserID"));
        user.setEmail(rs.getString("Email"));
        invoice.setUser(user);
        return invoice;
    }

    // dòng sản phẩm lưu sẵn mô tả và giá lúc đặt, nên không cần bảng product
    private static ArrayList<LineItem> selectLineItems(Connection connection,
            long invoiceId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<LineItem> items = new ArrayList<>();
        try {
            ps = connection.prepareStatement(
                    "SELECT * FROM lineitem WHERE InvoiceID = ?");
            ps.setLong(1, invoiceId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setCode(rs.getString("ProductCode"));
                p.setDescription(rs.getString("Description"));
                p.setPrice(rs.getDouble("Price"));
                LineItem item = new LineItem();
                item.setProduct(p);
                item.setQuantity(rs.getInt("Quantity"));
                items.add(item);
            }
            return items;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
        }
    }
}
