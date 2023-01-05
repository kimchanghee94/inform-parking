/* 주소 검색 메서드*/
function keywordSearch() {
    console.log("keyword Search Test");
    return false;
}

/* 회원 탈퇴 */
function logoutFunc(){
    $.ajax({
        type : "post",
        url : "/logout"
    });
}


/* 맵 작업 */
var mylatitude;
var mylongitude;
var map;

/*현재 위치로*/
function moveCurPos() {
    map.panTo(new kakao.maps.LatLng(mylatitude, mylongitude));
}

navigator.geolocation.getCurrentPosition(
  function (position) {
        mylatitude = position.coords.latitude;
        mylongitude = position.coords.longitude;
        map = new kakao.maps.Map(document.getElementById('kakao_map'), { // 지도를 표시할 div
            center : new kakao.maps.LatLng(mylatitude, mylongitude), // 지도의 중심좌표
            level : 5 // 지도의 확대 레벨
        });

      // 마커 클러스터러를 생성합니다
      var clusterer = new kakao.maps.MarkerClusterer({
          map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
          averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
          minLevel: 6 // 클러스터 할 최소 지도 레벨
      });

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
      var markers;

      getMapViewMarkers();

      var zoomControl = new kakao.maps.ZoomControl();
      map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
      var mapLevel = map.getLevel(); //현재 map level로 초기화를 시켜둔다
// 지도가 확대 또는 축소되면 마지막 파라미터로 넘어온 함수를 호출하도록 이벤트를 등록합니다
      //확대 이벤트
      kakao.maps.event.addListener(map, 'zoom_changed', function() {
          // 지도의 현재 레벨을 얻어옵니다
          mapLevel = map.getLevel();

          // 레벨에 따라 클러스터러에 마커들을 추가
          if(mapLevel > 7){
              clusterer.clear();
          }else{
              getMapViewMarkers();
          }
      });

      //이동 이벤트
      kakao.maps.event.addListener(map, 'dragend', function() {
          if(mapLevel < 8 ){
              getMapViewMarkers();
          }
      });

      //db를 통해 데이터를 가져와 사용한다.
      function getMapViewMarkers(){
          var latlngJson;
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
                    console.log("맵 안의 데이터가 존재하여 값을 받은 경우");
                    latlngJson = response.body;
                }else if(response.header.statusCode =="01"){
                    console.log("값을 따로 받은게 존재하지 않을 경우");
                    latlngJson = response.body;
                }
              }
          });

          console.log(latlngJson);

          markers = $(latlngJson.items).map(function(i, item) {
              return new kakao.maps.Marker({
                  position : new kakao.maps.LatLng(item.latitude, item.longitude)
              });
          });

          clusterer.addMarkers(markers);
      }

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
  }
);