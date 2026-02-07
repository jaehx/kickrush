# API 명세서 (API Specification)

> 이 문서는 Backend/Frontend 병렬 개발을 위한 API 계약입니다.
> Backend 구현 전에도 Frontend는 이 명세를 기준으로 Mock 데이터와 함께 개발할 수 있습니다.

## 기본 정보

| 항목 | 값 |
|------|-----|
| Base URL (Local) | `http://localhost:8080/api` |
| Content-Type | `application/json` |
| 인증 방식 | JWT Bearer Token |
| 날짜 형식 | ISO 8601 (`2026-02-07T15:00:00`) |

---

## 1. 상품 API (Shoes)

### 1.1 상품 목록 조회

```
GET /api/shoes
```

**Query Parameters:**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| brand | string | N | 브랜드 필터 |
| page | number | N | 페이지 번호 (default: 0) |
| size | number | N | 페이지 크기 (default: 20) |

**Response: 200 OK**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Air Jordan 1 Retro High OG",
      "brand": "Nike",
      "modelNumber": "DZ5485-612",
      "price": 209000
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 50,
  "totalPages": 3
}
```

---

### 1.2 상품 상세 조회

```
GET /api/shoes/{id}
```

**Path Parameters:**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| id | number | Y | 상품 ID |

**Response: 200 OK**
```json
{
  "id": 1,
  "name": "Air Jordan 1 Retro High OG",
  "brand": "Nike",
  "modelNumber": "DZ5485-612",
  "price": 209000,
  "description": "클래식한 에어 조던 1 레트로 하이",
  "imageUrl": "/images/shoes/aj1-retro-high.jpg"
}
```

**Response: 404 Not Found**
```json
{
  "code": "SHOE_NOT_FOUND",
  "message": "상품을 찾을 수 없습니다.",
  "timestamp": "2026-02-07T15:00:00"
}
```

---

## 2. 발매 API (Releases)

### 2.1 발매 목록 조회

```
GET /api/releases
```

**Query Parameters:**
| 파라미터 | 타입 | 필수 | 설명 |
|----------|------|------|------|
| status | string | N | `UPCOMING`, `ONGOING`, `ENDED` |
| page | number | N | 페이지 번호 (default: 0) |
| size | number | N | 페이지 크기 (default: 20) |

**Response: 200 OK**
```json
{
  "content": [
    {
      "id": 1,
      "shoe": {
        "id": 1,
        "name": "Air Jordan 1 Retro High OG",
        "brand": "Nike",
        "modelNumber": "DZ5485-612",
        "imageUrl": "/images/shoes/aj1-retro-high.jpg"
      },
      "releaseDateTime": "2026-02-10T11:00:00",
      "endDateTime": "2026-02-10T11:30:00",
      "status": "UPCOMING",
      "totalStock": 100
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 10,
  "totalPages": 1
}
```

---

### 2.2 발매 상세 조회 (사이즈별 재고 포함)

```
GET /api/releases/{id}
```

**Response: 200 OK**
```json
{
  "id": 1,
  "shoe": {
    "id": 1,
    "name": "Air Jordan 1 Retro High OG",
    "brand": "Nike",
    "modelNumber": "DZ5485-612",
    "price": 209000,
    "description": "클래식한 에어 조던 1 레트로 하이",
    "imageUrl": "/images/shoes/aj1-retro-high.jpg"
  },
  "releaseDateTime": "2026-02-10T11:00:00",
  "endDateTime": "2026-02-10T11:30:00",
  "status": "UPCOMING",
  "totalStock": 100,
  "sizes": [
    { "id": 1, "size": 250, "stock": 10, "price": 209000 },
    { "id": 2, "size": 255, "stock": 8, "price": 209000 },
    { "id": 3, "size": 260, "stock": 12, "price": 209000 },
    { "id": 4, "size": 265, "stock": 15, "price": 209000 },
    { "id": 5, "size": 270, "stock": 20, "price": 209000 },
    { "id": 6, "size": 275, "stock": 15, "price": 209000 },
    { "id": 7, "size": 280, "stock": 10, "price": 209000 },
    { "id": 8, "size": 285, "stock": 5, "price": 209000 },
    { "id": 9, "size": 290, "stock": 5, "price": 209000 }
  ]
}
```

**Response: 404 Not Found**
```json
{
  "code": "RELEASE_NOT_FOUND",
  "message": "발매 정보를 찾을 수 없습니다.",
  "timestamp": "2026-02-07T15:00:00"
}
```

---

## 3. 주문 API (Orders)

### 3.1 주문 생성

```
POST /api/orders
```

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "releaseSizeId": 5
}
```

**Response: 201 Created**
```json
{
  "id": 1001,
  "releaseSizeId": 5,
  "size": 270,
  "shoe": {
    "id": 1,
    "name": "Air Jordan 1 Retro High OG",
    "brand": "Nike"
  },
  "price": 209000,
  "status": "COMPLETED",
  "orderedAt": "2026-02-10T11:00:05"
}
```

**Response: 400 Bad Request - 발매 시간 전**
```json
{
  "code": "RELEASE_NOT_STARTED",
  "message": "발매가 아직 시작되지 않았습니다.",
  "timestamp": "2026-02-10T10:59:00"
}
```

**Response: 400 Bad Request - 발매 종료**
```json
{
  "code": "RELEASE_ENDED",
  "message": "발매가 종료되었습니다.",
  "timestamp": "2026-02-10T11:31:00"
}
```

**Response: 409 Conflict - 재고 부족**
```json
{
  "code": "STOCK_INSUFFICIENT",
  "message": "재고가 부족합니다.",
  "timestamp": "2026-02-10T11:00:10"
}
```

**Response: 409 Conflict - 중복 주문**
```json
{
  "code": "DUPLICATE_ORDER",
  "message": "이미 해당 상품을 주문하셨습니다.",
  "timestamp": "2026-02-10T11:00:10"
}
```

**Response: 503 Service Unavailable - 락 타임아웃**
```json
{
  "code": "LOCK_TIMEOUT",
  "message": "요청이 많아 처리가 지연되고 있습니다. 잠시 후 다시 시도해주세요.",
  "timestamp": "2026-02-10T11:00:10"
}
```

---

### 3.2 내 주문 목록 조회

```
GET /api/my/orders
```

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response: 200 OK**
```json
{
  "content": [
    {
      "id": 1001,
      "shoe": {
        "id": 1,
        "name": "Air Jordan 1 Retro High OG",
        "brand": "Nike",
        "imageUrl": "/images/shoes/aj1-retro-high.jpg"
      },
      "size": 270,
      "price": 209000,
      "status": "COMPLETED",
      "orderedAt": "2026-02-10T11:00:05"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 3.3 주문 상세 조회

```
GET /api/my/orders/{id}
```

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response: 200 OK**
```json
{
  "id": 1001,
  "shoe": {
    "id": 1,
    "name": "Air Jordan 1 Retro High OG",
    "brand": "Nike",
    "modelNumber": "DZ5485-612",
    "imageUrl": "/images/shoes/aj1-retro-high.jpg"
  },
  "size": 270,
  "price": 209000,
  "status": "COMPLETED",
  "orderedAt": "2026-02-10T11:00:05",
  "cancelledAt": null
}
```

---

### 3.4 주문 취소

```
POST /api/my/orders/{id}/cancel
```

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response: 200 OK**
```json
{
  "id": 1001,
  "status": "CANCELLED",
  "cancelledAt": "2026-02-10T11:05:00"
}
```

**Response: 409 Conflict - 취소 불가**
```json
{
  "code": "ORDER_NOT_CANCELLABLE",
  "message": "취소할 수 없는 주문입니다.",
  "timestamp": "2026-02-10T11:05:00"
}
```

---

## 4. 회원 API (Members)

### 4.1 회원가입

```
POST /api/members/register
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecureP@ss123",
  "name": "홍길동"
}
```

**Response: 201 Created**
```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "홍길동"
}
```

**Response: 409 Conflict - 중복 이메일**
```json
{
  "code": "DUPLICATE_EMAIL",
  "message": "이미 사용 중인 이메일입니다.",
  "timestamp": "2026-02-07T15:00:00"
}
```

---

### 4.2 로그인

```
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "SecureP@ss123"
}
```

**Response: 200 OK**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 900,
  "tokenType": "Bearer"
}
```

