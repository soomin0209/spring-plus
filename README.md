# ➕ CH 5 플러스 Spring 과제

## 🛠 기술 스택
- Java 17, Spring Boot 3.3.3
- Spring Security, JWT
- JPA, QueryDSL
- MySQL (RDS), H2
- AWS EC2, RDS, S3, Parameter Store

## ✅ 필수 기능
### Level. 1
<details>
  <summary>1. 코드 개선 퀴즈 - `@Transactional`의 이해</summary>
  <br>

- `TodoService.saveTodo()`에 `@Transactional(readOnly = true)`가 적용되어 있어 쓰기 작업이 불가능한 문제 발생
- `@Transactional`로 수정하여 정상적으로 할 일 저장이 가능하도록 개선

  <br>
</details>

<details>
  <summary>2. 코드 추가 퀴즈 - JWT의 이해</summary>
  <br>

- User 테이블에 nickname 칼럼 추가
- JWT 토큰 생성 시 nickname 클레임 포함하도록 수정
- 프론트엔드에서 JWT 파싱으로 nickname 사용 가능

  <br>
</details>

<details>
  <summary>3. 코드 개선 퀴즈 - JPA의 이해</summary>
  <br>

- 할 일 검색 시 weather 조건 및 수정일 기준 기간 검색 기능 추가
- 조건이 없을 경우에도 정상 동작하도록 JPQL 동적 쿼리 구현

  <br>
</details>

<details>
  <summary>4. 테스트 코드 퀴즈 - 컨트롤러 테스트의 이해</summary>
  <br>

- `todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다()` 테스트 실패 원인 파악 및 수정
- InvalidRequestException 예외 처리 및 응답 코드가 올바르게 동작하도록 테스트 코드 수정

  <br>
</details>

<details>
  <summary>5. 코드 개선 퀴즈 - AOP의 이해</summary>
  <br>

- AdminAccessLoggingAspect의 포인트컷이 잘못된 메서드를 대상으로 동작하는 문제 발견
- `UserAdminController.changeUserRole()` 실행 전에 AOP가 동작하도록 포인트컷 수정

  <br>
</details>

### Level. 2
<details>
  <summary>6. JPA Cascade</summary>
  <br>

- 할 일 저장 시 생성한 유저가 담당자로 자동 등록되지 않는 문제 발견
- Todo 엔티티의 managers 연관관계에 `CascadeType.PERSIST` 적용
- 할 일 생성 시 Manager 엔티티가 자동으로 함께 저장되도록 수정

  <br>
</details>

<details>
  <summary>7. N+1</summary>
  <br>

- `getComment()` API 호출 시 댓글 수만큼 유저 조회 쿼리가 추가 발생하는 N+1 문제 발견
- Fetch Join을 사용하여 댓글과 유저를 한 번의 쿼리로 조회하도록 수정

  <br>
</details>

<details>
  <summary>8. QueryDSL</summary>
  <br>

- JPQL로 작성된 `findByIdWithUser` 쿼리를 QueryDSL로 변경
- Fetch Join을 적용하여 N+1 문제 방지
- 타입 안전한 쿼리 작성으로 컴파일 시점에 오류 감지 가능

  <br>
</details>

<details>
  <summary>9. Spring Security</summary>
  <br>

- 기존 Filter, ArgumentResolver 기반 인증/인가 방식을 Spring Security로 전환
- JwtAuthenticationFilter를 구현하여 토큰 기반 인증 유지
- SecurityConfig에서 URL별 접근 권한 설정
- `@AuthenticationPrincipal`로 인증된 유저 정보 주입

  <br>
</details>

## ✅ 도전 기능
### Level. 3
<details>
  <summary>10. QueryDSL을 사용하여 검색 기능 만들기</summary>
  <br>

- 새로운 일정 검색 API 추가 (GET /search/todos)
- QueryDSL Projections를 활용하여 필요한 필드만 반환
- 검색 조건
    - 일정 제목 부분 검색
    - 생성일 범위 검색
    - 담당자 닉네임 부분 검색
- 응답 데이터: 일정 제목, 담당자 수, 댓글 수
- 페이징 처리 적용

  <br>
</details>

<details>
  <summary>11. Transaction 심화</summary>
  <br>

- 매니저 등록 요청 시 log 테이블에 요청 로그 저장
- `@Transactional(propagation = Propagation.REQUIRES_NEW)` 적용
- 매니저 등록 실패 여부와 관계없이 로그는 항상 저장되도록 독립적 트랜잭션으로 분리

  <br>
</details>

<details>
  <summary>12. AWS 활용</summary>
  <br>

**12-1. EC2**

<img src="https://github.com/user-attachments/assets/c1dbfb2c-a340-467c-aa12-87b95bfb63a2" width="750" />

- EC2 인스턴스에 Spring Boot 애플리케이션 배포
- 탄력적 IP 설정으로 외부 접속 가능
- Health Check API 구현 (GET /health, 인증 불필요)
    - 🔗 http://3.39.47.208:8080/health
- local / prod 프로필 분리

**12-2. RDS**

<img src="https://github.com/user-attachments/assets/f7af7fa3-91e7-403a-98e4-bcd023879d92" width="750" />

- MySQL RDS 인스턴스 생성 및 EC2 애플리케이션과 연결
- EC2 보안 그룹에서만 RDS 접근 허용하도록 보안 그룹 구성
- AWS Parameter Store를 통한 DB 접속 정보 관리

**12-3. S3**

<img src="https://github.com/user-attachments/assets/c1b32e04-1832-4eff-a1fe-a58eba2d41ea" width="750" />

- S3 버킷 생성 및 프로필 이미지 업로드/다운로드/삭제 API 구현
- Presigned URL 적용 (유효기간 7일)
- IAM Role을 통한 EC2 → S3 접근 권한 관리

  <br>
</details>

