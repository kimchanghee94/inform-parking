/* stomp로 구현 */
var socket = null;

function connectStomp(){
    var sock = new SockJS("/ws-stomp");
    var client = Stomp.over(sock);
    var headers = {};

    //추후에 csrf나 jwt 이용해보자
    headers[header] = token;

    socket = client;

    client.connect(headers, function(){
        console.log("Connected ws-stomp endpoing");

        //구독을 함으로써 stompnoti로 메세지를 뿌리면 해당 클라이언트들은 메세지를 받는다.
        client.subscribe('/topic/message', function (event) {
            console.log("!!!!!!!!!event>>", event.body);
            console.log(selectedParkingNo, selectedReferenceDate, parseInt(selectedParkingCnt));
            let jsonBody = JSON.parse(event.body);
            console.log(jsonBody.parkingNo, jsonBody.referenceDate);
            if(selectedParkingNo !== null
                && selectedParkingNo ==  jsonBody.parkingNo
                && selectedReferenceDate == jsonBody.referenceDate){
                console.log("come here");
                let remH = document.getElementById("parking-remain-count");
                remH.innerHTML = (parseInt(selectedParkingCnt)-jsonBody.parkingUseCnt)+"대";
            }
        });

    });
};

/////////WebSocket으로 구현했을 때////////////
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
 function onOpen(evt) {xw
     let user = '${userid}';
     console.log(user + "님이 입장하셨습니다.");
 }*/