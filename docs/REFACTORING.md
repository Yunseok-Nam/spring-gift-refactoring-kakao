# 리팩토링 기준 및 개선 포인트

> 작동을 유지하면서도 책임과 계산, 판단을 올바른 위치로 이동해 누수와 중복을 줄인다.

---

## 1. 리팩토링 원칙

### 1.1 도메인 책임 되찾기

책임, 계산, 판단이 올바른 객체에 있는지 확인하고, 누수된 로직을 원래 위치로 이동한다.

| 기준 | 질문 |
|------|------|
| **책임 소재** | 이 로직은 이 클래스가 알아야 할 내용인가? |
| **계산 위치** | 도메인 규칙에 따른 계산이 Service가 아닌 Entity에서 수행되는가? |
| **판단 위치** | 분기/조건 판단이 호출부가 아닌 책임 객체 내부에서 이루어지는가? |

### 1.2 추상화를 통한 결합도 감소

구체 구현(Kakao 등)에 직접 의존하지 않고, 인터페이스를 통해 의존성을 주입한다.

| 기준 | 질문 |
|------|------|
| **확장 가능성** | 다른 구현체(Naver, Google 등)로 교체할 수 있는가? |
| **테스트 용이성** | 외부 서비스 없이 단위 테스트가 가능한가? |
| **변경 영향 범위** | 외부 API 변경 시 수정이 한 곳에 국한되는가? |

### 1.3 null 전파 대신 예외로 흐름 명확화

`.orElse(null)` + null 반환 패턴은 호출부마다 null 체크 분기를 강제한다. 예외를 던지면 흐름이 단순해진다.

| 기준 | 질문 |
|------|------|
| **실패 의미** | null 반환이 "없음"인지 "오류"인지 호출부가 구분할 수 있는가? |
| **분기 전파** | null 체크가 여러 계층에 걸쳐 반복되고 있지 않은가? |

### 1.4 중복 제거 / 호출부 단순화 / 분기 감소

동일한 코드 패턴이 여러 곳에 반복되면 하나로 모으고, 호출부는 핵심 로직만 남긴다.

| 기준 | 질문 |
|------|------|
| **중복** | 같은 패턴이 3곳 이상 반복되는가? |
| **호출부 복잡도** | 호출부가 인프라 관심사(헤더 파싱, null 체크 등)를 직접 처리하는가? |
| **분기 수** | if/else가 프레임워크 기능이나 예외 처리로 대체 가능한가? |

---

## 2. 패키지별 현황 분석 및 개선 포인트

### 2.1 auth 패키지

#### 문제 1: 컨트롤러 인증 보일러플레이트 중복

`OrderController`(2곳), `WishController`(3곳)에서 동일한 4줄 패턴이 반복된다.

```java
// 현재 — 5개 메서드에 반복
var member = authenticationResolver.extractMember(authorization);
if (member == null) {
    return ResponseEntity.status(401).build();
}
```

| 원칙 | 위반 |
|------|------|
| 중복 제거 | 동일 패턴 5회 반복 |
| 호출부 단순화 | 컨트롤러가 토큰 파싱·null 체크를 직접 수행 |
| 분기 감소 | 매 메서드마다 null 분기 존재 |

**개선**: `@Login` 어노테이션 + `HandlerMethodArgumentResolver`로 인증 로직을 프레임워크 수준에서 처리. 컨트롤러는 `@Login Member member` 파라미터만 선언.

#### 문제 2: KakaoAuthService가 Member 직접 생성

```java
// KakaoAuthService.processCallback() — auth가 member 도메인 객체를 직접 생성
var member = memberRepository.findByEmail(email)
    .orElseGet(() -> new Member(email));
member.updateKakaoAccessToken(kakaoToken.accessToken());
memberRepository.save(member);
```

| 원칙 | 위반 |
|------|------|
| 책임 소재 | Member 생성은 member 패키지의 책임 |
| 결합도 | auth가 MemberRepository에 직접 의존 |

**개선**: `MemberService.findOrCreateByEmail()` 메서드로 위임. auth 패키지는 MemberService만 의존.

#### 문제 3: MemberService ↔ auth 양방향 의존

```
auth.KakaoAuthService  → member.MemberRepository  (auth → member)
member.MemberService   → auth.JwtProvider          (member → auth)
member.MemberService   → auth.TokenResponse        (member → auth)
```

| 원칙 | 위반 |
|------|------|
| 결합도 | 패키지 간 순환 의존 |
| 책임 소재 | 토큰 생성은 auth의 책임이나 MemberService가 수행 |

**개선**: `AuthService`(auth 패키지) 도입. register/login 시 MemberService가 Member를 반환하고, AuthService가 토큰 생성을 담당. MemberService에서 `JwtProvider` 의존 완전 제거.

```
[Before]  auth ←→ member (양방향)
[After]   auth  → member (단방향)
```

#### 문제 4: 카카오 종속 구현체에 대한 추상화 부재

`KakaoLoginClient`, `KakaoAuthService`가 구체 클래스로 직접 사용된다. 다른 OAuth 제공자(Naver, Google) 추가 시 기존 코드 수정이 불가피하다.

| 원칙 | 위반 |
|------|------|
| 확장 가능성 | 새 제공자 추가 시 기존 코드 변경 필요 |
| 테스트 용이성 | 외부 API 없이 테스트 어려움 |

