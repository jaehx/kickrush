# 인증/인가 계약서 (Authentication Contract)

> Backend/Frontend 간 인증 처리 방식에 대한 합의 문서입니다.

## 1. 인증 방식 개요

| 항목 | 값 |
|------|-----|
| 방식 | JWT (JSON Web Token) |
| Access Token 만료 | 15분 (900초) |
| Refresh Token 만료 | 7일 |
| 토큰 전달 방식 | Authorization Header |
| 저장 위치 | httpOnly Cookie (권장) 또는 localStorage |

---

## 2. 인증 플로우

### 2.1 로그인 플로우

```
┌──────────┐          ┌──────────┐          ┌──────────┐
│ Frontend │          │ Backend  │          │   DB     │
└────┬─────┘          └────┬─────┘          └────┬─────┘
     │                     │                     │
     │ POST /api/auth/login│                     │
     │ {email, password}   │                     │
     │────────────────────>│                     │
     │                     │ 회원 조회           │
     │                     │────────────────────>│
     │                     │<────────────────────│
     │                     │                     │
     │                     │ 비밀번호 검증       │
     │                     │ JWT 토큰 생성       │
     │                     │                     │
     │ 200 OK              │                     │
     │ {accessToken,       │                     │
     │  refreshToken,      │                     │
     │  expiresIn}         │                     │
     │<────────────────────│                     │
     │                     │                     │
     │ 토큰 저장           │                     │
     │ (Cookie/Storage)    │                     │
```

### 2.2 인증된 요청 플로우

```
┌──────────┐          ┌──────────┐
│ Frontend │          │ Backend  │
└────┬─────┘          └────┬─────┘
     │                     │
     │ GET /api/my/orders  │
     │ Authorization:      │
     │ Bearer {accessToken}│
     │────────────────────>│
     │                     │
     │                     │ JWT 검증
     │                     │ - 서명 확인
     │                     │ - 만료 확인
     │                     │ - 사용자 정보 추출
     │                     │
     │ 200 OK              │
     │ {orders: [...]}     │
     │<────────────────────│
```

### 2.3 토큰 갱신 플로우

```
┌──────────┐          ┌──────────┐
│ Frontend │          │ Backend  │
└────┬─────┘          └────┬─────┘
     │                     │
     │ POST /api/auth/refresh
     │ {refreshToken}      │
     │────────────────────>│
     │                     │
     │                     │ Refresh Token 검증
     │                     │ 새 Access Token 생성
     │                     │
     │ 200 OK              │
     │ {accessToken,       │
     │  expiresIn}         │
     │<────────────────────│
     │                     │
     │ 새 토큰 저장        │
```

---

## 3. JWT 토큰 구조

### 3.1 Access Token Payload

```json
{
  "sub": "1",                          // 회원 ID
  "email": "user@example.com",
  "name": "홍길동",
  "role": "USER",                      // USER | ADMIN
  "iat": 1707300000,                   // 발급 시간
  "exp": 1707300900                    // 만료 시간 (+15분)
}
```

### 3.2 Refresh Token Payload

```json
{
  "sub": "1",                          // 회원 ID
  "type": "refresh",
  "iat": 1707300000,
  "exp": 1707904800                    // 만료 시간 (+7일)
}
```

---

## 4. Frontend 구현 가이드

### 4.1 토큰 저장

```typescript
// 옵션 1: localStorage (간단하지만 XSS 취약점 주의)
localStorage.setItem('accessToken', token);

// 옵션 2: httpOnly Cookie (권장, Backend에서 Set-Cookie)
// Frontend에서 직접 접근 불가, 자동으로 요청에 포함됨
```

### 4.2 API 클라이언트 설정

