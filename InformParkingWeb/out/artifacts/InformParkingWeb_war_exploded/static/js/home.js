$(document).ready(
    execMap(0, null),
);

//csrf 적용으로 인해 Post로 전송 시 token을 담아 보내야한다.
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

/* 회원 탈퇴 */
function logoutFunc(){
    $.ajax({
        type : "post",
        url : "/logout",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        }
    });
}

/* 주소 검색 메서드*/
function enterkey(){
    if(window.event.keyCode == 13){
        keywordSearch();
    }
}

/* 주차장 관리하기 페이지 테이블 동적 추가 */
function adminRolePage(parkingNo, parkingName, referenceDate){
    var obj = document.getElementById("parking-admin-page-table-body");

    /* 레이아웃 설정 */
    var newTr = document.createElement("tr");
    newTr.id = "admin-page-table-row-id-" + parkingNo;
    var newTh = document.createElement("th");
    newTh.scope="row";
    var pnameTd = document.createElement("td");
    var refTd = document.createElement("td");
    var inputTd = document.createElement("td");
    var inputTag = document.createElement("input");
    inputTag.type="text";
    inputTag.name="parkingCnt";
    inputTag.class="form-control";
    inputTag.placeholder="숫자 입력";
    inputTag.style.width="100px";

    console.log(parkingNo + ", " + referenceDate);

    newTh.innerHTML = parkingNo;
    pnameTd.innerHTML = parkingName;
    refTd.innerHTML = referenceDate;

    /* 태그 조합 */
    inputTd.appendChild(inputTag);
    newTr.appendChild(newTh);
    newTr.appendChild(pnameTd);
    newTr.appendChild(refTd);
    newTr.appendChild(inputTd);

    obj.appendChild(newTr);
}

/* 관리 주차장 추가하기 */
function addParkingAdmin(){
    var obj = document.getElementById("parking-admin-change-modal-body");

    /* 레이아웃 설정 */
    var newDiv = document.createElement("div");
    newDiv.className="input-group mb-0";

    var newSpan1 = document.createElement("span");
    newSpan1.className="input-group-text";
    newSpan1.innerHTML="주차장 고유번호";

    var newInput1 = document.createElement("input");
    newInput1.className="form-control";
    newInput1.type="text";
    newInput1.name="parkingNo";
    newInput1.placeholder="(-) 하이폰까지 표기";

    var newSpan2 = document.createElement("span");
    newSpan2.className="input-group-text";
    newSpan2.innerHTML="주차장 등록일";

    var newInput2 = document.createElement("input");
    newInput2.className="form-control";
    newInput2.type="text";
    newInput2.name="referenceDate";
    newInput2.placeholder="ex) 2023-01-01";

    var newAuthDiv = document.createElement("div");
    newAuthDiv.className="auth-check-result";
    newAuthDiv.style.marginBottom ="8px";

    var newButton1 = document.createElement("button");
    newButton1.type="button";
    newButton1.className="btn btn-primary";
    newButton1.innerHTML="인증";
    newButton1.onmouseout=blur();

    newButton1.onclick=function() {
        $.ajax({
            type : "post",
            url : "/getParkingAuthCheck",
            data : {
                parkingNo : newInput1.value.toString().trim(),
                referenceDate : newInput2.value.toString().trim()
            },
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            statusCode: {
                200: function (data) {
                    console.log("get parking auth success");
                },
                404: function (data) {
                    console.log("get parking auth failed");
                }
            },
            success : function(response){
                if(response.header.statusCode == "00"){
                    newAuthDiv.style.color="blue";
                    newAuthDiv.innerHTML="인증에 성공하였습니다.";
                    addDBAdminRolePage(newInput1.value.toString().trim(),
                        newInput2.value.toString().trim());
                }else if(response.header.statusCode == "01"){
                    newAuthDiv.style.color="red";
                    newAuthDiv.innerHTML="인증에 실패하였습니다.";
                }else if(response.header.statusCode == "02"){
                    newAuthDiv.style.color="red";
                    newAuthDiv.innerHTML="인증 정보가 중복된 주차장입니다.";
                }else if(reponse.header.statusCode == "03"){
                    newAuthDiv.style.color="red";
                    newAuthDiv.innerHTML="이미 등록된 주차장입니다.";
                }
            }
        });
    };

    var newButton2 = document.createElement("button");
    newButton2.type="button";
    newButton2.className="btn btn-outline-danger";
    newButton2.innerHTML="삭제";
    newButton2.onclick=function() {
        var divP = this.parentElement; //부모 div
        var bodyP = divP.parentElement;
        bodyP.removeChild(newAuthDiv);
        bodyP.removeChild(divP); // 자신을 부모 태그로 부터 제거

        /*관리자 페이지에서도 제거*/
        var adminPageBody = document.getElementById("parking-admin-page-table-body");
        var adminPageTableRow = document.getElementById("admin-page-table-row-id-" + newInput1.value.toString().trim());
        if(adminPageTableRow !== null)
            adminPageBody.removeChild(adminPageTableRow);
    };

    /* 레이아웃 조합 */
    newDiv.appendChild(newSpan1);
    newDiv.appendChild(newInput1);
    newDiv.appendChild(newSpan2);
    newDiv.appendChild(newInput2);
    newDiv.appendChild(newButton2);
    newDiv.appendChild(newButton1);

    obj.appendChild(newDiv);
    obj.appendChild(newAuthDiv);
}

