package murach.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import murach.business.User;

public class AdminFilter implements Filter {

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
        ServletContext sc = filterConfig.getServletContext();

        HttpSession session = httpRequest.getSession(false);
        User user = (session == null) ? null
                : (User) session.getAttribute("user");
        String contextPath = httpRequest.getContextPath();

        if (user == null) {
            // chưa đăng nhập
            httpResponse.sendRedirect(contextPath + "/user?action=showLogin");
        } else if (!user.isAdmin()) {
            // đăng nhập rồi nhưng không phải admin
            sc.log(filterConfig.getFilterName() + " | chặn "
                    + user.getEmail() + " vào " + httpRequest.getServletPath());
            httpResponse.sendRedirect(contextPath + "/home");
        } else {
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
