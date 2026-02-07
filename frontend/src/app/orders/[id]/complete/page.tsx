"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Spinner } from "@/components/ui/Spinner";
import { apiClient } from "@/lib/api";
import { formatCurrency, formatDateTime } from "@/lib/format";
import type { OrderDetail } from "@/types";

interface OrderCompletePageProps {
  params: { id: string };
}

export default function OrderCompletePage({ params }: OrderCompletePageProps) {
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

  if (isLoading) {
    return (
      <section className="panel">
        <Spinner label="주문 내역을 불러오는 중" />
      </section>
    );
  }

  if (!order) {
    return (
      <section className="panel">
        <p className="meta">주문 정보를 불러오지 못했습니다.</p>
      </section>
    );
  }

  return (
    <section className="panel" style={{ maxWidth: 680, margin: "0 auto" }}>
      <div className="panel-header">
        <h1>Order confirmed</h1>
      </div>
      <div className="info-banner">
        <div>
          <p className="meta">Order ID</p>
          <strong>#{order.id}</strong>
        </div>
        <div>
          <p className="meta">Status</p>
          <strong>{order.status}</strong>
        </div>
      </div>
      <div className="form-grid" style={{ marginTop: 20 }}>
        <div>
          <p className="meta">Shoe</p>
          <strong>{order.shoe.name}</strong>
        </div>
        <div>
          <p className="meta">Size</p>
          <strong>{order.size}</strong>
        </div>
        <div>
          <p className="meta">Price</p>
          <strong>{formatCurrency(order.price)}</strong>
        </div>
        <div>
          <p className="meta">Ordered at</p>
          <strong>{formatDateTime(order.orderedAt)}</strong>
        </div>
      </div>
      <Link href="/my/orders" className="link-button" style={{ marginTop: 20 }}>
        View my orders
      </Link>
    </section>
  );
}
