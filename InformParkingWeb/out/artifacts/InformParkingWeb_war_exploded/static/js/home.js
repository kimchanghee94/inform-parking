$(document).ready(execMap(0, null));

/* 회원 탈퇴 */
function logoutFunc(){
    $.ajax({
        type : "post",
        url : "/logout"
    });
}

/* 맵 작업 */
/* 주소 검색 메서드*/

var map;
var ps;
var mylatitude;
var mylongitude;
var initMapLevel;
var clusterer;
var markers=[];

function enterkey(){
    if(window.event.keyCode == 13){
        keywordSearch();
    }
}

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
    });

    //이동 이벤트
    kakao.maps.event.addListener(map, 'dragend', function() {
        if(map.getLevel() < 8 ){
            getMapViewMarkers();
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
            position : new kakao.maps.LatLng(item.latitude, item.longitude)
        });

        var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="padding-left: 5px;">'+item.parkingName+'</div>' // 인포윈도우에 표시할 내용
        });

        kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
        kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
        kakao.maps.event.addListener(marker, 'click', function() {
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
        };
    }

    // 인포윈도우를 닫는 클로저를 만드는 함수입니다
    function makeOutListener(infowindow) {
        return function() {
            infowindow.close();
        };
    }
}