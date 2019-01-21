<%-- 
    Document   : editUserRoles
    Created on : Nov 7, 2018, 12:35:23 PM
    Author     : Melnikov
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
        <h1>Назначение ролей!</h1>
        <form action="changeUserRole" method="POST">
            <table>
                <tr>
                    <th>Пользователь</th>
                    <th>Новая роль</th>
                    <th></th>
                    <th></th>
                </tr>
                <tr>
                    <td>
                        <select name="user">
                            <option value="" disabled selected hidden></option>
                            <c:forEach var="entry" items="${mapUsers}">
                                <option value="${entry.key.id}">${entry.key.login}, роль: ${entry.value.name} </option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <select name="role">
                            <option value="" disabled selected hidden></option>
                            <c:forEach var="role" items="${listRoles}">
                                <option value="${role.id}">${role.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td><input type="submit" name="setButton" value="Назначить"></td>
                    <td><input type="submit" name="deleteButton" value="Удалить"></td>
                </tr>
                <tr>
                    <td col="4">
                        Добавить роль
                    </td>
                    
                </tr>
                <tr>
                    <td>Название</td>
                    <td>Уровень</td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        <input type="text" name="roleName">
                    </td>
                    <td><input type="text" name="layer"></td>
                    <td></td>
                    <td><input type="submit" name="addRoleButton" value="Добавить роль"</td>
                </tr>
                <tr>
                    <td col="4">
                        Удалить роль
                    </td>
                </tr>
                <tr>
                    <td>
                        <select name="deleteRoleId">
                            <option value="" disabled selected hidden></option>
                            <c:forEach var="deleteRole" items="${listRoles}">
                                <option value="${deleteRole.id}">${deleteRole.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td></td>
                    <td></td>
                    <td><input type="submit" name="deleteRoleButton" value="Удалить роль"</td>
                </tr>
            </table>
        </form>
    </body>
</html>