**개선**: `OAuthClient` 인터페이스 도입. `KakaoOAuthClient`가 구현. 서비스 계층은 인터페이스에만 의존.

---

### 2.2 member 패키지

#### 문제: JwtProvider 의존 (양방향 의존의 원인)

`MemberService.register()`와 `login()`이 `JwtProvider`를 직접 호출하여 `TokenResponse`를 반환한다.

**개선**: 위 auth 패키지 문제 3과 함께 해결. register/login은 `Member`를 반환하고, 토큰 생성은 `AuthService`에서 처리.

---

### 2.3 order 패키지

#### 문제 1: KakaoMessageClient 추상화 부재

`OrderService`가 `KakaoMessageClient`에 직접 의존한다. 메시지 전송 수단 변경 시 OrderService 수정이 필요하다.

| 원칙 | 위반 |
|------|------|
| 확장 가능성 | 다른 메시지 서비스(SMS, FCM 등)로 교체 불가 |
| 테스트 용이성 | 카카오 API 없이 테스트 어려움 |

**개선**: `MessageClient` 인터페이스 도입. `KakaoMessageClient`가 구현.

#### 문제 2: 예외 무시 (Silent Exception Catch)

```java
// OrderService.sendKakaoMessageIfPossible()
try {
    kakaoMessageClient.sendToMe(...);
} catch (Exception ignored) {  // 모든 예외를 무시
}
```

**개선**: 구체 예외만 catch하고 로깅 추가. 실패 원인 추적 가능하도록 개선.

---

### 2.4 product / option / wish / category 패키지

#### 문제 1: `.orElse(null)` + null 반환 패턴

5개 이상의 Service 클래스에서 동일한 패턴이 반복된다.

```java
// 현재 — Service 계층
var product = productRepository.findById(id).orElse(null);
if (product == null) { return null; }

// 현재 — Controller 계층
var saved = productService.create(request);
if (saved == null) { return ResponseEntity.notFound().build(); }
```

| 원칙 | 위반 |
|------|------|
| 분기 감소 | Service + Controller 양쪽에서 null 분기 |
| 호출부 단순화 | 컨트롤러가 Service의 null 결과를 해석해야 함 |
| 실패 의미 | null이 "not found"인지 "validation failure"인지 구분 불가 |

**개선**: `.orElseThrow()` + 도메인 예외(`NotFoundException` 등)로 교체. `@RestControllerAdvice`에서 예외를 HTTP 상태 코드로 매핑.

#### 문제 2: ProductService의 Admin/API 메서드 중복

```java
// REST API용
public Product create(ProductRequest request) { ... }
public Product update(Long id, ProductRequest request) { ... }

// Admin 폼용 — 거의 동일한 로직
public Product createFromAdmin(String name, int price, String imageUrl, Long categoryId) { ... }
public Product updateFromAdmin(Long id, String name, int price, String imageUrl, Long categoryId) { ... }
```

| 원칙 | 위반 |
|------|------|
| 중복 제거 | 생성/수정 로직이 2벌씩 존재 |

**개선**: 통합 메서드로 합치거나, Admin 컨트롤러에서 `ProductRequest`를 직접 구성하여 동일 메서드 호출.

#### 문제 3: ProductService.findAllCategories() 책임 누수

`ProductService`가 `CategoryRepository`를 통해 카테고리를 조회한다. 이는 category 패키지의 책임이다.

**개선**: `CategoryService.findAll()`로 이동. `AdminProductController`가 `CategoryService`를 직접 사용.

---

## 3. 리팩토링 로드맵

### Phase 1: auth 패키지 (핵심 구조 개선)

| 순서 | 작업 | 개선 효과 |
|------|------|----------|
| 1-1 | `@Login` + `LoginMemberArgumentResolver` + `UnauthorizedException` | 중복 5곳 제거, 분기 5개 감소 |
| 1-2 | KakaoAuthService → MemberService 위임 | Member 생성 책임 이동 |
| 1-3 | AuthService 도입, MemberService에서 JwtProvider 제거 | 양방향 → 단방향 의존 |
| 1-4 | OAuthClient 인터페이스 추상화 | 확장 가능성, 테스트 용이성 |

### Phase 2: order 패키지

| 순서 | 작업 | 개선 효과 |
|------|------|----------|
| 2-1 | MessageClient 인터페이스 추상화 | 확장 가능성, 테스트 용이성 |
| 2-2 | silent catch → 구체 예외 + 로깅 | 디버깅 용이성 |

### Phase 3: 전체 null 처리 개선

| 순서 | 작업 | 개선 효과 |
|------|------|----------|
| 3-1 | 도메인 예외 클래스 생성 | 실패 의미 명확화 |
| 3-2 | Service 계층 `.orElseThrow()` 전환 | null 전파 제거 |
| 3-3 | `@RestControllerAdvice` 확장 | 컨트롤러 분기 감소 |

### Phase 4: 중복 제거 및 책임 정리

| 순서 | 작업 | 개선 효과 |
|------|------|----------|
| 4-1 | ProductService create/update 통합 | 중복 제거 |
| 4-2 | findAllCategories() → CategoryService 이동 | 책임 정리 |
