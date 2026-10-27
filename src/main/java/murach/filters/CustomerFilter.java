package murach.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.business.User;

/**
 * Admin không được vào trang mua hàng (/home, /cart): chuyển về trang quản lý.
 */
public class CustomerFilter implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest =
            (HttpServletRequest) request;
        HttpServletResponse httpResponse =
            (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        User user = (session == null) ? null
                : (User) session.getAttribute("user");

        if (user != null && user.isAdmin()) {
            httpResponse.sendRedirect(
                    httpRequest.getContextPath() + "/admin/products");
        } else {
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
