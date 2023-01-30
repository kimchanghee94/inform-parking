# inform-parking(Mac OS)
주차장 위치와 남은 공간, 가격 등을 제공하는 웹프로젝트

* Front : jsp, html, css, java-script, bootstrap, apache tile
* Server : Apache, Tomcat, MySQL, Docker(각 이미지 서브넷으로 만들어 docker에 등록)
* Database : 로컬에 구축 (Mybatis, MySQLWorkBench 사용)
* IDE : intellij
* Framework : Spring MVC - Maven사용
* 주요 Library : 
  1. spring-security(auth-provider 사용, 각 post메서드 csrf 적용)
  2. transaction, aop, aspectjweaver(applicationContext에서 aop를 이용한 트랜잭션 처리)
  3. WebSocket기반 Stomp(with SockJS)
     * Apache와 Tomcat은 mod_jk기반 AJP프로토콜 사용
     * 웹소켓의 경우 AJP프로토콜에서 미지원으로 ProxyPass를 이용하여 Tomcat으로 접속
* REST-API : 
  1. 공공데이터포털(전국 주차장)
  2. Kakao Map
  3. Kakao Mobilty 길찾기
  4. Kakao Pay
* 다중공유기 포트포워딩으로 외부 접속 허용
* 가비아에서 www.inparking.online 도메인 구매, zero ssl을 통해 https 보안 적용


--메인페이지--

![image](https://user-images.githubusercontent.com/45596085/213401710-7b3c50e3-c3c0-4c30-8711-5206e336a1f6.png)


--사용자가 주차장 등록을 하게되면 DB작업과 spring-security의 룰을 admin으로 동적 변경시키고 확인 버튼 및 x버튼 클릭 시 오프캔버스에 관리자 전용 페이지 목록 생성--

![image](https://user-images.githubusercontent.com/45596085/214702516-03f09c04-51c7-4b78-9c61-19826a6ae505.png)


--관리자 전용페이지에서 현재 사용 중인 주차장 자리를 입력하면 DB작업과 해당 주차장을 보고있는 사용자에게는 실시간으로 남은 자릿수가 변경된다.(Stomp)--

(관리자)
![image](https://user-images.githubusercontent.com/45596085/214701845-f6c38438-14f6-4f94-8ecc-10a91785def1.png)

(사용자)
![image](https://user-images.githubusercontent.com/45596085/214701914-ff5a8734-64f3-44b6-8e7b-5f70b645499c.png)



--이용내역--
![image](https://user-images.githubusercontent.com/45596085/215577819-a8ce56cb-6e20-4605-8d81-aa9b3e00588d.png)





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
