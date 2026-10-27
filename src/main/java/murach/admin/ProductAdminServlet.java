package murach.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import murach.business.Product;
import murach.data.ProductDB;

public class ProductAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";  // default action
        }

        String url = "/admin/products.jsp";
        if (action.equals("add")) {
            request.setAttribute("product", new Product());
            request.setAttribute("mode", "add");
            url = "/admin/product_form.jsp";
        } else if (action.equals("edit")) {
            Product product = ProductDB.selectProduct(
                    request.getParameter("productCode"));
            if (product != null) {
                request.setAttribute("product", product);
                request.setAttribute("mode", "edit");
                request.setAttribute("priceString",
                        String.valueOf((long) product.getPrice()));
                url = "/admin/product_form.jsp";
            }
        } else if (action.equals("save")) {
            url = save(request);
        } else if (action.equals("delete")) {
            String code = request.getParameter("productCode");
            if (ProductDB.delete(code) > 0) {
                setMessage(request, "Đã xoá sản phẩm " + code + ".", true);
            }
        }

        if (url.equals("/admin/products.jsp")) {
            request.setAttribute("products", ProductDB.selectProducts());
        }
        getServletContext()
                .getRequestDispatcher(url)
                .forward(request, response);
    }

    private String save(HttpServletRequest request) {
        String mode = "edit".equals(request.getParameter("mode")) ? "edit" : "add";
        String code = trim(request.getParameter("productCode")).toLowerCase();
        String description = trim(request.getParameter("description"));
        String priceString = trim(request.getParameter("price"));

        Product product = new Product();
        product.setCode(code);
        product.setDescription(description);
        request.setAttribute("product", product);
        request.setAttribute("mode", mode);
        request.setAttribute("priceString", priceString);

        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            price = -1;
        }
        product.setPrice(Math.max(price, 0));

        String message = null;
        if (!code.matches("[a-z0-9]{1,10}")) {
            message = "Mã sản phẩm chỉ gồm chữ thường và số, tối đa 10 ký tự.";
        } else if (description.isEmpty()) {
            message = "Vui lòng nhập tên sản phẩm.";
        } else if (price <= 0) {
            message = "Giá phải là số lớn hơn 0.";
        } else if (mode.equals("add") && ProductDB.selectProduct(code) != null) {
            message = "Mã sản phẩm " + code + " đã tồn tại.";
        }
        if (message != null) {
            setMessage(request, message, false);
            return "/admin/product_form.jsp";
        }

        int count = mode.equals("add")
                ? ProductDB.insert(product) : ProductDB.update(product);
        if (count == 0) {
            setMessage(request, "Không lưu được sản phẩm. Vui lòng thử lại.", false);
            return "/admin/product_form.jsp";
        }
        setMessage(request, "Đã lưu sản phẩm " + code + ".", true);
        return "/admin/products.jsp";
    }

    private static void setMessage(HttpServletRequest request,
            String message, boolean success) {
        request.setAttribute("message", message);
        if (success) {
            request.setAttribute("messageType", "success");
        }
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
