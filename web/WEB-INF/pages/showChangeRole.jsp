<%-- 
    Document   : showChangeRole
    Created on : Feb 15, 2019, 1:49:31 PM
    Author     : Melnikov
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin panel</title>
    </head>
    <body>
        <h1>Страница администратора</h1>
        Список пользователей:
        <BR>
        <form action="changeRole" method="POST">
            <c:forEach var="role" items="${listRoles}">
                <c:if test="${role.id == 4}">
                    <p><input name="roleId" type="radio" checked value="${role.id}">${role.name}</p>
                </c:if>
                <c:if test="${role.id ne 4}">
                    <p><input name="roleId" type="radio" value="${role.id}">${role.name}</p>
                </c:if>    
                
            </c:forEach>
            <select name="userId" >
                <option value="#" selected></option>
                <c:forEach var="entry" items="${mapUsers}">
                    <option value="${entry.key.id}">${entry.key.person.name} ${entry.key.person.surname}, логин: "${entry.key.login}", роль: "${entry.value.name}"</option>
                </c:forEach>
            </select>
            <p><input type="submit" value="Назначить"></p>
        </form>
    </body>
</html>