/* 기존에 데이터가 없고 첫번째 화면 등장하는 첫 인덱스에 대한 주차장 관리 정보 인증하기 */
function checkAuthParkingAdmin(){
    var parkingNo = document.getElementById('parkingNo-id-0').value.toString().trim();
    var referenceDate = document.getElementById('referenceDate-id-0').value.toString().trim();
    var data = {
        parkingNo : parkingNo,
        referenceDate : referenceDate
    };

    $.ajax({
        type : "post",
        url : "/getParkingAuthCheck",
        data : data,
        dataType : "json",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function (data) {
                console.log("get parking auth success");
            },
            404: function (data) {
                console.log("get parking auth failed");
            }
        },
        success : function(response){
            if(response.header.statusCode == "00"){
                document.getElementById("auth-check-id-0").style.color="blue";
                document.getElementById("auth-check-id-0").innerHTML="인증에 성공하였습니다.";
                addDBAdminRolePage(parkingNo, referenceDate);
            }else if(response.header.statusCode =="01"){
                document.getElementById("auth-check-id-0").style.color="red";
                document.getElementById("auth-check-id-0").innerHTML="인증에 실패하였습니다.";
            }else if(response.header.statusCode =="02"){
                document.getElementById("auth-check-id-0").style.color="red";
                document.getElementById("auth-check-id-0").innerHTML="인증 정보가 중복된 주차장입니다.";
            }else if(response.header.statusCode == "03"){
                document.getElementById("auth-check-id-0").style.color="red";
                document.getElementById("auth-check-id-0").innerHTML="이미 등록된 주차장입니다.";
            }
        }
    });
}

/* 기존에 데이터가 없고 첫번째 화면 등장하는 첫 인덱스에 대한 주차장 관리 정보 삭제하기 */
function deleteAdminParkingRow(curElem){
    var divP = curElem.parentElement; //부모 div
    var bodyP = divP.parentElement;
    bodyP.removeChild(document.getElementById("auth-check-id-0"));
    bodyP.removeChild(divP); // 자신을 부모 태그로 부터 제거

    /*관리자 페이지에서도 제거*/
    var adminPageBody = document.getElementById("parking-admin-page-table-body");
    var parkingNo = document.getElementById("parkingNo-id-0").value.toString().trim();
    var adminPageTableRow = document.getElementById("admin-page-table-row-id-" + parkingNo);
    if(adminPageTableRow !== null)
        adminPageBody.removeChild(adminPageTableRow);
}

/* DB에 인증 된 주차장 정보 넣기 */
function addDBAdminRolePage(parkingNo, referenceDate){
    $.ajax({
        type : "post",
        url : "/addDBAdminRolePage",
        data : {
            parkingNo : parkingNo,
            referenceDate : referenceDate
        },
        dataType : "json",
            beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function (data) {
                console.log("get parking auth success");
            },
            404: function (data) {
                console.log("get parking auth failed");
            }
        },
        success : function(response){
            if(response.header.statusCode == "00"){
                console.log("add data success");
                adminRolePage(response.body.parkingNo, response.body.parkingName, response.body.referenceDate);
            }else {
                console.log("add data fail");
            }
        }
    });
}

/* 맵 작업 */
var map;
var ps;
var mylatitude;
var mylongitude;
var initMapLevel;
var clusterer;
var markers=[];

/*인포윈도우 클릭 후 유지 조건 분기 처리에 필요한 변수 */
var selectedMarker;
var clickInfowindows = [];

/*내비 길찾기에 필요한 변수*/
var selectedMarkerLat;
var selectedMarkerLng;

/* 내비경로 폴리라인 요소 */
var startPoly;
var endPoly;
var linePoly;
var lineOrderNum = [];

function keywordSearch() {
    var keywordValue = document.getElementById("search-place-input");
    if(keywordValue.value != null && keywordValue.value != ""){
        execMap(1, keywordValue.value);
    }
}

/*현재 위치로*/
function moveCurPos() {
    execMap(2, null);
}

