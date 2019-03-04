<%-- 
    Document   : upload
    Created on : 04.02.2019, 17:37:19
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
        <p>Загрузка файла</p>
        <p>
            <form action="upload" method="POST" enctype="multipart/form-data">
                <input type="text" name="description" />
                <input type="file" name="file" />
                <input type="submit" />
            </form>
        </p>
        <p>Доступные файлы</p>
              
        <c:forEach var="nameFile" items="${filesInFolder}">
            </>${nameFile.name}</p>
        </c:forEach>
                    
        
        
    </body>
</html>
