# 업무 관리 앱 (Task Management API)

Spring Boot를 활용한 업무 관리 REST API 서버입니다.

## 실행 방법

1. Maven 라퍼(Wrapper) 생성 (선택사항, IDE 사용 시 자동 처리됨):
   ```bash
   mvn -N io.takari:maven:wrapper
   ```
2. 앱 실행:
   ```bash
   ./mvnw spring-boot:run
   ```
   또는 IDE(IntelliJ 등)에서 `TaskApplication.java` 실행.

## API 명세
- `GET /api/tasks`: 모든 업무 조회
- `POST /api/tasks`: 업무 생성
- `PUT /api/tasks/{id}`: 업무 수정
- `DELETE /api/tasks/{id}`: 업무 삭제
