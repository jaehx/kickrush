"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Button } from "@/components/ui/Button";
import { useAuth } from "@/hooks/useAuth";
import clsx from "@/components/ui/clsx";

const navLinks = [
  { href: "/", label: "Releases" },
  { href: "/my/orders", label: "My Orders" },
  { href: "/orders/new", label: "Order" }
];

export function AppShell({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <div className="app-shell">
      <header className="app-header">
        <Link href="/" className="logo">
          KickRush
          <span className="logo-tag">Drop Lab</span>
        </Link>
        <nav className="nav">
          {navLinks.map((link) => (
            <Link
              key={link.href}
              href={link.href}
              className={clsx("nav-link", pathname === link.href && "nav-link-active")}
            >
              {link.label}
            </Link>
          ))}
        </nav>
        <div className="header-actions">
          {isAuthenticated ? (
            <>
              <span className="user-chip">{user?.name ?? "회원"}</span>
              <Button variant="ghost" size="sm" onClick={logout}>
                로그아웃
              </Button>
            </>
          ) : (
            <>
              <Link href="/login" className="link-muted">
                로그인
              </Link>
              <Link href="/register">
                <Button size="sm">회원가입</Button>
              </Link>
            </>
          )}
        </div>
      </header>
      <main className="app-main">{children}</main>
      <footer className="app-footer">
        KickRush Drop Lab · high-traffic release simulation
      </footer>
    </div>
  );
}
