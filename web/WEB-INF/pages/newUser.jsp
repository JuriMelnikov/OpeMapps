<%-- 
    Document   : newBook
    Created on : Sep 26, 2018, 10:50:01 AM
    Author     : Melnikov
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Новый читатель</title>
    </head>
    <body>
        <h1>Добавляем в библиотеку нового читателя</h1>
        <form action="addReader" method="POST" name="form1" id="_form1">
             Имя:<br>
            <input type="text" name="name"><br>
             Фамилия:<br>
            <input type="text" name="surname"><br>
             Электронная почта (email):<br>
            <input type="text" name="email"><br>
            <br>
             Логин<br>
            <input type="text" name="login"><br>
            <br>
             Пароль<br>
            <input type="password" name="password1"><br>
            <br>
             Повторите пароль<br>
            <input type="password" name="password2"><br>
            <br>
            <input type="submit" value="Добавить">
        </form><br>
    </body>
</html>
