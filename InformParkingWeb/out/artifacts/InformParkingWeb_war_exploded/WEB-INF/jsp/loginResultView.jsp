<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%--
  Created by IntelliJ IDEA.
  User: kimchanghee
  Date: 22. 12. 17.
  Time: 오후 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>조회 결과</title>
    <meta name="viewport" content="width=device-width", initial-scale="1">
    <link rel="shortcut icon" href="<c:url value='/static/image/favicons96.ico'/>" type="image/x-icon"/>
    <link rel="icon" href="<c:url value='/static/image/favicons96.ico'/>" type="image/x-icon"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
id: ${member.id}
password: ${member.passwd}
phone: ${member.phone}
name: ${member.userName}
car number: ${member.carNum}

<button type="button" class="btn btn-primary custom-button" id="day-buy-button" onmouseout="blur()"
        data-bs-toggle="modal" data-bs-target="#check-car-number-modal" onclick="checkCarNum(0)">하루 정기권 구매</button>
<button type="button" class="btn btn-primary custom-button" id="month-buy-button" onmouseout="blur()"
        data-bs-toggle="modal" data-bs-target="#check-car-number-modal" onclick="checkCarNum(1)">월 정기권 구매</button>

<!-- 차량번호 확인 모달 -->
<div class="modal fade" id="check-car-number-modal" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable" style="width:350px">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5">차량 번호 확인</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" onmouseout="blur()"></button>
            </div>
            <div class="modal-body" id="check-car-number-modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-danger" data-bs-dismiss="modal" onmouseout="blur()">닫기</button>
                <button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="kakaoPay()" onmouseout="blur()">확인</button>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script type="text/javascript">
    $('#check-car-number-modal').on('hidden.bs.modal', function () {
        document.body.focus();
    });
</script>

<input type="text" id="message" />
<input type="button" id="sendBtn" value="submit"/>
<div id="messageArea"></div>

<script type="text/javascript">
    $("#sendBtn").click(function() {
        console.log("click");
        sendMessage();
        $('#message').val('')
    });

    var sock = new SockJS("/ws-stomp");
    var client = Stomp.over(sock);
    // 메시지 전송

    client.connect({}, function(){
        console.log("Connected ws-stomp endpoing");

        //구독을 함으로써 stompnoti로 메세지를 뿌리면 해당 클라이언트들은 메세지를 받는다.
        client.subscribe('/topic/message', function (event) {
            console.log("!!!!!!!!!event>>", event.body);
            $("#messageArea").append(event.body + "<br/>");
        });
    });

    function sendMessage() {
        console.log("click");

        client.send('/stompnoti', {}, $("#message").val());
    }
    // 서버와 연결을 끊었을
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
