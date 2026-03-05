# Prompt Documentation Log

> 프롬프트 누적 기록 문서
> 최초 생성: 2026-02-26
> 최종 수정: 2026-03-05
> 총 기록 수: 42건

---

## 목차

| # | 제목 | 기록 일시 |
|---|------|-----------|
| 1 | 커밋 컨벤션 규칙 추가 | 2026-02-26 |
| 2 | 커밋 메시지 한국어 전환 | 2026-02-26 |
| 3 | Cucumber BDD 인수 테스트 전략 설계 | 2026-02-26 |
| 4 | 프롬프트 스킬 Claude Code 환경 적용 | 2026-02-26 15:14 |
| 5 | CLAUDE.md에 프롬프트 문서화 규칙 추가 | 2026-02-26 15:14 |
| 6 | 프로젝트 구조 분석 및 문서화 | 2026-02-26 15:14 |
| 7 | 커밋 및 프롬프트 기록 요청 | 2026-02-26 15:14 |
| 8 | 프롬프트 기록 포맷 간소화 | 2026-02-26 15:27 |
| 9 | 프로젝트 구조 문서 버전 관리 체계 도입 | 2026-02-26 15:27 |
| 10 | CLAUDE.md에 프로젝트 구조 관리 규칙 추가 | 2026-02-26 15:27 |
| 11 | CLAUDE.md 참조 목록 최신화 | 2026-02-26 15:27 |
| 12 | 커밋 및 프롬프트 자동 기록 요청 | 2026-02-26 15:27 |
| 13 | 프롬프트 자동 기록 규칙 강화 | 2026-02-26 15:32 |
| 14 | PROMPT.md 자동 커밋 제외 규칙 추가 | 2026-02-26 15:32 |
| 15 | 프로젝트 전체 소스 코드 파악 | 2026-02-26 |
| 16 | Cucumber BDD 인수 테스트 코드 작성 | 2026-02-26 16:10 |
| 17 | 코드 스타일 컨벤션 문서 작성 | 2026-02-26 |
| 18 | 코드 스타일 리팩토링 | 2026-02-26 |
| 19 | 미사용 코드 제거 | 2026-02-26 |
| 20 | 서비스 계층 추출 | 2026-02-26 |
| 21 | 도메인 책임 리팩토링 기준 문서화 | 2026-03-04 |
| 22 | 카카오 콜백 처리 객체지향 개선 | 2026-03-04 |
| 23 | auth 패키지 카카오 종속성 제거 추상화 리팩토링 | 2026-03-04 |
| 24 | KakaoLoginClient/Properties OAuthClient 추상화 | 2026-03-04 |
| 25 | KakaoAuthService 트랜잭션 경계 설정 | 2026-03-04 |
| 26 | Category update 절차지향 코드 개선 | 2026-03-04 |
| 27 | CategoryService 트랜잭션 도입 | 2026-03-04 |
| 28 | MemberService 객체지향 리팩토링 | 2026-03-04 |
| 29 | MemberService 트랜잭션 경계 설정 | 2026-03-04 |
| 30 | Category.update 원시 값 수신으로 변경 | 2026-03-04 |
| 31 | AdminMemberController 파라미터 통일 및 불필요 메서드 제거 | 2026-03-04 |
| 32 | OptionService 객체지향 리팩토링 | 2026-03-04 |
| 33 | 옵션 삭제 가능 여부 판단을 Product 도메인에 위임 | 2026-03-05 |
| 34 | OrderService 객체지향 리팩토링 | 2026-03-05 |
| 35 | 주문 완료 시 위시리스트 자동 정리 구현 | 2026-03-05 |
| 36 | ProductService 객체지향 리팩토링 | 2026-03-05 |
| 37 | ProductService 트랜잭션 경계 설정 | 2026-03-05 |
| 38 | OrderService 트랜잭션 경계 설정 | 2026-03-05 |
| 39 | 주문 메시지 발송을 이벤트 기반으로 분리 | 2026-03-05 |
| 40 | WishService 객체지향 리팩토링 | 2026-03-05 |
| 41 | 리팩토링 ADR 작성 | 2026-03-05 |
| 42 | 전체 코드 일관성 점검 및 순차 리팩토링 | 2026-03-05 |

