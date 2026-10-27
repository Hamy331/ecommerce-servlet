<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />

<c:import url="/includes/topbar.jsp" />

<main class="shop">
    <h1 class="page-title">Sản phẩm</h1>
    <p class="subtitle left">Chọn sản phẩm bạn thích để xem chi tiết.</p>

    <form action="home" method="get" class="search-bar">
        <input type="hidden" name="action" value="search">
        <span class="search-input">
            <svg class="icon"><use href="#i-search"/></svg>
            <input type="text" name="keyword" value="<c:out value='${keyword}'/>"
               placeholder="Tìm theo tên hoặc mã sản phẩm...">
        </span>
        <input type="submit" value="Tìm kiếm" class="btn-search">
    </form>

    <c:if test="${not empty keyword}">
        <p class="search-result">
            Tìm thấy ${products.size()} sản phẩm cho
            "<b><c:out value="${keyword}" /></b>".
            <a href="home">Xem tất cả</a>
        </p>
    </c:if>

    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <div class="product">
                <div class="thumb">
                    <c:if test="${product.hasImage}">
                        <img src="image?code=${product.code}"
                             alt="<c:out value='${product.description}'/>">
                    </c:if>
                </div>
                <div class="product-body">
                    <p class="product-name"><c:out value="${product.description}" /></p>
                    <p class="price">${product.priceCurrencyFormat}</p>
                    <a href="home?action=detail&amp;productCode=${product.code}" class="btn btn-primary">Xem chi tiết</a>
                </div>
            </div>
        </c:forEach>
    </div>
    <c:if test="${empty products}">
        <p class="empty-search"><svg class="icon icon-lg"><use href="#i-search"/></svg>Không có sản phẩm nào phù hợp.</p>
    </c:if>
</main>

<c:import url="/includes/footer.jsp" />
