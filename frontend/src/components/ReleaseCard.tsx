import Link from "next/link";
import { Card, CardBody, CardHeader } from "@/components/ui/Card";
import { formatShortDate } from "@/lib/format";
import type { Release } from "@/types";
import { StatusBadge } from "@/components/StatusBadge";

export function ReleaseCard({ release }: { release: Release }) {
  return (
    <Card className="release-card">
      <CardHeader>
        <div>
          <p className="meta">{release.shoe.brand}</p>
          <h3>{release.shoe.name}</h3>
        </div>
        <StatusBadge status={release.status} />
      </CardHeader>
      <CardBody>
        <div className="release-meta">
          <div>
            <span className="meta-label">Release</span>
            <span>{formatShortDate(release.releaseDateTime)}</span>
          </div>
          <div>
            <span className="meta-label">Stock</span>
            <span>{release.totalStock} pairs</span>
          </div>
          <div>
            <span className="meta-label">Status</span>
            <span>{release.status}</span>
          </div>
        </div>
        <Link href={`/releases/${release.id}`} className="link-button">
          View Release
        </Link>
      </CardBody>
    </Card>
  );
}
