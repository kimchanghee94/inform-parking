function kakaoPay(dayMonthFlag){

    $.ajax({
        type : "post",
        url : "/kakaopay",
        data : {
            parkingNo : selectedMarkerJson.parkingNo,
            parkingName : selectedMarkerJson.parkingName + (dayMonthFlag == 0 ? '(일정기권)' : '(월정기권)'),
            parkingPrice : (dayMonthFlag == 0 ? selectedMarkerJson.dayCharge : selectedMarkerJson.monthCharge)
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

function unLoginPay(){
    alert("로그인 이후 가능합니다.");
}