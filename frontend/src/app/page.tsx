import { ReleaseCard } from "@/components/ReleaseCard";
import { ShoeCard } from "@/components/ShoeCard";
import { Button } from "@/components/ui/Button";
import { fetchApi } from "@/lib/api";
import type { Page, Release, Shoe } from "@/types";

export default async function Home() {
  const releases = await fetchApi<Page<Release>>("/releases");
  const shoes = await fetchApi<Page<Shoe>>("/shoes");

  return (
    <div>
      <section className="hero">
        <div className="hero-card">
          <span className="meta">High-traffic release simulator</span>
          <h1>KickRush Drop Control Room</h1>
          <p>
            발매 일정, 사이즈별 재고, 주문 흐름을 한 번에 점검하는 드롭 허브입니다.
            Mock 데이터로 병렬 개발을 진행하며, 백엔드가 완성되면 즉시 실 API로 전환합니다.
          </p>
          <div className="section-title">
            <Button size="lg">Explore Releases</Button>
          </div>
        </div>
        <div className="hero-card">
          <h2>Parallel Dev Checklist</h2>
          <ul>
            <li>API 스펙: docs/api-spec.md 기준</li>
            <li>타입 동기화: docs/types.md → src/types</li>
            <li>Mock 모드: NEXT_PUBLIC_USE_MOCK=true</li>
          </ul>
        </div>
      </section>

      <div className="section-title">
        <h2>Upcoming Releases</h2>
        <span className="meta">{releases.totalElements} drops tracked</span>
      </div>
      <section className="grid">
        {releases.content.map((release) => (
          <ReleaseCard key={release.id} release={release} />
        ))}
      </section>

      <div className="section-title">
        <h2>Featured Shoes</h2>
        <span className="meta">Top silhouettes in the lineup</span>
      </div>
      <section className="grid">
        {shoes.content.map((shoe) => (
          <ShoeCard key={shoe.id} shoe={shoe} />
        ))}
      </section>
    </div>
  );
}
