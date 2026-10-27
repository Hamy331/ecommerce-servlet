<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/admin_topbar.jsp" />

<main class="shop">
    <div class="admin-head">
        <h1 class="page-title">Quản lý sản phẩm</h1>
        <a href="products?action=add" class="btn-add">+ Thêm sản phẩm</a>
    </div>
    <c:import url="/includes/message.jsp" />

    <div class="cart-box">
        <table class="cart-table">
            <tr>
                <th>Mã</th>
                <th>Sản phẩm</th>
                <th class="right">Giá</th>
                <th></th>
            </tr>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td><c:out value="${product.code}" /></td>
                    <td>
                        <div class="cart-product">
                            <div class="cart-thumb">
                                <c:if test="${product.hasImage}">
                                    <img src="${pageContext.request.contextPath}/image?code=${product.code}&amp;v=${product.imageVersion}" alt="">
                                </c:if>
                            </div>
                            <c:out value="${product.description}" />
                        </div>
                    </td>
                    <td class="right">${product.priceCurrencyFormat}</td>
                    <td class="right nowrap">
                        <div class="row-actions">
                        <a href="products?action=edit&amp;productCode=${product.code}" class="btn-small">Sửa</a>
                        <form action="products" method="post" class="inline"
                              onsubmit="return confirm('Xoá sản phẩm này?');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="productCode" value="<c:out value='${product.code}'/>">
                            <input type="submit" value="Xoá" class="btn-small btn-danger">
                        </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty products}">
                <tr><td colspan="4" class="empty-row">Chưa có sản phẩm nào.</td></tr>
            </c:if>
        </table>
    </div>
</main>

<c:import url="/includes/footer.jsp" />
