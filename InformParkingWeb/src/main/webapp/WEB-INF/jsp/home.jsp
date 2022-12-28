<%--
  Created by IntelliJ IDEA.
  User: kimchanghee
  Date: 22. 12. 16.
  Time: 오후 5:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html class="home_html">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="/css/home.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <title>주차장을 알리다</title>
</head>
<body class="home_body">
    <div class="left-top-layout">
        <div class="left-top-header">
            <h1 class="brand-title"><strong>주차장</strong>을 알리다</h1>
            <button class="user-inform-dropdown">
                <img src="https://t1.daumcdn.net/localimg/localimages/07/2018/pc/title/ico_menu.png" alt="drop down button">
            </button>
            <div class="search">
                <form class="keyword-search-form" onsubmit="return keywordSearch()">
                    <fieldset class="keyword-search-fieldset">
                        <div class="searchbar">
                            <input type="text" name="ks" placeholder="장소, 주소, 주차장 검색" class="keyword-searchBar" maxlength="100"
                            autocomplete="off">
                            <button class="search_button">
                                <img class="offmouse" src="https://t1.daumcdn.net/localimg/localimages/07/2018/pc/common/ico_search.png"
                                alter="search icon">
                                <img class="onmouse" src="https://t1.daumcdn.net/localimg/localimages/07/2018/pc/common/ico_search.png"
                                     alter="search icon">
                            </button>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
    <div class="left-bottom-layout">
        <div>주차장 정보에 대해 표시될 공간입니다.</div>
    </div>
    <div id="map" class="map-view"></div>
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=56aa35a066273ae1aab6fd6b7313eb8f"/>
    <script>
        var container = document.getElementById('map');
        var options = {
            center: new kakao.maps.LatLng(33.450701, 126.570667),
            level: 3
        };

        var map = new kakao.maps.Map(container, options);
    </script>
    <script src="/js/home.js"></script>
</body>
</html>
