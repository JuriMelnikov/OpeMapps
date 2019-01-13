<%-- 
    Document   : page2
    Created on : Sep 26, 2018, 9:37:07 AM
    Author     : Melnikov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Главная</title>
    </head>
    <body>
        <h1>Навигация по сайту</h1>
        ${info}<br>
        <br>
        <a href="showLogin">Войти в систему</a><br>
        <a href="logout">Выйти из системы</a><br>
        <a href="newUser">добавить пользователя</a><br>
        
        
        <br>
        <p>Для администратора:</p>
        <a href="showListUsers">Список пользователей</a><br>
        <a href="showUserRoles">Назначение ролей пользователям</a>
        <br><br>
        Добавлен пользователь:<br>
        Имя: ${user.name}<br>
        Фамилия: ${user.surname}
        
    </body>
</html>
