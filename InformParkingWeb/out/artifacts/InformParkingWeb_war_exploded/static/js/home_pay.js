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
        }
    });
};