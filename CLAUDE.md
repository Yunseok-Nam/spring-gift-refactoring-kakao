This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.


## 테스트 코드 컨벤션

| 항목 | 규칙 | 예시 |
|------|------|------|
| 메서드명 | `methodUnderTest_state_expectedBehavior` | `decrease_insufficientStock_throwsException` |
| `@DisplayName` | 한국어로 행위를 서술 | `"재고보다 많은 수량을 차감하면 예외가 발생한다"` |
| 구조 | Arrange-Act-Assert 주석으로 구분 | `// Arrange`, `// Act`, `// Assert` |
| 인수 테스트 검증 | HTTP 응답만 검증 (상태 코드, body) | DB 상태 검증은 단위 테스트 영역 |
| 테스트 데이터 | Repository로 직접 시드 | `categoryRepository.save(new Category("교환권"))` |
| 독립성 | 테스트 간 상태 공유 없음 | `@BeforeEach`에서 전체 정리 |

## Git Commit

- 커밋 시 반드시 `/commit` 스킬을 사용한다 (AngularJS commit convention 기반).
- 커밋 메시지 포맷: `<type>(<scope>): <subject>`
- 허용 type: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
- 커밋 메시지(subject, body)는 **한국어**로 작성한다. (예: `feat(product): 재고 수량 검증 추가`)
- 커밋은 **목적 1개**로 구성한다. `git diff`를 보고 의도를 30초 안에 설명할 수 없으면 더 쪼갠다.
- **파일 변경 후 커밋 확인**: 코드를 수정·생성·삭제한 뒤에는 반드시 변경 사항을 확인하고, 커밋 단위에 부합하면 사용자에게 커밋 여부를 물어본다. 커밋하지 않고 다음 작업으로 넘어가지 않는다.

## Prompt 문서화

- 사용자가 프롬프트를 입력할 때마다 해당 작업이 완료되면 **자동으로** `/prompt` 스킬을 사용하여 프롬프트를 기록한다. 사용자가 별도로 요청하지 않아도 수행한다.
- 프롬프트 기록 없이 다음 작업으로 넘어가지 않는다.
- 모든 프롬프트는 `docs/PROMPT.md` 파일 하나에 시간순으로 누적 기록한다.
- 매번 새 문서를 생성하지 않고, 기존 파일에 append 한다.
- `docs/PROMPT.md`는 자동 커밋하지 않는다. 사용자가 별도로 요청할 때만 커밋한다.

## 프로젝트 구조 관리

- `docs/INITIAL_PROJECT_STRUCTURE.md` — 초기 프로젝트 구조. **절대 수정하지 않는다.**
- `docs/CURRENT_PROJECT_STRUCTURE.md` — 현재 프로젝트 구조. 리팩터링으로 패키지/클래스/파일이 추가·수정·삭제되면 자동으로 반영한다.
- 코드 변경 시 프로젝트 구조에 영향이 있으면(파일 생성/삭제/이동, 패키지 변경, 엔드포인트 변경 등) `docs/CURRENT_PROJECT_STRUCTURE.md`를 함께 업데이트한다.

## 참조 목록

```
docs/
├── CURRENT_PROJECT_STRUCTURE.md
├── INITIAL_PROJECT_STRUCTURE.md
├── PROMPT.md
├── TEST_PLAN.md
└── TEST_STRATEGY.md
```
