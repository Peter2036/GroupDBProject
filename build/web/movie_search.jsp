<%-- 
    Document   : response
    Created on : Mar 25, 2017, 2:31:37 PM
    Author     : PurplePryana
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
  
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="<c:url value="/bootstrap-3.3.7-dist/css/bootstrap.css"/>" rel="stylesheet">
<strong><title>Movie Search</title></strong>
<form name ="home" action ="home" method ="GET" enctype =" text/plain">
<input type = "submit" style ="margin-bottom:10px;background-color:orange;color:blue;float:right" value = "Home">
</form>
</head>
<body style = "background-color:orange">
<div style = "background-color:blue">
<font color = "orange"><center><h1 style = "margin-top:0px;margin-bottom:-10px;margin-left:50px;" align ="center">Movie Search</h1></font>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>

