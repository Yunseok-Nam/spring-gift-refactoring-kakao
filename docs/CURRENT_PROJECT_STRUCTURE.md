# 프로젝트 구조

> Spring Boot 선물하기 플랫폼 - 프로젝트 구조 문서

## 디렉토리 트리

```
spring-gift-refactoring-kakao/
├── build.gradle.kts
├── settings.gradle.kts
├── CLAUDE.md
├── README.md
│
├── gradle/wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/gift/
│   │   │   ├── Application.java
│   │   │   ├── auth/
│   │   │   │   ├── KakaoAuthController.java
│   │   │   │   ├── KakaoAuthService.java
│   │   │   │   ├── KakaoLoginClient.java
│   │   │   │   ├── KakaoLoginProperties.java
│   │   │   │   ├── AuthenticationResolver.java
│   │   │   │   ├── JwtProvider.java
│   │   │   │   └── TokenResponse.java
│   │   │   ├── category/
│   │   │   │   ├── Category.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── CategoryRequest.java
│   │   │   │   └── CategoryResponse.java
│   │   │   ├── member/
│   │   │   │   ├── Member.java
│   │   │   │   ├── MemberController.java
│   │   │   │   ├── AdminMemberController.java
│   │   │   │   ├── MemberService.java
│   │   │   │   ├── MemberRepository.java
│   │   │   │   └── MemberRequest.java
│   │   │   ├── product/
│   │   │   │   ├── Product.java
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── AdminProductController.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── ProductRequest.java
│   │   │   │   ├── ProductResponse.java
│   │   │   │   └── ProductNameValidator.java
│   │   │   ├── option/
│   │   │   │   ├── Option.java
│   │   │   │   ├── OptionController.java
│   │   │   │   ├── OptionService.java
│   │   │   │   ├── OptionRepository.java
│   │   │   │   ├── OptionRequest.java
│   │   │   │   ├── OptionResponse.java
│   │   │   │   └── OptionNameValidator.java
│   │   │   ├── order/
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── OrderRequest.java
│   │   │   │   ├── OrderResponse.java
│   │   │   │   └── KakaoMessageClient.java
│   │   │   └── wish/
│   │   │       ├── Wish.java
│   │   │       ├── WishController.java
│   │   │       ├── WishService.java
│   │   │       ├── WishRepository.java
│   │   │       ├── WishRequest.java
│   │   │       └── WishResponse.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/migration/
│   │       │   ├── V1__Initialize_project_tables.sql
│   │       │   └── V2__Insert_default_data.sql
│   │       ├── static/
│   │       └── templates/
│   │           ├── member/
│   │           │   ├── list.html
│   │           │   ├── new.html
│   │           │   └── edit.html
│   │           └── product/
│   │               ├── list.html
│   │               ├── new.html
│   │               └── edit.html
│   │
│   └── test/
│       ├── java/gift/
│       │   └── acceptance/
│       │       ├── CucumberRunnerTest.java
│       │       ├── CucumberSpringConfig.java
│       │       ├── Hooks.java
│       │       ├── ScenarioContext.java
│       │       └── steps/
│       │           ├── CommonSteps.java
│       │           ├── MemberSteps.java
│       │           ├── CategorySteps.java
│       │           ├── ProductSteps.java
│       │           ├── OptionSteps.java
│       │           ├── WishSteps.java
│       │           └── OrderSteps.java
│       ├── resources/
│       │   ├── application-test.properties
│       │   └── features/
│       │       ├── member.feature
│       │       ├── category.feature
│       │       ├── product.feature
│       │       ├── option.feature
│       │       ├── wish.feature
│       │       └── order.feature
│       └── kotlin/gift/
│
└── docs/
    ├── TEST_PLAN.md
    └── PROJECT_STRUCTURE.md
```

## 기술 스택

| 기술 | 버전 | 용도 |
|------|------|------|
| Spring Boot | 3.5.9 | 애플리케이션 프레임워크 |
| Java | 21 | 프로그래밍 언어 |
| Kotlin | 1.9.25 | 대체 언어 (옵션) |
| Spring Data JPA | - | ORM |
| Thymeleaf | - | 서버 사이드 렌더링 |
| JJWT | 0.13.0 | JWT 토큰 |
| Flyway | 12.0.1 | DB 마이그레이션 |
| H2 | - | 개발 in-memory DB |
| MySQL | - | MySQL 드라이버 |
| Cucumber | 7.18.1 | BDD 인수 테스트 |
| RestAssured | 5.5.0 | HTTP API 테스트 |
| JUnit Platform Suite | 1.11.0 | Cucumber 테스트 실행 |
| Gradle | 8.14 | 빌드 도구 |
| Ktlint | 14.0.1 | 코드 포맷팅 |

## 패키지 구조

### auth - 인증 및 JWT 관리

| 클래스 | 역할 |
|--------|------|
| JwtProvider | JWT 토큰 생성/검증 |
| AuthenticationResolver | Authorization 헤더에서 인증된 회원 추출 |
| KakaoAuthController | 카카오 OAuth2 로그인 엔드포인트 |
| KakaoAuthService | 카카오 OAuth 인증 흐름 (URL 구성, 콜백 처리, 회원 동기화) |
| KakaoLoginClient | 카카오 API 호출 (토큰, 사용자 정보) |
| KakaoLoginProperties | 카카오 설정 값 (clientId, clientSecret, redirectUri) |
| TokenResponse | JWT 토큰 응답 DTO |

### category - 상품 카테고리 관리

| 클래스 | 역할 |
|--------|------|
| Category | 엔티티 (이름, 색상, 이미지, 설명) |
| CategoryController | REST API (CRUD) |
| CategoryService | 카테고리 비즈니스 로직 (CRUD) |
| CategoryRepository | JpaRepository |
| CategoryRequest / CategoryResponse | 요청/응답 DTO |

