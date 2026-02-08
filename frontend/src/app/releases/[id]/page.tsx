import { CountdownTimer } from "@/components/CountdownTimer";
import { ReleaseDetailClient } from "@/components/ReleaseDetailClient";
import { StatusBadge } from "@/components/StatusBadge";
import { fetchApi } from "@/lib/api";
import { formatCurrency, formatDateTime } from "@/lib/format";
import type { ReleaseDetail } from "@/types";

interface ReleasePageProps {
  params: Promise<{ id: string }>;
}

export default async function ReleasePage({ params }: ReleasePageProps) {
  const { id } = await params;
  const release = await fetchApi<ReleaseDetail>(`/releases/${id}`);

  return (
    <div className="form-grid">
      <div className="panel">
        <div className="panel-header">
          <div>
            <p className="meta">{release.shoe.brand}</p>
            <h1>{release.shoe.name}</h1>
            <p className="meta">{release.shoe.modelNumber}</p>
          </div>
          <StatusBadge status={release.status} />
        </div>
        <div className="grid">
          <div>
            <span className="meta-label">Release window</span>
            <p>
              {formatDateTime(release.releaseDateTime)} - {formatDateTime(release.endDateTime)}
            </p>
          </div>
          <div>
            <span className="meta-label">Total stock</span>
            <p>{release.totalStock} pairs</p>
          </div>
          <div>
            <span className="meta-label">Base price</span>
            <p>{formatCurrency(release.sizes[0]?.price ?? 0)}</p>
          </div>
        </div>
        <CountdownTimer target={release.releaseDateTime} />
        <p className="meta" style={{ marginTop: 16 }}>
          {release.shoe.description}
        </p>
      </div>

      <ReleaseDetailClient release={release} />
    </div>
  );
}
