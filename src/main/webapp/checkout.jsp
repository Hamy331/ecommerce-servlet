<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/user?action=showLogin" />
</c:if>
<c:if test="${empty cart.items}">
    <c:redirect url="/cart" />
</c:if>
<c:choose>
    <c:when test="${not empty invoice}">
        <c:set var="fullName" value="${invoice.fullName}" />
    </c:when>
    <c:otherwise>
        <c:set var="fullName" value="${sessionScope.user.lastName} ${sessionScope.user.firstName}" />
    </c:otherwise>
</c:choose>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/topbar.jsp" />

<main class="shop">
    <p class="breadcrumb">
        <a href="home">Trang chủ</a> / <a href="cart">Giỏ hàng</a> / Thanh toán
    </p>
    <h1 class="page-title">Thanh toán</h1>
    <c:import url="/includes/message.jsp" />

    <form action="cart" method="post" class="checkout">
        <input type="hidden" name="action" value="placeOrder">

        <div class="checkout-box">
            <h2>Thông tin giao hàng</h2>
            <label for="fullName">Họ và tên</label>
            <input type="text" id="fullName" name="fullName"
                   value="<c:out value='${fullName}'/>" required>

            <label for="phone">Số điện thoại</label>
            <input type="text" id="phone" name="phone"
                   value="<c:out value='${invoice.phone}'/>" placeholder="09xx xxx xxx" required>

            <label for="address">Địa chỉ nhận hàng</label>
            <input type="text" id="address" name="address"
                   value="<c:out value='${invoice.address}'/>"
                   placeholder="Số nhà, đường, phường, quận, tỉnh/thành" required>

            <h2>Phương thức thanh toán</h2>
            <label class="radio">
                <input type="radio" name="paymentMethod" value="cod"
                       ${invoice.paymentMethod != 'bank' ? 'checked' : ''}>
                Thanh toán khi nhận hàng (COD)
            </label>
            <label class="radio">
                <input type="radio" name="paymentMethod" value="bank"
                       ${invoice.paymentMethod == 'bank' ? 'checked' : ''}>
                Chuyển khoản ngân hàng
            </label>
        </div>

        <div class="checkout-box">
            <h2>Đơn hàng (${cart.count} sản phẩm)</h2>
            <c:forEach var="item" items="${cart.items}">
                <div class="summary-row">
                    <span><c:out value="${item.product.description}" /> × ${item.quantity}</span>
                    <span>${item.totalCurrencyFormat}</span>
                </div>
            </c:forEach>
            <div class="summary-row summary-total">
                <span>Tổng cộng</span>
                <span class="price">${cart.totalCurrencyFormat}</span>
            </div>
            <input type="submit" value="Đặt hàng" class="btn btn-primary">
            <a href="cart" class="btn btn-outline">Quay lại giỏ hàng</a>
        </div>
    </form>
</main>

<c:import url="/includes/footer.jsp" />
