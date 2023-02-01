function initSettingRegisterCar(){
   var repCarNum = getCarNum();
    var obj = document.getElementById("register-car-modal-body");
    obj.innerHTML = "";

    if(repCarNum == null || repCarNum.length == 0){
        addRegisterCarField(null);
    }else{
        addRegisterCarField(repCarNum);

        $.ajax({
            type : "post",
            url : "/getSubCarNumberList",
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            statusCode: {
                200: function () {
                    console.log("get Sub Car Number List call success");
                },
                404: function () {
                    console.log("get Sub Car Number List call failed");
                }
            },
            async : false,
            success : function(response) {
                if(response.header.statusCode == "00") {
                    carNum = response.body.carNum;
                    $(response.body.items).map(function (i, item) {
                        addRegisterCarField(item.carNum);
                    });
                }else{
                    console.log(response.header.msg);
                }
            }
        });
    }
}

/* 관리 주차장 등록페이지 칼럼 추가하기 */
function addRegisterCarField(carNum){
    var obj = document.getElementById("register-car-modal-body");

    /* 레이아웃 설정 */
    var newDiv = document.createElement("div");
    newDiv.className="input-group mb-0";

    var newSpan1 = document.createElement("span");
    newSpan1.className="input-group-text";
    newSpan1.innerHTML="차량 번호";

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

    if(carNum !== null){
        newInput.value = carNum;
        newInput.disabled = true;
    }

    var newAuthDiv = document.createElement("div");
    newAuthDiv.className="auth-check-result";
    newAuthDiv.style.marginBottom ="8px";

    var newButton1 = document.createElement("button");
    newButton1.type="button";
    newButton1.className="btn btn-primary";
    newButton1.innerHTML="등록";
    newButton1.onmouseout=blur();

    if(carNum !== null){
        newButton1.disabled = true;
    }

    newButton1.onclick=function() {
        $.ajax({
            type : "post",
            url : "/addCarNum",
            data : {
                carNum : newInput.value.toString().trim(),
            },
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            statusCode: {
                200: function (data) {
                    console.log("add Car Number success");
                },
                404: function (data) {
                    console.log("add Car Number failed");
                }
            },
            success : function(response){
                if(response.header.statusCode == "00"){
                    newAuthDiv.style.color="blue";
                    newAuthDiv.innerHTML="등록에 성공하였습니다.";

                    /* 한번 등록에 성공한 필드는 더 이상 입력 불가능한 필드로 바꿔준다. */
                    newButton1.disabled = true;
                    newInput.disabled = true;
                }else if(response.header.statusCode == "01"){
                    newAuthDiv.style.color="red";
                    newAuthDiv.innerHTML="이미 등록된 차량입니다.";
                }else if(response.header.statusCode == "02"){
                    newAuthDiv.style.color="red";
                    newAuthDiv.innerHTML="주차 번호 양식이 옳지 않습니다.";
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

        var deleteData = {
            carNum : carNum
        };

        $.ajax({
            type : "post",
            url : "/deleteCarNum",
            data : deleteData,
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            statusCode: {
                200: function () {
                    console.log("Delete Car Num success");
                },
                404: function () {
                    console.log("Delete Car Num failed");
                }
            },
            success : function(response) {
                console.log(response.header.msg);
            }
        });
    };

    /* 레이아웃 조합 */
    newDiv.appendChild(newSpan1);
    newDiv.appendChild(newInput);
    newDiv.appendChild(newButton2);
    newDiv.appendChild(newButton1);

    obj.appendChild(newDiv);
    obj.appendChild(newAuthDiv);
}