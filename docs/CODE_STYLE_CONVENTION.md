# Code Style Convention

> 프로젝트 코드 스타일 가이드
> 작성일: 2026-02-26

## 1. 목적과 원칙

### 목적

프로젝트 전반의 스타일 불일치를 정리하고 일관성을 확보한다.

### 원칙

| 원칙 | 설명                              |
|------|---------------------------------|
| **동작 불변** | 스타일 정리로 인해 런타임 동작은 절대 바뀌지 않는다   |
| **도구 우선** | 포매터와 린터를 우선시한다                  |
| **점진 적용** | 한 번에 전체를 바꾸지 않고, 변경하는 파일부터 적용한다 |

---

## 2. 현재 상태 분석

### 기술 스택

| 기술 | 버전 |
|------|------|
| Java | 21 |
| Spring Boot | 3.5.9 |
| Gradle (Kotlin DSL) | 8.14 |
| Ktlint | 14.0.1 (설정만 존재, Java에는 미적용) |

### 발견된 스타일 불일치

| 카테고리 | 불일치 내용 | 심각도 |
|----------|------------|--------|
| 생성자 주입 | `@Autowired` 명시 vs 생략 혼재 | 중 |
| 예외 메시지 언어 | 영어/한국어 혼재 (`Member.java` 내에서도 혼용) | 높 |
| 주석 스타일 | Javadoc(`/** */`) vs 블록 주석(`/* */`) 혼재 | 중 |
| `@RequestMapping` | `"/path"` vs `path = "/path"` 혼재 | 낮 |
| 변수 선언 | `var` vs 명시적 타입 vs `final` 혼재 | 중 |
| 필드 간격 | 엔티티 필드 사이 빈 줄 유무 혼재 | 낮 |
| null 처리 | `orElse(null)` + if문 vs `orElseThrow()` 혼재 | 중 |

---

## 3. 필수 도구 및 설정

### 3.1 EditorConfig

프로젝트 루트에 `.editorconfig` 파일을 생성하여 IDE 간 기본 포맷을 통일한다.

```ini
# .editorconfig
root = true

[*]
charset = utf-8
end_of_line = lf
insert_final_newline = true
trim_trailing_whitespace = true
indent_style = space
indent_size = 4

[*.{yml,yaml}]
indent_size = 2

[*.md]
trim_trailing_whitespace = false

[*.kts]
indent_size = 4
```

### 3.2 Spotless (Gradle Plugin)

Java 소스 자동 포매팅 도구. `build.gradle.kts`에 추가한다.

```kotlin
plugins {
    id("com.diffplug.spotless") version "7.0.2"
}

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat("1.25.2")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
        importOrder("java|javax", "jakarta", "org.springframework", "", "gift")
    }
}
```

> **왜 Spotless인가?**
> - Gradle 네이티브 통합 (별도 바이너리 불필요)
> - Google Java Format 기반으로 논쟁 없는 포맷 통일
> - `spotlessApply`로 자동 수정, `spotlessCheck`로 CI 검증

### 3.3 Checkstyle (선택)

Spotless가 커버하지 못하는 규칙(네이밍 컨벤션, Javadoc 유무 등)을 검증한다. 초기에는 Spotless만으로 시작하고, 필요 시 추가한다.

---

## 4. 자동화 방법

### 로컬

```bash
# 포맷 자동 적용
./gradlew spotlessApply

# 포맷 검증만 (CI와 동일)
./gradlew spotlessCheck
```

### Pre-commit Hook

Git 커밋 전에 포맷을 자동 검증한다. `.git/hooks/pre-commit` 또는 Gradle 태스크로 연결:

```kotlin
// build.gradle.kts
tasks.register("installGitHook", Copy::class) {
    from("scripts/pre-commit")
    into(".git/hooks")
    filePermissions { unix("rwxr-xr-x") }
}
```

