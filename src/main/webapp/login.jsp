<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />

<h1>Chào mừng trở lại!</h1>
<p class="subtitle">Đăng nhập vào tài khoản MiniShop của bạn</p>
<c:import url="/includes/message.jsp" />

<form action="user" method="post">
    <input type="hidden" name="action" value="login">

    <label for="email">Email</label>
    <input type="email" id="email" name="email"
           value="<c:out value='${email}'/>" placeholder="ban@email.com" required>

    <label for="password">Mật khẩu</label>
    <div class="password-field">
        <input type="password" id="password" name="password"
               placeholder="••••••" required>
        <button type="button" class="toggle-password" aria-label="Hiện mật khẩu"><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z"/><circle cx="12" cy="12" r="3"/><line class="slash" x1="3" y1="3" x2="21" y2="21"/></svg></button>
    </div>

    <p class="forgot">Quên mật khẩu? <a href="#">Đặt lại</a></p>

    <input type="submit" value="Đăng nhập" class="btn btn-primary">
</form>

<p class="divider"><span>Mới đến MiniShop?</span></p>
<a href="user?action=showRegister" class="btn btn-outline">Đăng ký</a>

<p class="demo-hint">Tài khoản mẫu: <b>demo@minishop.vn</b> / <b>123456</b></p>

<c:import url="/includes/footer.jsp" />