function execMap(kmFlag, keywordValue){
    navigator.geolocation.getCurrentPosition(
      function (position) {
          console.log("kmFlag : " + kmFlag + ", kewordValue" + keywordValue);
            mylatitude = position.coords.latitude;
            mylongitude = position.coords.longitude;
            console.log(mylatitude + ", " + mylongitude)
            if(kmFlag ==0){
                map = new kakao.maps.Map(document.getElementById('kakao_map'), { // 지도를 표시할 div
                    center : new kakao.maps.LatLng(mylatitude, mylongitude), // 지도의 중심좌표
                    level : 5 // 지도의 확대 레벨
                });

                // 마커 클러스터러를 생성합니다
                clusterer = new kakao.maps.MarkerClusterer({
                    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
                    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
                    minLevel: 6 // 클러스터 할 최소 지도 레벨
                });

                mapInitSetting();
                getMapViewMarkers();
            }else if(kmFlag==1){
                ps = new kakao.maps.services.Places();
                ps.keywordSearch(keywordValue, placesSearchCB);

                /*키워드 검색 시 경로 infowindow를 지워준다. */
                if(clickInfowindows.length !== 0) {
                    clickInfowindows[0].close();
                    clickInfowindows= [];
                }

                /*poly라인도 초기화*/
                initPolyLine();

                // 키워드 검색 완료 시 호출되는 콜백함수 입니다
                function placesSearchCB (data, status, pagination) {
                    if (status === kakao.maps.services.Status.OK) {

                        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                        // LatLngBounds 객체에 좌표를 추가합니다
                        var bounds = new kakao.maps.LatLngBounds();
                        for (var i=0; i<data.length; i++) {
                            bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
                        }
                        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                        // KCH 경로타는 가운데 비동기 실시간 내 위치에 주차장
                        map.setBounds(bounds);
                        getMapViewMarkers();
                    }else if(status === kakao.maps.services.Status.ZERO_RESULT){
                        document.getElementById("markerClick-page").className = "left-bottom-main markerClick HIDDEN";
                        document.getElementById("markerNonClick-page").className = "left-bottom-main markerClick";

                        document.getElementById("left-bottom-main-markerNonClick-text").innerHTML=
                            '<h4 style="font-size: 20px;padding-left: 38px">'+ "검색 결과가 없습니다.😅" + '</h4>';
                    }else if(status === kakao.maps.services.Status.ERROR){
                        document.getElementById("markerClick-page").className = "left-bottom-main markerClick HIDDEN";
                        document.getElementById("markerNonClick-page").className = "left-bottom-main markerClick";

                        document.getElementById("left-bottom-main-markerNonClick-text").innerHTML=
                            '<h4 style="font-size: 15px;padding-left: 5px">'+ "검색 과정에 서버 에러가 발생하였어요.😵" + '</h4>';
                    }
                }

            } else if(kmFlag==2){
                // map.setCenter(new kakao.maps.LatLng(mylatitude, mylongitude));
                map.setLevel(initMapLevel);
                map.panTo(new kakao.maps.LatLng(mylatitude, mylongitude));
                getMapViewMarkers();
            }
      }
    );
}

function mapInitSetting(){
    var zoomControl = new kakao.maps.ZoomControl();
    map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
    initMapLevel = map.getLevel(); //현재 map level로 초기화를 시켜둔다
    // 지도가 확대 또는 축소되면 마지막 파라미터로 넘어온 함수를 호출하도록 이벤트를 등록합니다
    //확대 이벤트
    kakao.maps.event.addListener(map, 'zoom_changed', function() {
        // 레벨에 따라 클러스터러에 마커들을 추가
        if(map.getLevel() > 7){
            clusterer.clear();

        }else{
            getMapViewMarkers();
        }

        if(clickInfowindows.length !== 0) {
            if (map.getLevel() > 6) {
                clickInfowindows[0].close();
            }else {
                clickInfowindows[0].open(map, selectedMarker);
            }
        }
    });

    //이동 이벤트
    kakao.maps.event.addListener(map, 'dragend', function() {
        if(map.getLevel() < 8 ){
            getMapViewMarkers();
        }
    });

    //지도 클 이벤릭트
    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
        if(clickInfowindows.length !== 0) {
            clickInfowindows[0].close();
        }
    });

    /* 해당 메서드는 json파일로 데이터를 불러올 때 사용한 메서드
    function getMarker(){
        $.get("/json/parking.json", function(data) {
            // 데이터에서 좌표 값을 가지고 마커를 표시합니다
            // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
            markers = $(data.positions).map(function(i, position) {
                var neLat = map.getBounds().getNorthEast().getLat();
                var neLng = map.getBounds().getNorthEast().getLng();
                var swLat = map.getBounds().getSouthWest().getLat();
                var swLng = map.getBounds().getSouthWest().getLng();

                if(swLng < position.lng && position.lng < neLng
                    && swLat < position.lat && position.lat < neLat){
                    return new kakao.maps.Marker({
                        position : new kakao.maps.LatLng(position.lat, position.lng)
                    });
                }
            });
            clusterer.addMarkers(markers);
        });
    }*/

    /*  var latlngJson;
//전역변수에 담기 위해 ajax 설정에 async false로 해주었다.
  $.ajax({
      type : "post",
      url : "/getLatLng",
      async : false,
      success : function(result){
          if(result != null){
              latlngJson = result;
          }
      }
  });

  console.log(latlngJson);
  console.log(JSON.parse(latlngJson));

  // 데이터에서 좌표 값을 가지고 마커를 표시합니다
  // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
  var markers = $(JSON.parse(latlngJson).positions).map(function(i, position) {
      return new kakao.maps.Marker({
          position : new kakao.maps.LatLng(position.lat, position.lng)
      });
  });*/
    //위에서 처럼 위도와 경도를 api를 매페이지 전부 호출해 가져오게 될경우 시간 비효율로 인해 json파일에 담아 빠르게 불러올 수 있도록 한다.
}

