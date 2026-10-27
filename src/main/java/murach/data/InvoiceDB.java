package murach.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import murach.business.Invoice;
import murach.business.LineItem;

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
}
