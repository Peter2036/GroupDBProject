<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
  
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="<c:url value="/bootstrap-3.3.7-dist/css/bootstrap.css"/>" rel="stylesheet">
<strong><title>Gator Movie Database</title></strong>
</head>
<body style = "background-color:blue">
<div style = "background-color:blue">
<font color = "orange"><center><h1 style = "margin-top:0px;margin-bottom:0px">Gator Movie Database</h1></font>
</div>
<div align = "center" style = "background-color: orange">    
    <strong><p style = "color:blue">Peter Rose</p></strong>
    <strong><p style = "color:blue">Pryana Lewis</p></strong>
    <strong><p style = "color:blue">Mikael Fontanez</p></strong>
    <strong><p style = "color:blue">Andrew Mon</p></strong>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<form name ="basicStatsButtons" action ="stats" method ="GET" enctype ="text/plain">
    <div style = "margin-top:-10px;margin-bottom:10px;border-color: orange;background-color:blue" align = "center">
        <font color = "orange"><center><h3 style = "margin-top:10px">Get info about data in this database</h3></font>
        <input type = "submit" style ="margin-bottom:20px;background-color:orange;color:blue" value = "Show">
    </div>
</form>

<form name="Search bar input" action="Searchbar" method="GET" enctype = text/plain>
    <div style ="margin-top:-20px;background-color:orange" align = "center">     
    <font color = "blue"><center><h3 style = "margin-top:20px">Search individual records</h3></font>
    <label style ="margin-bottom:20px;background-color:orange;color:blue" class = "radio-inline"><input type="radio" name="Selection" checked = "checked" value="0"/>Movies</label>        
    <label style ="margin-bottom:20px;background-color:orange;color:blue" class = "radio-inline"><input type="radio" name="Selection" value="1"/>Stars</label>
    <label style ="margin-bottom:20px;background-color:orange;color:blue" class = "radio-inline"><input type="radio" name="Selection" value="2"/>Directors</label>                
    <label style ="margin-bottom:20px;background-color:orange;color:blue" class = "radio-inline"><input type="radio" name="Selection" value="3"/>Writers</label>            
    </div>  
 
    <div style ="margin-top:0px;background-color:orange" align = "center">
    <input type="text" class ="form-group-sm" name="Search bar" value="" size="50px" />
    <input type="submit" style ="margin-bottom:20px;background-color:blue;color:orange" class ="btn-form" value="Search" name="Search button" />
    </div>
</form>    
</body>
</html>