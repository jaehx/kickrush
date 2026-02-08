"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { useAuth } from "@/hooks/useAuth";
import { useToast } from "@/components/ui/Toast";
import { getErrorMessage } from "@/lib/error";

export default function LoginPage() {
  const { login } = useAuth();
  const toast = useToast();
  const router = useRouter();
  const [email, setEmail] = useState("user@example.com");
  const [password, setPassword] = useState("SecureP@ss123");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setIsLoading(true);
    try {
      await login(email, password);
      toast.push("로그인 완료. 주문 대시보드로 이동합니다.", "success");
      router.push("/my/orders");
    } catch (error) {
      toast.push(getErrorMessage(error, "로그인에 실패했습니다."), "error");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <section className="panel" style={{ maxWidth: 520, margin: "0 auto" }}>
      <div className="panel-header">
        <h1>Login</h1>
      </div>
      <form className="form-grid" onSubmit={handleSubmit}>
        <Input
          label="Email"
          type="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          required
        />
        <Input
          label="Password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          required
        />
        <Button type="submit" size="lg" disabled={isLoading}>
          {isLoading ? "Logging in..." : "Login"}
        </Button>
      </form>
      <p className="meta" style={{ marginTop: 16 }}>
        아직 계정이 없나요? <Link href="/register">회원가입</Link>
      </p>
    </section>
  );
}