**Response: 401 Unauthorized**
```json
{
  "code": "INVALID_CREDENTIALS",
  "message": "이메일 또는 비밀번호가 올바르지 않습니다.",
  "timestamp": "2026-02-07T15:00:00"
}
```

---

### 4.3 토큰 갱신

```
POST /api/auth/refresh
```

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response: 200 OK**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 900,
  "tokenType": "Bearer"
}
```

---

### 4.4 내 정보 조회

```
GET /api/my/profile
```

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response: 200 OK**
```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "홍길동",
  "role": "USER"
}
```

---

## 5. 관리자 API (Admin) - 선택

### 5.1 상품 등록

```
POST /api/admin/shoes
```

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "name": "Air Jordan 1 Retro High OG",
  "brand": "Nike",
  "modelNumber": "DZ5485-612",
  "price": 209000,
  "description": "클래식한 에어 조던 1 레트로 하이",
  "imageUrl": "/images/shoes/aj1-retro-high.jpg"
}
```

**Response: 201 Created**
```json
{
  "id": 1,
  "name": "Air Jordan 1 Retro High OG",
  "brand": "Nike",
  "modelNumber": "DZ5485-612",
  "price": 209000
}
```

---

### 5.2 발매 등록

```
POST /api/admin/releases
```

**Request Body:**
```json
{
  "shoeId": 1,
  "releaseDateTime": "2026-02-10T11:00:00",
  "endDateTime": "2026-02-10T11:30:00",
  "sizes": [
    { "size": 250, "stock": 10, "price": 209000 },
    { "size": 255, "stock": 8, "price": 209000 },
    { "size": 260, "stock": 12, "price": 209000 }
  ]
}
```

