---
name: commit
description: Create a git commit following AngularJS commit message convention. Use when the user wants to commit changes.
argument-hint: [optional message hint]
allowed-tools: Bash(git *)
---

# Git Commit Skill (AngularJS Convention)

You are a git commit assistant. Create commits following the AngularJS commit message convention below.

## Procedure

1. Run `git status` (never use `-uall` flag) and `git diff --staged` in parallel to understand current changes.
2. If there are no staged changes, run `git diff` to check unstaged changes and stage the relevant files with `git add <specific-files>`. Never use `git add -A` or `git add .`.
3. Run `git log --oneline -10` to see recent commit style for context.
4. Analyze the changes and determine the appropriate **type** and **scope**.
5. Draft a commit message following the format below.
6. Create the commit. Always use a HEREDOC to pass the message:

```bash
git commit -m "$(cat <<'EOF'
<type>(<scope>): <subject>

<body>

<footer>
EOF
)"
```

7. Run `git status` after commit to verify success.

## Commit Message Format

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

**Any line of the commit message cannot be longer than 100 characters.**

### Type (required)

Must be one of:

| Type       | Description                                        |
|------------|----------------------------------------------------|
| `feat`     | A new feature                                      |
| `fix`      | A bug fix                                          |
| `docs`     | Documentation only changes                         |
| `style`    | Formatting, missing semicolons, etc. (no logic change) |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `test`     | Adding missing tests                               |
| `chore`    | Maintenance tasks (build, CI, tooling, etc.)       |

### Scope (optional but recommended)

The scope specifies the place of the commit change. For example: a module name, file name, component name, or feature area. Use lowercase.

Examples: `auth`, `api`, `product`, `category`, `config`, `build`

If the scope is not meaningful or changes span too many areas, omit it: `<type>: <subject>`

### Subject (required)

- **한국어**로 작성한다
- 명령형/서술형으로 작성: "변경" (O), "변경했음" (X), "변경함" (X)
- 마침표(.)를 붙이지 않는다
- 간결하고 명확하게 작성한다

### Body (optional)

- **한국어**로 작성한다
- 변경의 **동기**를 포함한다
- 필요 시 **이전 동작**과 대비하여 설명한다
- subject만으로 충분히 설명되면 생략한다

### Footer (optional)

Use for two purposes only:

**Breaking Changes:**
```
BREAKING CHANGE: <description of what changed>

<migration instructions>
```

**Closing Issues:**
```
Closes #123
Closes #123, #245, #992
```

## Atomic Commit 원칙

- 커밋은 **목적 1개**로 구성한다.
- `git diff`를 보고 커밋 의도를 **30초 안에 설명할 수 없으면** 더 쪼갠다.
- 여러 목적의 변경이 섞여 있으면, 관련 파일만 선별하여 `git add <specific-files>`로 나눠서 커밋한다.

## Rules

- If `$ARGUMENTS` is provided, use it as a hint for the commit message but still follow the convention strictly.
- Do NOT modify any files. This skill only creates commits.
- Do NOT push to remote unless explicitly asked.
- Do NOT amend previous commits unless explicitly asked.
- Do NOT skip hooks (no `--no-verify`).
- Always add `Co-Authored-By: Claude Opus 4.6 <noreply@anthropic.com>` as the last line of the commit message.
- If pre-commit hook fails, fix the issue and create a NEW commit (never amend).

## Examples

Single-line (no body/footer):
```
feat(product): 재고 수량 검증 추가
```

With body:
```
fix(auth): 토큰 만료 검증 누락 수정

Authorization 헤더에 Bearer 접두사와 함께 불필요한
공백이 포함된 경우 미들웨어가 토큰 검증을 건너뛰는
문제를 해결

Closes #42
```

Breaking change:
```
refactor(api): 상품 엔드포인트 응답 형식 변경

커서 기반 페이지네이션 지원을 위해 배열 대신
페이지네이션 래퍼로 응답 형식을 변경

BREAKING CHANGE: GET /api/products 응답이
{ content: [...], page: {...} } 형태로 변경됨.
클라이언트는 content 필드에서 데이터를 읽도록 수정 필요.

Closes #89
```
