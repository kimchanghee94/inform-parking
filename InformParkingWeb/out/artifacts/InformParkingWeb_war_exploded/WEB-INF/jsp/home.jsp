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
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="shortcut icon" href="<:url value='/static/image/favicons96.ico'/>" type="image/x-icon"/>
    <link rel="icon" href="<c:url value='/static/image/favicons96.ico'/>" type="image/x-icon"/>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script src="https://developer.mozilla.org/ko/docs/Web/API/Geolocation_API"></script>
    <script type="text/javascript"
            src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9d3a5d31dd0c907fa7e70281e7a04d44&libraries=clusterer,services">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <link rel="stylesheet" href="/css/home.css">
    <script type="text/javascript" src="/js/home.js"></script>
    <script type="text/javascript" src="/js/home_admin.js"></script>
    <title>주차장을 알리다</title>
</head>
<body class="home_body">
    <div class="left-top-layout left-layout">
        <div class="left-top-header">
            <h1 class="brand-title"><strong>주차장</strong>을 알리다</h1>
            <a id="move-my-pos" href="#" onclick="moveCurPos()">현재 위치로</a>
            <button class="user-inform-button" data-bs-toggle="offcanvas" href="#offcanvas-start-id" role="button" aria-controls="offcanvas-start-id">
                <img src="https://t1.daumcdn.net/localimg/localimages/07/2018/pc/title/ico_menu.png" alt="drop down button">
            </button>
            <div class="offcanvas offcanvas-start" style="width: 300px" tabindex="-1" id="offcanvas-start-id" data-bs-keyboard="false" aria-labelledby="offcanvas-start-idLabel">
                <div class="offcanvas-header" id="offcanvas-header-id">
                    <h5 class="offcanvas-title" id="offcanvasExampleLabel"><strong>주차장</strong>을 알리다
                    <img src="/static/image/faviconsMac.svg" style="width: 30px; padding-bottom: 4px" alt="...">
                    </h5>
                    <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                </div>
                <div id="load-for-div-body-id">
                <div class="offcanvas-body" id="offcanvas-body-list-id">
                    <ul class="canvas-ul" id="offcanvas-body-list-top-ul-id">
                        <sec:authorize access="isAnonymous()">
                            <li><a id="submenu-login" href="login">로그인</a></li>
                            <li><a id="submenu-join" href="join">회원가입</a></li>
                        </sec:authorize>
                        <sec:authorize access="isAuthenticated()">
                            <li><a id="submenu-logout" href="login" onclick="logoutFunc()">로그아웃</a></li>
                            <li><a id="submenu-reserve" href="#">예매내역</a></li>
                            <li><a id="submenu-withdrawal" href="#">회원탈퇴</a></li>
                        </sec:authorize>
                    </ul>

                    <ul class="canvas-ul" id="offcanvas-body-list-bottom-ul-id" style="border-top: 1px solid black; padding-top:16px">
                        <!--<li><a href="#">관리자 신청하기</a></li>-->
                        <li><a href="#">공지사항</a></li>
                        <li><a href="#">고객센터</a></li>
                        <sec:authorize access="hasAnyRole('manager', 'admin')">
                            <li><a type="button" id="submenu-admin" data-bs-toggle="modal" href="#role-change-admin" onclick="initSettingParkingRegisterAdmin()">주차장 등록</a></li>

                            <!-- 관리자 전환 Modal -->
                            <!-- 앞으로 어떤 관리 창을 띄어야 할 경우 해당 버튼 아래에 기록하도록 하자 -->
                            <div class="modal fade" id="role-change-admin" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                <div class="modal-dialog modal-lg">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h1 class="modal-title fs-5" id="staticBackdropLabel">주차장 등록</h1>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onclick="showAdminRolePageInCanvas()"></button>
                                        </div>
                                        <div class="modal-body" id="parking-admin-change-modal-body">
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-outline-primary" onclick="addParkingAdminField(null, null)">관리 주차장 추가</button>
                                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="showAdminRolePageInCanvas()">확인</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </sec:authorize>
                        <sec:authentication property="principal" var="user"/>
                        <sec:authorize access="hasRole('admin')">
                            <li><a type="button" id="submenu-admin-page" data-bs-toggle="modal" href="#admin-role-page" onclick="initSettingParkingAdminRolePage()">관리자 전용 페이지</a></li>

                            <!-- 관리자 전용 페이지 -->
                            <!-- 캔버스 창에서 data-bs-keyboard="false"로 막아줌으로 자식 노드도 그에 따라 옵션을 따로 넣어줄 필요가 없다. true로 바꿔줘도 먹히지 않는다. -->
                            <div class="modal fade" id="admin-role-page" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                <div class="modal-dialog modal-lg">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h1 class="modal-title fs-5">관리자 전용 페이지</h1>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body" id="parking-admin-page-modal-body">
                                            <table class="table" id="parking-admin-page-table">
                                                <thead>
                                                <tr>
                                                    <th scope="col">주차장 관리 번호</th>
                                                    <th scope="col">주차장 이름</th>
                                                    <th scope="col">등록 일자</th>
                                                    <th scope="col">사용중인 차 대수</th>
                                                </tr>
                                                </thead>
                                                <tbody class="table-group-divider" id="parking-admin-page-table-body">
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </sec:authorize>
                    </ul>
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
                <!-- mouse가 버튼 밖으로 이동시 포커스를 없애기 위해 blur처리 -->
                <button type="button" class="btn btn-primary custom-button" id="day-buy-button" onmouseout="blur()">하루 정기권 구매</button>
                <button type="button" class="btn btn-primary custom-button" id="month-buy-button" onmouseout="blur()">월 정기권 구매</button>
                <button type="button" class="btn btn-primary custom-button" id="nav-road-button" onclick="naviRoad()" onmouseout="blur()">길찾기</button>
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
    <div class="map-view" id="kakao_map">
        <div id="spinner-content" class="">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </div>

    <!-- 토스트 레이아웃 -->
    <div class="toast-container" style="z-index: 11">
        <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="toast-header">
                <img src="/static/image/faviconsMac.svg" class="rounded me-2" alt="..." style="width:20px; height: 20px">
                <strong class="me-auto">주차장을 알리다</strong>
                <small>길찾기 정보</small>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body" id="nav-road-inform"></div>
        </div>
    </div>
