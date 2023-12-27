## 와이어프레임

<img src="https://github.com/gxdxx-new/numble-instagram/assets/35963403/a0f80bb3-1eca-4021-aa5c-60e944e7cb28" wdith="500">

## 기술 스택

- Java 17
- Spring Boot 3.0.5
- Mysql 8, Spring Data JPA
- Junit5

## ERD

<img src="https://github.com/gxdxx-new/numble-instagram/assets/35963403/0c29ebd5-9449-46fb-9848-796fc1030524" width="500">

## 배포 과정

<img src="https://github.com/gxdxx-new/numble-instagram/assets/35963403/dd2e6402-69ed-48dc-991a-ad5e5c39c5a6" width="700">

## 구현 기능

- 글 생성, 수정, 삭제 (이미지 포함)
- 댓글 생성, 수정, 삭제
- 답글 생성, 수정, 삭제
- 피드 조회
- 회원 가입, 탈퇴
- 프로필 조회, 수정
- 팔로우, 취소
- DM (목록, 내역, 전송)
- JWT와 Spring Security를 적용한 보안 및 인증
- AWS EC2를 이용한 배포
- Github Action을 이용한 배포 자동화 (CI/CD 파이프라인 구축)

## 개발 순서

다음과 같이 개발할 기능 또는 수정해야 할 사항들을 Project에 등록합니다.

<img src="https://github.com/gxdxx-new/numble-instagram/assets/35963403/1eb006ad-150b-400d-b5c1-5753c8318479">

Project에 등록된 사항에 대해 개발을 진행할 사항을 Issue로 전환하고 로컬 브랜치에서 개발을 진행합니다.

<img src="https://github.com/gxdxx-new/numble-instagram/assets/35963403/fb61a3e1-3614-4c6b-b80b-1de00f96d4fd">

개발 완료 후 로컬 브랜치에서 push를 하고 PR을 생성합니다.

<img src="https://github.com/gxdxx-new/numble-instagram/assets/35963403/b6dbd195-4bbb-4a9c-aec0-dc275c2376cd">

최종적으로 코드를 확인한 후 main 브랜치에 merge합니다.

main 브랜치에 merge 시 Github Actions로 서버에 자동 배포가 완료됩니다.

<img src="https://github.com/gxdxx-new/numble-instagram/assets/35963403/b9c34cd2-f6a5-4be0-9d1a-a095958f701f">
