<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<!DOCTYPE html>
<html class="layout_html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width", initial-scale="1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
    <link rel="stylesheet" id="main-css" href=<tiles:getAsString name="css-path"/>/>
    <script src=<tiles:getAsString name="js-path"/>></script>
    <title id="haed-title"><tiles:getAsString name="title"/></title>
</head>
<body class="layout_body">
<!-- header -->
    <tiles:insertAttribute name="header"/>
<div class="login-join-info">
    <div class="column1">
        <!-- aside -->
            <tiles:insertAttribute name="aside"/>
    </div>
    <div class="column2">
        <!-- main -->
            <tiles:insertAttribute name="body"/>
    </div>
</div>
<!-- footer -->
    <tiles:insertAttribute name="footer"/>
</body>
</html>
