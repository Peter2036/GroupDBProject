<%-- 
    Document   : stats
    Created on : Apr 4, 2017, 1:06:24 AM
    Author     : peter
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
<strong><title>Information about this database application</title></strong>
<form name ="home" action ="home" method ="GET" enctype =" text/plain">
<input type = "submit" style ="margin-bottom:10px;background-color:orange;color:blue;float:right" value = "Home">
</form>
</head>
<body style = "background-color:orange">
<div style = "background-color:blue">
<font color = "orange"><center><h1 style = "margin-top:0px;margin-bottom:-10px;margin-left:50px;">Information about data in this database application</h1></font>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
    
<form name ="amountOfRecords" action ="records" method ="GET" enctype ="text/plain">
    <div style = "margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange" align = "center">
        <font color = "blue"><center><h3 style = "margin-top:10px">Get amount of records in database tables</h3></font>
        <input type = "submit" style ="margin-bottom:10px;background-color:blue;color:orange" value = "Show">
    </div>
</form>  
<form name ="amountOfRecordsChart" action ="recordsChartGetter" method ="GET" enctype ="text/plain">
    <div style = "margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange" align = "center">
        <font color = "blue"><center><h3 style = "margin-top:10px">Get amount of records in database as pie chart</h3></font>
        <input type = "submit" style ="margin-bottom:10px;background-color:blue;color:orange" value = "Show">
    </div>
</form>  
<form name ="query2" action ="actorsStarredIn10" method ="GET" enctype ="text/plain">
    <div style = "margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange" align = "center">
        <font color = "blue"><center><h3 style = "margin-top:10px">Get actors who have only starred in movies with a rating of 10</h3></font>
        <input type = "submit" style ="margin-bottom:10px;background-color:blue;color:orange" value = "Show">
    </div>
</form> 
<form name ="query1" action ="first50rankedMovies" method ="GET" enctype ="text/plain">
    <div style = "margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange" align = "center">
        <font color = "blue"><center><h3 style = "margin-top:10px">Get 50 highest ranked movies</h3></font>
        <input type = "submit" style ="margin-bottom:10px;background-color:blue;color:orange" value = "Show">
    </div>
</form>
<form name ="top10countriesQuery" action ="top10CountryGetter" method ="GET" enctype ="text/plain">
    <div style = "margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:orange" align = "center">
        <font color = "blue"><center><h3 style = "margin-top:10px">Get top 10 movie producing countries as pie chart</h3></font>
        <input type = "submit" style ="margin-bottom:10px;background-color:blue;color:orange" value = "Show">
    </div>
</form>  
</body>
</html>