## Description - 구현 기능

<!-- 구현 및 작업 내용을 적어주세요 -->
**로그인 한 유저 전용 타임머신 CRUD 기능**

- 기본적인 회원가입
- AccessToken 을 이용한 로그인
- AccessToken + RefreshToken 을 이용한 로그인
- 인증된 사용자만 가능한 TimeMachine CRUD 
- 인증된 사용자에 관한 User CRUD
- Spring Security 인증 예외 커스텀 처리 (CustomAuthenticationEntryPoint) 및 기타 예외 처리

## API 목록
| 메서드 | 경로                           | 설명                   | 요청 DTO              | 응답 DTO               |
|----|-----------------------|------------|---------|-------|
| **AuthController** |
| POST   | /api/auth/signUp               | 회원가입                | `SignUpReqDto`        | `SignUpResDto`         |
| POST   | /api/auth/signIn               | 로그인 (JWT 발급)       | `SignInReqDto`        | `AccessTokenResDto`    |
| POST   | /api/auth/signIn/refresh  | Refresh Token 로그인   | `RefreshTokenSignInReqDto` | `RefreshTokenSignInResDto` |
| POST   | /api/auth/token/reissue             | Access Token 재발급    | -                     | `AccessTokenResDto`    |
| **TimeMachineController** |
| POST   | /api/time-machines             | 타임머신 생성           | `TimeMachineCreateReqDto` | `TimeMachineInfoResDto` |
| GET    | /api/time-machines/{timeMachineId}        | 특정 타임머신 조회      | -                     | `TimeMachineInfoResDto` |
| GET    | /api/time-machines       | 모든 타임머신 조회      | -                     | `List<TimeMachineInfoResDto>` |
| PATCH  | /api/time-machines/{timeMachineId}        | 타임머신 수정           | `TimeMachineUpdateReqDto` | `TimeMachineInfoResDto` |
| DELETE | /api/time-machines/{timeMachineId}        | 타임머신 삭제           | -                     | -                      |
| **UserController** |
| GET    | /api/users/profile                  | 사용자 정보 조회        | -                     | `UserInfoResDto`       |
| PATCH  | /api/users/profile                   | 사용자 정보 수정        | `UserUpdateReqDto`    | `UserUpdateResDto`     |
| DELETE | /api/users/profile                 | 사용자 탈퇴             | -                     | -                      |

## JWT 를 이용한 로그인 동작 과정
1. 회원가입한 사용자가 로그인을 합니다.
2. 로그인 성공 시, 서버에서 토큰이 발급됩니다. 이 때, 두 가지 방식의 토큰을 사용할 수 있습니다.
2-1. `AccessToken`: JWT 형식으로 발급됩니다. 
2-2. `AccessToken + RefreshToken`: AccessToken은 JWT형식으로 발급되지만, RefreshToken은 굳이 JWT 형식이 아니어도됩니다. 이번 과제에서는 둘 다 JWT 형식으로 발급하였습니다. RefreshToken 은 서버의 db나 별도 저장소에 저장됩니다.
3. 토큰을 받은 클라이언트는 로컬 스토리지와 같은 곳에 AccessToken을 저장해두고, 이후 서버로 요청을 보낼 때마다 AccessToken 을 함께 보냅니다.
4. 토큰을 받은 서버는 토큰에 있는 사용자의 인증정보, 권한 등을 확인하여 해당 사용자가 요청한 리소스에 접근을 허락할 지 결정합니다. 접근 가능하다면, 클라이언트는 원하는 요청을 처리할 수 있고 서버는 그에 합당한 응답을 보냅니다.
5. 만약, 클라이언트로부터 온 토큰이 만료되었다면 클라이언트에게 토큰이 만료되었다는 응답을 합니다.
6. 토큰 만료 응답을 받은 클라이언트에게는 두 가지 선택지가 있습니다. 
6-1. `RefreshToken 을 사용하지 않은 경우` : 재로그인을 하여 AccessToken 을 재발급 받아야합니다.
6-2. `RefreshToken 을 사용하는 경우` : 서버로 보내는 요청에 RefreshToken을 담아서 보냅니다. 그러면 서버는 처음에 db에 저장해두었던 RefreshToken 을 꺼내와서 요청에 들어있는 RefreshToken 과 일치한지 확인합니다. 만약 일치하다면, AccessToken을 자동으로 재발급해줍니다.
7. 최종적으로 클라이언트가 보내는 요청에 있는 AccessToken 이 유효하다면, 서버는 이에 맞는 응답을 보낼 수 있습니다.
