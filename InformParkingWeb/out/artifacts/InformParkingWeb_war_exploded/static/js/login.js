function loginChk(){
    let id = document.getElementById("login-id-input");
    let passwd = document.getElementById("login-passwd-input");
    console.log(id.value + "," + passwd.value);

    let data = {
        id : id.value,
        passwd : passwd.value
    };
    console.log(data);

    $.ajax({
       type : "post",
       url : "/login",
       data : data,
       success : function (result) {
            if(result == 'success'){

            }else{

            }
       }
    });
}