# 인수 테스트 전략 — 요구사항 1: Cucumber BDD

## 1. 테스트 전략 개요

### 목표

RestAssured 기반 HTTP 인수 테스트를 **Cucumber BDD** 형식으로 작성한다.
각 시나리오는 한글 Gherkin(Given-When-Then)으로 표현하여, 코드를 모르는 사람도 어떤 동작을 검증하는지 이해할 수 있어야 한다.

### 범위

- **요구사항 1만** 다룬다.
- DB는 H2(in-memory, MySQL 호환 모드)를 사용한다.
- Docker는 사용하지 않는다.

### 검증 원칙

| 원칙 | 설명 |
|------|------|
| HTTP 응답만 검증 | 상태 코드와 응답 바디만 단언한다. DB 직접 조회는 하지 않는다 |
| 시나리오 독립성 | 각 시나리오 실행 전 DB를 초기화하여 실행 순서에 무관하게 동일한 결과를 보장한다 |
| 한글 Gherkin | `주어진`/`만약`/`그러면`/`그리고` 키워드를 사용한다 |
| Repository 시드 | SQL 스크립트 대신 Spring Repository로 테스트 데이터를 삽입한다 |

### 현재 아키텍처 특이사항

| 사항 | 영향 |
|------|------|
| 서비스 레이어 없음 | 비즈니스 로직이 컨트롤러와 엔티티에 분산되어 있다 |
| `@ExceptionHandler` 유무 | MemberController, ProductController, OptionController에는 있고(→ 400), OrderController에는 없다(→ 500) |
| `@RequestHeader("Authorization")` 필수 | 헤더 누락 시 Spring이 400을 반환한다. 인증 실패(401) 테스트는 유효하지 않은 토큰을 전송해야 한다 |
| `deleteById` 동작 | Category, Product의 DELETE는 존재하지 않는 ID에도 204를 반환한다 |

---

## 2. 기술 스택 및 의존성

### 추가할 의존성 (`build.gradle.kts`)

```kotlin
val cucumberVersion = "7.18.1"

testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
testImplementation("io.cucumber:cucumber-spring:$cucumberVersion")
testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
testImplementation("org.junit.platform:junit-platform-suite-api:1.11.0")
testImplementation("org.junit.platform:junit-platform-suite-engine:1.11.0")
testImplementation("io.rest-assured:rest-assured:5.5.0")
```

### 라이브러리 역할

| 라이브러리 | 역할 |
|-----------|------|
| `cucumber-java` | Gherkin Step을 Java 메서드에 연결. `io.cucumber.java.ko` 패키지로 한글 키워드 제공 |
| `cucumber-spring` | Cucumber와 Spring ApplicationContext 통합. `@CucumberContextConfiguration` 지원 |
| `cucumber-junit-platform-engine` | JUnit 5 플랫폼에서 Cucumber 테스트 실행 |
| `junit-platform-suite-api/engine` | `@Suite` 러너 클래스로 Cucumber 테스트 묶음 실행 |
| `rest-assured` | HTTP 요청/응답 검증용 DSL |

---

## 3. 테스트 인프라 구조

### 파일 구조

```
src/test/
├── java/gift/acceptance/
│   ├── CucumberRunnerTest.java      # @Suite 러너
│   ├── CucumberSpringConfig.java    # @CucumberContextConfiguration + @SpringBootTest
│   ├── ScenarioContext.java         # @ScenarioScope 상태 공유 Bean
│   ├── Hooks.java                   # @Before DB 초기화, RestAssured 포트 설정
│   └── steps/
│       ├── CommonSteps.java         # 공통 응답 검증 Step
│       ├── MemberSteps.java         # 회원 등록/로그인
│       ├── CategorySteps.java       # 카테고리 CRUD
│       ├── ProductSteps.java        # 상품 CRUD
│       ├── OptionSteps.java         # 옵션 CRUD
│       ├── WishSteps.java           # 위시리스트
│       └── OrderSteps.java          # 주문
└── resources/
    ├── application-test.properties  # H2 테스트 설정
    └── features/
        ├── member.feature
        ├── category.feature
        ├── product.feature
        ├── option.feature
        ├── wish.feature
        └── order.feature
```