---

## [#1] 커밋 컨벤션 규칙 추가
> 2026-02-26 (KST)

```
현재 .cluade 폴더에 commit skills 가 있는데 일부 수정이 필요해.
- 커밋은 목적 1개로 구성한다.
- **`git diff`**를 보고 커밋 의도를 30초 안에 설명할 수 없으면 더 쪼갠다.

그리고 claude.md 에서도 수정이 필요해. 우리는 앞으로 파일 내용이 바뀌면 항상 바뀐 부분을 체크해서 commit 의 단위에 맞으면 commit 을 하도록 물어보게끔 해야하거든. 그래서 실수로 커밋을 하지않고 그 다음 작업을 하지 않게끔 바꿀거야. 이해했어?
```

---

## [#2] 커밋 메시지 한국어 전환
> 2026-02-26 (KST)

```
아 그리고 커밋 내용 한국어로 작성하게 변경해줘
```

---

## [#3] Cucumber BDD 인수 테스트 전략 설계
> 2026-02-26 (KST)

```
docs/TEST_PLAN.md 를 참고해서 현재 프로젝트의 인수 테스트를 설계해야해. 요구사항 1,2,3 중에서 요구사항 1까지만 반영할거야. 해당 내용을 기반으로 인수 테스트 코드 설계전략 관련 내용을 docs/TEST_STRATEGY.md 에 작성해줘

(후속 수정) 음 잠깐만 TEST_STRATEGY.md 에서 시나리오가 너무 많아보여. 정말 사용자가 서비스를 이용하는데 있어서 크리티컬하게 문제가 발생할 수 있는 시나리오만 남기고 나머지는 제거해줘. 유지보수 측면에서 불리해보여.
```

---

## [#4] 프롬프트 스킬 Claude Code 환경 적용
> 2026-02-26 15:14 (KST)

```
prompt/SKILL.md를 수정하려고해. 나는 docs/PROMPT.md파일안에 내가 프롬프팅 하는 내용들이 누정해서 순서대로 작성되면 좋겠어.
```

---

## [#5] CLAUDE.md에 프롬프트 문서화 규칙 추가
> 2026-02-26 15:14 (KST)

```
Claud.md에 promt스킬을 사용한다라는 내용을 추가해야해. ex) Claud.md에서 Git Commit처럼
```

---

## [#6] 프로젝트 구조 분석 및 문서화
> 2026-02-26 15:14 (KST)

```
프로젝트 구조를 분석해서 docs에 프로젝트 구조를 문서화해.
```

---

## [#7] 커밋 및 프롬프트 기록 요청
> 2026-02-26 15:14 (KST)

```
ㅇㅇ 커밋하고 프롬프트기록해줘.
```

---

## [#8] 프롬프트 기록 포맷 간소화
> 2026-02-26 15:27 (KST)

```
promt스킬을 수정할거야. 제목과 원본 프롬프트만 남고 나머지는(개요, 역할설정, 입력변수, 제약조건, 출력형식, Few-shot예시)는 없애. 스킬과 Prompt.md 둘 다 수정해.
```

---

## [#9] 프로젝트 구조 문서 버전 관리 체계 도입
> 2026-02-26 15:27 (KST)

```
야 PROJECT_STRUCTURE.md 버전 관리할거야. INITIAL_PROJECT_STRUCTURE.md 는 제일 처음 프로젝트 구조(지금의 PROJECT_STRUCTURE.md) 가 되어야함. 이건 파일 수정 X. 그리고 CURRENT_PROJECT_STRUCTURE.md 를 만들어서 우리가 프로젝트를 리팩터링 시에 구조가 변경되면 해당 수정사항을 반영하는 용도로 사용할거야. 현재는 INITIAL 과 CURRENT 가 같겠지? 자 한번 해볼까?
```

