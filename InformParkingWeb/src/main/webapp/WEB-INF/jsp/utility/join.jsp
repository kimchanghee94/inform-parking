<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<main id="main">
    <div class="container-join">
        <div class="">
            <div class="rounded d-flex remove-attr">
                <div class=>
                    <form action="join" method="post">
                        <div class="p-5">
                            <div class="input-group">
                                           <span class="input-group-text bg-dark">
                                               <i class="bi bi-emoji-smile-fill text-white"></i></span>
                                <input type="text" name="id" id="id" class="form-control" placeholder="사용자 아이디"
                                       onkeyup="idCheck()">
                            </div>
                            <div id="idError" class="error"></div>
                            <div class="input-group">
                                           <span class="input-group-text bg-dark">
                                               <i class="bi bi-person-plus-fill text-white"></i></span>
                                <input type="text" name="userName" id="userName" class="form-control" placeholder="이름"
                                    onkeyup="userNameCheck()">
                            </div>
                            <div id="userNameError" class="error"></div>
                            <div class="input-group">
                                            <span class="input-group-text bg-dark">
                                                <i class="bi bi-key text-white"></i></span>
                                <input type="password" name="passwd" id="passwd" class="form-control" placeholder="비밀번호"
                                       onkeyup="passwdCheck()">
                            </div>
                            <div id="passwdError" class="error"></div>
                            <div class="input-group">
                                            <span class="input-group-text bg-dark">
                                                <i class="bi bi-key-fill text-white"></i></span>
                                <input type="password" name="passwd2" id="passwd2" class="form-control" placeholder="비밀번호 확인"
                                       onkeyup="passwd2Check()">
                            </div>
                            <div id="passwd2Error" class="error"></div>
                            <div class="input-group">
                                            <span class="input-group-text bg-dark">
                                                <i class="bi bi-car-front-fill text-white"></i></span>
                                <input type="text" name="carNum" id="carNum" class="form-control" placeholder="차량 번호(공백 없이 입력해주세요)"
                                       onkeyup="carNumCheck()">
                            </div>
                            <div id="carNumError" class="error"></div>
                            <div class="input-group">
                                            <span class="input-group-text bg-dark">
                                                <i class="bi bi-telephone-fill text-white"></i></span>
                                <input type="text" name="phone" id="phone" class="form-control" placeholder="핸드폰 번호"
                                       onkeyup="phoneCheck()" oninput="autoHyphen(this)">
                            </div>
                            <div id="phoneError" class="error"></div>
                            <button class="btn btn-dark text-center mt-2" id="join-button" type="submit" disabled="disabled">
                                생성완료
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
