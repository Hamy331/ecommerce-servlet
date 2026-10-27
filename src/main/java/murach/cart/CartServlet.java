package murach.cart;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.business.Cart;
import murach.business.Invoice;
import murach.business.LineItem;
import murach.business.Product;
import murach.business.User;
import murach.data.InvoiceDB;
import murach.data.ProductDB;

public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        ServletContext sc = getServletContext();

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";  // default action
        }

        // perform action and set URL to appropriate page
        String url = "/home";
        if (action.equals("shop")) {
            url = "/home";    // trang chủ (slide: /index.jsp)
        }
        else if (action.equals("cart")) {
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }

            // chỉ mở giỏ hàng (bấm link "Giỏ hàng") thì không có productCode
            Product product = ProductDB.selectProduct(productCode);
            if (product != null) {
                //if the user enters a negative or invalid quantity,
                //the quantity is automatically reset to 1.
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity < 0) {
                        quantity = 1;
                    }
                } catch (NumberFormatException nfe) {
                    quantity = 1;
                }

                LineItem lineItem = new LineItem();
                lineItem.setProduct(product);
                lineItem.setQuantity(quantity);
                if (quantity > 0) {
                    cart.addItem(lineItem);
                } else if (quantity == 0) {
                    cart.removeItem(lineItem);
                }
            }

            session.setAttribute("cart", cart);
            url = "/cart.jsp";
        }
        else if (action.equals("checkout")) {
            Cart cart = (Cart) request.getSession().getAttribute("cart");
            if (cart == null || cart.getCount() == 0) {
                url = "/cart.jsp";
            } else {
                url = "/checkout.jsp";
            }
        }
        else if (action.equals("placeOrder")) {
            url = placeOrder(request);
        }

        sc.getRequestDispatcher(url)
                .forward(request, response);
    }

    private String placeOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        User user = (User) session.getAttribute("user");
        if (cart == null || cart.getCount() == 0) {
            return "/cart.jsp";
        }
        if (user == null) {
            return "/login.jsp";
        }

        String fullName = trim(request.getParameter("fullName"));
        String phone = trim(request.getParameter("phone"));
        String address = trim(request.getParameter("address"));
        String paymentMethod = request.getParameter("paymentMethod");
        if (!"bank".equals(paymentMethod)) {
            paymentMethod = "cod";
        }

        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setFullName(fullName);
        invoice.setPhone(phone);
        invoice.setAddress(address);
        invoice.setPaymentMethod(paymentMethod);

        if (fullName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            request.setAttribute("invoice", invoice);
            request.setAttribute("message",
                    "Vui lòng nhập đầy đủ họ tên, số điện thoại và địa chỉ.");
            return "/checkout.jsp";
        }

        invoice.setLineItems(cart.getItems());
        if (InvoiceDB.insert(invoice) == 0) {
            request.setAttribute("invoice", invoice);
            request.setAttribute("message",
                    "Không lưu được đơn hàng. Vui lòng thử lại sau.");
            return "/checkout.jsp";
        }

        // đặt hàng xong thì làm trống giỏ
        session.removeAttribute("cart");
        request.setAttribute("invoice", invoice);
        return "/thanks.jsp";
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