---

## [#10] CLAUDE.md에 프로젝트 구조 관리 규칙 추가
> 2026-02-26 15:27 (KST)

```
아니? 아직. claude.md 에 리팩토링 시, current 에 변경 사항 자동으로 반영하도록 넣어줘. 알겠지?
```

---

## [#11] CLAUDE.md 참조 목록 최신화
> 2026-02-26 15:27 (KST)

```
아니? claude.md 에 하단의 참조목록에 폴더 구조가 최신화가 안되어있어 반영해라
```

---

## [#12] 커밋 및 프롬프트 자동 기록 요청
> 2026-02-26 15:27 (KST)

```
프롬프트 기록해줘 야 그리고 skills/prompt 이거 계속 자동으로 해달라니깐 왜 안해주냐? 장난해?
```

---

## [#13] 프롬프트 자동 기록 규칙 강화
> 2026-02-26 15:32 (KST)

```
아니 기다려. 지금 promppt skills 가 왜 자동으로 안될까? claude.md 에다가 자동으로 되도록 해라.
```

---

## [#14] PROMPT.md 자동 커밋 제외 규칙 추가
> 2026-02-26 15:32 (KST)

```
PROMPT.md 는 자동으로 커밋하지 못하게 해줘. 나중에 우리가 한번에 커밋할거야. 이건 제약사항에 넣어주고, 커밋해줘
```

---

## [#15] 프로젝트 전체 소스 코드 파악
> 2026-02-26 (KST)

```
Read all source files in the project to understand the codebase. I need to understand:
1. All entity classes (Category, Product, Option, Order, Wish, Member)
2. All controller classes (CategoryController, ProductController, OptionController, OrderController, WishController, MemberController, AdminMemberController, AdminProductController, KakaoAuthController)
3. All repository classes
4. All request/response DTOs
5. All validator classes (ProductNameValidator, OptionNameValidator)
6. Auth classes (JwtProvider, AuthenticationResolver, KakaoLoginClient, KakaoLoginProperties, TokenResponse)
7. Application.java
8. Flyway migration files (V1, V2)
9. application.properties
10. build.gradle.kts

The project root is at /Users/charlie.woo/Desktop/onboarding/spring-gift-refactoring-kakao

Return the FULL content of each file (no summaries). This is critical for writing tests.
```

---

## [#16] Cucumber BDD 인수 테스트 코드 작성
> 2026-02-26 16:10 (KST)

```
자 docs/TEST_STRATEGY.md 에 따라서 테스트 코드 작성해줘. 가보자!
```

---

## [#17] 코드 스타일 컨벤션 문서 작성
> 2026-02-26 (KST)

```
우리 프로젝트 기준으로 docs/CODE_STYLE_CONVENTION.md를 만들어줘.

목표:
- 프로젝트 전반의 스타일 불일치를 정리하고 일관성을 만든다.
- 스타일 정리로 인해 런타임 동작은 절대 바뀌면 안 된다.
- 사람 규칙보다 도구(포매터, 린터, IDE 인스펙션, pre-commit, CI)로 강제하는 방식을 우선한다.

해야 할 일:
1. PROJECT_STRUCTURE.md파일을 기반으로 프로젝트 구조를 파악해서 현재 사용 언어/프레임워크/도구를 파악
2. 스타일 불일치가 발생할 수 있는 지점 정리
3. 가장 적합한 스타일 + 자동화 도구 조합 제안
4. 완성된 CODE_STYLE_CONVENTION.md를 마크다운으로 작성

문서에는 반드시 포함:
- 목적과 원칙 (동작 불변, 도구 우선)
- 필수 도구 및 설정
- 자동화 방법 (로컬/CI)
- 핵심 스타일 규칙 (짧고 강제 가능한 것만)
- 마이그레이션 전략 (점진 적용)
```

---

