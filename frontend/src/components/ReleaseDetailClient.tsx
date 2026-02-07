"use client";

import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/Button";
import { SizeGrid } from "@/components/SizeGrid";
import { useToast } from "@/components/ui/Toast";
import { formatCurrency, formatDateTime } from "@/lib/format";
import type { ReleaseDetail, ReleaseSize } from "@/types";

export function ReleaseDetailClient({ release }: { release: ReleaseDetail }) {
  const [selectedSize, setSelectedSize] = useState<ReleaseSize | null>(null);
  const router = useRouter();
  const toast = useToast();

  const canOrder = useMemo(() => release.status === "ONGOING", [release.status]);

  const handleOrder = () => {
    if (!selectedSize) {
      toast.push("사이즈를 먼저 선택해주세요.", "error");
      return;
    }
    router.push(`/orders/new?releaseId=${release.id}&sizeId=${selectedSize.id}`);
  };

  return (
    <div className="panel">
      <div className="panel-header">
        <h2>Order Control</h2>
        <span className="meta">{formatDateTime(release.releaseDateTime)} drop</span>
      </div>
      <div className="info-banner">
        <div>
          <strong>{formatCurrency(selectedSize?.price ?? release.sizes[0]?.price ?? 0)}</strong>
          <p className="meta">선택 사이즈 가격</p>
        </div>
        <Button size="lg" onClick={handleOrder} disabled={!canOrder}>
          {canOrder ? "Order Now" : "Release Closed"}
        </Button>
      </div>
      <div className="form-grid" style={{ marginTop: 16 }}>
        <span className="meta">Size selection</span>
        <SizeGrid sizes={release.sizes} onSelect={setSelectedSize} />
      </div>
    </div>
  );
}
