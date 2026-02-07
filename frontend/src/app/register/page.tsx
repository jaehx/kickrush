"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { useAuth } from "@/hooks/useAuth";
import { useToast } from "@/components/ui/Toast";

export default function RegisterPage() {
  const { register } = useAuth();
  const toast = useToast();
  const router = useRouter();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setIsLoading(true);
    try {
      await register({ name, email, password });
      toast.push("계정 생성 완료. 대시보드로 이동합니다.", "success");
      router.push("/my/orders");
    } catch (error) {
      const message = error instanceof Error ? error.message : "회원가입에 실패했습니다.";
      toast.push(message, "error");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <section className="panel" style={{ maxWidth: 520, margin: "0 auto" }}>
      <div className="panel-header">
        <h1>Create account</h1>
      </div>
      <form className="form-grid" onSubmit={handleSubmit}>
        <Input
          label="Name"
          value={name}
          onChange={(event) => setName(event.target.value)}
          required
        />
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
          helperText="8자 이상, 특수문자를 포함하세요."
          required
        />
        <Button type="submit" size="lg" disabled={isLoading}>
          {isLoading ? "Creating..." : "Create account"}
        </Button>
      </form>
      <p className="meta" style={{ marginTop: 16 }}>
        이미 계정이 있나요? <Link href="/login">로그인</Link>
      </p>
    </section>
  );
}