## [#18] 코드 스타일 리팩토링
> 2026-02-26 (KST)

```
- 코드 스타일을 정리할거야. 아래 기준 + CODE_STYLE_CONVENTION.md를 참고해서 코드스타일을 일관되게 리팩토링해.
    - 프로젝트 전반의 스타일 불일치를 찾아 일관되게 정리한다.
    - 스타일 정리로 인해 작동이 바뀌지 않아야 한다.
```

---

## [#19] 미사용 코드 제거
> 2026-02-26 (KST)

```
자 이제 불필요한 코드 제거 관련 작업을 진행해야해. 아래는 명세야

- IDE 또는 정적 분석 도구가 "미사용"으로 표시하는 항목을 제거할 수 있다.
- 단, 삭제 전에 반드시 근거를 확인한다.
    - 주변 주석 또는 TODO에 의도가 있는가
    - **`git blame`**으로 누가 왜 추가했는가
    - 이후 단계(작동 변경)와 충돌하지 않는가

코드를 변경하고나서 왜 그 코드를 변경했는지에 대한 이유를 문서화 시켜줘야해. docs 에 저장해줘.
```

---

## [#20] 서비스 계층 추출
> 2026-02-26 (KST)

```
자 그다음 작업해주고 작업한 내용 문서 정리해줘
- 서비스 계층 추출(구조 변경, 작동 변경 없음)
    - Controller의 비즈니스 로직을 Service로 이동한다.
    - Controller는 요청 검증과 위임만 담당하도록 얇게 만든다.
    - 이 단계에서는 신규 기능을 추가하지 않는다.
```

---

## [#21] 도메인 책임 리팩토링 기준 문서화
> 2026-03-04 (KST)

```
도메인 책임 되찾기
작동을 유지하면서도 책임과 계산, 판단을 올바른 위치로 이동해 누수와 중복을 줄인다.
변경 전후가 분명한 개선을 최소 2개 이상 수행한다.
예:
중복 제거
호출부 단순화
테스트 가독성 개선
분기, 조건, 반복 감소 즉, 객체를 리팩토링할거야. 일단 auth패키지부터. 어떤 부분을 리팩토링하면 좋을까

(추가 요청) 일단, 어떤 기준으로 객체를 리팩토링할건지 문서화하는것이 좋을 것 같아. KakaoAuthService or KakaoLoginClient처럼 카카오에 종속된 서비스 혹은 객체를 만들기 보다, 추상화를 해서 의존성을 주입하는 것이 더 좋을 것 같아. 확장에도 유리하고. 이 외에도 추가적으로 어떤 것을 기준으로 혹은 중점으로 리팩토링을 해야하는지 문서화를해. docs/refactoring.md에 작성해.
```

---

## [#22] 카카오 콜백 처리 객체지향 개선
> 2026-03-04 14:55 (KST)

```
processCallback에서 kakaoUser.email();나 jwtProvider.createToken(member.getEmail()); 등등 이런 절차지향적인 것들을 좀 더 객체지향적으로 개선하면 좋겠어.
```

---

## [#23] auth 패키지 카카오 종속성 제거 추상화 리팩토링
> 2026-03-04 15:10 (KST)

```
auth 패키지 카카오 종속성 제거 — 추상화 리팩토링

현재 auth 패키지의 컨트롤러·서비스가 카카오에 직접 결합되어 있다.
OAuth 프로바이더 교체/추가 시 컨트롤러까지 수정해야 하는 구조이므로,
인터페이스를 도입하여 전략 패턴으로 추상화한다.

변경 사항:
1. AuthService 인터페이스 생성
2. KakaoAuthService → AuthService 구현체로 변경
3. KakaoAuthController → AuthController로 이름 변경 (/api/auth/*)
4. application.properties 리다이렉트 URI 변경
5. CURRENT_PROJECT_STRUCTURE.md 업데이트
```

---

## [#24] KakaoLoginClient/Properties OAuthClient 추상화
> 2026-03-04 15:44 (KST)

