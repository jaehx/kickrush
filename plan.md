# KickRush TDD Plan

## Phase 0: 아키텍처 정리 (ADR)

- [ ] ADR-001 동시성 제어: Pessimistic Lock 선택
- [ ] ADR-002 중복 주문 방지: DB Unique Constraint
- [ ] ADR-003 인증: JWT (Access/Refresh)
- [ ] ADR-004 캐시: Redis (조회 캐시, TTL 전략)
- [ ] ADR-005 에러 응답 표준(`code`, `message`, `timestamp`)

## Phase 1: 기본 도메인 설정

### 1.1 상품(Shoe) 도메인
- [x] `Shoe` 엔티티 생성 테스트
- [x] `ShoeRepository` 기본 CRUD 테스트
- [x] `ShoeService` 상품 조회 테스트

### 1.2 발매(Release) 도메인
- [x] `Release` 엔티티 생성 테스트
- [x] `ReleaseRepository` 기본 CRUD 테스트
- [x] `ReleaseService` 발매 상태 조회 테스트

### 1.3 사이즈/사이즈별 재고(ReleaseSize) 도메인
- [ ] `ReleaseSize` 엔티티 생성 테스트
- [ ] `ReleaseSizeRepository` 기본 CRUD 테스트
- [ ] `Release` ↔ `ReleaseSize` 연관관계 테스트
- [ ] 사이즈 범위/단위(220~300, 5mm) 유효성 테스트

---

## Phase 2: 재고 관리

### 2.1 재고(ReleaseSize 기반) 도메인
- [ ] `ReleaseSizeService` 재고 조회 테스트
- [ ] `ReleaseSizeService` 재고 차감 테스트 (단일 요청)
- [ ] `ReleaseSizeService` 재고 부족 시 예외 발생 테스트 (`STOCK_INSUFFICIENT`)

### 2.2 동시성 처리
- [ ] 동시 재고 차감 테스트 (Pessimistic Lock)
- [ ] 동시 재고 차감 시 초과 판매 방지 테스트
- [ ] 락 타임아웃 발생 시 `LOCK_TIMEOUT` 테스트

### 2.3 핵심 경로 스파이크 (조기 검증)
- [ ] 주문 생성 핵심 경로 통합 테스트 (재고 차감 + 중복 방지 + 시간 검증)
- [ ] 표준 에러 응답 포맷 통합 테스트 (서비스/컨트롤러)

---

## Phase 3: 주문 처리

### 3.1 주문(Order) 도메인
- [ ] `Order` 엔티티 생성 테스트
- [ ] `OrderRepository` 기본 CRUD 테스트
- [ ] `OrderService` 주문 생성 테스트
- [ ] `OrderService` 중복 주문 방지 테스트 (동일 회원 + 동일 발매 + 동일 사이즈)

### 3.2 주문 상태 관리
- [ ] 주문 상태 변경 테스트
- [ ] 주문 취소 시 재고 복구 테스트
- [ ] 주문 취소 권한 테스트 (본인/관리자만 허용)

---

## Phase 4: 회원 관리

- [ ] `Member` 엔티티 생성 테스트
- [ ] `MemberRepository` 기본 CRUD 테스트
- [ ] 회원 가입 테스트
- [ ] 로그인 테스트 (JWT)
- [ ] JWT 인증 실패 시 표준 에러 응답 포맷 테스트

---

## Phase 5: API 레이어

### 5.1 상품 API
- [ ] `GET /api/shoes` 테스트
- [ ] `GET /api/shoes/{id}` 테스트
- [ ] `GET /api/releases` 테스트
- [ ] `GET /api/releases/{id}` 테스트 (사이즈별 재고 포함)
- [ ] 비회원 조회 접근 테스트 (회원/비회원 동일 응답 규격)

### 5.2 주문 API
- [ ] `POST /api/orders` 테스트
- [ ] `GET /api/orders/{id}` 테스트
- [ ] `POST /api/orders/{id}/cancel` 테스트
- [ ] 발매 시간 검증 테스트 (`RELEASE_NOT_STARTED`, `RELEASE_ENDED`)
- [ ] 에러 응답 형식 테스트 (`code`, `message`, `timestamp`)

---

## Phase 6: 비기능 요구사항 (간단 검증)

- [ ] 동시성 시나리오 통합 테스트 (재고 10, 동시 100명 → 정확히 10명 성공)
- [ ] Rate Limiting 정책 테스트 (비회원/회원 제한)
- [ ] 락 경합/타임아웃 모니터링 지표 설계
- [ ] 요청/에러 로그 구조화 포맷 정의

---

## Phase 7: Frontend 프로젝트 셋업 (Next.js 14 + TypeScript)

