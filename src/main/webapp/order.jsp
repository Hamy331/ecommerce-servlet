<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/topbar.jsp" />

<main class="shop">
    <p class="breadcrumb"><a href="orders">Đơn hàng của tôi</a> / #${invoice.invoiceNumber}</p>

    <div class="checkout">
        <div class="checkout-box">
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
        </div>

        <div class="checkout-box">
            <h2>Đơn #${invoice.invoiceNumber}</h2>
            <div class="summary-row"><span>Trạng thái</span>
                <span class="status ${invoice.processed ? 'done' : 'pending'}">
                    ${invoice.processed ? 'Đã xác nhận' : 'Chờ xác nhận'}</span></div>
            <div class="summary-row"><span>Ngày đặt</span><span>${invoice.invoiceDateDefaultFormat}</span></div>
            <div class="summary-row"><span>Người nhận</span><span><c:out value="${invoice.fullName}" /></span></div>
            <div class="summary-row"><span>Điện thoại</span><span><c:out value="${invoice.phone}" /></span></div>
            <div class="summary-row"><span>Địa chỉ</span><span><c:out value="${invoice.address}" /></span></div>
            <div class="summary-row"><span>Thanh toán</span><span>${invoice.paymentMethodName}</span></div>

            <a href="orders" class="btn btn-outline">Quay lại danh sách</a>
        </div>
    </div>
</main>

<c:import url="/includes/footer.jsp" />
