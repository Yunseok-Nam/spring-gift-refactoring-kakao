# 인수 테스트 체계 고도화

## 목표

누구나 동일하게 실행 가능하고, 프로덕션과 유사한 환경에서 테스트할 수 있는 시스템을 만든다.

개인 로컬 설정에 의존하지 않고, 반복 실행 가능하며 자동화된 테스트 환경을 구축한다.

### 왜 이것이 중요한가?

| 가치 | 설명 |
|------|------|
| 재현 가능성 | 다른 개발자도 동일한 결과를 얻을 수 있음 |
| Production Parity | 실제 배포 환경과 동일한 구조로 테스트 |
| 자동화 | 환경 준비부터 정리까지 자동으로 처리 |
| 격리 | 로컬 환경 오염 없이 독립적으로 실행 |

## 요구사항 개요

3단계 순차 구성이며, 이전 단계의 결과물을 기반으로 진행한다.

```
요구사항 1: Cucumber BDD 적용
    ↓ (Gherkin 시나리오 기반 테스트 완성)
요구사항 2: PostgreSQL + Docker Compose 통합
    ↓ (프로덕션 DB로 전환, 환경 자동화)
요구사항 3: Application 컨테이너화
    ↓ (전체 시스템 Docker 실행, E2E 테스트)
```

---

## 요구사항 1: Cucumber BDD 적용

### 목표

RestAssured 기반 인수 테스트를 Cucumber BDD 형식으로 전환하여, 비개발자도 이해할 수 있는 테스트 시나리오를 작성한다.

### 핵심 요구사항

- Gherkin 형식으로 테스트 시나리오 작성 (한글 Given-When-Then)
- Cucumber와 Spring Boot 통합
- Step Definitions 구현 (Given/When/Then)
- 시나리오 간 데이터 격리

### 검증

```bash
./gradlew test
# Cucumber 시나리오가 실행되고 통과해야 함
```

### 제출

- `README.md`에 실행 방법 추가

### 힌트

#### 핵심 키워드

| 키워드 | 설명 |
|--------|------|
| `io.cucumber:cucumber-spring` | Spring 통합 |
| `@CucumberContextConfiguration` | Spring Boot 설정 |
| `@ScenarioScope` | 시나리오별 Bean 생성 |
| `io.cucumber.java.ko` | 한글 Step Definitions |
| `src/test/resources/features/` | Feature file 위치 |
| JUnit Platform Suite API | 테스트 실행 엔진 |

#### 탐구 질문

- Gherkin의 Given/When/Then은 무엇을 의미하는가?
- Step Definitions에서 파라미터를 어떻게 추출하는가?
- 시나리오 간 Response 객체를 어떻게 공유하는가?
- `@Before` hook은 언제 실행되는가?
- RestAssured 포트 설정은 어디서 하는가?

#### 참고 자료