```typescript
// frontend/src/lib/api.ts

class ApiClient {
  private baseUrl: string;

  constructor() {
    this.baseUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';
  }

  private getAccessToken(): string | null {
    if (typeof window === 'undefined') return null;
    return localStorage.getItem('accessToken');
  }

  private setAccessToken(token: string): void {
    localStorage.setItem('accessToken', token);
  }

  private getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  async fetch<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const accessToken = this.getAccessToken();
    
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      ...options?.headers,
    };

    if (accessToken) {
      headers['Authorization'] = `Bearer ${accessToken}`;
    }

    let response = await fetch(`${this.baseUrl}${endpoint}`, {
      ...options,
      headers,
    });

    // 401 에러 시 토큰 갱신 시도
    if (response.status === 401) {
      const refreshed = await this.refreshToken();
      if (refreshed) {
        // 갱신 성공 시 원래 요청 재시도
        headers['Authorization'] = `Bearer ${this.getAccessToken()}`;
        response = await fetch(`${this.baseUrl}${endpoint}`, {
          ...options,
          headers,
        });
      } else {
        // 갱신 실패 시 로그아웃 처리
        this.logout();
        throw { code: 'UNAUTHORIZED', message: '다시 로그인해주세요.' };
      }
    }

    if (!response.ok) {
      const error = await response.json();
      throw error;
    }

    return response.json();
  }

  async refreshToken(): Promise<boolean> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) return false;

    try {
      const response = await fetch(`${this.baseUrl}/auth/refresh`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken }),
      });

      if (!response.ok) return false;

      const data = await response.json();
      this.setAccessToken(data.accessToken);
      return true;
    } catch {
      return false;
    }
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    window.location.href = '/login';
  }
}

export const api = new ApiClient();
```

### 4.3 인증 컨텍스트

```typescript
// frontend/src/context/AuthContext.tsx

'use client';

import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { api } from '@/lib/api';
import { Member } from '@/types';

interface AuthContextType {
  user: Member | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<Member | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // 초기 로드 시 사용자 정보 확인
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      const profile = await api.fetch<Member>('/my/profile');
      setUser(profile);
    } catch {
      setUser(null);
    } finally {
      setIsLoading(false);
    }
  };

  const login = async (email: string, password: string) => {
    const response = await api.fetch<{
      accessToken: string;
      refreshToken: string;
    }>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });

    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
    
    await checkAuth();
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{
      user,
      isLoading,
      isAuthenticated: !!user,
      login,
      logout,
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
```

### 4.4 라우트 보호 (미들웨어)

```typescript
// frontend/middleware.ts

import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

// 인증이 필요한 경로
const protectedPaths = ['/my', '/orders'];

// 인증 시 접근 불가 경로 (로그인/회원가입)
const authPaths = ['/login', '/register'];

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  const accessToken = request.cookies.get('accessToken')?.value;

  // 보호된 경로에 비인증 접근
  if (protectedPaths.some(path => pathname.startsWith(path))) {
    if (!accessToken) {
      const loginUrl = new URL('/login', request.url);
      loginUrl.searchParams.set('redirect', pathname);
      return NextResponse.redirect(loginUrl);
    }
  }

  // 인증 경로에 이미 로그인된 사용자 접근
  if (authPaths.some(path => pathname.startsWith(path))) {
    if (accessToken) {
      return NextResponse.redirect(new URL('/', request.url));
    }
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/my/:path*', '/orders/:path*', '/login', '/register'],
};
```

---

## 5. Backend 구현 가이드 (간략)

### 5.1 JWT 설정

```yaml
# application.yml
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key-here}
  access-token-expiration: 900000    # 15분 (밀리초)
  refresh-token-expiration: 604800000 # 7일 (밀리초)
```

### 5.2 인증 필터

```java
// JwtAuthenticationFilter.java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        String token = resolveToken(request);
        
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

---

## 6. 에러 응답 규격

| HTTP | 코드 | 설명 | 대응 방법 |
|------|------|------|----------|
| 401 | `UNAUTHORIZED` | 토큰 없음 | 로그인 페이지로 이동 |
| 401 | `INVALID_CREDENTIALS` | 잘못된 로그인 정보 | 에러 메시지 표시 |
| 401 | `TOKEN_EXPIRED` | 토큰 만료 | Refresh Token으로 갱신 시도 |
| 403 | `FORBIDDEN` | 권한 없음 | 접근 거부 페이지 표시 |
