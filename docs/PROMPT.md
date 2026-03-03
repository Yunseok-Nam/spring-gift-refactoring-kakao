# Prompt Documentation Log

> 프롬프트 누적 기록 문서
> 최초 생성: 2026-02-26
> 최종 수정: 2026-02-26
> 총 기록 수: 20건

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
