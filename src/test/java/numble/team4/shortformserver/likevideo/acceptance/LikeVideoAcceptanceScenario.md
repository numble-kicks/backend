## 영상 좋아요 기능 시나리오

* 초기 설정 : user1, video1을 생성
* 토큰 설정 : @WithMockUser 애노테이션으로 대체
<br>

### 좋아요 등록 여부 확인
<hr>

#### 성공
1. 좋아요를 등록하지 않은 영상에 대해 테스트
    * given  
        user1을 생성
    * when  
        user1의 토큰으로 요청  
        video_id를 임의의 수로 하여 좋아요 여부 확인 요청
    * then  
        body에 exist_like_video : false를 포함하며 성공
<br>

2. 좋아요를 등록한 영상에 대해 테스트
    * given  
        user1, video1을 생성  
        user1 -> video1 좋아요 생성
    * when  
        user1의 토큰으로 요청  
        video_id를 video1의 아이디로 하여 좋아요 여부 확인 요청
    * then  
        body에 exist_like_video : true, likes_id를 포함하며 성공

<br>

### 좋아요 등록
<hr>

#### 성공
1. 좋아요를 누르지 않는 동영상에 좋아요 등록 요청
   * given  
        user1, video1을 생성
   * when  
        user1의 토큰으로 요청  
        video_id를 video1의 아이디로 하여 좋아요 등록 요청  
   * then
        좋아요가 등록되었다는 메시지와 함께 성공 (status: 200)

<br>

#### 실패
1. 존재하지 않는 동영상에 좋아요 등록 요청  
    * given  
        user1을 생성
    * when  
        user1의 토큰으로 요청  
        video_id를 임의의 수로 하여 좋아요 등록 요청
    * then  
        존재하지 않는 영상이라는 메시지와 함께 에러 발생

<br>

2. 이미 좋아요를 누른 동영상에 좋아요 등록 요청  
    * given  
        user1, video1을 생성  
        user1의 토큰으로 요청  
        video_id를 video1의 아이디로 하여 좋아요 등록 요청
    * when  
        user1의 토큰으로 요청  
        video_id를 video1의 아이디로 하여 좋아요 등록 요청
    * then  
        이미 좋아요를 누른 동영상이라는 메시지와 함꼐 에러 발생

<br>

### 좋아요 삭제
<hr>

#### 성공

1. 본인이 생성한 좋아요 삭제 요청 
    * given
        user1, video1 생성  
        user1의 토큰으로 요청  
        video_id를 video1의 아이디로 하여 좋아요 등록 요청  
    * when  
        user1의 토큰으로 요청  
        video_id를 video1의 아이디로 하여 좋아요 삭제 요청
    * then
        좋아요가 삭제되었다는 메시지와 함께 성공 (status: 200)

#### 실패
1. 존재하지 않는 좋아요 삭제 요청
    * given  
        user1 생성
    * when  
        user1의 토큰으로 요청  
        video_id를 임의의 수로 하여 좋아요 삭제 요청
    * then
        존재하지 않는 좋아요라는 메시지와 함께 에러 발생

<br>

2. 본인이 생성하지 않은 좋아요 삭제 요청
    * given  
        user1, user2, video1 생성  
        user1 -> video1 좋아요 생성  
    * when  
        user2의 토큰으로 요청
        video_id를 video1의 아이디로 하여 좋아요 삭제 요청
    * then  
        본인이 등록한 좋아요만 삭제할 수 있다는 메시지와 함께 에러 발생