package murach.data;

import java.util.ArrayList;
import java.util.List;

import murach.business.Invoice;

/**
 * Lưu đơn hàng trong bộ nhớ, giống UserDB.
 * Sau này đổi sang JDBC (ch 12) thì chỉ sửa class này.
 */
public class InvoiceDB {

    private static final List<Invoice> invoices = new ArrayList<>();
    private static long nextNumber = 1001;

    public static synchronized long insert(Invoice invoice) {
        invoice.setInvoiceNumber(nextNumber++);
        invoices.add(invoice);
        return invoice.getInvoiceNumber();
    }
}
