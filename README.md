# Gluu Project


# 사내 gluu 서버
https://testgluu.dsmcorps.com


# Gluu Test Page
http://ec2-15-164-100-174.ap-northeast-2.compute.amazonaws.com:8090/


- Authorization Code Flow 테스트

1. Authorization Code Grant Type 버튼 클릭

2. gluu 로그인 화면
ID : msyu / PWD : 1234

3. 토큰 생성 확인

- accessToken 테스트

1. access_token 복사
2. acccessToken 입력창에 입력 후 userInfo 버튼 클릭
3. 사용자 정보 확인 후 종료

- refresh_token 테스트

1. refresh_token  복사
2. refreshToken 입력창에 입력 후 refreshAccess Token 버튼 클릭
3. 정상 토큰 발생 확인


# git source info

Spring boot

Maven

openjdk-11

localhost 환경 구축 후 test 진행


# 과제
1. 현재 project를 local에 구성 해 보기
2. local에서 정상 test 확인 해 보기
3. 현재 project를 PHP로 변경 해 보기 (최종)

# local 변경 사항

2020.01.19
-----------------------------------

- /oauth/token

access_token를 JWT 방식으로 발급 받지 않음.
id_token에서 payload를 가져옴.

- simple 화면 추가

- localStorage 사용


