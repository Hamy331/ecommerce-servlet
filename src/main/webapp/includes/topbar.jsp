<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="topbar">
    <a href="home" class="brand"><svg class="icon"><use href="#i-bag"/></svg>MiniShop</a>
    <div class="topbar-right">
        <a href="orders" class="cart-link"><svg class="icon"><use href="#i-receipt"/></svg>Đơn hàng</a>
        <a href="cart" class="cart-link">
            <svg class="icon"><use href="#i-cart"/></svg>Giỏ hàng
            <c:if test="${sessionScope.cart.count > 0}">
                <span class="badge">${sessionScope.cart.count}</span>
            </c:if>
        </a>
        <span>Xin chào, <b><c:out value="${sessionScope.user.lastName} ${sessionScope.user.firstName}" /></b></span>
        <form action="user" method="post">
            <input type="hidden" name="action" value="logout">
            <input type="submit" value="Đăng xuất" class="btn-small">
        </form>
    </div>
</header>
