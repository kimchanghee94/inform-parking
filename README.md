# inform-parking(Mac OS)
주차장 위치와 남은 공간, 가격 등을 제공하는 웹프로젝트

* Front : jsp, html, css, java-script, bootstrap, apache tile
* Server : Apache, Tomcat, MySQL, Docker(각 이미지 서브넷으로 만들어 docker에 등록)
* DataBase : 로컬에 구축 (Mybatis, MySQLWorkBench 사용)
* IDE : intellij
* Framework : Spring MVC - Maven사용
* 주요 Library : 
  1. spring-security(auth-provider 사용, 각 post메서드 csrf 적용)
  2. transaction, aop, aspectjweaver(applicationContext에서 aop를 이용한 트랜잭션 처리)
* REST-API : 공공데이터포털(전국 주차장), Kakao Map, Kakao Mobilty 길찾기
* 다중공유기 포트포워딩으로 외부 접속 허용
* 가비아에서 www.inparking.online 도메인 구매, zero ssl을 통해 https 보안 적용


--메인페이지--

![image](https://user-images.githubusercontent.com/45596085/213401710-7b3c50e3-c3c0-4c30-8711-5206e336a1f6.png)




--로그인 페이지--

![image](https://user-images.githubusercontent.com/45596085/211064015-fa93cc91-5f54-4e54-83e5-cd0c3385e20b.png)



--회원가입 페이지--

![image](https://user-images.githubusercontent.com/45596085/211064181-79edc61d-2adf-428d-892c-a21cff840257.png)



--반응형 메인페이지--


![image](https://user-images.githubusercontent.com/45596085/212411868-a611b966-d90d-4000-b6c8-ab6d5be7a3cf.png)




--반응형 로그인 페이지--


![image](https://user-images.githubusercontent.com/45596085/212411854-674741a2-82e9-4430-be41-898814ce025b.png)




--반응형 회원가입 페이지--


![image](https://user-images.githubusercontent.com/45596085/212411814-c4708ef6-aa82-4375-8253-971349c2517e.png)
