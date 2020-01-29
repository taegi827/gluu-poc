# Gluu Project[ 2020.01.29 ]
[Version]
 - Apache-2.4.41
 - mod_jk-1.2.46 // 아파치 버전에 따라 호환되는 mod_jk가 따로 있어 버전을 맞춰야합니다.

[Apache24 구동을 위해 설정 해야하는 것들]
 1. Microsoft Visual C++ Redistributable Visual Studio 2015 설치해야 합니다.
 
 2. Apache24\conf\httpd.conf 파일의 SRVROOT 경로를 자신의 경로에 맞게 설정해야 합니다.
 
	ex) Define SRVROOT "D:\자신의 경로\Apache24"

# Apache - Tomcat(Spring-boot) 연동 내용 설명
 - Apache와 Spring-Boot의 내장 톰켓을 mod_jk를 이용해 연동한 내용입니다.
 
 - Apache port = 8080
 
 - Tomcat(Spring-Boot) port = 9090
 
 - ajp port = 18009
 
[Apache24]
 1. Apache24\modules\ 폴더에 mod_jk.so 파일을 추가했습니다.
 
 2. Apache24\conf\ 폴더에 workers.properties 파일과 httpd_vhosts.conf 파일을 추가했습니다.
 
 3. workers.properties 파일을 통해 worker1을 하나를 만들고 ajp프로토콜을 사용하기 위한 설정을 했습니다.
 
 4. httpd_vhosts.conf 파일에서 tomcat으로 넘길 url을 위에서 설정한 worker1과 맵핑했습니다.
 
 5. 마지막으로 앞선 설정들을 읽어들이기 위해 httpd.conf 파일 맨 아래줄에 아래의 내용을 추가했습니다.
 
	LoadModule jk_module modules/mod_jk.so

	JkWorkersFile conf/workers.propertiestt
	
	JkLogFile logs/mod_jk.log
	
	JkLogLevel info

	include conf/httpd_vhosts.conf

[Spring-Boot 내장 Tomcat]
 1. Spring-Boot 프로젝트에 있는 application.properties 파일에 아래의 변수들을 추가했습니다.
	
	server.port=9090
	
	tomcat.ajp.protocol=AJP/1.3
	
	tomcat.ajp.port=18009
	
	tomcat.ajp.enable=true
	
 2. 위에서 설정한 변수들을 사용해 ajp 프로토콜 설정을 위한 ContainerConfig 클래스를 생성했습니다.