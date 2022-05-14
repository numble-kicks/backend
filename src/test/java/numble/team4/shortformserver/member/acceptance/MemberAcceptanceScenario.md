## 사용자의 동영상 목록 조회

### 사용자가 업로드한 동영상 목록 조회
<hr>

* 초기 설정 : member, video 20개를 생성

#### 성공
<hr>

1. 존재하는 사용자가 업로드한 영상 목록 조회
    * given  
        member, video 20개를 생성
    * when  
        memberId를 memeber의 아이디, last_video_id를 null, 3, 8, 30으로 각각 api 요청
    * then  
        last_video_id보다 id가 작은 컨텐츠가 18개 미만일 때는 컨텐츠의 갯수만큼 영상 정보 반환
        last_video_id보다 id가 작은 컨텐츠가 18개 이상일 때는 18개의 영상 정보 반환

#### 실패
<hr>

1. 존재하지 않는 사용자가 업로드한 영상 목록 조회
    * given
    * when  
        memberId를 임의의 수로 하여 api 요창
    * then  
        존재하지 않는 사용자라는 메시지와 함께 에러 발생

### 사용자가 좋아요를 누른 동영상 목록 조회
<hr>

* 초기 설정 : member, video 20개를 생성

#### 성공
<hr>

1. 존재하는 사용자가 좋아요를 누른 영상 목록 조회
    * given  
        member, video 20개를 생성
    * when  
        memberId를 memeber의 아이디, last_video_id를 null, 3, 8, 30으로 각각 api 요청
    * then  
        last_video_id보다 id가 작은 컨텐츠가 18개 미만일 때는 컨텐츠의 갯수만큼 영상 정보 반환
        last_video_id보다 id가 작은 컨텐츠가 18개 이상일 때는 18개의 영상 정보 반환

#### 실패
<hr>

1. 존재하지 않는 사용자가 좋아요를 누른 영상 목록 조회
    * given
    * when  
        memberId를 임의의 수로 하여 api 요청
    * then  
        존재하지 않는 사용자라는 메시지와 함께 에러 발생

<hr>

## 사용자 정보 조회

* 초기 설정 : member를 생성 

#### 성공
<hr>

1. 존재하는 사용자의 정보 조회
    * given  
        member를 생성
    * when  
        memberId를 member의 아이디로 하여 정보 조회 요청
    * then  
        member의 정보 반환
<br>

#### 실패
<hr>

1. 존재하지 않는 사용자의 정보 조회
    * given
    * when  
        memberId를 임의의 수로 하여 정보 조회 요청
    * then  
        존재하지 않는 사용자라는 메시지와 함께 에러 발생

## 사용자 프로필 이미지 등록

* 초기 설정 : member를 생성

### 성공
<hr>

1. 사용자의 프로필 이미지 수정
    * given  
        member를 생성
    * when  
        member의 토큰으로 요청  
        이미지를 전송
    * then  
        프로필 이미지가 수정되었다는 메시지와 함께 성공

<br>

## 사용자 닉네임 수정

* 초기 설정 : member를 생성

### 성공
<hr>

1. 사용자의 닉네임 수정
    * given  
        member를 생성
    * when  
        member의 토큰으로 요청  
        수정하려는 닉네임을 json으로 전송
    * then  
        닉네임이 수정되었다는 메시지와 함께 성공

<br>

## 사용자 이메일 등록

* 초기 설정 : member를 생성

#### 성공
<hr>

1. 사용자의 이메일 등록
    * given  
        member를 생성
    * when  
        member의 토큰으로 요청  
        수정하려는 이메일을 json으로 전송
    * then  
        이메일이 등록되었다는 메시지와 함께 성공