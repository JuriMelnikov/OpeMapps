<%-- 
    Document   : istReader
    Created on : Oct 5, 2018, 9:11:16 AM
    Author     : Melnikov
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Список читателей</title>
    </head>
    <body>
        <h1>Список читателей</h1>
        <ul>
            <c:forEach var="user" items="${listUser}">
                <li>${user.name} ${user.surname}</li>
            </c:forEach>
        </ul>
    </body>
</html>
