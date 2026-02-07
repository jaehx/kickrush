# KickRush TDD Plan

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
