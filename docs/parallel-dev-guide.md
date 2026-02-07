# 병렬 개발 가이드 (Parallel Development Guide)

> Backend와 Frontend를 동시에 개발하기 위한 워크플로우 가이드입니다.

## 📁 문서 목록

| 문서 | 설명 | 주 사용자 |
|------|------|----------|
| [api-spec.md](./api-spec.md) | API 명세서 (엔드포인트, 요청/응답) | BE & FE |
| [types.md](./types.md) | TypeScript 타입 정의 | FE |
| [mock-data.md](./mock-data.md) | Mock 데이터 및 Mock API | FE |
| [auth-contract.md](./auth-contract.md) | 인증/인가 계약 (JWT) | BE & FE |
| [requirements.md](./requirements.md) | 요구사항 정의서 | BE & FE |

---

## 🔄 병렬 개발 워크플로우

```
Week 1: API 계약 확정 (완료)
├── api-spec.md 작성 ✅
├── types.md 작성 ✅
├── auth-contract.md 작성 ✅
└── mock-data.md 작성 ✅

Week 2-3: 병렬 개발
┌─────────────────────────┬─────────────────────────┐
│   Backend (Phase 2-5)   │  Frontend (Phase 7-8)   │
├─────────────────────────┼─────────────────────────┤
│ 재고 관리 (동시성)      │ 프로젝트 설정           │
│ 주문 처리 (중복 방지)   │ 공용 컴포넌트           │
│ 회원 관리 (JWT)         │ Mock API로 페이지 구현  │
│ API 레이어              │                         │
└─────────────────────────┴─────────────────────────┘

Week 4: 통합
├── Mock → 실제 API 전환
├── 통합 테스트
└── 버그 수정
```

---

## 🚀 시작하기

### Backend 개발자

```bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=local'
```

**개발 순서:**
1. `plan.md`의 Phase 2 (재고 관리) 진행
2. API 구현 시 `docs/api-spec.md` 준수
3. 에러 응답은 표준 형식 사용

### Frontend 개발자

```bash
cd frontend
npm run dev
```

**개발 순서:**
1. `.env.local`에 `NEXT_PUBLIC_USE_MOCK=true` 설정
2. `docs/types.md`를 `src/types/`에 복사
3. `docs/mock-data.md`를 참고하여 Mock API 구현
4. UI 구현 후 실제 API로 전환

---

## ⚠️ 규칙 및 주의사항

### 1. API 변경 시

```
1. docs/api-spec.md 먼저 수정
2. docs/types.md 동기화
3. Slack/Discord로 변경 공유 (또는 커밋 메시지에 명시)
4. Breaking Change는 최소 1일 전 공지
```

### 2. 에러 코드 추가 시

```
1. docs/api-spec.md의 "부록: 공통 에러 응답" 섹션에 추가
2. docs/types.md의 ErrorCode 상수에 추가
3. Frontend는 해당 에러 핸들링 추가
```

### 3. 커밋 메시지 규칙

```
feat(be): 재고 차감 동시성 처리 구현
feat(fe): 발매 상세 페이지 구현
fix(be): 중복 주문 방지 로직 수정
docs: API 명세서 주문 취소 응답 추가
```

---

## 🔗 API 연동 전환 체크리스트

Frontend에서 Mock → 실제 API 전환 시:

- [ ] `.env.local`에서 `NEXT_PUBLIC_USE_MOCK=false` 설정
- [ ] CORS 설정 확인 (Backend)
- [ ] 인증 토큰 저장/전달 확인
- [ ] 에러 응답 핸들링 확인
- [ ] 날짜 형식(ISO 8601) 파싱 확인
- [ ] 페이지네이션 동작 확인

---

## 📋 동기화 체크포인트

주간 체크포인트에서 확인할 사항:

| 항목 | 확인 사항 |
|------|----------|
| API 명세 | 변경된 엔드포인트 있는지 |
| 타입 정의 | 새로운 필드/타입 추가됐는지 |
| 에러 코드 | 새로운 에러 코드 있는지 |
| 인증 | 토큰 처리 방식 변경 있는지 |
| 일정 | 지연된 기능 있는지 |

---

## 🛠️ 도구

### API 테스트
- Postman / Insomnia / Bruno
- `docs/api-spec.md`를 기반으로 Collection 생성

### Mock Server (선택)
- [MSW (Mock Service Worker)](https://mswjs.io/)
- [json-server](https://github.com/typicode/json-server)

### 협업
- Git branching: `feature/be-*`, `feature/fe-*`
- PR 리뷰 시 API 계약 준수 여부 확인
