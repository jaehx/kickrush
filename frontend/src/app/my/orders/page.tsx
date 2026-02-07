"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Spinner } from "@/components/ui/Spinner";
import { apiClient } from "@/lib/api";
import { formatCurrency, formatDateTime } from "@/lib/format";
import type { Order, Page } from "@/types";

export default function MyOrdersPage() {
  const [orders, setOrders] = useState<Page<Order> | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const data = await apiClient.fetch<Page<Order>>("/my/orders");
        setOrders(data);
      } finally {
        setIsLoading(false);
      }
    };

    void load();
  }, []);

  if (isLoading) {
    return (
      <section className="panel">
        <Spinner label="주문 정보를 불러오는 중" />
      </section>
    );
  }

  if (!orders) {
    return (
      <section className="panel">
        <p className="meta">주문 정보를 불러오지 못했습니다.</p>
      </section>
    );
  }

  return (
    <section className="panel">
      <div className="panel-header">
        <h1>My Orders</h1>
        <span className="meta">{orders.totalElements} records</span>
      </div>
      <div className="form-grid">
        {orders.content.length === 0 ? (
          <p className="meta">주문 내역이 없습니다.</p>
        ) : (
          orders.content.map((order) => (
            <Link key={order.id} href={`/my/orders/${order.id}`} className="info-banner">
              <div>
                <strong>{order.shoe.name}</strong>
                <p className="meta">{order.shoe.brand}</p>
              </div>
              <div>
                <p className="meta">Size {order.size}</p>
                <strong>{formatCurrency(order.price)}</strong>
              </div>
              <div>
                <p className="meta">{formatDateTime(order.orderedAt)}</p>
                <strong>{order.status}</strong>
              </div>
            </Link>
          ))
        )}
      </div>
    </section>
  );
}