### CucumberRunnerTest

```java
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "gift.acceptance")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class CucumberRunnerTest {}
```

- `GLUE`: Step Definition과 Hook이 위치한 패키지
- `features`: `src/test/resources/features/` 아래 `.feature` 파일 자동 수집

### CucumberSpringConfig

```java
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberSpringConfig {}
```

- `RANDOM_PORT`: 내장 톰캣을 랜덤 포트로 기동하여 실제 HTTP 요청 가능
- `@ActiveProfiles("test")`: `application-test.properties` 활성화

### ScenarioContext

시나리오 내 Step 간 상태(HTTP 응답, 인증 토큰, 생성된 리소스 ID)를 공유한다.

```java
@Component
@ScenarioScope
public class ScenarioContext {
    private Response lastResponse;
    private String authToken;
    private Long lastCreatedId;
    // getter/setter
}
```

- `@ScenarioScope`: 각 시나리오마다 새 인스턴스가 생성되어 상태 누출이 없다

### Hooks

```java
public class Hooks {
    @Autowired private OrderRepository orderRepository;
    @Autowired private WishRepository wishRepository;
    @Autowired private OptionRepository optionRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private CategoryRepository categoryRepository;
    @LocalServerPort private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        // FK 제약 순서를 준수하여 삭제
        orderRepository.deleteAll();
        wishRepository.deleteAll();
        optionRepository.deleteAll();
        productRepository.deleteAll();
        memberRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
```

### application-test.properties

```properties
spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=none
spring.flyway.enabled=true
jwt.secret=test-secret-key-that-is-at-least-256-bits-long-for-testing
jwt.expiration=3600000
kakao.login.client-id=test
kakao.login.client-secret=test
kakao.login.redirect-uri=http://localhost/callback
```

- `MODE=MySQL`: Flyway 마이그레이션(V1)의 MySQL 문법(`auto_increment`)을 H2에서 지원
- V2 시드 데이터는 Hooks의 `@Before`에서 전체 삭제하여 시나리오 독립성 보장

---

## 4. Feature 파일 설계

### 설계 원칙

| 원칙 | 내용 |
|------|------|
| 도메인 1개 = Feature 파일 1개 | 시나리오 탐색과 유지보수 용이 |
| `배경(Background)` 활용 | 공통 전제조건(카테고리 생성, 로그인 등)은 Background로 중복 제거 |
| 시나리오 제목 = 동작 서술 | "~하면 ~를 반환한다" 형태로 한 문장 완결 |

### Feature 파일 목록

| 파일 | 대상 API | 인증 |
|------|---------|------|
| `member.feature` | POST /api/members/register, /login | 불필요 |
| `category.feature` | GET/POST/PUT/DELETE /api/categories | 불필요 |
| `product.feature` | GET/POST/PUT/DELETE /api/products | 불필요 |
| `option.feature` | GET/POST/DELETE /api/products/{id}/options | 불필요 |
| `wish.feature` | GET/POST/DELETE /api/wishes | **필요** |
| `order.feature` | GET/POST /api/orders | **필요** |

---

## 5. Step Definitions 설계

### 클래스 분리 전략

```
CommonSteps    → 응답 상태 코드 검증, 응답 바디 필드 검증 (모든 Feature에서 재사용)
MemberSteps    → 회원 등록/로그인 요청, 토큰 추출하여 ScenarioContext에 저장
CategorySteps  → 카테고리 CRUD 요청
ProductSteps   → 상품 CRUD 요청
OptionSteps    → 옵션 CRUD 요청
WishSteps      → 위시리스트 추가/조회/삭제 요청
OrderSteps     → 주문 생성/조회 요청
```

### 공통 Step 패턴

```java
// 응답 검증 (CommonSteps)
@그러면("응답 상태 코드가 {int}이다")
@그러면("응답 바디의 {string} 필드 값이 {string}이다")
@그러면("응답 바디에 {string} 필드가 존재한다")
```

### 인증 처리 패턴

인증이 필요한 API 테스트에서는 `ScenarioContext.authToken`을 사용한다.