### product - 상품 관리

| 클래스 | 역할 |
|--------|------|
| Product | 엔티티 (이름, 가격, 이미지, 카테고리) |
| ProductController | REST API (페이징 지원) |
| AdminProductController | Thymeleaf MVC 컨트롤러 |
| ProductService | 상품 비즈니스 로직 (CRUD, 이름 검증, 카테고리 조회) |
| ProductRepository | JpaRepository |
| ProductNameValidator | 상품명 검증 (최대 15자, 특수문자, "카카오" 제한) |
| ProductRequest / ProductResponse | 요청/응답 DTO |

### option - 상품 옵션 관리

| 클래스 | 역할 |
|--------|------|
| Option | 엔티티 (옵션명, 수량, 상품 참조) |
| OptionController | REST API (CRUD, 최소 1개 옵션 유지) |
| OptionService | 옵션 비즈니스 로직 (CRUD, 이름 검증, 중복 체크) |
| OptionRepository | JpaRepository |
| OptionNameValidator | 옵션명 검증 (최대 50자, 특수문자 제한) |
| OptionRequest / OptionResponse | 요청/응답 DTO |

### order - 주문 처리

| 클래스 | 역할 |
|--------|------|
| Order | 엔티티 (옵션, 회원ID, 수량, 메시지, 주문시간) |
| OrderController | REST API (주문 생성, 내 주문 조회) |
| OrderService | 주문 비즈니스 로직 (재고 차감, 포인트 차감, 카카오 알림) |
| OrderRepository | JpaRepository (회원별 페이징 조회) |
| KakaoMessageClient | 카카오톡 나에게 보내기 API 호출 |
| OrderRequest / OrderResponse | 요청/응답 DTO |

### wish - 찜 리스트

| 클래스 | 역할 |
|--------|------|
| Wish | 엔티티 (회원ID, 상품) |
| WishController | REST API (찜 추가/삭제/조회, 인증 필수) |
| WishService | 위시 비즈니스 로직 (추가, 중복 방지, 소유 검증) |
| WishRepository | JpaRepository (회원별/상품별 조회) |
| WishRequest / WishResponse | 요청/응답 DTO |

### member - 회원 관리

| 클래스 | 역할 |
|--------|------|
| Member | 엔티티 (이메일, 비밀번호, 포인트, 카카오 토큰) |
| MemberController | REST API (회원가입, 로그인) |
| AdminMemberController | Thymeleaf MVC (CRUD + 포인트 충전) |
| MemberService | 회원 비즈니스 로직 (가입, 로그인, CRUD, 포인트 충전) |
| MemberRepository | JpaRepository (이메일 기반 조회) |
| MemberRequest | 요청 DTO |

## 엔티티 관계도

```
Category ──1:N──→ Product ──1:N──→ Option ──1:N──→ Order
                     │
                     └──────────── Wish ←──N:1── Member
                                                    │
                                                    └──1:N──→ Order (memberId)
```

## API 엔드포인트

### 인증

| Method | URI | 설명 |
|--------|-----|------|
| POST | /api/members/register | 회원가입 |
| POST | /api/members/login | 로그인 |
| GET | /api/auth/kakao/login | 카카오 로그인 리다이렉트 |
| GET | /api/auth/kakao/callback | 카카오 콜백 |

### 카테고리

| Method | URI | 설명 |
|--------|-----|------|
| GET | /api/categories | 전체 조회 |
| POST | /api/categories | 생성 |
| PUT | /api/categories/{id} | 수정 |
| DELETE | /api/categories/{id} | 삭제 |

### 상품

| Method | URI | 설명 |
|--------|-----|------|
| GET | /api/products | 페이징 조회 |
| GET | /api/products/{id} | 단건 조회 |
| POST | /api/products | 생성 |
| PUT | /api/products/{id} | 수정 |
| DELETE | /api/products/{id} | 삭제 |

### 옵션

| Method | URI | 설명 |
|--------|-----|------|
| GET | /api/products/{productId}/options | 옵션 목록 |
| POST | /api/products/{productId}/options | 옵션 생성 |
| DELETE | /api/products/{productId}/options/{optionId} | 옵션 삭제 |

### 주문 (인증 필수)

| Method | URI | 설명 |
|--------|-----|------|
| GET | /api/orders | 내 주문 목록 (페이징) |
| POST | /api/orders | 주문 생성 |

### 찜 (인증 필수)

| Method | URI | 설명 |
|--------|-----|------|
| GET | /api/wishes | 찜 목록 (페이징) |
| POST | /api/wishes | 찜 추가 |
| DELETE | /api/wishes/{id} | 찜 제거 |

### 관리자 (Thymeleaf MVC)

| URI | 설명 |
|-----|------|
| /admin/products | 상품 관리 |
| /admin/members | 회원 관리 (포인트 충전) |

## 주문 처리 흐름

```
인증 확인 → 옵션 검증 → 재고 차감 → 포인트 차감 → 주문 저장 → 카카오톡 메시지 발송
```

- 카카오톡 메시지 발송은 best-effort (실패해도 주문은 유지)

## 검증 규칙

### 상품명

- 최대 15자
- 허용: 한글, 영문, 숫자, 공백, `()[]+-&/_`
- "카카오" 포함 불가 (admin 권한 시 허용)

### 옵션명

- 최대 50자
- 허용: 한글, 영문, 숫자, 공백, `()[]+-&/_`
- 동일 상품 내 중복 불가

### 회원

- 이메일: 유효한 형식 + 중복 불가
- 비밀번호: 필수

### 주문

- 옵션ID: 필수
- 수량: 최소 1개
- 메시지: 선택사항
