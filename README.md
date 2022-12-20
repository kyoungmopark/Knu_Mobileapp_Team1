# Knu_Mobileapp_Team1
## 주요 레포지토리 구성

```
/app              // 지도 앱
/mapdata        // 공공데이터 파일
/server              // 공공데이터를 firebase 서버에 전송하는 앱
```

## Knu_Mobileapp_Team5 README

어플리케이션에 대한 자세한 설명은 (Knu_Mobileapp_Team5/README.md)를 참고해주세요.

# App Implementation

## 실행 방법

"server" 프로그램을 실행하면 firebase에 애플리케이션에서 사용할 데이터베이스가 생성됩니다.

`app` 프로그램은 google maps api를 받아오는데, 이를 위해 SHA-1 인증서 등록을 할 필요가 있습니다. 

SHA-1 인증서와 관련된 자세한 내용은 [터미널 창]
Windows : (keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android)
Linux 또는 macOS:(keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android) 입력 후  확인해주세요.


## 아키텍처

 - Android Studio 


## 개발환경

 - Android Studio 
 - InteliJ

## 설치

- git clone  
  > (command) git clone https://github.com/kyoungmopark/Knu_Mobileapp_Team1
  > > 프로젝트를 소스 코드 다운로드 


## 생성

- Android Studio 
  > - server 파일 빌드 
  > - app 파일 빌드 

## 실행

> (command) <내용 추가>

## 풀더 설명

- map 
  > <내용 추가> 
- app 
  > <내용 추가>
- gradle 
  > <내용 추가>
- mapdata
  > <내용 추가>
- server 
  > <내용 추가>