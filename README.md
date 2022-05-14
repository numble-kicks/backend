# 넘블 모바일 웹 서비스 연계 챌린지 백엔드 저장소
## 신발 p2p 거래 숏폼 콘텐츠 웹 서비스. Kicks
<hr>

### 컴포넌트 아키텍쳐


<hr>

### ERD Diagram


<hr>

### 기술 스택
<br>

1. 코어 기술
   * Spring Boot 2.6
   * Spring Security
   * Spring Data JPA
   * QueryDsl

2. 채팅 기능
   * Stomp

3. 메일 기능
    * JavaMailSender

4. 클라우드 서버
    * 애플리케이션 서버 : AWS EC2
    * 데이터베이스 서버 : AWS RDS
    * 업로드 된 컨텐츠 저장소 : AWS S3

5. 빌드, 배포 자동화
    * Github Actions
    * spring-dotenv
    * AWS CodeDeploy

<hr>


### 구현 기능

1. 인증/인가
    * 로그인
    * 회원가입
    * 로그아웃
    * 탈퇴
2. 동영상
   * 업로드 / 수정 / 조회
   * 검색
   * 인기 영상 / 추천 영상
3. 좋아요
    * 동영상에 좋아요 등록 / 취소
4. 사용자
    * 이메일 인증 / 인증 번호 발급
    * 닉네임 등록
    * 사용자가 업로드 / 좋아요한 영상 목록
5. 팔로우
    * 사용자 팔로우 / 취소
    * 사용자의 팔로잉, 팔로워 목록 조회
6. 채팅
    * 사용자 간 일대일 채팅
7. 관리자
    * 사용자 조회 / 강제 탈퇴
    * 동영상 조회 / 강제 삭제
    
<br>

### API

* api 문서 툴은 사용하지 않고, notion을 사용
