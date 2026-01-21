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

## 구조 설명 (MVC + DTO)
- **Controller (TaskController)**: HTTP 요청/응답만 담당하며, 모든 비즈니스 로직을 서비스로 위임합니다.
- **Service (TaskService)**: 업무 생성/수정/삭제 등 비즈니스 로직을 처리하고, 엔티티와 DTO 간 변환을 수행합니다.
- **Repository (TaskRepository)**: JPA 기반 데이터 접근만 책임집니다.
- **DTO (TaskRequest/TaskResponse)**: 엔티티와 API 스키마를 분리하며, 요청/응답 객체는 Lombok `@Builder` 기반으로 생성됩니다.
