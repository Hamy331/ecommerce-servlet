package murach.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import murach.business.Invoice;
import murach.data.InvoiceDB;

public class InvoiceAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";  // default action
        }

        String url = "/admin/invoices.jsp";
        if (action.equals("view") || action.equals("process")) {
            long invoiceId = parseId(request.getParameter("invoiceId"));
            if (action.equals("process")) {
                boolean processed = "true".equals(request.getParameter("processed"));
                InvoiceDB.updateProcessed(invoiceId, processed);
                request.setAttribute("message", processed
                        ? "Đã đánh dấu đơn là đã xử lý."
                        : "Đã chuyển đơn về chưa xử lý.");
                request.setAttribute("messageType", "success");
            }
            Invoice invoice = InvoiceDB.selectInvoice(invoiceId);
            if (invoice != null) {
                request.setAttribute("invoice", invoice);
                url = "/admin/invoice.jsp";
            }
        }

        if (url.equals("/admin/invoices.jsp")) {
            // lọc: all (mặc định) hoặc pending (chưa xử lý)
            String status = request.getParameter("status");
            List<Invoice> invoices = InvoiceDB.selectInvoices();
            if ("pending".equals(status)) {
                List<Invoice> pending = new ArrayList<>();
                for (Invoice i : invoices) {
                    if (!i.isProcessed()) {
                        pending.add(i);
                    }
                }
                invoices = pending;
            }
            request.setAttribute("status", "pending".equals(status) ? "pending" : "all");
            request.setAttribute("invoices", invoices);
        }

        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    private static long parseId(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
