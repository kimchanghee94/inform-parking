<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<main id="main">
    <script type="text/javascript">
        let message = "${msg}";
            console.log(message);
        if(message != ""){
            alert(message);
            history.back();
        }
    </script>
    <div class="container-login">
        <div class="">
            <div class="d-flex">
                <div class=login-div>
                    <form class="login-form" action="/login" method="post">
                        <div class="p-5">
                            <div class="input-group mb-3">
                                           <span class="input-group-text bg-dark"><i
                                                   class="bi bi-person-plus-fill text-white"></i></span>
                                <input type="text" name="id" id="login-id-input" class="form-control" placeholder="사용자 아이디">
                            </div>
                            <div class="input-group mb-3">
                                            <span class="input-group-text bg-dark"><i
                                                    class="bi bi-key-fill text-white"></i></span>
                                <input type="password" name="passwd" id="login-passwd-input" class="form-control" placeholder="비밀번호">
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault">
                                <label class="form-check-label" for="flexCheckDefault">
                                    내 정보 기억하기
                                </label>
                            </div>
                            <button class="btn btn-dark text-center mt-2" id="login-button" type="submit">
                                로그인
                            </button>
                            <p class="text-center mt-5">계정이 없으신가요?
                                <span class="text-dark login-text">
                                    <a href="join">회원가입</a>
                                </span>
                            </p>
                            <p class="text-center text-dark login-text">비밀번호를 잊어버리셨나요?</p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>
