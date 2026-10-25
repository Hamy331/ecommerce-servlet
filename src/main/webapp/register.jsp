<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />

<h1>Đăng ký tài khoản</h1>
<p class="message"><c:out value="${message}" /></p>

<form action="user" method="post">
    <input type="hidden" name="action" value="register">
    <label>Họ</label>
    <input type="text" name="lastName" value="<c:out value='${user.lastName}'/>" required><br>
    <label>Tên</label>
    <input type="text" name="firstName" value="<c:out value='${user.firstName}'/>" required><br>
    <label>Email</label>
    <input type="email" name="email" value="<c:out value='${user.email}'/>" required><br>
    <label>Mật khẩu</label>
    <input type="password" name="password" required><br>
    <label>Nhập lại mật khẩu</label>
    <input type="password" name="confirmPassword" required><br>
    <label>&nbsp;</label>
    <input type="submit" value="Đăng ký" class="margin_left">
</form>

<p>Đã có tài khoản? <a href="user?action=showLogin">Đăng nhập</a></p>

<c:import url="/includes/footer.jsp" />
