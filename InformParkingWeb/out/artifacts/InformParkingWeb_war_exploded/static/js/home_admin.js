/* 초기 주차장 등록 페이지 세팅 */
function initSettingParkingRegisterAdmin(){
    $.ajax({
        type : "post",
        url : "/getInitSettingAdmin",
        dataType : "json",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function () {
                console.log("get parking admin init Setting success");
            },
            404: function () {
                console.log("get parking admin init Setting failed");
            }
        },
        async : false,
        success : function(response) {
            console.log(response.header.msg);
            if (response.header.statusCode == "00") {
                /* 동적으로 추가해주면서 기존 자식들은 전부 삭제한다 */
                document.getElementById("parking-admin-change-modal-body").innerHTML = "";
                $(response.body.items).map(function (i, item) {
                    addParkingAdminField(item.parkingNo, item.referenceDate);
                });
            } else if (response.header.statusCode == "01") {
                addParkingAdminField(null, null);
            }
        }
    });
}

/* 관리 주차장 등록페이지 칼럼 추가하기 */
function addParkingAdminField(parkingNo, referenceDate){
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

    if(parkingNo !== null){
        newInput1.value = parkingNo;
        newInput1.disabled = true;
    }

    var newSpan2 = document.createElement("span");
    newSpan2.className="input-group-text";
    newSpan2.innerHTML="주차장 등록일";

    var newInput2 = document.createElement("input");
    newInput2.className="form-control";
    newInput2.type="text";
    newInput2.name="referenceDate";
    newInput2.placeholder="ex) 2023-01-01";

    if(referenceDate !== null){
        newInput2.value = referenceDate;
        newInput2.disabled = true;
    }

    var newAuthDiv = document.createElement("div");
    newAuthDiv.className="auth-check-result";
    newAuthDiv.style.marginBottom ="8px";

    var newButton1 = document.createElement("button");
    newButton1.type="button";
    newButton1.className="btn btn-primary";
    newButton1.innerHTML="인증";
    newButton1.onmouseout=blur();

    if(parkingNo !== null){
        newButton1.disabled = true;
    }

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

                    /* 한번 인증에 성공한 필드는 더 이상 입력 불가능한 필드로 바꿔준다. */
                    newButton1.disabled = true;
                    newInput1.disabled = true;
                    newInput2.disabled = true;

                    addDBAdminRolePage(newInput1.value.toString().trim(), newInput2.value.toString().trim());
                }else if(response.header.statusCode == "01"){
                    newAuthDiv.style.color="red";
                    newAuthDiv.innerHTML="인증에 실패하였습니다.";
                }else if(response.header.statusCode == "02"){
                    newAuthDiv.style.color="red";
                    newAuthDiv.innerHTML="인증 정보가 중복된 주차장입니다.";
                }else if(response.header.statusCode == "03"){
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

        var deleteData = {
            parkingNo : parkingNo,
            referenceDate : referenceDate
        };

        $.ajax({
            type : "post",
            url : "/deleteAdminParkingField",
            data : deleteData,
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            statusCode: {
                200: function () {
                    console.log("Delete Parking Admin " + parkingNo + " success");
                },
                404: function () {
                    console.log("Delete Parking Admin " + parkingNo + " failed");
                }
            },
            success : function(response) {
                console.log(response.header.msg);
            }
        });
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

/* 주차장 관리하기 페이지 테이블 초기화 세팅 */
function initSettingParkingAdminRolePage() {
    $.ajax({
        type : "post",
        url : "/getInitSettingAdmin",
        dataType : "json",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        statusCode: {
            200: function () {
                console.log("get parking admin init Setting success");
            },
            404: function () {
                console.log("get parking admin init Setting failed");
            }
        },
        success : function(response) {
            console.log(response.header.msg);
            if (response.header.statusCode == "00") {
                document.getElementById("parking-admin-page-table-body").innerHTML = "";
                $(response.body.items).map(function (i, item) {
                    addParkingAdminRolePageField(item.parkingNo, item.parkingName, item.referenceDate, item.parkingUseCnt);
                });
            }else if(response.header.statusCode == "01"){
                /* 받은 데이터가 없을 시 기존 테이블 입력된 데이터까지 지워준다 */
                document.getElementById("parking-admin-page-table-body").innerHTML = "";
            }
        }
    });
}

function addParkingAdminRolePageField(parkingNo, parkingName, referenceDate, parkingUseCnt){
    var obj = document.getElementById("parking-admin-page-table-body");

    /* 레이아웃 설정 */
    var newTr = document.createElement("tr");
    newTr.id = "admin-page-table-row-id-" + parkingNo;

    var newTh = document.createElement("th");
    newTh.scope = "row";

    var pnameTd = document.createElement("td");
    var refTd = document.createElement("td");

    var inputTd = document.createElement("td");
    var newDiv = document.createElement("div");
    newDiv.className="input-group";
    var inputTag = document.createElement("input");
    inputTag.type = "text";
    inputTag.name = "parkingCnt";
    inputTag.class = "form-control";
    inputTag.placeholder = "숫자 입력";
    inputTag.style.width = "100px";
    inputTag.value = parkingUseCnt;
    inputTag.onkeyup = function(){
        $(this).val( $(this).val().replace(/[^0-9]/gi,""));
        console.log(inputTag.value);
    }

    var newButton = document.createElement("button");
    newButton.type="button";
    newButton.className="btn btn-primary";
    newButton.innerHTML="변경";
    newButton.onmouseout=blur();

    var newChangeCntResultDiv = document.createElement("div");
    newChangeCntResultDiv.className="parking-cnt-result";
    newChangeCntResultDiv.style.marginBottom ="4px";

    newButton.onclick =  function(){
        $.ajax({
            type : "post",
            url : "/updateParkingUseCnt",
            data : {
                parkingNo : parkingNo,
                referenceDate : referenceDate,
                parkingUseCnt : parseInt(inputTag.value)
            },
            dataType : "json",
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            statusCode: {
                200: function () {
                    console.log("Update Parking Use Cnt " + parkingNo + " success");
                },
                404: function () {
                    console.log("Delete Parking Admin " + parkingNo + " failed");
                }
            },
            success : function(response) {
                if(response !== null) {
                    console.log(response.header.msg);
                    if(response.header.statusCode=="00"){
                        newChangeCntResultDiv.style.color="blue";
                        newChangeCntResultDiv.innerHTML="변경 성공";
                    }else{
                        newChangeCntResultDiv.style.color="red";
                        newChangeCntResultDiv.innerHTML="서버 오류로 변경 실패";
                    }
                }
            }
        });
    };

    console.log(parkingNo + ", " + referenceDate);

    newTh.innerHTML = parkingNo;
    pnameTd.innerHTML = parkingName;
    refTd.innerHTML = referenceDate;

    /* 태그 조합 */
    newDiv.appendChild(inputTag);
    newDiv.appendChild(newButton);
    inputTd.appendChild(newDiv);
    inputTd.appendChild(newChangeCntResultDiv);
    newTr.appendChild(newTh);
    newTr.appendChild(pnameTd);
    newTr.appendChild(refTd);
    newTr.appendChild(inputTd);

    obj.appendChild(newTr);
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
            if(response.header.statusCode == "001" || response.header.statusCode == "000"){
                console.log("add data success");
            }else {
                console.log("add data fail");
            }
        }
    });
}