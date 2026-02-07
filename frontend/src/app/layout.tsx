import "./globals.css";
import type { ReactNode } from "react";
import { Space_Grotesk } from "next/font/google";
import { Providers } from "./providers";

const spaceGrotesk = Space_Grotesk({
  subsets: ["latin"],
  variable: "--font-space-grotesk"
});

export const metadata = {
  title: "KickRush",
  description: "Limited-edition sneaker drop platform showcasing high-traffic systems."
};

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="ko" className={spaceGrotesk.variable}>
      <body>
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
