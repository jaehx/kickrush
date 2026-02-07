# Mock 데이터 (Frontend 개발용)

> Backend API 완성 전에 Frontend 개발을 위한 Mock 데이터입니다.
> `frontend/src/mocks/` 디렉토리에 복사하여 사용하세요.

## Mock 데이터 파일 구조

```
frontend/src/mocks/
├── data/
│   ├── shoes.json
│   ├── releases.json
│   ├── orders.json
│   └── members.json
├── handlers.ts       # MSW 핸들러 (선택)
└── index.ts
```

---

## 1. 상품 Mock 데이터

```json
// frontend/src/mocks/data/shoes.json
{
  "shoes": [
    {
      "id": 1,
      "name": "Air Jordan 1 Retro High OG 'Chicago Lost and Found'",
      "brand": "Nike",
      "modelNumber": "DZ5485-612",
      "price": 209000,
      "description": "1985년 오리지널 시카고 컬러웨이를 재현한 에어 조던 1. 빈티지 가공된 솔과 크랙 페인트 효과가 특징.",
      "imageUrl": "/images/shoes/aj1-chicago.jpg"
    },
    {
      "id": 2,
      "name": "Nike Dunk Low 'Panda'",
      "brand": "Nike",
      "modelNumber": "DD1391-100",
      "price": 139000,
      "description": "클래식한 블랙 앤 화이트 컬러웨이의 덩크 로우. 깔끔한 디자인으로 어떤 스타일에도 매치 가능.",
      "imageUrl": "/images/shoes/dunk-panda.jpg"
    },
    {
      "id": 3,
      "name": "New Balance 550 'White Green'",
      "brand": "New Balance",
      "modelNumber": "BB550WT1",
      "price": 139000,
      "description": "90년대 농구화에서 영감받은 레트로 디자인. 화이트 베이스에 그린 포인트.",
      "imageUrl": "/images/shoes/nb550-green.jpg"
    },
    {
      "id": 4,
      "name": "Adidas Samba OG 'White Black'",
      "brand": "Adidas",
      "modelNumber": "B75806",
      "price": 129000,
      "description": "1950년대 인도어 축구화에서 시작된 아디다스 삼바. 클래식한 실루엣과 T-toe 디자인.",
      "imageUrl": "/images/shoes/samba-og.jpg"
    },
    {
      "id": 5,
      "name": "Nike Air Force 1 '07 Low 'White'",
      "brand": "Nike",
      "modelNumber": "CW2288-111",
      "price": 139000,
      "description": "1982년 처음 출시된 에어포스 1의 클래식 화이트 버전. 세련된 디자인과 편안한 착화감.",
      "imageUrl": "/images/shoes/af1-white.jpg"
    }
  ]
}
```

---

## 2. 발매 Mock 데이터

```json
// frontend/src/mocks/data/releases.json
{
  "releases": [
    {
      "id": 1,
      "shoeId": 1,
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
    },
    {
      "id": 2,
      "shoeId": 2,
      "releaseDateTime": "2026-02-08T10:00:00",
      "endDateTime": "2026-02-08T10:30:00",
      "status": "ONGOING",
      "totalStock": 50,
      "sizes": [
        { "id": 10, "size": 250, "stock": 5, "price": 139000 },
        { "id": 11, "size": 255, "stock": 3, "price": 139000 },
        { "id": 12, "size": 260, "stock": 8, "price": 139000 },
        { "id": 13, "size": 265, "stock": 10, "price": 139000 },
        { "id": 14, "size": 270, "stock": 12, "price": 139000 },
        { "id": 15, "size": 275, "stock": 7, "price": 139000 },
        { "id": 16, "size": 280, "stock": 5, "price": 139000 }
      ]
    },
    {
      "id": 3,
      "shoeId": 3,
      "releaseDateTime": "2026-02-05T11:00:00",
      "endDateTime": "2026-02-05T11:30:00",
      "status": "ENDED",
      "totalStock": 0,
      "sizes": [
        { "id": 17, "size": 250, "stock": 0, "price": 139000 },
        { "id": 18, "size": 255, "stock": 0, "price": 139000 },
        { "id": 19, "size": 260, "stock": 0, "price": 139000 },
        { "id": 20, "size": 265, "stock": 0, "price": 139000 },
        { "id": 21, "size": 270, "stock": 0, "price": 139000 }
      ]
    }
  ]
}
```

---

## 3. 주문 Mock 데이터

```json
// frontend/src/mocks/data/orders.json
{
  "orders": [
    {
      "id": 1001,
      "memberId": 1,
      "releaseSizeId": 5,
      "shoe": {
        "id": 1,
        "name": "Air Jordan 1 Retro High OG 'Chicago Lost and Found'",
        "brand": "Nike",
        "modelNumber": "DZ5485-612",
        "imageUrl": "/images/shoes/aj1-chicago.jpg"
      },
      "size": 270,
      "price": 209000,
      "status": "COMPLETED",
      "orderedAt": "2026-02-10T11:00:05",
      "cancelledAt": null
    },
    {
      "id": 1002,
      "memberId": 1,
      "releaseSizeId": 14,
      "shoe": {
        "id": 2,
        "name": "Nike Dunk Low 'Panda'",
        "brand": "Nike",
        "modelNumber": "DD1391-100",
        "imageUrl": "/images/shoes/dunk-panda.jpg"
      },
      "size": 270,
      "price": 139000,
      "status": "CANCELLED",
      "orderedAt": "2026-02-08T10:00:10",
      "cancelledAt": "2026-02-08T10:05:00"
    }
  ]
}
```

