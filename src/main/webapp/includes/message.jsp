<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty message}">
    <p class="message ${messageType == 'success' ? 'success' : 'error'}">
        <span class="message-icon">${messageType == 'success' ? '&#10003;' : '!'}</span>
        <c:out value="${message}" />
    </p>
</c:if>
