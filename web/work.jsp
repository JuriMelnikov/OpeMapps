<%-- 
    Document   : work
    Created on : 10.03.2019, 21:24:35
    Author     : jvm
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1></h1>
        <ul>
            <c:forEach var="article" items="${listArticles}">
                <li>
                    <p>${article.caption}</p>
                    <p>${article.content}</p>
                    <p>${article.date}</p>
                </li>
            </c:forEach>
        </ul>
    </body>
</html>