---

## 4. 회원 Mock 데이터

```json
// frontend/src/mocks/data/members.json
{
  "members": [
    {
      "id": 1,
      "email": "user@example.com",
      "password": "$2a$10$hashedpassword",
      "name": "홍길동",
      "role": "USER"
    },
    {
      "id": 2,
      "email": "admin@example.com",
      "password": "$2a$10$hashedpassword",
      "name": "관리자",
      "role": "ADMIN"
    }
  ]
}
```

---

## 5. Mock API 유틸리티

```typescript
// frontend/src/mocks/index.ts

import shoesData from './data/shoes.json';
import releasesData from './data/releases.json';
import ordersData from './data/orders.json';

// 지연 시뮬레이션
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// Mock API 함수들
export const mockApi = {
  // 상품 목록 조회
  async getShoes(params?: { brand?: string; page?: number; size?: number }) {
    await delay(300);
    let shoes = shoesData.shoes;
    
    if (params?.brand) {
      shoes = shoes.filter(s => s.brand === params.brand);
    }
    
    return {
      content: shoes,
      page: params?.page ?? 0,
      size: params?.size ?? 20,
      totalElements: shoes.length,
      totalPages: 1,
    };
  },

  // 상품 상세 조회
  async getShoe(id: number) {
    await delay(200);
    const shoe = shoesData.shoes.find(s => s.id === id);
    if (!shoe) {
      throw { code: 'SHOE_NOT_FOUND', message: '상품을 찾을 수 없습니다.' };
    }
    return shoe;
  },

  // 발매 목록 조회
  async getReleases(params?: { status?: string; page?: number; size?: number }) {
    await delay(300);
    let releases = releasesData.releases.map(r => {
      const shoe = shoesData.shoes.find(s => s.id === r.shoeId);
      return {
        ...r,
        shoe: shoe ? {
          id: shoe.id,
          name: shoe.name,
          brand: shoe.brand,
          modelNumber: shoe.modelNumber,
          imageUrl: shoe.imageUrl,
        } : null,
      };
    });
    
    if (params?.status) {
      releases = releases.filter(r => r.status === params.status);
    }
    
    return {
      content: releases,
      page: params?.page ?? 0,
      size: params?.size ?? 20,
      totalElements: releases.length,
      totalPages: 1,
    };
  },

  // 발매 상세 조회
  async getRelease(id: number) {
    await delay(200);
    const release = releasesData.releases.find(r => r.id === id);
    if (!release) {
      throw { code: 'RELEASE_NOT_FOUND', message: '발매 정보를 찾을 수 없습니다.' };
    }
    
    const shoe = shoesData.shoes.find(s => s.id === release.shoeId);
    return {
      ...release,
      shoe,
    };
  },

  // 주문 생성 (Mock)
  async createOrder(releaseSizeId: number) {
    await delay(500);
    
    // 재고 부족 시뮬레이션 (랜덤)
    if (Math.random() < 0.1) {
      throw { code: 'STOCK_INSUFFICIENT', message: '재고가 부족합니다.' };
    }
    
    return {
      id: Date.now(),
      releaseSizeId,
      size: 270,
      shoe: shoesData.shoes[0],
      price: 209000,
      status: 'COMPLETED',
      orderedAt: new Date().toISOString(),
    };
  },

  // 내 주문 목록
  async getMyOrders() {
    await delay(300);
    return {
      content: ordersData.orders,
      page: 0,
      size: 20,
      totalElements: ordersData.orders.length,
      totalPages: 1,
    };
  },
};
```

---

## 6. 환경 변수 설정

```bash
# frontend/.env.local

# Mock 모드 사용 여부
NEXT_PUBLIC_USE_MOCK=true

# 실제 API URL (Backend 완성 후 사용)
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

```typescript
// frontend/src/lib/api.ts

const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === 'true';
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export async function fetchApi<T>(
  endpoint: string,
  options?: RequestInit
): Promise<T> {
  if (USE_MOCK) {
    // Mock 모드일 때는 mockApi 사용
    const { mockApi } = await import('@/mocks');
    // endpoint에 맞는 mock 함수 호출...
  }
  
  const response = await fetch(`${API_URL}${endpoint}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
    ...options,
  });
  
  if (!response.ok) {
    const error = await response.json();
    throw error;
  }
  
  return response.json();
}
```

---

## 사용 방법

1. **Mock 모드로 개발 시작**
   ```bash
   # .env.local
   NEXT_PUBLIC_USE_MOCK=true
   ```

2. **Backend 완성 후 실제 API로 전환**
   ```bash
   # .env.local
   NEXT_PUBLIC_USE_MOCK=false
   NEXT_PUBLIC_API_URL=http://localhost:8080/api
   ```
