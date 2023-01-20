let idFlag = false;
let userNameFlag = false;
let passwdFlag = false;
let passwd2Flag = false;
let carNumFlag = false;
let phoneFlag = false

//csrf 적용으로 인해 Post로 전송 시 token을 담아 보내야한다.
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

function joinCheck(){
    if(idFlag == true && userNameFlag == true && passwdFlag == true &&
        passwd2Flag == true && carNumFlag == true && phoneFlag == true){
        document.getElementById("join-button").disabled=false;
        console.log("회원가입 가능 폼 완료");
    }else{
        document.getElementById("join-button").disabled=true;
        console.log("회원가입 못하는 폼");
    }
}

function idCheck(){
    let id = document.getElementById("id").value;
    let str_space = /\s/;

    if(str_space.exec(id)){
        document.getElementById("id").focus();
        document.getElementById("id").value = document.getElementById("id").value.replace(' ', '');
        id = document.getElementById("id").value;
    }

    if(id.length < 7){
        document.getElementById("idError").innerHTML="아이디의 길이가 총 7자 이상이어야 합니다.";
        idFlag = false;
        joinCheck();
    }else{
        let data = {id : id};

        $.ajax({
            type : "post",
            url : "/memberIdChk",
            data : data,
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success : function(result){
                if(result == 'success'){
                    document.getElementById("idError").innerHTML="사용 가능한 id입니다.";
                    idFlag = true;
                    joinCheck();
                }else{
                    document.getElementById("idError").innerHTML="중복된 id입니다.";
                    idFlag = false;
                    joinCheck();
                }
            }
        });
    }
}

function userNameCheck(){
    let userName = document.getElementById("userName").value;

    let str_space = /\s/;

    if(str_space.exec(userName)){
        document.getElementById("userName").focus();
        document.getElementById("userName").value = document.getElementById("userName").value.replace(' ', '');
        userName = document.getElementById("userName").value;
    }

    if(userName === ""){
        document.getElementById("userNameError").innerHTML="이름이 비어있습니다.";
        userNameFlag = false;
    }else{
        document.getElementById("userNameError").innerHTML="";
        userNameFlag = true;
    }
    joinCheck();
}

function passwdCheck(){
    let passwd = document.getElementById("passwd").value;
    let passwd2 = document.getElementById("passwd2").value;

    let str_space = /\s/;

    if(str_space.exec(passwd)){
        document.getElementById("passwd").focus();
        document.getElementById("passwd").value = document.getElementById("passwd").value.replace(' ', '');
        passwd = document.getElementById("passwd").value;
    }

    let regexPw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

    if(!regexPw.test(passwd)){
        document.getElementById("passwdError").innerHTML="영문, 특수문자 및 숫자 조합 8자 이상으로 되야합니다.";
        passwdFlag = false;
    }else{
        document.getElementById("passwdError").innerHTML="";
        passwdFlag = true;
    }

    if(passwd != passwd2 && passwd2 != ""){
        document.getElementById("passwd2Error").innerHTML="비밀번호가 같지 않습니다.";
        passwd2Flag = false;
    }else{
        document.getElementById("passwd2Error").innerHTML="";
        passwd2Flag = true;
    }

    joinCheck();
}

function passwd2Check(){
    let passwd = document.getElementById("passwd").value;
    let passwd2 = document.getElementById("passwd2").value;
    let str_space = /\s/;

    if(str_space.exec(passwd2)){
        document.getElementById("passwd2").focus();
        document.getElementById("passwd2").value = document.getElementById("passwd2").value.replace(' ', '');
        passwd2 = document.getElementById("passwd2").value;
    }

    if(passwd != passwd2){
        document.getElementById("passwd2Error").innerHTML="비밀번호가 같지 않습니다.";
        passwd2Flag = false;
    }else{
        document.getElementById("passwd2Error").innerHTML="";
        passwd2Flag = true;
    }
    joinCheck();
}

function carNumCheck(){
    let carNum = document.getElementById("carNum").value;

    let str_space = /\s/;

    if(str_space.exec(carNum)){
        document.getElementById("carNum").focus();
        document.getElementById("carNum").value = document.getElementById("carNum").value.replace(' ', '');
        carNum = document.getElementById("carNum").value;
    }

    if(carNum.toString() === ""){
        document.getElementById("carNumError").innerHTML="차량 번호가 비어있습니다";
        carNumFlag = false;
    }else{
        document.getElementById("carNumError").innerHTML="";
        carNumFlag = true;
    }

    joinCheck();
}

const autoHyphen = (target) => {
    target.value = target.value
        .replace(/[^0-9]/g, '')
        .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");
}

function phoneCheck(){
    let phone = document.getElementById("phone").value;

    if(phone.toString() === ''){
        document.getElementById("phoneError").innerHTML="핸드폰 번호가 비어있습니다.";
        phoneFlag = false;
        joinCheck();
    } else if(phone.length != 13){
        document.getElementById("phoneError").innerHTML="핸드폰 번호 길이가 틀립니다.";
        phoneFlag = false;
        joinCheck();
    }
    else{
        let data = {phone : phone};
        $.ajax({
            type : "post",
            url : "/memberPhoneChk",
            data : data,
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success : function(result){
                if(result == 'success'){
                    document.getElementById("phoneError").innerHTML="";
                    phoneFlag = true;
                    joinCheck();
                }else{
                    document.getElementById("phoneError").innerHTML="이미 해당 번호로 회원가입하셨습니다.";
                    phoneFlag = false;
                    joinCheck();
                }
            }
        });
    }
}