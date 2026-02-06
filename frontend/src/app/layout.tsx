import "./globals.css";
import type { ReactNode } from "react";

export const metadata = {
  title: "KickRush",
  description: "Limited-edition sneaker drop platform showcasing high-traffic systems."
};

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="ko">
      <body>{children}</body>
    </html>
  );
}
