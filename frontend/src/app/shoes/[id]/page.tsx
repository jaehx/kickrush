import Link from "next/link";
import { fetchApi } from "@/lib/api";
import { formatCurrency, formatDateTime } from "@/lib/format";
import type { Page, Release, ShoeDetail } from "@/types";

interface ShoePageProps {
  params: { id: string };
}

export default async function ShoePage({ params }: ShoePageProps) {
  const shoe = await fetchApi<ShoeDetail>(`/shoes/${params.id}`);
  const releases = await fetchApi<Page<Release>>("/releases");
  const related = releases.content.filter((release) => release.shoe.id === shoe.id);

  return (
    <div className="form-grid">
      <section className="panel">
        <div className="panel-header">
          <div>
            <p className="meta">{shoe.brand}</p>
            <h1>{shoe.name}</h1>
            <p className="meta">{shoe.modelNumber}</p>
          </div>
        </div>
        <p>{shoe.description}</p>
        <div className="info-banner">
          <div>
            <span className="meta-label">Retail price</span>
            <strong>{formatCurrency(shoe.price)}</strong>
          </div>
          <div>
            <span className="meta-label">Active releases</span>
            <strong>{related.length}</strong>
          </div>
        </div>
      </section>

      <section className="panel">
        <div className="panel-header">
          <h2>Release timeline</h2>
        </div>
        {related.length === 0 ? (
          <p className="meta">이 상품에 대한 발매 일정이 아직 없습니다.</p>
        ) : (
          <div className="form-grid">
            {related.map((release) => (
              <Link key={release.id} href={`/releases/${release.id}`} className="info-banner">
                <div>
                  <strong>{release.status}</strong>
                  <p className="meta">{formatDateTime(release.releaseDateTime)}</p>
                </div>
                <span>View</span>
              </Link>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
