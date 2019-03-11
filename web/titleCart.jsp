<%-- 
    Document   : titleCart
    Created on : 10.03.2019, 18:36:52
    Author     : jvm
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Учебная карта</title>
    </head>
    <body>
        <h1>Учебная карта</h1>
        <p>${team.name} ${peaple.name} ${peaple.surname}</p>
        <p>
            <c:forEach var="subject" items="${listSubjects}">
                <span><a href="showTitleCart?subjectId=${subject.id}">${subject.name}</a></span>
            </c:forEach>
        </p>
        
        <p>
            <c:forEach var="entry" items="${mapWorksInSubject}">
                <span><a href="showTitleCart?workId=${entry.key.id}&subjectId=${entry.value}">${entry.key.name}</a></span>
            </c:forEach>
        </p>
        <p>
            <ul>
            <c:forEach var="article" items="${work.articles}">
                <li>
                    <p>${article.caption}</p>
                    <p>${article.content}</p>
                    <p>${article.date}</p>
                </li>
            </c:forEach>
        </ul>
        </p>
    </body>
</html>
