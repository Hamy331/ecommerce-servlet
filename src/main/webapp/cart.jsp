<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/user?action=showLogin" />
</c:if>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/topbar.jsp" />

<main class="shop">
    <h1 class="page-title">Giỏ hàng của bạn</h1>

    <c:choose>
        <c:when test="${empty cart.items}">
            <div class="cart-empty">
                <p class="cart-empty-title">Giỏ hàng đang trống</p>
                <p class="subtitle">Hãy chọn vài sản phẩm ở trang chủ nhé.</p>
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="shop">
                    <input type="submit" value="Tiếp tục mua sắm" class="btn btn-primary">
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-box">
                <table class="cart-table">
                    <tr>
                        <th>Số lượng</th>
                        <th>Sản phẩm</th>
                        <th class="right">Đơn giá</th>
                        <th class="right">Thành tiền</th>
                        <th></th>
                    </tr>
                    <c:forEach var="item" items="${cart.items}">
                        <tr>
                            <td>
                                <form action="cart" method="post" class="qty-form">
                                    <input type="hidden" name="productCode"
                                           value="<c:out value='${item.product.code}'/>">
                                    <input type="text" name="quantity" class="qty"
                                           value="<c:out value='${item.quantity}'/>">
                                    <input type="submit" value="Cập nhật" class="btn-small">
                                </form>
                            </td>
                            <td>
                                <div class="cart-product">
                                    <div class="cart-thumb">
                                        <img src="images/${item.product.code}.png" alt=""
                                             onerror="this.remove()">
                                    </div>
                                    <c:out value='${item.product.description}'/>
                                </div>
                            </td>
                            <td class="right">${item.product.priceCurrencyFormat}</td>
                            <td class="right"><b>${item.totalCurrencyFormat}</b></td>
                            <td class="right">
                                <form action="cart" method="post">
                                    <input type="hidden" name="productCode"
                                           value="<c:out value='${item.product.code}'/>">
                                    <input type="hidden" name="quantity" value="0">
                                    <input type="submit" value="Xoá" class="btn-remove">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr class="total-row">
                        <td colspan="3" class="right">Tổng cộng</td>
                        <td class="right price">${cart.totalCurrencyFormat}</td>
                        <td></td>
                    </tr>
                </table>
            </div>

            <p class="hint"><b>Muốn đổi số lượng</b>, nhập số mới rồi bấm Cập nhật.</p>

            <div class="cart-actions">
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="shop">
                    <input type="submit" value="Tiếp tục mua sắm" class="btn btn-outline">
                </form>
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="checkout">
                    <input type="submit" value="Thanh toán" class="btn btn-primary">
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<c:import url="/includes/footer.jsp" />
