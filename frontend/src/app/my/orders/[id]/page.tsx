"use client";

import { useEffect, useState } from "react";
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
        <p className="meta">주문 정보를 불러오지 못했습니다.</p>
      </section>
    );
  }

  return (
    <section className="panel" style={{ maxWidth: 720, margin: "0 auto" }}>
      <div className="panel-header">
        <div>
          <h1>Order #{order.id}</h1>
          <p className="meta">{order.status}</p>
        </div>
        <OrderDetailClient
          orderId={order.id}
          status={order.status}
          onCancelled={(cancelledAt) =>
            setOrder((prev) =>
              prev
                ? {
                    ...prev,
                    status: "CANCELLED",
                    cancelledAt
                  }
                : prev
            )
          }
        />
      </div>
      <div className="form-grid">
        <div>
          <span className="meta-label">Shoe</span>
          <p>{order.shoe.name}</p>
        </div>
        <div>
          <span className="meta-label">Size</span>
          <p>{order.size}</p>
        </div>
        <div>
          <span className="meta-label">Price</span>
          <p>{formatCurrency(order.price)}</p>
        </div>
        <div>
          <span className="meta-label">Ordered at</span>
          <p>{formatDateTime(order.orderedAt)}</p>
        </div>
        {order.cancelledAt ? (
          <div>
            <span className="meta-label">Cancelled at</span>
            <p>{formatDateTime(order.cancelledAt)}</p>
          </div>
        ) : null}
      </div>
    </section>
  );
}