<script type="text/javascript">
    /* WebSocket을 통해 주차장 남은 자리가 동적으로 바뀌도록 한다 */
   /* var sock = new SockJS('https://www.inparking.online/echo');
    sock.onmessage = onMessage;
    sock.onclose = onClose;
    sock.onopen = onOpen;

    function sendMessage(){
        console.log("여기에 parkingUseCnt값을 넘긴다");
        sock.send("Test Socket Test!!!");
    }

    //서버로부터 메세지를 받는다.
    function onMessage(msg){
        var data = msg.data;
        var sessionId = null; // 데이터를 보낸 사람
        var message = null;

        console.log("받은 메세지 : " + msg + ", 데이터 파밍" + data);
        var arr = data.split(":");

        for(var i=0; i<arr.length; i++){
            console.log('arr[' + i + ']: ' + arr[i]);
        }

        //현재 세션에 로그인 한 사람
        var cur_session = '${userid}';
        console.log("cur_session : " + cur_session);

        sessionId = arr[0];
        message = arr[1];

        console.log(sessionId + "가 보낸 메세지 : " + message);

    }

    //서버로부터 나감
    function onClose(evt){
        let user = '${userid}';
        console.log(user + "님이 퇴장하셨습니다.");
    }

    //서버 입장
    function onOpen(evt) {
        let user = '${userid}';
        console.log(user + "님이 입장하셨습니다.");
    }*/

    /* stomp로 구현 */
    var socket = null;

    function connectStomp(){
        var sock = new SockJS("/ws-stomp");
        var client = Stomp.over(sock);

        socket = client;

        client.connect({}, function(){
            console.log("Connected ws-stomp endpoing");
            client.send('/stompnoti', {}, "msg: Haha~~~");

            client.subscribe('/topic/message', function (event) {
                console.log("!!!!!!!!!event>>", event);
            })

        });
    };

    /*var stompClient;
    function serverConnect(){
        var socket = new SockJS('ws-stomp');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' +  frame);
            stompClient.subscribe('topic/ws-stomp', function(response){
                console.log(response);
                console.log(JSON.parse(response.body));
            })
        })
    }

    function clientConnect(){
        var socket = new SockJS('ws-stomp');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' +  frame);
            stompClient.subscribe('topic/a', function(response){
                console.log(response);
                console.log(JSON.parse(response.body));
            })
        })
    }

    function send(){
        console.log("sending");
        stompClient.send("app/stompnoti", {}, JSON.stringify({name:'testStomp'}));
    }*/
</script>
</body>
</html>
