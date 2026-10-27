<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/admin_topbar.jsp" />

<main class="shop">
    <div class="admin-head">
        <h1 class="page-title">Quản lý đơn hàng</h1>
        <div class="tabs">
            <a href="orders" class="${status == 'all' ? 'active' : ''}">Tất cả</a>
            <a href="orders?status=pending" class="${status == 'pending' ? 'active' : ''}">Chưa xử lý</a>
        </div>
    </div>

    <div class="cart-box">
        <table class="cart-table">
            <tr>
                <th>Mã đơn</th>
                <th>Ngày đặt</th>
                <th>Khách hàng</th>
                <th class="right">Tổng tiền</th>
                <th>Trạng thái</th>
                <th></th>
            </tr>
            <c:forEach var="invoice" items="${invoices}">
                <tr>
                    <td><b>#${invoice.invoiceNumber}</b></td>
                    <td>${invoice.invoiceDateDefaultFormat}</td>
                    <td>
                        <c:out value="${invoice.fullName}" /><br>
                        <span class="muted"><c:out value="${invoice.user.email}" /></span>
                    </td>
                    <td class="right"><b>${invoice.totalCurrencyFormat}</b></td>
                    <td>
                        <span class="status ${invoice.processed ? 'done' : 'pending'}">
                            ${invoice.processed ? 'Đã xử lý' : 'Chưa xử lý'}
                        </span>
                    </td>
                    <td class="right">
                        <a href="orders?action=view&amp;invoiceId=${invoice.invoiceNumber}" class="btn-small">Xem</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty invoices}">
                <tr><td colspan="6" class="empty-row">Không có đơn hàng nào.</td></tr>
            </c:if>
        </table>
    </div>
</main>

<c:import url="/includes/footer.jsp" />
