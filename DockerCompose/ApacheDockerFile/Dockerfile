FROM httpd:2.4.54

RUN apt-get update && \
    #vim 편집기
    apt-get install -y vim && \
    #ping test 용도
    apt-get install -y inetutils-ping && \
    #mod-jk
    apt-get install wget && \
    apt-get install tar && \
    apt-get install -y libaprutil1-dev perl gcc make 

RUN cd /usr/local/src && wget http://www.apache.org/dist/tomcat/tomcat-connectors/jk/tomcat-connectors-1.2.48-src.tar.gz && tar -xzf tomcat-connectors-1.2.48-src.tar.gz
RUN cd /usr/local/src/tomcat-connectors-1.2.48-src/native && ./configure --with-apxs=/usr/local/apache2/bin/apxs && make && make install    

#아래처럼도 사용가능    
#RUN ["apt-get", "update"]
#RUN ["apt-get", "install", "-y", "vim"]