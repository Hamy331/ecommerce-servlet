<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />
<c:import url="/includes/admin_topbar.jsp" />

<main class="shop">
    <p class="breadcrumb"><a href="products">Sản phẩm</a> /
        ${mode == 'edit' ? 'Sửa sản phẩm' : 'Thêm sản phẩm'}</p>

    <div class="checkout-box form-narrow">
        <h1 class="page-title">${mode == 'edit' ? 'Sửa sản phẩm' : 'Thêm sản phẩm'}</h1>
        <c:import url="/includes/message.jsp" />

        <form action="products" method="post">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="mode" value="${mode}">

            <label for="productCode">Mã sản phẩm</label>
            <c:choose>
                <c:when test="${mode == 'edit'}">
                    <input type="text" id="productCode" value="<c:out value='${product.code}'/>" disabled>
                    <input type="hidden" name="productCode" value="<c:out value='${product.code}'/>">
                </c:when>
                <c:otherwise>
                    <input type="text" id="productCode" name="productCode"
                           value="<c:out value='${product.code}'/>" placeholder="ví dụ: p07" required>
                </c:otherwise>
            </c:choose>

            <label for="description">Tên sản phẩm</label>
            <input type="text" id="description" name="description"
                   value="<c:out value='${product.description}'/>" required>

            <label for="price">Giá (đồng)</label>
            <input type="text" id="price" name="price"
                   value="<c:out value='${priceString}'/>"
                   placeholder="ví dụ: 199000" required>

            <p class="hint">Ảnh: đặt file <b>images/<c:out value="${empty product.code ? 'mã' : product.code}"/>.png</b>
                trong thư mục webapp, không có ảnh thì khung để trống.</p>

            <input type="submit" value="Lưu" class="btn btn-primary">
            <a href="products" class="btn btn-outline">Huỷ</a>
        </form>
    </div>
</main>

<c:import url="/includes/footer.jsp" />
