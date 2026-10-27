package murach.shop;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.business.Product;
import murach.data.ProductDB;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext sc = getServletContext();

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "home";  // default action
        }

        HttpSession session = request.getSession();
        String url = "/home.jsp";
        if (session.getAttribute("user") == null) {
            url = "/user?action=showLogin";  // thử tự đăng nhập bằng cookie
        } else if (action.equals("detail")) {
            String productCode = request.getParameter("productCode");
            Product product = ProductDB.selectProduct(productCode);
            if (product != null) {
                request.setAttribute("product", product);
                url = "/product.jsp";
            } else {
                request.setAttribute("products", ProductDB.selectProducts());
                url = "/home.jsp";
            }
        } else {
            // "home", hoặc action=login khi UserServlet forward sang
            List<Product> products = ProductDB.selectProducts();
            request.setAttribute("products", products);
            url = "/home.jsp";
        }

        sc.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
