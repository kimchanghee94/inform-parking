function kakaoPay(){
    $.ajax({
        type : "post",
        url : "/kakaopay",
        data : {
        },
        dataType : "json",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function () {
                console.log("kakao pay API call success");
            },
            404: function () {
                console.log("kakao pay API call failed");
            }
        },
        success : function(response) {
            console.log(response);
            if(response.tid != null && response.tid.length > 0) {
                location.href = response.next_redirect_pc_url;
            }
        }
    });
}

function approveKakaoPay(tid, kpToken){
    $.ajax({
        type : "post",
        url : "/approveKakaopay",
        data : {
            tid : tid,
            token : kpToken
        },
        dataType : "json",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function () {
                console.log("kakao pay Approve API call success");
            },
            404: function () {
                console.log("kakao pay Approve API call failed");
            }
        },
        success : function(response) {
            console.log(response);
        }
    });
}