//db를 통해 데이터를 가져와 사용한다.
function getMapViewMarkers(){
    var itemInfoJson;
    var neLat = map.getBounds().getNorthEast().getLat();
    var neLng = map.getBounds().getNorthEast().getLng();
    var swLat = map.getBounds().getSouthWest().getLat();
    var swLng = map.getBounds().getSouthWest().getLng();

    markers=[];

    var data = {
        neLat : neLat,
        neLng : neLng,
        swLat : swLat,
        swLng : swLng
    };
    $.ajax({
        type : "post",
        url : "/getMapViewMarkers",
        data : data,
        dataType : "json",
        async : false,
        statusCode: {
            200 : function(data){
                console.log("get MapViewMarkers success");
            },
            404 : function(data){
                console.log("get MapViewMarkers failed");
            }
        },
        async : false,
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(response){
            console.log(response.header.statusCode);
            if(response.header.statusCode == "00"){
                itemInfoJson = response.body;
            }else if(response.header.statusCode =="01"){
                itemInfoJson = response.body;
            }
        }
    });

    $(itemInfoJson.items).map(function(i, item) {
        var marker =  new kakao.maps.Marker({
            map : map,
            position : new kakao.maps.LatLng(item.latitude, item.longitude),
            clickable: true
        });

        var infowindow = new kakao.maps.InfoWindow({
            content: '<span class="info-title">' + item.parkingName + '</span>'
                /*'<div style="padding-left: 5px;">'
                +item.parkingName
                +'</div>' // 인포윈도우에 표시할 내용*/
                ,
            /*인포윈도우가 뒤로 가려지는 현상 때문에 설정 */
            zIndex : 10
        });

        kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
        kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(marker, infowindow));
        kakao.maps.event.addListener(marker, 'click', function() {
            /* click한 인포윈도우 값이 비어있지 않을 경우 기존 인포윈도우를 닫아주고 지운다 */
            if(clickInfowindows.length !== 0){
                clickInfowindows[0].close();
                clickInfowindows = [];
            }

            infowindow.open(map, marker);

            if (matchMedia("screen and (max-width: 600px)").matches) {
                console.log("phone info css Custom action");
                infoCssCustom();
            }

            clickInfowindows.push(infowindow);
            selectedMarker = marker;

            /* 내비의 목적지를 담는다. */
            selectedMarkerLat = item.latitude;
            selectedMarkerLng = item.longitude;

            document.getElementById("markerNonClick-page").className = "left-bottom-main markerNonClick HIDDEN";
            document.getElementById("markerClick-page").className = "left-bottom-main markerClick";

            var parkingName = document.getElementById("parking-name-text");
            var phone = document.getElementById("parking-phone-text");
            var rdnmadr = document.getElementById("parking-addr-text");
            var lnmadr = document.getElementById("parking-addr-text");

            var parkingFreeInfo = document.getElementById("parking-sub-info");
            var basicPriceInfo = document.getElementById("basic-price-text");
            var addPriceInfo = document.getElementById("add-price-text");
            var dayPriceInfo = document.getElementById("day-price-text");
            var monthPriceInfo = document.getElementById("month-price-text");

            if(item.parkingFor != "" && item.parkingStruct != ""){
                parkingName.innerHTML = item.parkingName + "&nbsp"
                    + '<span style="font-size: 13px">(' + item.parkingFor + ", " + item.parkingStruct +')</span>';
            }else if(item.parkingFor != ""){
                parkingName.innerHTML = item.parkingName + "&nbsp"
                    + '<span style="font-size: 13px">(' + item.parkinFor + ')</span>';
            }else if(item.parkingStruct != ""){
                parkingName.innerHTML = item.parkingName + "&nbsp"
                    + '<span style="font-size: 13px">(' + item.parkingStruct + ')</span>';
            }else{
                parkingName.innerHTML = item.parkingName;
            }

            if(item.rdnmadr != ""){
                rdnmadr.innerHTML = "(도로) " + item.rdnmadr;
            }else{
                lnmadr.innerHTML = "(지번) " + item.lnmadr;
            }
            phone.innerHTML = "(tel) " + item.phone;

            if(item.parkingFreeInfo == "혼합"){
                parkingFreeInfo.innerHTML = "저희 주차장은 " + '<strong style="color:lightslategray">' + "무료+유료" + '</strong>' + "이며 "
                    + "총 " + '<strong style="color:lightslategray">' + item.parkingCnt + '</strong>' + "대의 자리와<br>"
                    + '<strong style="color:lightslategray">' + item.openDay + '</strong>' + "에 운영된답니다.";
            }else{
                parkingFreeInfo.innerHTML = "저희 주차장은 " + '<strong style="color:lightslategray">' + item.parkingFreeInfo + '</strong>' + "이며 "
                    + "총 " + '<strong style="color:lightslategray">' + item.parkingCnt + '</strong>' + "대의 자리와<br>"
                    + '<strong style="color:lightslategray">' + item.openDay + '</strong>' + "에 운영된답니다.";
            }

            if(item.parkingFreeInfo == "무료"){
                document.getElementById("pay-parking").className = "parking-price-info HIDDEN";
                document.getElementById("non-pay-parking").className = "parking-price-info";
            }else{
                document.getElementById("pay-parking").className = "parking-price-info";
                document.getElementById("non-pay-parking").className = "parking-price-info HIDDEN";

                if(item.basicTime == "1440"){
                    basicPriceInfo.innerHTML = '<strong style=\"color:lightslategray\">' + "기본요금" + '</strong>' + "은 "
                        + '<strong style="color:blue">' + parseInt(item.basicCharge).toLocaleString() + "원"+ '</strong>'
                        + "이며, 기본 이용시간은 " + '<strong style="color:blue">' + "24시간" + '</strong>' + "입니다."
                }else if(item.basicTime != "" && item.basicCharge != "" && item.basicTime != "0" && item.basicCharge != "0"){
                    basicPriceInfo.innerHTML = '<strong style=\"color:lightslategray\">' + "기본요금" + '</strong>' + "은 "
                        + '<strong style="color:blue">' + parseInt(item.basicCharge).toLocaleString() + "원"+ '</strong>'
                        + "이며, 기본 이용시간은 " + '<strong style="color:blue">' + item.basicTime + "분" + '</strong>' + "입니다."
                }else{
                    basicPriceInfo.innerHTML = "기본이용료 및 시간에 대한 정보가 없네요😰"
                }

                if(item.addUnitTime != "" && item.addUnitCharge != "" && item.addUnitTime != "0" && item.addUnitCharge != "0"){
                    addPriceInfo.innerHTML = '<strong style=\"color:lightslategray\">' + "추가요금" + '</strong>' + "은 "
                        + '<strong style="color:blue">' + parseInt(item.addUnitCharge).toLocaleString() + "원"+ '</strong>'
                        + "이며, 추가 이용시간은 " + '<strong style="color:blue">' + item.addUnitTime + "분" + '</strong>' + "입니다."
                }else{
                    addPriceInfo.innerHTML = "추가이용료 및 시간에 대한 정보가 없네요😰"
                }

                if(item.dayCharge != "" && item.dayChargeTime != "" && item.dayCharge != "0" && item.dayChargeTime != "0"){
                    var unitTime = parseInt(item.dayChargeTime, 10);
                    var unitTimeH;
                    var unitTimeM;
                    if(unitTime > 24){
                        unitTimeH = parseInt(unitTime/60);
                        unitTimeM = unitTime%60;
                        if(unitTimeM == 0){
                            dayPriceInfo.innerHTML = '<strong style=\"color:lightslategray\">하루 정기권</strong>' + ": " +
                                unitTimeH.toString() + "시간에 " + parseInt(item.dayCharge).toLocaleString() + "원";
                        }else{
                            dayPriceInfo.innerHTML = '<strong style=\"color:lightslategray\">하루 정기권</strong>' + ": " +
                                unitTimeH.toString() + "시간 " + unitTimeM.toString() +"분에 " + parseInt(item.dayCharge).toLocaleString() + "원";
                        }
                    }else{
                        dayPriceInfo.innerHTML = '<strong style=\"color:lightslategray\">하루 정기권</strong>' + ": " +
                            item.dayChargeTime + "시간에 " + parseInt(item.dayCharge).toLocaleString() + "원";
                    }

                    document.getElementById("day-buy-button").disabled = false;
                }else{
                    document.getElementById("day-buy-button").disabled = true;
                    dayPriceInfo.innerHTML = "하루 정기권은 따로 존재하지 않네요😰";
                }

                if(item.monthCharge != "" && item.monthCharge != "0"){
                    monthPriceInfo.innerHTML = '<strong style=\"color:lightslategray\">월 정기권</strong>' + ": "
                        + parseInt(item.monthCharge).toLocaleString() + "원";
                    document.getElementById("month-buy-button").disabled = false;
                }else {
                    monthPriceInfo.innerHTML = "월 정기권은 따로 존재하지 않네요😰";
                    document.getElementById("month-buy-button").disabled = true;
                }

            }

            if(item.openDay == "평일"){
                if(item.weekOpen == item.weekClose){
                    document.getElementById("week-time-list").innerHTML = "<strong style='color:lightslategray'>평일: </strong>"
                        + "24시간 운영"
                }else{
                    document.getElementById("week-time-list").innerHTML = "<strong style='color:lightslategray'>평일: </strong>"
                        + item.weekOpen + " ~ " + item.weekClose;
                }

                document.getElementById("week-time-list").className = "";
                document.getElementById("sat-time-list").className = "HIDDEN";
                document.getElementById("holi-time-list").className = "HIDDEN";
            }else if(item.openDay == "평일+토요일"){
                if(item.weekOpen == item.weekClose){
                    document.getElementById("week-time-list").innerHTML = "<strong style='color:lightslategray'>평일: </strong>"
                        + "24시간 운영"
                }else{
                    document.getElementById("week-time-list").innerHTML = "<strong style='color:lightslategray'>평일: </strong>"
                        + item.weekOpen + " ~ " + item.weekClose;
                }

                if(item.satOpen == item.satClose){
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + "24시간 운영";
                }else{
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + item.satOpen + " ~ " + item.satClose;
                }

                document.getElementById("week-time-list").className = "";
                document.getElementById("sat-time-list").className = "";
                document.getElementById("holi-time-list").className = "HIDDEN";
            }else if(item.openDay == "평일+토요일+공휴일"){
                if(item.weekOpen == item.weekClose){
                    document.getElementById("week-time-list").innerHTML = "<strong style='color:lightslategray'>평일: </strong>"
                        + "24시간 운영"
                }else{
                    document.getElementById("week-time-list").innerHTML = "<strong style='color:lightslategray'>평일: </strong>"
                        + item.weekOpen + " ~ " + item.weekClose;
                }

                if(item.satOpen == item.satClose){
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + "24시간 운영";
                }else{
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + item.satOpen + " ~ " + item.satClose;
                }

                if(item.holiOpen == item.holiClose){
                    document.getElementById("holi-time-list").innerHTML = "<strong style='color:lightslategray'>공휴일: </strong>"
                        + "24시간 운영";
                }else{
                    document.getElementById("holi-time-list").innerHTML = "<strong style='color:lightslategray'>공휴일: </strong>"
                        + item.holiOpen + " ~ " + item.holiClose;
                }

                document.getElementById("week-time-list").className = "";
                document.getElementById("sat-time-list").className = "";
                document.getElementById("holi-time-list").className = "";
            }else if(item.openDay == "토요일"){
                if(item.satOpen == item.satClose){
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + "24시간 운영";
                }else{
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + item.satOpen + " ~ " + item.satClose;
                }

                document.getElementById("week-time-list").className = "HIDDEN";
                document.getElementById("sat-time-list").className = "";
                document.getElementById("holi-time-list").className = "HIDDEN";
            }else if(item.openDay == "토요일+공휴일"){
                if(item.satOpen == item.satClose){
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + "24시간 운영";
                }else{
                    document.getElementById("sat-time-list").innerHTML = "<strong style='color:lightslategray'>토요일: </strong>"
                        + item.satOpen + " ~ " + item.satClose;
                }

                if(item.holiOpen == item.holiClose){
                    document.getElementById("holi-time-list").innerHTML = "<strong style='color:lightslategray'>공휴일: </strong>"
                        + "24시간 운영";
                }else{
                    document.getElementById("holi-time-list").innerHTML = "<strong style='color:lightslategray'>공휴일: </strong>"
                        + item.holiOpen + " ~ " + item.holiClose;
                }

                document.getElementById("week-time-list").className = "HIDDEN";
                document.getElementById("sat-time-list").className = "";
                document.getElementById("holi-time-list").className = "";
            }else if(item.openDay == "공휴일"){
                if(item.holiOpen == item.holiClose){
                    document.getElementById("holi-time-list").innerHTML = "<strong style='color:lightslategray'>공휴일: </strong>"
                        + "24시간 운영";
                }else{
                    document.getElementById("holi-time-list").innerHTML = "<strong style='color:lightslategray'>공휴일: </strong>"
                        + item.holiOpen + " ~ " + item.holiClose;
                }

                document.getElementById("week-time-list").className = "HIDDEN";
                document.getElementById("sat-time-list").className = "HIDDEN";
                document.getElementById("holi-time-list").className = "";
            }
        });
        markers.push(marker);
    });
    clusterer.addMarkers(markers);

    // 인포윈도우를 표시하는 클로저를 만드는 함수입니다
    function makeOverListener(map, marker, infowindow) {
        return function() {
            infowindow.open(map, marker);
            /* 제일 처음 인포윈도우가 발생하는 지점에 css설정이 먹히도록 설정한다 */
            infoCssCustom();
        };
    }

    // 인포윈도우를 닫는 클로저를 만드는 함수입니다
    function makeOutListener(marker, infowindow) {
        return function() {
            if(clickInfowindows.length == 0 || selectedMarker !== marker) {
                infowindow.close();
            }
        };
    }
}

