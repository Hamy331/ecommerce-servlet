<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />

<h1>Đăng nhập</h1>
<p class="message"><c:out value="${message}" /></p>

<form action="user" method="post">
    <input type="hidden" name="action" value="login">
    <label>Email</label>
    <input type="email" name="email" value="<c:out value='${email}'/>" required><br>
    <label>Mật khẩu</label>
    <input type="password" name="password" required><br>
    <label>&nbsp;</label>
    <input type="submit" value="Đăng nhập" class="margin_left">
</form>

<p>Chưa có tài khoản? <a href="user?action=showRegister">Đăng ký</a></p>

<c:import url="/includes/footer.jsp" />
