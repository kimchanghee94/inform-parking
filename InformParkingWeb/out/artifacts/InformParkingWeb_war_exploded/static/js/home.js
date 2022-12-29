/* 주소 검색 메서드*/
function keywordSearch() {
    console.log("keyword Search Test");
    return false;
}

/* 맵 작업*/
var container = document.getElementById('kakao_map');
var options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 3
};

var map = new kakao.maps.Map(container, options);