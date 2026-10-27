package murach.shop;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.business.Invoice;
import murach.business.User;
import murach.data.InvoiceDB;

public class OrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext sc = getServletContext();

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";  // default action
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String url = "/orders.jsp";
        if (user == null) {
            url = "/user?action=showLogin";
        } else {
            if (action.equals("view")) {
                long invoiceId = parseId(request.getParameter("invoiceId"));
                Invoice invoice = InvoiceDB.selectInvoice(invoiceId);
                // chỉ cho xem đơn của chính mình
                if (invoice != null
                        && invoice.getUser().getUserId() == user.getUserId()) {
                    request.setAttribute("invoice", invoice);
                    url = "/order.jsp";
                } else {
                    request.setAttribute("message", "Không tìm thấy đơn hàng.");
                }
            }
            if (url.equals("/orders.jsp")) {
                List<Invoice> invoices = InvoiceDB.selectInvoices(user.getUserId());
                request.setAttribute("invoices", invoices);
            }
        }

        sc.getRequestDispatcher(url).forward(request, response);
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