/* info windown 강제로 css 변경하기 */
function infoCssCustom(){
    var infoTitle = document.querySelectorAll('.info-title');

    infoTitle.forEach(function(e) {
        // 인포윈도우 마진 값 때문에 한번 거친 element의 경우 또 거칠 경우 가로 길이가 계속 커지는 문제점이 발생하여 조건분기로 막아준다.
        if(e.className.includes("adapt-css") == false){
            var w = e.offsetWidth + 10;
            var ml = w/2;
            e.parentElement.style.left = "50%";
            e.parentElement.style.marginLeft = -ml+"px";
            e.parentElement.style.width = w+"px";
            e.parentElement.previousSibling.style.display = "none";
            e.parentElement.parentElement.style.border = "0px";
            e.parentElement.parentElement.style.background = "unset";
            e.className = 'info-title adapt-css';
        }
    });
}

/* Poly 라인 초기화 */
function initPolyLine(){
    if(startPoly != null){
        startPoly.setMap(null);
    }
    if(linePoly != null){
        linePoly.setMap(null);
    }
    if(endPoly != null){
        endPoly.setMap(null);
    }
    if(lineOrderNum.length != 0){
        $(lineOrderNum).map(function (i, lineOrder) {
            lineOrder.setMap(null);
        });
        lineOrderNum=[];
    }
}

