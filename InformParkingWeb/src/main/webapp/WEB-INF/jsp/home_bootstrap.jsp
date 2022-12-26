<%--
  Created by IntelliJ IDEA.
  User: kimchanghee
  Date: 22. 12. 16.
  Time: 오후 5:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width", initial-scale="1">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css">
    <title>주차장을 알리다 🚗</title>
</head>
<body>
    <nav class="navbar navbar-default">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
                    aria-expanded="false">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="home">주차장을 알리다 🚗</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="home">메인</a></li>
                <li><a href="loginResultView/changhee94">나의 쿠폰</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle"
                       data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">접속하기<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li class="active"><a href="home">로그인</a></li>
                        <li><a href="home">회원가입</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="/bootstrap/js/bootstrap.js"></script>
</body>
</html>