```java
// 인증 헤더 포함 요청
private RequestSpecification withAuth() {
    return RestAssured.given()
        .header("Authorization", "Bearer " + scenarioContext.getAuthToken())
        .contentType(ContentType.JSON);
}

// 인증 실패 테스트 — 유효하지 않은 토큰 전송 (헤더 누락 ≠ 401)
private RequestSpecification withInvalidAuth() {
    return RestAssured.given()
        .header("Authorization", "Bearer invalid-token")
        .contentType(ContentType.JSON);
}
```

> **주의**: `@RequestHeader("Authorization")`가 `required=true`(기본값)이므로 헤더를 아예 보내지 않으면 Spring이 400을 반환한다.
> 인증 실패(401)를 테스트하려면 반드시 유효하지 않은 토큰을 포함한 헤더를 전송해야 한다.

### ID 체이닝

POST 응답에서 받은 리소스 ID를 `ScenarioContext.lastCreatedId`에 저장하여, 후속 Step(수정/삭제)에서 참조한다.

---

## 6. 데이터 전략

### 시나리오 간 격리

Hooks의 `@Before`에서 모든 테이블을 FK 역순으로 DELETE한다.

```
삭제 순서: orders → wishes → options → products → members → categories
```

V2 마이그레이션이 삽입한 시드 데이터도 매 시나리오마다 삭제되므로 간섭이 없다.

### 테스트 데이터 시드

각 시나리오의 `주어진(Given)` Step에서 Repository를 직접 호출하여 필요한 데이터만 삽입한다.

```java
// CategorySteps
@주어진("{string} 카테고리가 존재한다")
public void 카테고리_존재(String name) {
    Category category = categoryRepository.save(
        new Category(name, "#FF0000", "https://img.example.com/img.jpg", "설명")
    );
    scenarioContext.setLastCreatedId(category.getId());
}
```

### 회원 인증 데이터 시드

인증이 필요한 시나리오(wish, order)에서는 Background에서 회원 등록 → 로그인 → 토큰 저장까지 수행한다.

```gherkin
배경:
  주어진 이메일이 "user@test.com", 비밀번호가 "pass123"인 회원이 등록되어 있다
  그리고 해당 회원으로 로그인하여 토큰을 획득한다
```

토큰 획득은 두 가지 방식 중 선택:
1. **API 호출**: `/api/members/register` → 응답에서 토큰 추출 (실제 플로우와 동일)
2. **JwtProvider 직접 호출**: `jwtProvider.createToken(email)` (더 빠르고 단순)

→ **방식 1(API 호출) 권장**: 인수 테스트는 외부 인터페이스만 사용하는 것이 원칙이다.

---

## 7. 시나리오 목록

> **선별 기준**: 서비스 이용 불가, 데이터 무결성 위반, 보안/인가 위반, 매출 영향이 발생하는 크리티컬 시나리오만 포함한다.

### 7.1 member.feature — 회원 인증

```gherkin
# language: ko
기능: 회원 인증
  회원은 이메일과 비밀번호로 등록하고 로그인하여 JWT 토큰을 발급받는다.

  시나리오: 새로운 이메일로 회원을 등록하면 201과 토큰을 반환한다
    만약 이메일이 "new@example.com", 비밀번호가 "secret123"인 회원 등록을 요청한다
    그러면 응답 상태 코드가 201이다
    그리고 응답 바디에 "token" 필드가 존재한다

  시나리오: 이미 등록된 이메일로 회원 등록을 시도하면 400을 반환한다
    주어진 이메일이 "dup@example.com", 비밀번호가 "pass"인 회원이 등록되어 있다
    만약 이메일이 "dup@example.com", 비밀번호가 "pass"인 회원 등록을 요청한다
    그러면 응답 상태 코드가 400이다

  시나리오: 등록된 회원이 올바른 자격증명으로 로그인하면 200과 토큰을 반환한다
    주어진 이메일이 "user@example.com", 비밀번호가 "password123"인 회원이 등록되어 있다
    만약 이메일이 "user@example.com", 비밀번호가 "password123"으로 로그인을 요청한다
    그러면 응답 상태 코드가 200이다
    그리고 응답 바디에 "token" 필드가 존재한다
```

---

### 7.2 category.feature — 카테고리

