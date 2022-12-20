# Knu_Mobileapp_Team1

## 주요 레포지토리 구성

```
/app		// 야운동 app
/mapdata	// 야운동 app과 서버 app이 서로 공유하는 class들의 라이브러리
/server		// 서버 app
```



# App Implementation


## 실행 방법

`app`은 google maps api를 받아오는데, 이를 위해 SHA-1 인증서 등록을 할 필요가 있습니다. 

SHA-1 인증서와 관련된 자세한 내용은 [터미널 창]
Windows : (keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android)
Linux 또는 macOS:(keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android) 입력 후  확인해주세요.


## 개발환경

 - Android Studio 
 - Firebase


## 설치

- git clone  
  > (command) git clone https://github.com/kyoungmopark/Knu_Mobileapp_Team1
  > > 프로젝트 소스 코드 다운로드