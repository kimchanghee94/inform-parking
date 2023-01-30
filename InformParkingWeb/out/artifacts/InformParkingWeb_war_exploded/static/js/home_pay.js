var dayOrMonthFlag = 0;
var firstCarNum = null;
var lastCarNum = null;

function checkCarNum(tmpDayOrMonthFlag){
    dayOrMonthFlag = tmpDayOrMonthFlag;
    var carNum = getCarNum();
    firstCarNum = carNum;
    lastCarNum = carNum;
    var obj = document.getElementById("check-car-number-modal-body");
    obj.innerHTML = ""; //한번 초기화 해주고 동적으로 초기화해준다.

    var newDiv = document.createElement("div");
    newDiv.className = "input-group";

    var newSpan = document.createElement("span");
    newSpan.className = "input-group-text";
    newSpan.innerHTML = "차량 번호";

    var newInput = document.createElement("input");
    newInput.className = "form-control";
    newInput.type = "text";
    newInput.name = "carNum";
    newInput.placeholder = "공백없이 입력해주세요";
    newInput.value =  carNum;
    newInput.onkeyup = function(){
        $(this).val($(this).val().replace(' ', ''));
        lastCarNum = newInput.value;
    }; //공백 입력 제한

    //태그 조합
    newDiv.appendChild(newSpan);
    newDiv.appendChild(newInput);
    obj.appendChild(newDiv);
}

function getCarNum(){
    var carNum;
    $.ajax({
        type : "post",
        url : "/getCarNumber",
        dataType : "json",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function () {
                console.log("get Car Number call success");
            },
            404: function () {
                console.log("get Car Number call failed");
            }
        },
        async : false,
        success : function(response) {
            if(response.header.statusCode == "00") {
                carNum = response.body.carNum;
            }else{
                console.log(response.header.msg);
            }
        }
    });
    return carNum;
}

function kakaoPay(){

    console.log(firstCarNum, lastCarNum);
    if(firstCarNum != lastCarNum){
        $.ajax({
            type : "post",
            url : "/updateUserCarNum",
            data : {
                carNum : lastCarNum
            },
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            statusCode: {
                200: function () {
                    console.log("car Num Update success");
                },
                404: function () {
                    console.log("car Num Update failed");
                }
            },
            success : function(response) {
                console.log(response);
            }
        })
    }

    $.ajax({
        type : "post",
        url : "/kakaopay",
        data : {
            parkingNo : selectedMarkerJson.parkingNo,
            parkingName : selectedMarkerJson.parkingName,
            dayMonthFlag : dayOrMonthFlag,
            carNum : lastCarNum,
            parkingPrice : (dayOrMonthFlag == 0 ? selectedMarkerJson.dayCharge : selectedMarkerJson.monthCharge)
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

/*비로그인 시 구매 기능 사용 불가*/
function unLoginPay(){
    alert("로그인 이후 가능합니다.");
}

/*구매 내역*/
function initSettingPurchaseHistory(){
    $.ajax({
        type : "post",
        url : "/getInitSettingPurchaseHistory",
        dataType : "json",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function () {
                console.log("get purchase history init Setting success");
            },
            404: function () {
                console.log("get purchase history init Setting failed");
            }
        },
        async : false,
        success : function(response) {
            console.log(response.header.msg);
            if (response.header.statusCode == "00") {
                /* 동적으로 추가해주면서 기존 자식들은 전부 삭제한다 */
                document.getElementById("purchase-history-modal-table-body").innerHTML = "";
                $(response.body.items).map(function (i, item) {
                    addPurchaseHistoryField(item);
                });
            } else if (response.header.statusCode == "01") {
                /* 아무 데이터가 없기 때문에 한번 다 비워주고 새로 하나의 필드를 추가해준다.*/
                document.getElementById("purchase-history-modal-table-body").innerHTML = "";
            }
        }
    });
}

function addPurchaseHistoryField(item){
    var obj = document.getElementById("purchase-history-modal-table-body");

    /* 레이아웃 설정 */
    var newTr = document.createElement("tr");

    var newTh = document.createElement("th");
    newTh.scope = "row";
    newTh.innerHTML = item.parkingName;

    var dateTime = item.purchaseTime.split(" ");

    var dateTd = document.createElement("td");
    dateTd.innerHTML = dateTime[0];
    var timeTd = document.createElement("td");
    timeTd.innerHTML = dateTime[1];

    var dayMonthTd = document.createElement("td");
    dayMonthTd.innerHTML = item.dayMonth;

    var carNumTd = document.createElement("td");
    carNumTd.innerHTML = item.carNum;

    var priceTd = document.createElement("td");
    priceTd.innerHTML = parseInt(item.parkingPrice).toLocaleString() + "원";

    newTr.appendChild(newTh);
    newTr.appendChild(dateTd);
    newTr.appendChild(timeTd);
    newTr.appendChild(dayMonthTd);
    newTr.appendChild(carNumTd);
    newTr.appendChild(priceTd);
    obj.appendChild(newTr);
}