```gherkin
# language: ko
기능: 카테고리 관리
  카테고리는 상품의 전제조건이므로 생성과 조회가 정상 동작해야 한다.

  시나리오: 새로운 카테고리를 생성하면 201을 반환한다
    만약 이름이 "생활용품", 색상이 "#00FF00"인 카테고리를 생성 요청한다
    그러면 응답 상태 코드가 201이다
    그리고 응답 바디의 "name" 필드 값이 "생활용품"이다

  시나리오: 카테고리 목록을 조회하면 200을 반환한다
    주어진 "교환권" 카테고리가 존재한다
    만약 카테고리 목록을 조회한다
    그러면 응답 상태 코드가 200이다
```

---

### 7.3 product.feature — 상품

```gherkin
# language: ko
기능: 상품 관리
  상품 이름에 "카카오"를 포함할 수 없는 비즈니스 규칙이 존재한다.

  배경:
    주어진 "교환권" 카테고리가 존재한다

  시나리오: 유효한 상품을 생성하면 201을 반환한다
    만약 이름이 "아이스라떼", 가격이 5000원인 상품을 생성 요청한다
    그러면 응답 상태 코드가 201이다
    그리고 응답 바디의 "name" 필드 값이 "아이스라떼"이다

  시나리오: "카카오"가 포함된 이름으로 상품을 생성하면 400을 반환한다
    만약 이름이 "카카오 선물"인 상품을 생성 요청한다
    그러면 응답 상태 코드가 400이다

  시나리오: 상품 목록을 조회하면 200을 반환한다
    주어진 해당 카테고리에 "아이스아메리카노" 상품이 존재한다
    만약 첫 번째 페이지 상품 목록을 조회한다
    그러면 응답 상태 코드가 200이다
    그리고 응답 바디에 "content" 필드가 존재한다

  시나리오: 존재하지 않는 카테고리 ID로 상품을 생성하면 404를 반환한다
    만약 카테고리 ID가 99999인 상품을 생성 요청한다
    그러면 응답 상태 코드가 404이다
```

---

### 7.4 option.feature — 옵션

```gherkin
# language: ko
기능: 상품 옵션 관리
  각 상품에는 하나 이상의 옵션이 있어야 하며, 동일 이름의 옵션은 허용되지 않는다.

  배경:
    주어진 "교환권" 카테고리가 존재한다
    그리고 해당 카테고리에 "아이스아메리카노" 상품이 존재한다

  시나리오: 유효한 옵션을 추가하면 201을 반환한다
    만약 해당 상품에 이름이 "L 사이즈", 수량이 10인 옵션을 추가 요청한다
    그러면 응답 상태 코드가 201이다
    그리고 응답 바디의 "name" 필드 값이 "L 사이즈"이다

  시나리오: 같은 상품에 동일한 이름의 옵션을 추가하면 400을 반환한다
    주어진 해당 상품에 "L 사이즈" 옵션이 존재한다
    만약 해당 상품에 이름이 "L 사이즈", 수량이 5인 옵션을 추가 요청한다
    그러면 응답 상태 코드가 400이다

  시나리오: 마지막 남은 옵션을 삭제하면 400을 반환한다
    주어진 해당 상품에 "L 사이즈" 옵션만 존재한다
    만약 "L 사이즈" 옵션을 삭제 요청한다
    그러면 응답 상태 코드가 400이다
```

---

### 7.5 wish.feature — 위시리스트

