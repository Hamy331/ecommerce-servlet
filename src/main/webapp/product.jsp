<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty product}">
    <c:redirect url="/home" />
</c:if>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/topbar.jsp" />

<main class="shop">
    <p class="breadcrumb">
        <a href="home">Trang chủ</a> / <c:out value="${product.description}" />
    </p>

    <div class="detail">
        <div class="detail-image">
            <img src="images/${product.code}.png"
                 alt="<c:out value='${product.description}'/>"
                 onerror="this.remove()">
        </div>

        <div class="detail-info">
            <h1 class="page-title"><c:out value="${product.description}" /></h1>
            <p class="detail-code">Mã sản phẩm: <c:out value="${product.code}" /></p>
            <p class="detail-price">${product.priceCurrencyFormat}</p>

            <form action="cart" method="post">
                <input type="hidden" name="productCode"
                       value="<c:out value='${product.code}'/>">
                <label for="quantity">Số lượng</label>
                <input type="text" id="quantity" name="quantity" value="1" class="qty">
                <input type="submit" value="Thêm vào giỏ hàng" class="btn btn-primary">
            </form>
            <a href="home" class="btn btn-outline">Tiếp tục mua sắm</a>
        </div>
    </div>
</main>

<c:import url="/includes/footer.jsp" />
