"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { OrderDetailClient } from "@/components/OrderDetailClient";
import { Spinner } from "@/components/ui/Spinner";
import { apiClient } from "@/lib/api";
import { formatCurrency, formatDateTime } from "@/lib/format";
import type { OrderDetail } from "@/types";

interface OrderDetailPageProps {
  params: { id: string };
}

export default function OrderDetailPage({ params }: OrderDetailPageProps) {
  const [order, setOrder] = useState<OrderDetail | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await apiClient.fetch<OrderDetail>(`/my/orders/${params.id}`);
        setOrder(data);
      } finally {
        setIsLoading(false);
      }
    };

    void load();
  }, [params.id]);

  const handleCancelled = (cancelledAt: string) => {
    setOrder((prev) => (prev ? { ...prev, status: "CANCELLED", cancelledAt } : prev));
  };

  if (isLoading) {
    return (
      <section className="panel">
        <Spinner label="주문 상세를 불러오는 중" />
      </section>
    );
  }

  if (!order) {
    return (
      <section className="panel">
        <div className="panel-header">
          <h1>Order not found</h1>
        </div>
        <p className="meta">주문 정보를 찾을 수 없습니다.</p>
        <Link href="/my/orders" className="link-muted">
          주문 목록으로 돌아가기
        </Link>
      </section>
    );
  }

  return (
    <div className="form-grid">
      <section className="panel">
        <div className="panel-header">
          <div>
            <p className="meta">{order.shoe.brand}</p>
            <h1>{order.shoe.name}</h1>
            <p className="meta">{order.shoe.modelNumber}</p>
          </div>
          <div className="info-banner">
            <span className="meta-label">Status</span>
            <strong>{order.status}</strong>
          </div>
        </div>
        <div className="info-banner">
          <div>
            <span className="meta-label">Size</span>
            <strong>{order.size}</strong>
          </div>
          <div>
            <span className="meta-label">Price</span>
            <strong>{formatCurrency(order.price)}</strong>
          </div>
          <div>
            <span className="meta-label">Ordered at</span>
            <strong>{formatDateTime(order.orderedAt)}</strong>
          </div>
        </div>
        {order.cancelledAt ? (
          <p className="meta" style={{ marginTop: 12 }}>
            Cancelled at: {formatDateTime(order.cancelledAt)}
          </p>
        ) : null}
      </section>

      <section className="panel">
        <div className="panel-header">
          <h2>Manage order</h2>
        </div>
        <div className="form-grid">
          <OrderDetailClient
            orderId={order.id}
            status={order.status}
            onCancelled={handleCancelled}
          />
          <Link href="/my/orders" className="link-muted">
            주문 목록으로 돌아가기
          </Link>
        </div>
      </section>
    </div>
  );
}