```
KakaoAuthService도 AuthService가 되도록 KakaoLoginClient, KakaoLoginProperties를 추상화하도록해.
```

---

## [#25] KakaoAuthService 트랜잭션 경계 설정
> 2026-03-04 16:39 (KST)

```
KakaoAuthService에서 트랜잭션 경계 세우기
여러 저장 작업이 하나의 논리 작업이라면, 중간 실패에서 부분 반영이 발생하지 않도록 경계를 설정한다. 이 부분에 대해서 수정해봐 할 부분 있으면.
```

---

## [#26] Category update 절차지향 코드 개선
> 2026-03-04 16:52 (KST)

```
CategoryService에서 update가 너무 절차지향적이야. 모든 값을 꺼내서 사용하니깐. 그리고 Category내부 구현을 보면 update는 전체업데이트만을 지원하고 일부 업데이트를 하려해도 전체 값을 넣어야해서 확장성이 떨어져. 개선해.
```

---

## [#27] CategoryService 트랜잭션 도입
> 2026-03-04 17:11 (KST)

```
CategoryService도 트랜잭션 도입할거야. 어떻게 하면 좋을까
```

---

## [#28] MemberService 객체지향 리팩토링
> 2026-03-04 17:40 (KST)

```
MemberService를 객체지향적으로 리팩토링하려고해. 어떤 부분을 수정하면 좋을까?

(적용 선택) 2,3,5번을 적용
- register와 create 중복 로직 통합
- Member.update(MemberRequest) → 원시 값으로 변경하여 도메인-DTO 의존성 제거
- AdminMemberController.create 파라미터를 MemberRequest로 통일
```

---

## [#29] MemberService 트랜잭션 경계 설정
> 2026-03-04 18:01 (KST)

```
MemberService에 트랜잭션 설정할거야. 어떻게 하면 좋을까
```

---

## [#30] Category.update 원시 값 수신으로 변경
> 2026-03-04 18:09 (KST)

```
Category의 update를 값을 외부에서 받아오는걸로 해야해. 왜냐하면 지금 방식에선
CategoryRequest에 따라서만 값 업데이트가 가능한데, 각 값에 대해서 커스텀하게
update할 수 없어. 추가적으로, name, color, imageUrl, description을 모두 넣지
않고 각각을 부분적으로 update할 수 있도록 하는 것이 확장가능한 설계인것같아.

(후속) 그냥 Member의 update처럼 바꿔.
```

---

## [#31] AdminMemberController 파라미터 통일 및 불필요 메서드 제거
> 2026-03-04 18:19 (KST)

```
AdminMemberController에서 리팩토링 할 부분 없어?

(후속) populateNewFormError 이코드는 뭐야. 한군데서만 쓰이는데 굳이 함수분리를
할 필요가 있나?
```

---

## [#32] OptionService 객체지향 리팩토링
> 2026-03-04 18:23 (KST)

```
OptionService도 객체지향적으로 리팩토링해.
```

---

## [#33] 옵션 삭제 가능 여부 판단을 Product 도메인에 위임
> 2026-03-05 12:18 (KST)

```
옵션이 1개인 상품은 옵션을 삭제할 수 없다는 핵심 비즈니스 규칙을
Service에서 직접 확인하지 않고 도메인 객체가 스스로 판단하도록 책임 부여.
```

---

## [#34] OrderService 객체지향 리팩토링
> 2026-03-05 12:43 (KST)

```
OrderService에서 객체지향적으로 리팩토링 할 부분있나 찾아봐.

(적용 선택) 1,3,4,5번을 우선 적용
- null 반환 대신 예외 던지기
- 가격 계산을 Option.calculateTotalPrice()로 도메인에 위임
- 카카오 토큰 확인을 Member.hasKakaoAccount()로 도메인에 위임
- MessageClient 인터페이스 도입으로 KakaoMessageClient 추상화
```

---

