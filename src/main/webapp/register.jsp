<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/includes/header.jsp" />

<h1>Tạo tài khoản</h1>
<p class="subtitle">Đăng ký để bắt đầu mua sắm tại MiniShop</p>
<c:import url="/includes/message.jsp" />

<form action="user" method="post">
    <input type="hidden" name="action" value="register">

    <div class="row">
        <div>
            <label for="lastName">Họ</label>
            <input type="text" id="lastName" name="lastName"
                   value="<c:out value='${requestScope.user.lastName}'/>" placeholder="Nguyễn" required>
        </div>
        <div>
            <label for="firstName">Tên</label>
            <input type="text" id="firstName" name="firstName"
                   value="<c:out value='${requestScope.user.firstName}'/>" placeholder="An" required>
        </div>
    </div>

    <label for="email">Email</label>
    <input type="email" id="email" name="email"
           value="<c:out value='${requestScope.user.email}'/>" placeholder="ban@email.com" required>

    <label for="password">Mật khẩu</label>
    <div class="password-field">
        <input type="password" id="password" name="password"
               placeholder="Ít nhất 6 ký tự" required>
        <button type="button" class="toggle-password" aria-label="Hiện mật khẩu"><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z"/><circle cx="12" cy="12" r="3"/><line class="slash" x1="3" y1="3" x2="21" y2="21"/></svg></button>
    </div>

    <label for="confirmPassword">Nhập lại mật khẩu</label>
    <div class="password-field">
        <input type="password" id="confirmPassword" name="confirmPassword"
               placeholder="Nhập lại mật khẩu" required>
        <button type="button" class="toggle-password" aria-label="Hiện mật khẩu"><svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-7 11-7 11 7 11 7-4 7-11 7S1 12 1 12z"/><circle cx="12" cy="12" r="3"/><line class="slash" x1="3" y1="3" x2="21" y2="21"/></svg></button>
    </div>

    <input type="submit" value="Đăng ký" class="btn btn-primary">
</form>

<p class="divider"><span>Đã có tài khoản?</span></p>
<a href="user?action=showLogin" class="btn btn-outline">Đăng nhập</a>

<c:import url="/includes/footer.jsp" />
