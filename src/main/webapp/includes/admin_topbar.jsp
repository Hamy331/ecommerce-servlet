<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<header class="topbar">
    <a href="${ctx}/admin/products" class="brand">MiniShop <span class="admin-tag">Admin</span></a>
    <div class="topbar-right">
        <a href="${ctx}/admin/products" class="cart-link">Sản phẩm</a>
        <a href="${ctx}/admin/orders" class="cart-link">Đơn hàng</a>
        <form action="${ctx}/user" method="post">
            <input type="hidden" name="action" value="logout">
            <input type="submit" value="Đăng xuất" class="btn-small">
        </form>
    </div>
</header>
