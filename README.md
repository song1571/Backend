
## 📊 DB 다이어그램

아래는 이 프로젝트에서 사용한 DB 테이블 구조 다이어그램입니다.

![스크린샷 2024-12-29 224204](https://github.com/user-attachments/assets/c23aa7cc-7bde-4750-941f-429dff18cee2)

### 소개
HRD-NET에서 받은 교육을 기반으로 타인의 도움을 받지 않고 처음으로 제작한 프로젝트입니다. <br>
게시판을 직접 제작해보면서, React의 동작 방식과 효율적인 데이터 처리 변환 등과 같은 문제를 스스로 하며 해결책을 찾았습니다.
***

### 구현된 기능
<b>1. 회원 기능</b>
- 로그인 및 로그아웃
- 회원 가입 (JWT 사용)

<b>2. 게시판 기능</b>
- 모든 게시글 표시
- 게시물 작성
  
***

### 미구현된 기능
<b>1. 게시판 기능</b>
- 게시물에 파일 첨부
- 게시물에 태그 첨부

<b>2. 게시판 기능</b>
- 게시물 수정
- 게시글 삭제

<b>3. 댓글 기능</b>
- 댓글 작성
- 댓글 삭제
***



### 기능 구현에 사용한 API 목록 
#### ▼ LoginController
API 이름 | URL | 요청 방식 | 설명
---------|---------|---------|---------
login | /logins | POST | User DB에서 가입된 유저가 맞는지 확인합니다.<br>아이디가 없거나 패스워드 불일치하면 alert 실패 메시지를 전송합니다.<br>일치하면 쿠키에 저장된 토큰과 일치하는지 확인합니다.<br>Token DB에 해댱 유저의 토큰이 없거나 불일치하면 새로운 토큰을 발급합니다.<br>UserId와 UserKey를 세션에 저장합니다.
loginNoticeBoard | /users | GET | 세션에 저장된 유저 아이디를 가져와 브라우저의 로컬스토리지에 적재합니다.
logoutNoticeBoard | /user/logout | POST | 쿠키의 JSESSIONID를 제거하고 세션을 종료합니다.

#### ▼ RegisterController
API 이름 | URL | 요청 방식 | 설명
---------|---------|---------|---------
registerMailSendAlert | /users/{email}/{id} | GET | User DB에 해당하는 아이디와 이메일이 있는지 확인합니다. 아이디나 이메일이 있는 경우엔 alert 실패 메시지를 전송합니다.
registerMailSend | /mail/send | POST | 받은 이메일로 UUID로 생성한 인증번호를 전송합니다. 전송된 UUID는 세션에 저장됩니다.
registerMailCheck | /mail/check | POST | 인증번호가 세션에 저장된 인증번호와 일치하는지 확인합니다. 인증에 성공하면 쿠키의 JSESSIONID를 제거하고 세션을 종료합니다.
registers | /registers | POST | User DB와 Profile DB에 유저 정보를 저장합니다.

#### ▼ RegisterController
API 이름 | URL | 요청 방식 | 설명
---------|---------|---------|---------
loadPostList | /posts | GET | 세션에 저장된 UserKey를 사용해 Post DB에서 삭제되지 않은 게시물들을 가져옵니다. 비공개 게시물은 해당 유저가 로그인한 상태이면 가져옵니다. 
loadPostCount | /posts/count | GET | 페이징 기능을 위해 모든 게시물의 갯수를 가져옵니다.
uploadPost | /posts | POST | 받은 데이터를 Post, Tag, PostTag DB에 저장합니다. 게시물 업로드에 사용됩니다.
editPost | /posts/{postId} | PUT | 받은 데이터를 Post, Tag, PostTag DB에 저장합니다. 게시물 수정에 사용됩니다.
LoadPostDetail | /posts/{postId} | GET | 게시물의 상세 정보를 Post, Tag, PostTag DB에서 가져옵니다.
updatePostView| /posts/views/{postId} | PATCH | 해당 게시물의 조회수를 1 증가시키도록 Post DB를 업데이트 합니다.
postComment | /comments/{postId} | GET | 게시물에 표시될 식제되지 않은 댓글 목록을 Comment DB에서 가져옵니다.
deletePost | /posts/{postId} | DELETE | 해당 게시물이 더이상 보이지 않도록 Post DB에서 해당 게시물의 is_delete를 N으로 변경합니다.
addComment | /comments | POST | 받은 데이터를 Comment DB에 저장합니다.

## ▶ 4. 기술 스택
기술 | 버전
---------|---------
Spring Boot | 3.3.5
mysql-connector-java | 8.0.30
spring-boot-starter-data-redis | 2.7.7
querydsl-jpa | 5.0.0
querydsl-apt | 5.0.0
jakarta.annotation-api |
jakarta.persistence-api |
jjwt | 0.9.1
spring-cloud-starter-aws | 2.2.6.RELEASE
spring-boot-starter-mail | 3.2.1

#### ▼ 실행 이미지


- 회원가입
  
![스크린샷 2024-12-29 221939](https://github.com/user-attachments/assets/f8b5b15f-0cce-4092-a419-20e164571223)

- 이메일 인증
  
![스크린샷 2024-12-29 222315](https://github.com/user-attachments/assets/48ecd1e9-a4fa-4e0e-8152-11a6bf9f0563)

- 로그인
  
![스크린샷 2024-12-29 221107](https://github.com/user-attachments/assets/5556e6a4-b9e2-408e-b5e7-0f1b317091ad)

- 게시물 테스트
  
![스크린샷 2024-12-29 222343](https://github.com/user-attachments/assets/b9a43ff0-62e0-49c5-afc8-8ef1226c811b)

- 게시물 작성성공
  
![스크린샷 2024-12-29 222406](https://github.com/user-attachments/assets/fd9c7962-d974-4ebc-b9d5-a3902ab811a1)

- 댓글 작성
  
![스크린샷 2024-12-29 222431](https://github.com/user-attachments/assets/fa8400f3-7adb-49a7-bb50-0eafe648c9fe)