```gherkin
# language: ko
기능: 위시리스트 관리
  인증된 회원만 자신의 위시리스트를 관리할 수 있다.

  배경:
    주어진 "교환권" 카테고리가 존재한다
    그리고 해당 카테고리에 "아이스아메리카노" 상품이 존재한다
    그리고 이메일이 "member@test.com", 비밀번호가 "pass123"인 회원이 등록되어 있다
    그리고 해당 회원으로 로그인하여 토큰을 획득한다

  시나리오: 인증된 회원이 상품을 위시에 추가하면 201을 반환한다
    만약 인증 헤더를 포함하여 해당 상품을 위시에 추가 요청한다
    그러면 응답 상태 코드가 201이다

  시나리오: 이미 위시에 있는 상품을 다시 추가하면 200과 기존 항목을 반환한다
    주어진 인증 헤더를 포함하여 해당 상품을 위시에 추가했다
    만약 인증 헤더를 포함하여 동일한 상품을 위시에 다시 추가 요청한다
    그러면 응답 상태 코드가 200이다

  시나리오: 다른 회원의 위시 항목을 삭제하면 403을 반환한다
    주어진 이메일이 "other@test.com", 비밀번호가 "pass456"인 다른 회원이 등록되어 있다
    그리고 다른 회원이 로그인하여 해당 상품을 위시에 추가했다
    만약 첫 번째 회원의 인증 헤더로 다른 회원의 위시 항목을 삭제 요청한다
    그러면 응답 상태 코드가 403이다

  시나리오: 유효하지 않은 토큰으로 위시에 추가하면 401을 반환한다
    만약 유효하지 않은 토큰으로 해당 상품을 위시에 추가 요청한다
    그러면 응답 상태 코드가 401이다
```

---

### 7.6 order.feature — 주문

```gherkin
# language: ko
기능: 주문 처리
  인증된 회원만 주문할 수 있으며, 주문 시 재고와 포인트가 차감된다.

  배경:
    주어진 "교환권" 카테고리가 존재한다
    그리고 해당 카테고리에 가격이 4500원인 "아이스아메리카노" 상품이 존재한다
    그리고 해당 상품에 수량이 10인 "L 사이즈" 옵션이 존재한다
    그리고 포인트가 100000인 "buyer@test.com" 회원이 등록되어 있다
    그리고 해당 회원으로 로그인하여 토큰을 획득한다

  시나리오: 충분한 포인트와 재고가 있을 때 주문하면 201을 반환한다
    만약 인증 헤더를 포함하여 "L 사이즈" 옵션 1개를 주문 요청한다
    그러면 응답 상태 코드가 201이다
    그리고 응답 바디에 "optionId" 필드가 존재한다

  시나리오: 재고보다 많은 수량을 주문하면 500을 반환한다
    만약 인증 헤더를 포함하여 "L 사이즈" 옵션 99개를 주문 요청한다
    그러면 응답 상태 코드가 500이다

  시나리오: 포인트가 부족할 때 주문하면 500을 반환한다
    주어진 포인트가 100인 "poor@test.com" 회원이 등록되어 있다
    그리고 해당 회원으로 로그인하여 토큰을 갱신한다
    만약 인증 헤더를 포함하여 "L 사이즈" 옵션 1개를 주문 요청한다
    그러면 응답 상태 코드가 500이다

  시나리오: 유효하지 않은 토큰으로 주문하면 401을 반환한다
    만약 유효하지 않은 토큰으로 주문 요청한다
    그러면 응답 상태 코드가 401이다
```

---

## 시나리오 수 요약

| Feature 파일 | 시나리오 수 | 선별 근거 |
|-------------|-----------|----------|
| member.feature | 3 | 서비스 진입 불가 방지 |
| category.feature | 2 | 상품 생성의 전제조건 |
| product.feature | 4 | 핵심 비즈니스 규칙(카카오 금지), 데이터 무결성 |
| option.feature | 3 | 주문의 전제조건, 데이터 무결성(마지막 옵션 보호) |
| wish.feature | 4 | 인가 위반 방지, 멱등성 보장 |
| order.feature | 4 | 매출 영향(재고/포인트 초과), 인증 보안 |
| **합계** | **20** | |

---

## 구현 체크리스트

```
[ ] build.gradle.kts에 Cucumber, RestAssured, JUnit Platform Suite 의존성 추가
[ ] src/test/resources/application-test.properties 생성
[ ] CucumberRunnerTest.java 생성
[ ] CucumberSpringConfig.java 생성
[ ] ScenarioContext.java 생성
[ ] Hooks.java 생성 (@Before DB 초기화 + RestAssured 포트 설정)
[ ] CommonSteps.java 생성 (공통 응답 검증)
[ ] 6개 .feature 파일 생성
[ ] 6개 도메인별 Step Definition 클래스 생성
[ ] ./gradlew test 실행하여 전체 시나리오 통과 확인
```
