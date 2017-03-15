<%-- 
    Document   : index
    Created on : Mar 15, 2017, 12:18:21 PM
    Author     : peter
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Movie Database</title>
    </head>
    <body>
    <center><h1>Movie Database</h1>
    <p>Peter Rose</p>
    <p>Pryana Lewis</p>
    <p>Mikael Fontanez</p>
    <p>Andrew Mon</p>
    </center>
    
    <div style ="margin-top: 80px" align = "center">
        <p> Search Movie Database for Actor, Movie, Composer, or Director </p>
        <div style align = "center">
        <label><input type="radio" name="Selection" value="" checked="checked" />Movie</label>
        <label><input type="radio" name="Selection" value="" checked="checked" />Actor/Actress</label>
        <label><input type="radio" name="Selection" value="" checked="checked" />Director</label>
        <label><input type="radio" name="Selection" value="" checked="checked" />Composer</label>
        </div>
        <form name="Search bar input" action="search.jsp" method="POST">
            <input type="text" name="Search bar" value="" size="50px" />
            <input type="submit" value="Search" name="Search button" />
        </form>
    </div>
    </body>
</html>
