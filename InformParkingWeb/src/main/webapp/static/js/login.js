/*
여기서는 대체 왜 클래스네임으로 Hidden쓰는게 안먹히고...
동적으로 동작하는 건 되지만 초기에 실행되는 화면에서 초기화를 시켜줘도 안먹히고...
이유가 뭐지 대체...

window.addEventListener("load", function() {
    var mql = window.matchMedia("(max-width: 600px)");
    mql.addListener(responsive);

});

function responsive(e) {
    console.log("여기타나");
    if (e.matches) {
    console.log("여기타나1");
        $("#header-li-nav").append('<li id="noti" class="nav-item"><a href="#" class="nav-link">공지사항</a></li>');
        $("#header-li-nav").append('<li id="cust" class="nav-item"><a href="#" class="nav-link">고객센터</a></li>');
    }
    else {
    console.log("여기타나2");
        $('#noti').remove();
        $('#cust').remove();
    }
}
*/