# TypeScript 타입 정의 (Frontend 공유용)

> 이 파일의 타입들을 Frontend `src/types/` 디렉토리에 복사하여 사용합니다.
> Backend API 응답과 정확히 일치하도록 설계되었습니다.

## 공통 타입

```typescript
// src/types/common.ts

// 페이지네이션 응답
export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

// 에러 응답
export interface ApiError {
  code: string;
  message: string;
  timestamp: string;
}

// 에러 코드 상수
export const ErrorCode = {
  // 400 Bad Request
  INVALID_PARAMETER: 'INVALID_PARAMETER',
  RELEASE_NOT_STARTED: 'RELEASE_NOT_STARTED',
  RELEASE_ENDED: 'RELEASE_ENDED',
  
  // 401 Unauthorized
  UNAUTHORIZED: 'UNAUTHORIZED',
  INVALID_CREDENTIALS: 'INVALID_CREDENTIALS',
  TOKEN_EXPIRED: 'TOKEN_EXPIRED',
  
  // 403 Forbidden
  FORBIDDEN: 'FORBIDDEN',
  
  // 404 Not Found
  SHOE_NOT_FOUND: 'SHOE_NOT_FOUND',
  RELEASE_NOT_FOUND: 'RELEASE_NOT_FOUND',
  ORDER_NOT_FOUND: 'ORDER_NOT_FOUND',
  
  // 409 Conflict
  STOCK_INSUFFICIENT: 'STOCK_INSUFFICIENT',
  DUPLICATE_ORDER: 'DUPLICATE_ORDER',
  DUPLICATE_EMAIL: 'DUPLICATE_EMAIL',
  ORDER_NOT_CANCELLABLE: 'ORDER_NOT_CANCELLABLE',
  
  // 503 Service Unavailable
  LOCK_TIMEOUT: 'LOCK_TIMEOUT',
} as const;

export type ErrorCodeType = typeof ErrorCode[keyof typeof ErrorCode];
```

---

## 상품 (Shoe) 타입

```typescript
// src/types/shoe.ts

export interface Shoe {
  id: number;
  name: string;
  brand: string;
  modelNumber: string;
  price: number;
}

export interface ShoeDetail extends Shoe {
  description: string;
  imageUrl: string;
}

// 상품 목록 조회 파라미터
export interface ShoeListParams {
  brand?: string;
  page?: number;
  size?: number;
}
```

---

## 발매 (Release) 타입

```typescript
// src/types/release.ts

import { Shoe, ShoeDetail } from './shoe';

export type ReleaseStatus = 'UPCOMING' | 'ONGOING' | 'ENDED';

export interface ReleaseSize {
  id: number;
  size: number;      // 220, 225, 230, ... 300
  stock: number;
  price: number;
}

export interface Release {
  id: number;
  shoe: Pick<Shoe, 'id' | 'name' | 'brand' | 'modelNumber'> & {
    imageUrl: string;
  };
  releaseDateTime: string;  // ISO 8601
  endDateTime: string;      // ISO 8601
  status: ReleaseStatus;
  totalStock: number;
}

export interface ReleaseDetail {
  id: number;
  shoe: ShoeDetail;
  releaseDateTime: string;
  endDateTime: string;
  status: ReleaseStatus;
  totalStock: number;
  sizes: ReleaseSize[];
}

// 발매 목록 조회 파라미터
export interface ReleaseListParams {
  status?: ReleaseStatus;
  page?: number;
  size?: number;
}
```

---

## 주문 (Order) 타입

```typescript
// src/types/order.ts

import { Shoe } from './shoe';

export type OrderStatus = 'COMPLETED' | 'CANCELLED';

export interface Order {
  id: number;
  shoe: Pick<Shoe, 'id' | 'name' | 'brand'> & {
    imageUrl: string;
  };
  size: number;
  price: number;
  status: OrderStatus;
  orderedAt: string;       // ISO 8601
}

export interface OrderDetail extends Order {
  shoe: Pick<Shoe, 'id' | 'name' | 'brand' | 'modelNumber'> & {
    imageUrl: string;
  };
  cancelledAt: string | null;
}

// 주문 생성 요청
export interface CreateOrderRequest {
  releaseSizeId: number;
}

// 주문 생성 응답
export interface CreateOrderResponse {
  id: number;
  releaseSizeId: number;
  size: number;
  shoe: Pick<Shoe, 'id' | 'name' | 'brand'>;
  price: number;
  status: OrderStatus;
  orderedAt: string;
}

// 주문 취소 응답
export interface CancelOrderResponse {
  id: number;
  status: 'CANCELLED';
  cancelledAt: string;
}
```

---

## 회원 (Member) 타입

```typescript
// src/types/member.ts

export type MemberRole = 'USER' | 'ADMIN';

export interface Member {
  id: number;
  email: string;
  name: string;
  role: MemberRole;
}

// 회원가입 요청
export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
}

// 회원가입 응답
export interface RegisterResponse {
  id: number;
  email: string;
  name: string;
}
```

---

## 인증 (Auth) 타입

```typescript
// src/types/auth.ts

// 로그인 요청
export interface LoginRequest {
  email: string;
  password: string;
}

// 로그인 응답
export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;      // 초 단위 (900 = 15분)
  tokenType: 'Bearer';
}

// 토큰 갱신 요청
export interface RefreshTokenRequest {
  refreshToken: string;
}

// 토큰 갱신 응답
export interface RefreshTokenResponse {
  accessToken: string;
  expiresIn: number;
  tokenType: 'Bearer';
}
```

---

## 상수 정의

```typescript
// src/types/constants.ts

// 신발 사이즈 범위
export const SHOE_SIZE = {
  MIN: 220,
  MAX: 300,
  STEP: 5,
} as const;

// 사용 가능한 모든 사이즈 배열 생성
export const AVAILABLE_SIZES = Array.from(
  { length: (SHOE_SIZE.MAX - SHOE_SIZE.MIN) / SHOE_SIZE.STEP + 1 },
  (_, i) => SHOE_SIZE.MIN + i * SHOE_SIZE.STEP
);
// [220, 225, 230, 235, 240, 245, 250, 255, 260, 265, 270, 275, 280, 285, 290, 295, 300]

// 주요 브랜드
export const BRANDS = ['Nike', 'Adidas', 'New Balance', 'Asics', 'Converse'] as const;
export type Brand = typeof BRANDS[number];
```

---

## 인덱스 파일

```typescript
// src/types/index.ts

export * from './common';
export * from './shoe';
export * from './release';
export * from './order';
export * from './member';
export * from './auth';
export * from './constants';
```
