import type { ApiError, LoginResponse, RefreshTokenResponse } from "@/types";

const USE_MOCK = process.env.NEXT_PUBLIC_USE_MOCK === "true";
const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";

const createApiError = (fallback: Partial<ApiError>): ApiError => ({
  code: fallback.code ?? "UNKNOWN_ERROR",
  message: fallback.message ?? "알 수 없는 오류가 발생했습니다.",
  timestamp: fallback.timestamp ?? new Date().toISOString()
});

const readAccessToken = () => {
  if (typeof window === "undefined") return null;
  return localStorage.getItem("accessToken");
};

const readRefreshToken = () => {
  if (typeof window === "undefined") return null;
  return localStorage.getItem("refreshToken");
};

const saveAccessToken = (token: string) => {
  if (typeof window === "undefined") return;
  localStorage.setItem("accessToken", token);
};

export async function fetchApi<T>(endpoint: string, options?: RequestInit): Promise<T> {
  if (USE_MOCK) {
    return mockFetch(endpoint, options);
  }

  const response = await fetch(`${API_URL}${endpoint}`, {
    headers: {
      "Content-Type": "application/json",
      ...options?.headers
    },
    ...options
  });

  if (!response.ok) {
    const error = (await response.json()) as ApiError;
    throw createApiError(error);
  }

  return (await response.json()) as T;
}

export class ApiClient {
  async fetch<T>(endpoint: string, options?: RequestInit): Promise<T> {
    if (USE_MOCK) {
      return mockFetch(endpoint, options);
    }

    const accessToken = readAccessToken();
    const headers = new Headers(options?.headers);
    headers.set("Content-Type", "application/json");

    if (accessToken) {
      headers.set("Authorization", `Bearer ${accessToken}`);
    }

    let response = await fetch(`${API_URL}${endpoint}`, {
      ...options,
      headers
    });

    if (response.status === 401) {
      const refreshed = await this.refreshToken();
      if (refreshed) {
        const updatedToken = readAccessToken();
        if (updatedToken) {
          headers.set("Authorization", `Bearer ${updatedToken}`);
        } else {
          headers.delete("Authorization");
        }
        response = await fetch(`${API_URL}${endpoint}`, {
          ...options,
          headers
        });
      } else {
        this.logout();
        throw createApiError({ code: "UNAUTHORIZED", message: "다시 로그인해주세요." });
      }
    }

    if (!response.ok) {
      const error = (await response.json()) as ApiError;
      throw createApiError(error);
    }

    return (await response.json()) as T;
  }

  async login(email: string, password: string): Promise<LoginResponse> {
    const response = await this.fetch<LoginResponse>("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password })
    });

    if (typeof window !== "undefined") {
      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);
      document.cookie = `accessToken=${response.accessToken}; path=/; max-age=900`;
    }

    return response;
  }

  async refreshToken(): Promise<boolean> {
    const refreshToken = readRefreshToken();
    if (!refreshToken) return false;

    try {
      const data = await fetchApi<RefreshTokenResponse>("/auth/refresh", {
        method: "POST",
        body: JSON.stringify({ refreshToken })
      });
      saveAccessToken(data.accessToken);
      if (typeof window !== "undefined") {
        document.cookie = `accessToken=${data.accessToken}; path=/; max-age=900`;
      }
      return true;
    } catch {
      return false;
    }
  }

  logout() {
    if (typeof window === "undefined") return;
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    document.cookie = "accessToken=; path=/; max-age=0";
  }
}

export const apiClient = new ApiClient();

async function mockFetch<T>(endpoint: string, options?: RequestInit): Promise<T> {
  const { mockApi } = await import("@/mocks");

  const url = new URL(endpoint, "http://localhost:8080");
  const path = url.pathname;
  const method = (options?.method ?? "GET").toUpperCase();

  const parseJson = async <P>() => {
    if (!options?.body) return null;
    if (typeof options.body === "string") {
      return JSON.parse(options.body) as P;
    }
    return null;
  };

  if (method === "GET" && path === "/shoes") {
    return (await mockApi.getShoes({
      brand: url.searchParams.get("brand") ?? undefined,
      page: url.searchParams.get("page") ? Number(url.searchParams.get("page")) : undefined,
      size: url.searchParams.get("size") ? Number(url.searchParams.get("size")) : undefined
    })) as T;
  }

  if (method === "GET" && path.startsWith("/shoes/")) {
    const id = Number(path.split("/")[2]);
    return (await mockApi.getShoe(id)) as T;
  }

  if (method === "GET" && path === "/releases") {
    return (await mockApi.getReleases({
      status: (url.searchParams.get("status") ?? undefined) as
        | "UPCOMING"
        | "ONGOING"
        | "ENDED"
        | undefined,
      page: url.searchParams.get("page") ? Number(url.searchParams.get("page")) : undefined,
      size: url.searchParams.get("size") ? Number(url.searchParams.get("size")) : undefined
    })) as T;
  }

  if (method === "GET" && path.startsWith("/releases/")) {
    const id = Number(path.split("/")[2]);
    return (await mockApi.getRelease(id)) as T;
  }

  if (method === "POST" && path === "/orders") {
    const body = await parseJson<{ releaseSizeId: number }>();
    if (!body) {
      throw createApiError({ code: "INVALID_PARAMETER", message: "요청이 비어 있습니다." });
    }
    return (await mockApi.createOrder(body)) as T;
  }

  if (method === "GET" && path === "/my/orders") {
    return (await mockApi.getMyOrders()) as T;
  }

  if (method === "GET" && path.startsWith("/my/orders/")) {
    const id = Number(path.split("/")[3] ?? path.split("/")[2]);
    return (await mockApi.getMyOrder(id)) as T;
  }

  if (method === "POST" && path.startsWith("/my/orders/") && path.endsWith("/cancel")) {
    const id = Number(path.split("/")[3]);
    return (await mockApi.cancelOrder(id)) as T;
  }

  if (method === "POST" && path === "/members/register") {
    const body = await parseJson<{ email: string; password: string; name: string }>();
    if (!body) {
      throw createApiError({ code: "INVALID_PARAMETER", message: "요청이 비어 있습니다." });
    }
    return (await mockApi.register(body)) as T;
  }

  if (method === "POST" && path === "/auth/login") {
    const body = await parseJson<{ email: string; password: string }>();
    if (!body) {
      throw createApiError({ code: "INVALID_PARAMETER", message: "요청이 비어 있습니다." });
    }
    return (await mockApi.login(body)) as T;
  }

  if (method === "POST" && path === "/auth/refresh") {
    const body = await parseJson<{ refreshToken: string }>();
    if (!body) {
      throw createApiError({ code: "INVALID_PARAMETER", message: "요청이 비어 있습니다." });
    }
    return (await mockApi.refreshToken(body)) as T;
  }

  if (method === "GET" && path === "/my/profile") {
    if (typeof window !== "undefined") {
      const token = localStorage.getItem("accessToken");
      if (!token) {
        throw createApiError({ code: "UNAUTHORIZED", message: "로그인이 필요합니다." });
      }
    }
    return (await mockApi.getProfile()) as T;
  }

  throw createApiError({ code: "INVALID_PARAMETER", message: "지원하지 않는 요청입니다." });
}