- [Cucumber Documentation](https://cucumber.io/docs)
- [Cucumber-Spring Integration](https://cucumber.io/docs/installation/java/)

---

## 요구사항 2: PostgreSQL + Docker Compose 통합

### 목표

H2 in-memory DB를 PostgreSQL로 전환하고, Docker Compose로 테스트 환경을 자동화한다.

### 핵심 요구사항

- Docker Compose로 PostgreSQL 실행 환경 구성
- Spring 프로파일로 테스트/개발 DB 분리
- 테스트 실행 시 DB 자동 시작 및 체크
- 각 시나리오마다 DB 초기화 (Test Isolation)

### 검증

```bash
./gradlew cucumberTest
# PostgreSQL이 자동으로 준비되고 테스트가 실행되어야 함
```

### 제출

- `README.md`에 실행 방법 업데이트
- 학습 내용을 별도 문서로 기록 (선택사항)

### 힌트

#### 핵심 키워드

| 키워드 | 설명 |
|--------|------|
| `docker-compose.yml` | 서비스 정의 |
| `healthcheck` | 컨테이너 준비 상태 확인 |
| `pg_isready` | PostgreSQL 체크 명령 |
| `@ActiveProfiles("cucumber")` | 테스트 프로파일 |
| `application-cucumber.properties` | 설정 분리 |
| Gradle Exec task | 외부 명령 실행 |
| `doFirst` / `finalizedBy` | 태스크 생명주기 관리 |

#### 탐구 질문

- Docker Compose의 `services`, `volumes`는 무엇인가?
- Health check는 왜 필요한가?
- Spring Profile은 어떻게 동작하는가?
- Gradle Task에서 Shell 스크립트를 어떻게 실행하는가?
- 테스트 실패 시에도 DB를 정리하려면 어떻게 해야 하는가?
- H2 단위 테스트와 PostgreSQL 통합 테스트를 어떻게 분리하는가?

#### 네트워크 이해

```
테스트 코드 (Host)  →  JDBC  →  localhost:5432  →  PostgreSQL (Container)
```

- 테스트 코드는 Host(로컬 머신)에서 실행된다.
- PostgreSQL은 Docker 컨테이너 내부에서 실행되며, 포트 매핑으로 접근한다.

#### 참고 자료

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)
- [Gradle Exec Task](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Exec.html)

---

## 요구사항 3: Application 컨테이너화

### 목표

Spring Boot 애플리케이션까지 Docker 컨테이너로 실행하여, 프로덕션과 완전히 동일한 환경에서 End-to-End 테스트를 수행한다.

### 핵심 요구사항

- Dockerfile 작성 (Multi-stage build)
- Docker Compose에 애플리케이션 서비스 추가
- 테스트가 Docker 컨테이너의 애플리케이션에 HTTP 요청
- 전체 시스템 빌드/시작/종료 자동화

### 검증

```bash
./gradlew dockerBuild
./gradlew dockerUp
curl http://localhost:28080        # 애플리케이션 응답 확인
./gradlew cucumberTest             # Docker 환경에서 테스트
./gradlew dockerDown
```

### 제출

- `README.md`에 Docker 기반 실행 방법 추가
- 학습 내용을 별도 문서로 기록 (선택사항)

### 힌트

#### 핵심 키워드

| 키워드 | 설명 |
|--------|------|
| Multi-stage build | `FROM ... AS builder`, `COPY --from=builder` |
| `eclipse-temurin:21-jre-alpine` | 경량 런타임 이미지 |
| `depends_on: condition: service_healthy` | 컨테이너 시작 순서 제어 |
| Docker network | service name이 hostname으로 사용됨 |
| `SPRING_DATASOURCE_URL` | 환경변수로 Spring 설정 주입 |
| `webEnvironment = NONE` | embedded 서버 제거 |
| Port mapping `28080:8080` | 호스트-컨테이너 포트 매핑 |

#### 탐구 질문

- Multi-stage build는 무엇이고 왜 사용하는가?
- Builder stage와 Runtime stage의 역할은?
- Docker 네트워크에서 service name이 어떻게 hostname이 되는가?
- 컨테이너 내부에서는 `postgres:5432`, 테스트에서는 `localhost:28080`인 이유는?
- `webEnvironment = NONE`을 사용하는 이유는?
- 왜 JdbcTemplate은 필요한가? (cleanup 용도)
- `.dockerignore`는 왜 필요한가?

#### 아키텍처 이해

```
┌─────────────────────────────────────────────────────┐
│  Host (로컬 머신)                                     │
│                                                       │
│  테스트 코드 ──HTTP──→ localhost:28080                  │
│  테스트 코드 ──JDBC──→ localhost:5432                   │
└───────────────────┬─────────────────┬───────────────┘
                    │                 │
    ┌───────────────▼──┐   ┌─────────▼────────────┐
    │  Docker: App      │   │  Docker: PostgreSQL   │
    │  (port 8080)      │   │  (port 5432)          │
    │                   │   │                       │
    │  ──JDBC──→ postgres:5432 ◄────────────────── │
    └───────────────────┘   └──────────────────────┘
```

- 테스트(Host) → HTTP → `localhost:28080` (Docker App)
- 테스트(Host) → JDBC → `localhost:5432` (Docker DB)
- App(Container) → JDBC → `postgres:5432` (Docker DB, 내부 네트워크)

#### 트러블슈팅 키워드

| 명령 | 용도 |
|------|------|
| `docker ps` | 컨테이너 상태 확인 |
| `docker logs` | 로그 확인 |
| `docker exec` | 컨테이너 내부 명령 실행 |
| `docker system prune` | 캐시 정리 |

#### 참고 자료

- [Multi-stage builds](https://docs.docker.com/build/building/multi-stage/)
- [Docker Compose Networking](https://docs.docker.com/compose/networking/)
- [Spring Boot Docker](https://spring.io/guides/gs/spring-boot-docker/)

---

## 미션 수행 가이드

### 자가 점검 질문

#### 전체 미션

- 다른 개발자도 동일한 명령으로 실행 가능한가?
- 실행 방법이 충분히 단순한가?
- 실패했을 때 원인을 쉽게 파악할 수 있는가?

#### 요구사항 1 (Cucumber)

- 비개발자가 시나리오를 읽고 이해할 수 있는가?
- Step Definitions는 재사용 가능하게 작성되었는가?
- 시나리오 간 데이터가 격리되는가?

#### 요구사항 2 (PostgreSQL)

- 왜 H2 대신 PostgreSQL을 사용하는가?
- Production Parity란 무엇인가?
- 테스트 실패 시에도 DB가 정리되는가?

#### 요구사항 3 (Docker)

- 왜 애플리케이션까지 컨테이너로 실행하는가?
- 테스트는 어디서 실행되고, 애플리케이션은 어디서 실행되는가?
- Multi-stage build를 사용하는 이유는?

### 핵심 개념

| 개념 | 설명 |
|------|------|
| Production Parity | 개발/테스트/프로덕션 환경을 최대한 동일하게 |
| Test Isolation | 각 테스트가 독립적으로 실행 가능 |
| Idempotency | 반복 실행해도 같은 결과 |
| BDD | 비즈니스 언어로 테스트 작성 |

### 학습 팁

- 에러 메시지를 주의 깊게 읽고 검색하기
- 각 요구사항 완료 시마다 커밋하기
- Claude Code에게 개념 질문하기 (코드 작성 요청 X)
- 왜 이렇게 하는지 이해하고 넘어가기
- 학습한 내용을 별도 문서로 정리하기
