<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/user?action=showLogin" />
</c:if>
<c:import url="/includes/header.jsp" />

<h1>Xin chào, <c:out value="${sessionScope.user.lastName} ${sessionScope.user.firstName}" />!</h1>
<p>Bạn đã đăng nhập bằng email <c:out value="${sessionScope.user.email}" />.</p>

<form action="user" method="post">
    <input type="hidden" name="action" value="logout">
    <input type="submit" value="Đăng xuất">
</form>

<c:import url="/includes/footer.jsp" />
