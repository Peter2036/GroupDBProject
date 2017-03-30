<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

    
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
            <sql:query var="result" dataSource="jdbc/group">
                SELECT Directors.name FROM Directors
            </sql:query>
                
            <table border="1">
                <!-- column headers -->
                <tr border = "0">
                    <c:forEach var="columnName" items="${result.columnNames}">
                        <th><c:out value="${columnName}"/></th>
                        </c:forEach>
                </tr>
                <!-- column data -->
                <c:forEach var="row" items="${result.rowsByIndex}">
                    <tr border = "0">
                        <c:forEach var="column" items="${row}">
                            <td><c:out value="${column}"/></td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </form>
    </div>
    </body>
</html>