## [#35] 주문 완료 시 위시리스트 자동 정리 구현
> 2026-03-05 (KST)

```
OrderService에서 create메서드에 로직이 빠진 것 같아. 'cleanUp wish'이 부분.
주문한 상품이 위시리스트에 있으면 자동으로 제거하는 로직 구현.
```

---

## [#36] ProductService 객체지향 리팩토링
> 2026-03-05 (KST)

```
ProductService에서 객체지향적으로 리팩토링 할 부분있나 봐.

(적용 선택) 1,2,4,5번 적용
- null 반환 대신 예외(NoSuchElementException) 던지기
- create/createFromAdmin, update/updateFromAdmin 중복 메서드 통합
- findAllCategories() 제거, CategoryService에 책임 위임
- var를 명시적 타입으로 변경
```

---

## [#37] ProductService 트랜잭션 경계 설정
> 2026-03-05 (KST)

```
ProductService에 트랜잭션 어떻게 적용하면 좋을까?

update 메서드에 @Transactional 적용.
읽기-수정-저장 패턴에서 중간 실패 시 부분 반영 방지.
```

---

## [#38] OrderService 트랜잭션 경계 설정
> 2026-03-05 (KST)

```
OrderService에 트랜잭션 적용해.

create 메서드에 @Transactional 적용.
재고 차감, 포인트 차감, 주문 저장, 위시 정리가 하나의 원자적 작업으로 처리.
```

---

## [#39] 주문 메시지 발송을 이벤트 기반으로 분리
> 2026-03-05 (KST)

```
OrderService의 create메서드에서 카카오톡 메시지 발송이 트랜잭션 안에 있어
카카오 API 지연 시 DB 커넥션을 불필요하게 점유하는 문제.
주문 핵심 트랜잭션과 메시지 발송을 느슨하게 결합하는 구조로 변경.

OrderCompletedEvent + @TransactionalEventListener(phase = AFTER_COMMIT)
도입으로 트랜잭션 커밋 후 메시지 발송.
```

---

## [#40] WishService 객체지향 리팩토링
> 2026-03-05 (KST)

```
WishService 객체지향적으로 리팩토링할 부분 있나 봐.

(적용 선택) 1,2,3,4번 모두 적용
- null 반환 대신 예외(NoSuchElementException) 던지기
- remove의 WishDeleteResult enum 반환을 예외+void로 변경
- 소유권 확인을 Wish.isOwnedBy()로 도메인에 위임
- var를 명시적 타입으로 변경
```

---

## [#41] 리팩토링 ADR 작성
> 2026-03-05 (KST)

```
ADR은 선택이 아니라 필요 시 남긴다.
다음 조건 중 하나라도 해당하면 ADR을 작성한다.
- 선택지가 2개 이상이고 트레이드오프가 있었던 경우
- 팀이 반복해서 따라야 할 규칙이나 경계를 정한 경우
- 테스트 전략과 검증 방식이 결정의 핵심이었던 경우

ADR 3건 작성:
1. 외부 API 호출을 트랜잭션에서 분리
2. 서비스 반환값 규칙 — null/enum 대신 예외
3. 외부 시스템 연동 인터페이스 추상화

(후속) ADR을 하나의 docs/ADR.md 파일로 통합, 목차 포함.
```

---

## [#42] 전체 코드 일관성 점검 및 순차 리팩토링
> 2026-03-05 (KST)

```
파일 전체 보면서 일관성이 안맞는 부분 or 리팩토링이 필요한 부분 or
트랜잭션 혹은 성능적으로 개선할 수 있는 부분을 찾아봐.
각 항목을 순차적으로 작업(리팩토링 -> 커밋)하자.

적용 항목:
1. var → 명시적 타입 (auth, category, member, option 패키지)
2. CategoryService.update() null 반환 → orElseThrow
3. CategoryController, AuthController에 @ExceptionHandler 추가
4. @Transactional 메서드에서 불필요한 save() 제거 (dirty checking 활용)
```

---
