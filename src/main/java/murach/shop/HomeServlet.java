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
import murach.data.ProductIO;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext sc = getServletContext();
        HttpSession session = request.getSession();
        String url = "/home.jsp";
        if (session.getAttribute("user") == null) {
            url = "/login.jsp";
        } else {
            String path = sc.getRealPath("/WEB-INF/products.txt");
            List<Product> products = ProductIO.getProducts(path);
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
