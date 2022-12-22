FROM tomcat:9.0.70

RUN apt-get update && \
    #vim 편집기
    apt-get install -y vim && \
    #ping test 용도
    apt-get install -y inetutils-ping

#아래처럼도 사용가능    
#RUN ["apt-get", "update"]
#RUN ["apt-get", "install", "-y", "vim"]