/* 내비 길 찍어주는 로직 */
function naviRoad(){
    /* 출발지 및 도착지 url에 맞게 조합 */
    var org = mylongitude + "," + mylatitude;
    var dest = selectedMarkerLng + "," + selectedMarkerLat;

    var roadsJsonArray;
    var guidesJsonArray;

    var distance;
    var duration;

    initPolyLine();

    console.log("navi Road Btn Clicked!");

    /*내비 호출*/
    $.ajax({
        type : "GET",
        url : "https://apis-navi.kakaomobility.com/v1/directions?" +
            "origin=" + org +
            "&destination=" + dest +
            "&waypoints=" +
            "&priority=RECOMMEND" +
            "&car_fuel=GASOLINE" +
            "&car_hipass=false" +
            "&alternatives=false" +
            "&road_details=false",
        headers : {'Authorization': 'KakaoAK 344e68558b6ae398a53fbad7ddd5f4b9'},
        async :  false,
        success : function(data){
            roadsJsonArray = data.routes[0].sections[0].roads;
            guidesJsonArray = data.routes[0].sections[0].guides;
            distance = data.routes[0].summary.distance;
            duration = data.routes[0].summary.duration;
            console.log("Navi Response Success");
        },
        error : function(data){
            console.log("Navi Response error");
        }
    });

    /* 토스트로 걸리는 시간과 거리를 알려준다. */
    const toastLiveExample = document.getElementById('liveToast');
    const toast = new bootstrap.Toast(toastLiveExample);

    const toastInfo = document.getElementById('nav-road-inform');

    var kmDist, mDist, hDur, mDur;

    kmDist = parseInt(distance / 1000);
    mDist = distance % 1000;

    hDur = parseInt(duration / 3600);
    duration = duration % 3600;

    /* 분 단위까지만 표시한다 */
    mDur = parseInt(duration / 60);

    var infoDist ="", infoTime="";

    if(kmDist != 0){
        infoDist = kmDist + "km ";
    }
    if(mDist != 0){
        infoDist = infoDist + mDist + "m";
    }

    if(hDur != 0){
        infoTime = hDur + "시간 ";
    }
    if(mDur != 0){
        infoTime = infoTime + mDur + "분";
    }

    toastInfo.innerHTML = '<strong>예상 걸릴 시간:</strong> ' + infoTime + '<br>'
        + '<strong>거리:</strong> ' + infoDist;

    toast.show();

    /* 위도와 경도를 받아온다 */
    var naviLng = [];
    var naviLat = [];

    for(var i=0; i<roadsJsonArray.length; i++){
        var road = roadsJsonArray[i];
        /* road의 마지막과 다음 road의 처음은 겹치므로 이에 대한 조건 분기 처리를 해준다. */
        if(i == roadsJsonArray.length - 1){
            for(var ri=0; ri<road.vertexes.length; ri++){
                if(ri % 2 === 0) naviLng.push(road.vertexes[ri]);
                else naviLat.push(road.vertexes[ri]);
            }
        }else{
            for(var ri=0; ri<road.vertexes.length - 2; ri++){
                if(ri % 2 === 0) naviLng.push(road.vertexes[ri]);
                else naviLat.push(road.vertexes[ri]);
            }
        }
    }

    /* Poly line을 통해 길을 그린다. */
    //시작선과 끝선에 대해서는 건물에서 출발하므로 연한 선으로 표시한다.
    var startPath = [
        new kakao.maps.LatLng(mylatitude, mylongitude),
        new kakao.maps.LatLng(naviLat[0], naviLng[0])
    ];

    var endPath = [
        new kakao.maps.LatLng(naviLat[naviLat.length-1], naviLng[naviLng.length-1]),
        new kakao.maps.LatLng(selectedMarkerLat, selectedMarkerLng)
    ];

    startPoly = new kakao.maps.Polyline({
        path: startPath, // 선을 구성하는 좌표배열 입니다
        strokeWeight: 6, // 선의 두께 입니다
        strokeColor: 'blue', // 선의 색깔입니다
        strokeOpacity: 0.4, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid' // 선의 스타일입니다
    });

    endPoly = new kakao.maps.Polyline({
        path: endPath, // 선을 구성하는 좌표배열 입니다
        strokeWeight: 6, // 선의 두께 입니다
        strokeColor: 'cornflowerblue', // 선의 색깔입니다
        strokeOpacity: 0.4, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid' // 선의 스타일입니다
    });

    startPoly.setMap(map);
    endPoly.setMap(map);

    // 선을 구성하는 좌표 배열입니다. 이 좌표들을 이어서 선을 표시합니다
    var linePath = [];
    for(var i=0; i<naviLng.length; i++){
        linePath.push(new kakao.maps.LatLng(naviLat[i], naviLng[i]));
    }

    // 지도에 표시할 선을 생성합니다
    linePoly = new kakao.maps.Polyline({
        path: linePath, // 선을 구성하는 좌표배열 입니다
        strokeWeight: 8, // 선의 두께 입니다
        strokeColor: 'cornflowerblue', // 선의 색깔입니다
        strokeOpacity: 1, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid' // 선의 스타일입니다
    });

    // 지도에 선을 표시합니다
    linePoly.setMap(map);

    /* 경로에 순서 번호를 추가하기 위한 작업 */
    var startContent = '<div class ="label" style="border: 1px solid cornflowerblue; border-radius: 50%; background: cornflowerblue; color: white; font-size: 10px">' +
        '<span class="center">S</span>' +
        '</div>';

    var startLineOrderNum = new kakao.maps.CustomOverlay({
        position : new kakao.maps.LatLng(mylatitude, mylongitude),
        content : startContent
    });

    startLineOrderNum.setMap(map);
    lineOrderNum.push(startLineOrderNum);

    for(var i=0; i<guidesJsonArray.length; i++){
        var guide = guidesJsonArray[i];
        var content = '<div class ="numbering-navi" style="border: 1px solid gray; ' +
            'width: 20px;' +
            'height: 20px;' +
            'padding-top: 2px;' +
            'border-radius: 50%;' +
            'background: whitesmoke;' +
            'color: dimgray;' +
            'font-size: 10px;' +
            'text-align: center">' +
            + (i + 1) + '</div>';
        // 커스텀 오버레이가 표시될 위치입니다
        var position = new kakao.maps.LatLng(guide.y, guide.x);

// 커스텀 오버레이를 생성합니다
        var tmpLineOrderNum = new kakao.maps.CustomOverlay({
            position: position,
            content: content
        });

// 커스텀 오버레이를 지도에 표시합니다
        tmpLineOrderNum.setMap(map);
        lineOrderNum.push(tmpLineOrderNum);
    }
}
