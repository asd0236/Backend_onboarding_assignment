# Backend_onboarding_assignment
## 커리어톤 한 달 인턴 백엔드 온보딩 과제 (JAVA)

### 배포 주소 : http://18.206.255.95:8080

### Swagger UI 주소 : http://18.206.255.95:8080/swagger-ui/index.html

### 회원 가입 API 요청 주소(POST) : http://18.206.255.95:8080/signup
- request body
```json
{
	"username": "JIN HO",
	"password": "12341234",
	"nickname": "Mentos"
}
```

- response body
```json
{
    "username": "JIN HO",
    "nickname": "Mentos",
    "authorities": {
        "authorityName": "ROLE_USER"
    }
}
```

### 로그인 API 요청 주소(POST) : http://18.206.255.95:8080/sign
- request body
```json
{
	"username": "JIN HO",
	"password": "12341234"
}
```

- response body
```json
{
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwidXNlcm5hbWUiOiJKSU4gSE8iLCJyb2xlIjp7ImF1dGhvcml0eU5hbWUiOiJST0xFX1VTRVIifSwiZXhwIjoxNzMxMTU4OTM0LCJpYXQiOjE3MzExNTUzMzR9.cqYcF4zxTeEibhOOg91c_Mp8f3p18FEKpsb5Z7Brx44"
}
```

### 실행 방법
- 로컬에서 H2 DB 서버를 띄움
- applcation.yml 파일의 spring.profiles.active 옵션을 dev로 수정
- applcation-dev.yml 파일의 jwt.secret.key 에 필요한 JWT_SECRET_KEY 환경 변수 추가 후 실행