`scripts/pre-commit`:
```bash
#!/bin/sh
./gradlew spotlessCheck --daemon
```

### CI

```yaml
# GitHub Actions 예시
- name: Check code style
  run: ./gradlew spotlessCheck
```

---

## 5. 핵심 스타일 규칙

포매터가 자동 처리하지 못하는 규칙만 명시한다. 포맷(들여쓰기, 줄바꿈, 임포트 순서 등)은 Spotless가 처리한다.

### 5.1 생성자 주입

`@Autowired`를 사용하지 않는다. Spring 4.3+ 단일 생성자 자동 주입에 의존한다.

```java
// Good
public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
}

// Bad
@Autowired
public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
}
```

### 5.2 예외 메시지 언어

예외 메시지는 **한국어**로 통일한다. API 응답에 노출될 수 있는 메시지이므로 사용자 친화적 언어를 사용한다.

```java
// Good
throw new IllegalArgumentException("충전 금액은 1 이상이어야 합니다.");

// Bad
throw new IllegalArgumentException("Amount must be greater than zero.");
```

### 5.3 변수 선언

- 지역 변수에 `var`를 사용한다 (타입이 우변에서 명확한 경우).
- `final`은 사용하지 않는다 (재할당이 필요한 경우에만 주의).

```java
// Good — 우변에서 타입이 명확
var member = memberRepository.findById(id).orElseThrow();
var token = jwtProvider.createToken(email);

// Good — 타입이 불명확하면 명시
String kakaoAuthUrl = UriComponentsBuilder.fromUriString(baseUrl)
    .build()
    .toUriString();
```

### 5.4 `@RequestMapping` 속성

값이 하나인 경우 `value` 생략 형태를 사용한다.

```java
// Good
@RequestMapping("/api/categories")
@GetMapping("/{id}")

// Bad
@RequestMapping(path = "/api/categories")
@GetMapping(path = "/{id}")
```

### 5.5 null 처리 패턴

| 상황 | 패턴 | 응답 |
|------|------|------|
| 단건 조회 (404) | `findById().orElse(null)` + `if (x == null) return notFound()` | 404 |
| 비즈니스 규칙 위반 | `orElseThrow(() -> new IllegalArgumentException(...))` | 400/500 |

> 이 규칙은 기존 컨트롤러 패턴을 반영한 것이다. 서비스 레이어 도입 시 재검토한다.

### 5.6 주석

- 파일/클래스 수준 주석: 사용하지 않는다. 클래스명과 패키지 구조로 의도를 표현한다.
- 인라인 주석: 코드만으로 의도가 불명확한 경우에만 사용한다.
- Javadoc: public API(라이브러리 배포용)에만 사용한다. 이 프로젝트에서는 불필요.

### 5.7 엔티티 필드 간격

엔티티 필드 사이에 빈 줄을 넣지 않는다. `@Id` 어노테이션 블록과 일반 필드 사이만 빈 줄을 허용한다.

```java
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String kakaoAccessToken;
    private int point;
```

---

## 6. 마이그레이션 전략

### 단계별 적용

| 단계 | 작업 | 시점 |
|------|------|------|
| **0단계** | `.editorconfig` 추가, Spotless 플러그인 설정 | 즉시 |
| **1단계** | `spotlessApply` 실행 → 포맷 일괄 정리 커밋 (단독 커밋) | 즉시 |
| **2단계** | `@Autowired` 제거, 예외 메시지 한국어 통일 | 해당 파일 수정 시 |
| **3단계** | Pre-commit hook 설치, CI 연동 | 0단계 이후 |

### 마이그레이션 커밋 규칙

- 스타일 변경은 `style` 타입으로 커밋한다: `style: Spotless 포맷 일괄 적용`
- 기능 변경과 스타일 변경을 같은 커밋에 섞지 않는다.
- 포맷 일괄 적용은 단독 커밋 1회로 처리한다 (git blame 오염 최소화).
