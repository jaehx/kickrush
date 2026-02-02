# KickRush TDD Plan

## Phase 1: 기본 도메인 설정

### 1.1 상품(Shoe) 도메인
- [x] `Shoe` 엔티티 생성 테스트
- [x] `ShoeRepository` 기본 CRUD 테스트
- [x] `ShoeService` 상품 조회 테스트

### 1.2 발매(Release) 도메인
- [x] `Release` 엔티티 생성 테스트
- [x] `ReleaseRepository` 기본 CRUD 테스트
- [ ] `ReleaseService` 발매 상태 조회 테스트

---

## Phase 2: 재고 관리

### 2.1 재고(Stock) 도메인
- [ ] `Stock` 엔티티 생성 테스트
- [ ] `StockRepository` 기본 CRUD 테스트
- [ ] `StockService` 재고 조회 테스트
- [ ] `StockService` 재고 차감 테스트 (단일 요청)
- [ ] `StockService` 재고 부족 시 예외 발생 테스트

### 2.2 동시성 처리
- [ ] 동시 재고 차감 테스트 (Pessimistic Lock)
- [ ] 동시 재고 차감 시 초과 판매 방지 테스트

---

## Phase 3: 주문 처리

### 3.1 주문(Order) 도메인
- [ ] `Order` 엔티티 생성 테스트
- [ ] `OrderRepository` 기본 CRUD 테스트
- [ ] `OrderService` 주문 생성 테스트
- [ ] `OrderService` 중복 주문 방지 테스트

### 3.2 주문 상태 관리
- [ ] 주문 상태 변경 테스트
- [ ] 주문 취소 시 재고 복구 테스트

---

## Phase 4: 회원 관리

- [ ] `Member` 엔티티 생성 테스트
- [ ] `MemberRepository` 기본 CRUD 테스트
- [ ] 회원 가입 테스트
- [ ] 로그인 테스트 (JWT)

---

## Phase 5: API 레이어

### 5.1 상품 API
- [ ] `GET /api/shoes` 테스트
- [ ] `GET /api/shoes/{id}` 테스트
- [ ] `GET /api/releases` 테스트

### 5.2 주문 API
- [ ] `POST /api/orders` 테스트
- [ ] `GET /api/orders/{id}` 테스트
- [ ] `POST /api/orders/{id}/cancel` 테스트
