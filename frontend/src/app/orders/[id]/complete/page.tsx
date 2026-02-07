"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/Button";
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
        <Spinner label="주문 완료 정보를 불러오는 중" />
      </section>
    );
  }

  if (!order) {
    return (
      <section className="panel">
        <div className="panel-header">
          <h1>Order complete</h1>
        </div>
        <p className="meta">주문 정보를 찾을 수 없습니다.</p>
        <Link href="/my/orders" className="link-muted">
          주문 목록으로 이동
        </Link>
      </section>
    );
  }

  return (
    <div className="form-grid">
      <section className="panel">
        <div className="panel-header">
          <div>
            <p className="meta">Order completed</p>
            <h1>#{order.id}</h1>
          </div>
          <div className="info-banner">
            <span className="meta-label">Status</span>
            <strong>{order.status}</strong>
          </div>
        </div>
        <div className="info-banner">
          <div>
            <span className="meta-label">Shoe</span>
            <strong>{order.shoe.name}</strong>
            <p className="meta">{order.shoe.brand}</p>
          </div>
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
        <p className="meta" style={{ marginTop: 16 }}>
          주문 확인 메일이 등록된 이메일로 전송되었습니다.
        </p>
      </section>

      <section className="panel">
        <div className="panel-header">
          <h2>Next steps</h2>
        </div>
        <div className="form-grid">
          <Link href={`/my/orders/${order.id}`}>
            <Button size="lg">주문 상세 보기</Button>
          </Link>
          <Link href="/my/orders" className="link-muted">
            내 주문 목록으로 이동
          </Link>
          <Link href="/">
            <Button variant="ghost">다른 발매 보기</Button>
          </Link>
        </div>
      </section>
    </div>
  );
}
