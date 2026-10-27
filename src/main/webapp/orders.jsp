<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/topbar.jsp" />

<main class="shop">
    <h1 class="page-title">Đơn hàng của tôi</h1>
    <c:import url="/includes/message.jsp" />

    <c:choose>
        <c:when test="${empty invoices}">
            <div class="empty-search">
                <svg class="icon icon-lg"><use href="#i-receipt"/></svg>
                Bạn chưa có đơn hàng nào.
                <p><a href="home" class="btn-add">Mua sắm ngay</a></p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-box">
                <table class="cart-table">
                    <tr>
                        <th>Mã đơn</th>
                        <th>Ngày đặt</th>
                        <th>Sản phẩm</th>
                        <th class="right">Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th></th>
                    </tr>
                    <c:forEach var="invoice" items="${invoices}">
                        <tr>
                            <td><b>#${invoice.invoiceNumber}</b></td>
                            <td>${invoice.invoiceDateDefaultFormat}</td>
                            <td>
                                <c:forEach var="item" items="${invoice.lineItems}" varStatus="s">
                                    <c:out value="${item.product.description}" /> × ${item.quantity}<c:if test="${!s.last}"><br></c:if>
                                </c:forEach>
                            </td>
                            <td class="right"><b>${invoice.totalCurrencyFormat}</b></td>
                            <td>
                                <span class="status ${invoice.processed ? 'done' : 'pending'}">
                                    ${invoice.processed ? 'Đã xác nhận' : 'Chờ xác nhận'}
                                </span>
                            </td>
                            <td class="right">
                                <a href="orders?action=view&amp;invoiceId=${invoice.invoiceNumber}" class="btn-small">Xem</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<c:import url="/includes/footer.jsp" />
