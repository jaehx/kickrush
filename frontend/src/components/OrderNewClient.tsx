"use client";

import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { Button } from "@/components/ui/Button";
import { SizeGrid } from "@/components/SizeGrid";
import { useToast } from "@/components/ui/Toast";
import { apiClient } from "@/lib/api";
import { getErrorMessage } from "@/lib/error";
import { formatCurrency } from "@/lib/format";
import type { CreateOrderResponse, Release, ReleaseDetail, ReleaseSize } from "@/types";

interface OrderNewClientProps {
  releases: Release[];
  releaseDetail?: ReleaseDetail | null;
  initialSizeId?: number | null;
}

export function OrderNewClient({ releases, releaseDetail, initialSizeId }: OrderNewClientProps) {
  const router = useRouter();
  const toast = useToast();
  const [selectedSize, setSelectedSize] = useState<ReleaseSize | null>(() => {
    if (!releaseDetail || !initialSizeId) return null;
    return releaseDetail.sizes.find((size) => size.id === initialSizeId) ?? null;
  });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const selectedRelease = releaseDetail ?? null;
  const canOrder = useMemo(() => selectedRelease?.status === "ONGOING", [selectedRelease]);

  const handleOrder = async () => {
    if (!selectedSize) {
      toast.push("사이즈를 선택해주세요.", "error");
      return;
    }

    setIsSubmitting(true);
    try {
      const response = await apiClient.fetch<CreateOrderResponse>("/orders", {
        method: "POST",
        body: JSON.stringify({ releaseSizeId: selectedSize.id })
      });
      toast.push("주문이 완료되었습니다.", "success");
      router.push(`/orders/${response.id}/complete`);
    } catch (error) {
      toast.push(getErrorMessage(error, "주문 생성에 실패했습니다."), "error");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="form-grid">
      <section className="panel">
        <div className="panel-header">
          <h1>Place your order</h1>
        </div>
        <div className="form-grid">
          {selectedRelease ? (
            <div>
              <p className="meta">Release selected</p>
              <h2>{selectedRelease.shoe.name}</h2>
              <p className="meta">{selectedRelease.shoe.brand}</p>
            </div>
          ) : (
            <div>
              <p className="meta">Choose a release</p>
              <div className="form-grid">
                {releases.map((release) => (
                  <Link
                    key={release.id}
                    href={`/orders/new?releaseId=${release.id}`}
                    className="info-banner"
                  >
                    <div>
                      <strong>{release.shoe.name}</strong>
                      <p className="meta">{release.status}</p>
                    </div>
                    <span>Select</span>
                  </Link>
                ))}
              </div>
            </div>
          )}

          {selectedRelease ? (
            <>
              <div>
                <p className="meta">Select size</p>
                <SizeGrid sizes={selectedRelease.sizes} onSelect={setSelectedSize} />
              </div>
              <div className="info-banner">
                <div>
                  <span className="meta-label">Price</span>
                  <strong>
                    {formatCurrency(selectedSize?.price ?? selectedRelease.sizes[0]?.price ?? 0)}
                  </strong>
                </div>
                <Button size="lg" onClick={handleOrder} disabled={!canOrder || isSubmitting}>
                  {isSubmitting ? "Placing..." : canOrder ? "Confirm order" : "Release closed"}
                </Button>
              </div>
            </>
          ) : null}
        </div>
      </section>
    </div>
  );
}
