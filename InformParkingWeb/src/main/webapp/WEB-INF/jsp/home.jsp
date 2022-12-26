<%--
  Created by IntelliJ IDEA.
  User: kimchanghee
  Date: 22. 12. 16.
  Time: 오후 5:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html class="html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="/css/home.css">
    <title>주차장을 알리다 🚗</title>
</head>
<body class="body">
        <div class="left-layout">
            <div class="left-layout-top">
                <div class="navbar">
                    <button type="button" class="navbar-toggle collapsed"
                            data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
                            aria-expanded="false">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <div class="brand_name">주차장을 알리다 🚗</div>
                </div>
                <div class="search">
                    <input type="text" placeholder="장소, 주소, 주차장 검색">
                    <img src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png">
                </div>
            </div>
            <div class="left-layout-bottom">
                <div class="information_parking">
                    선택된 주차장 정보가 표시됩니다.
                </div>
            </div>
        </div>
        <div class="right-layout">
            <div>맵이 표시될 공간입니다.</div>
        </div>
</body>
</html>
