<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />

<header class="topbar">
    <a href="home" class="brand">MiniShop</a>
    <div class="topbar-right">
        <span>Xin chào, <b><c:out value="${sessionScope.user.lastName} ${sessionScope.user.firstName}" /></b></span>
        <form action="user" method="post">
            <input type="hidden" name="action" value="logout">
            <input type="submit" value="Đăng xuất" class="btn-small">
        </form>
    </div>
</header>

<main class="shop">
    <h1 class="page-title">Sản phẩm</h1>
    <p class="subtitle left">Chọn sản phẩm bạn thích để xem chi tiết.</p>

    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <div class="product">
                <%-- ảnh đặt tên theo mã sản phẩm: images/p01.png ...
                     không có file thì ẩn ảnh, khung để trống --%>
                <div class="thumb">
                    <img src="images/${product.code}.png"
                         alt="<c:out value='${product.description}'/>"
                         onerror="this.remove()">
                </div>
                <div class="product-body">
                    <p class="product-name"><c:out value="${product.description}" /></p>
                    <p class="price">${product.priceCurrencyFormat}</p>
                    <a href="#" class="btn btn-primary">Xem chi tiết</a>
                </div>
            </div>
        </c:forEach>
    </div>
</main>

<c:import url="/includes/footer.jsp" />
