"use client";

import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";
import { apiClient } from "@/lib/api";
import type { Member, RegisterRequest } from "@/types";

interface AuthContextValue {
  user: Member | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (payload: RegisterRequest) => Promise<void>;
  logout: () => void;
  refresh: () => Promise<boolean>;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<Member | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const checkAuth = useCallback(async () => {
    try {
      const profile = await apiClient.fetch<Member>("/my/profile");
      setUser(profile);
    } catch {
      setUser(null);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    void checkAuth();
  }, [checkAuth]);

  const login = useCallback(
    async (email: string, password: string) => {
      await apiClient.login(email, password);
      await checkAuth();
    },
    [checkAuth]
  );

  const register = useCallback(
    async (payload: RegisterRequest) => {
      await apiClient.fetch("/members/register", {
        method: "POST",
        body: JSON.stringify(payload)
      });
      await login(payload.email, payload.password);
    },
    [login]
  );

  const logout = useCallback(() => {
    apiClient.logout();
    setUser(null);
  }, []);

  const refresh = useCallback(async () => apiClient.refreshToken(), []);

  const value = useMemo(
    () => ({
      user,
      isLoading,
      isAuthenticated: Boolean(user),
      login,
      register,
      logout,
      refresh
    }),
    [user, isLoading, login, register, logout, refresh]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuthContext() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuthContext must be used within AuthProvider");
  }
  return context;
}