**Response: 201 Created**
```json
{
  "id": 1,
  "shoeId": 1,
  "releaseDateTime": "2026-02-10T11:00:00",
  "totalStock": 30
}
```

---

## 부록: 공통 에러 응답

모든 에러는 아래 형식을 따릅니다:

```json
{
  "code": "ERROR_CODE",
  "message": "사용자에게 보여줄 메시지",
  "timestamp": "2026-02-07T15:00:00"
}
```

| HTTP | 코드 | 설명 |
|------|------|------|
| 400 | `INVALID_PARAMETER` | 잘못된 요청 파라미터 |
| 400 | `RELEASE_NOT_STARTED` | 발매 시작 전 |
| 400 | `RELEASE_ENDED` | 발매 종료 |
| 401 | `UNAUTHORIZED` | 인증 필요 |
| 401 | `INVALID_CREDENTIALS` | 잘못된 인증 정보 |
| 401 | `TOKEN_EXPIRED` | 토큰 만료 |
| 403 | `FORBIDDEN` | 권한 없음 |
| 404 | `SHOE_NOT_FOUND` | 상품 없음 |
| 404 | `RELEASE_NOT_FOUND` | 발매 없음 |
| 404 | `ORDER_NOT_FOUND` | 주문 없음 |
| 409 | `STOCK_INSUFFICIENT` | 재고 부족 |
| 409 | `DUPLICATE_ORDER` | 중복 주문 |
| 409 | `DUPLICATE_EMAIL` | 중복 이메일 |
| 409 | `ORDER_NOT_CANCELLABLE` | 취소 불가 상태 |
| 503 | `LOCK_TIMEOUT` | 락 획득 실패 |
