<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%--
  Created by IntelliJ IDEA.
  User: kimchanghee
  Date: 22. 12. 16.
  Time: 오후 5:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html class="home_html"/>
<head class="home_head">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width", initial-scale="1">
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.css">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="https://developer.mozilla.org/ko/docs/Web/API/Geolocation_API"></script>
    <script type="text/javascript"
            src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9d3a5d31dd0c907fa7e70281e7a04d44&libraries=clusterer,services">
    </script>
    <script src="/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="/js/home.js"></script>
    <link rel="stylesheet" href="/css/home.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <title>주차장을 알리다</title>
</head>
<body class="home_body">
    <div class="left-top-layout left-layout">
        <div class="left-top-header">
            <h1 class="brand-title"><strong>주차장</strong>을 알리다</h1>
            <a id="move-my-pos" href="#" onclick="moveCurPos()">현재 위치로</a>
            <button class="user-inform-button btn-modal" data-toggle="modal" data-target="#user-info-modal">
                <img src="https://t1.daumcdn.net/localimg/localimages/07/2018/pc/title/ico_menu.png" alt="drop down button">
            </button>
            <div class="modal" id="user-info-modal" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="modal-close-btn close" data-dismiss="modal">x</button>
                            <h4 class="modal-title"><strong>주차장</strong>을 알리다 🚙</h4>
                        </div>
                        <div class="modal-body">
                            <ul class="usr-inform-body">
                                <sec:authorize access="isAnonymous()">
                                    <li><a id="submenu-login" href="login">로그인</a></li>
                                    <li><a id="submenu-join" href="join">회원가입</a></li>
                                </sec:authorize>
                                <sec:authorize access="isAuthenticated()">
                                    <sec:authentication property="principal" var="user"/>
                                    <sec:authorize access="hasRole('admin')">
                                        <li><a id="submenu-admin-page" href="/loginResultView/${user.username}">관리자 전용 페이지</a></li>
                                    </sec:authorize>
                                    <li><a id="submenu-logout" href="login" onclick="logoutFunc()">로그아웃</a></li>
                                    <li><a id="submenu-reserve" href="#">예매내역</a></li>
                                    <li><a id="submenu-admin" href="#">관리자로 전환</a></li>
                                    <li><a id="submenu-withdrawal" href="#">회원탈퇴</a></li>
                                </sec:authorize>
                            </ul>
                        </div>
                        <div class="modal-footer">
                            <ul class="usr-inform-footer">
                                <!--<li><a href="#">관리자 신청하기</a></li>-->
                                <li><a href="#">공지사항</a></li>
                                <li><a href="#">고객센터</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="search">
                <form class="keyword-search-form" onsubmit="return false">
                    <fieldset class="keyword-search-fieldset">
                        <div class="searchbar">
                            <input id="search-place-input" type="text" name="ks" placeholder="장소, 주소, 주차장 검색" class="keyword-searchBar" maxlength="100"
                            autocomplete="off" onkeypress="enterkey()">
                            <button class="search_button" type="button" onclick="keywordSearch()" id="search-button-img">
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
        <div id="login-activate">
            <sec:authorize access="isAnonymous()">
                <h4 class="login-activate-text">안녕하세요😄</h4>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <h4 class="login-activate-text"><sec:authentication property="principal.username2"/>님 반갑습니다😄</h4>
            </sec:authorize>
        </div>
    </div>
    <div class="left-bottom-layout left-layout" value="markerClickFlag">
        <div class="left-bottom-main markerNonClick" id="markerNonClick-page">
            <div id="left-bottom-main-markerNonClick-text">
                마커를 클릭하시면 주차장 정보가 표시됩니다❗️
            </div>
        </div>
        <div class="left-bottom-main markerClick HIDDEN" id="markerClick-page">
            <div id="parking-basic-info">
                <h4 id="parking-name-text"></h4>
                <h4 id="parking-addr-text"></h4>
                <h4 id="parking-phone-text"></h4>
            </div>
            <div id="parking-operating-time">
                <h4 id="parking-sub-info"></h4>
                <ul id="time-list">
                    <li id="week-time-list"></li>
                    <li id="sat-time-list"></li>
                    <li id="holi-time-list"></li>
                </ul>
            </div>
            <div class="parking-price-info HIDDEN" id="pay-parking">
                <h4 class="parking-price-list" id="basic-price-text"></h4>
                <h4 class="parking-price-list" id="add-price-text"></h4>
                <h4 class="parking-price-list" id="empty"></h4>
                <h4 class="parking-price-list" id="day-price-text"></h4>
                <h4 class="parking-price-list" id="month-price-text"></h4>
                <button type="button" class="btn btn-primary custom-button" id="day-buy-button">하루 정기권 구매</button>
                <button type="button" class="btn btn-primary custom-button" id="month-buy-button">월 정기권 구매</button>
            </div>
            <div class="parking-price-info HIDDEN" id="non-pay-parking">
                <div id="non-pay-text">
                    무료주차장에는 가격정보를 따로 표시하지 않습니다😳️
                </div>
            </div>
        </div>
    </div>
    <div class="left-bottom-footer left-layout">
        <p id="footer-text">'주차장을 알리다's Homepage is powered by <span class="text-primary">Changhee</span></p>
    </div>
    <div class="map-view right-layout" id="kakao_map">
    </div>
</body>
</html>
