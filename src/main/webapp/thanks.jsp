<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty invoice}">
    <c:redirect url="/home" />
</c:if>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/topbar.jsp" />

<main class="shop">
    <div class="checkout-box thanks">
        <p class="thanks-icon">&#10003;</p>
        <h1>Đặt hàng thành công!</h1>
        <p class="subtitle">Cảm ơn bạn đã mua sắm tại MiniShop.</p>

        <div class="summary-row"><span>Mã đơn hàng</span><b>#${invoice.invoiceNumber}</b></div>
        <div class="summary-row"><span>Ngày đặt</span><span>${invoice.invoiceDateDefaultFormat}</span></div>
        <div class="summary-row"><span>Người nhận</span><span><c:out value="${invoice.fullName}" /> · <c:out value="${invoice.phone}" /></span></div>
        <div class="summary-row"><span>Địa chỉ</span><span><c:out value="${invoice.address}" /></span></div>
        <div class="summary-row"><span>Thanh toán</span><span>${invoice.paymentMethodName}</span></div>

        <h2>Sản phẩm</h2>
        <c:forEach var="item" items="${invoice.lineItems}">
            <div class="summary-row">
                <span><c:out value="${item.product.description}" /> × ${item.quantity}</span>
                <span>${item.totalCurrencyFormat}</span>
            </div>
        </c:forEach>
        <div class="summary-row summary-total">
            <span>Tổng cộng</span>
            <span class="price">${invoice.totalCurrencyFormat}</span>
        </div>

        <a href="home" class="btn btn-primary">Tiếp tục mua sắm</a>
    </div>
</main>

<c:import url="/includes/footer.jsp" />
