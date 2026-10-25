<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/user?action=showLogin" />
</c:if>
<c:import url="/includes/header.jsp" />

<h1>Xin chào, <c:out value="${sessionScope.user.lastName} ${sessionScope.user.firstName}" />!</h1>
<p class="subtitle">Bạn đã đăng nhập bằng <c:out value="${sessionScope.user.email}" /></p>
<p class="subtitle">Trang chủ sản phẩm sẽ làm ở bước tiếp theo.</p>

<form action="user" method="post">
    <input type="hidden" name="action" value="logout">
    <input type="submit" value="Đăng xuất" class="btn btn-outline">
</form>

<c:import url="/includes/footer.jsp" />