> 기술 스택: **Next.js 14.2.5** / **React 18.3** / **TypeScript 5.5**
> 기존 구조: `src/app/` (App Router), `src/components/`, `src/context/`, `src/hooks/`

### 7.1 프로젝트 설정 보완
- [ ] 환경 변수 설정 (`.env.local` - API Base URL)
- [ ] Prettier 설정 추가 (코드 포맷팅)
- [ ] 절대 경로 import 설정 (`@/` alias)
- [ ] `src/types/` 디렉토리 생성 (API 응답 타입)
- [ ] `src/lib/` 디렉토리 생성 (유틸리티)

### 7.2 API 클라이언트 설정
- [ ] Fetch wrapper 함수 생성 (`src/lib/api.ts`)
- [ ] 에러 핸들링 유틸리티 (표준 에러 응답 파싱)
- [ ] JWT 토큰 관리 (쿠키 또는 localStorage)
- [ ] API 응답 타입 정의 (`src/types/api.ts`)

### 7.3 인증 상태 관리
- [ ] AuthContext 구현 (`src/context/AuthContext.tsx`)
- [ ] useAuth 커스텀 훅 (`src/hooks/useAuth.ts`)
- [ ] JWT Access Token 갱신 로직

### 7.4 디자인 시스템
- [ ] 글로벌 CSS 변수 정의 (컬러, 폰트)
- [ ] 공용 컴포넌트: Button, Input, Card, Modal (`src/components/ui/`)
- [ ] 로딩 스피너 / 스켈레톤 컴포넌트
- [ ] 토스트 알림 컴포넌트

---

## Phase 8: Frontend 페이지 구현 (App Router)

> Next.js App Router 기반 (`src/app/`)

### 8.1 공개 페이지 (비회원 접근 가능)
- [ ] 홈페이지 (`src/app/page.tsx`) - 발매 목록
- [ ] 발매 상세 페이지 (`src/app/releases/[id]/page.tsx`) - 사이즈별 재고 표시
- [ ] 상품 상세 페이지 (`src/app/shoes/[id]/page.tsx`)
- [ ] 발매 카운트다운 타이머 컴포넌트 (`src/components/CountdownTimer.tsx`)

### 8.2 인증 페이지
- [ ] 로그인 페이지 (`src/app/login/page.tsx`)
- [ ] 회원가입 페이지 (`src/app/register/page.tsx`)
- [ ] 미들웨어 기반 라우트 보호 (`middleware.ts`)

### 8.3 회원 전용 페이지
- [ ] 주문하기 페이지 (`src/app/orders/new/page.tsx`) - 사이즈 선택 → 주문 확인
- [ ] 주문 완료 페이지 (`src/app/orders/[id]/complete/page.tsx`)
- [ ] 내 주문 목록 페이지 (`src/app/my/orders/page.tsx`)
- [ ] 주문 상세 페이지 (`src/app/my/orders/[id]/page.tsx`) - 취소 기능 포함

### 8.4 관리자 페이지 (선택)
- [ ] 상품 관리 (`src/app/admin/shoes/page.tsx`)
- [ ] 발매 관리 (`src/app/admin/releases/page.tsx`)
- [ ] 재고 관리 (`src/app/admin/stocks/page.tsx`)

---

## Phase 9: Frontend API 연동 및 테스트

### 9.1 API 연동
- [ ] 상품/발매 조회 API 연동
- [ ] 로그인/회원가입 API 연동 (JWT 처리)
- [ ] 주문 생성 API 연동 (에러 핸들링 포함)
- [ ] 주문 조회/취소 API 연동

### 9.2 에러 핸들링 UX
- [ ] 재고 부족 시 사용자 피드백 (`STOCK_INSUFFICIENT`)
- [ ] 중복 주문 시 사용자 피드백 (`DUPLICATE_ORDER`)
- [ ] 발매 시간 외 접근 시 피드백 (`RELEASE_NOT_STARTED`, `RELEASE_ENDED`)
- [ ] 네트워크 에러 / 서버 에러 처리

### 9.3 E2E 테스트 (선택)
- [ ] Playwright/Cypress 설정
- [ ] 발매 조회 → 주문 → 주문 확인 플로우 테스트
- [ ] 재고 부족 시 에러 표시 테스트
- [ ] 로그인/로그아웃 플로우 테스트

---

## Phase 10: 통합 및 배포 준비 (선택)

- [ ] Backend + Frontend 로컬 통합 테스트
- [ ] Docker Compose 설정 (BE + FE + DB + Redis)
- [ ] README 업데이트 (실행 방법, 데모 시나리오)
- [ ] 포트폴리오용 스크린샷/GIF 캡처
