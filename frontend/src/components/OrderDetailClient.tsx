"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/Button";
import { useToast } from "@/components/ui/Toast";
import { apiClient } from "@/lib/api";
import { getErrorMessage } from "@/lib/error";
import type { CancelOrderResponse } from "@/types";

interface OrderDetailClientProps {
  orderId: number;
  status: "COMPLETED" | "CANCELLED";
  onCancelled: (cancelledAt: string) => void;
}

export function OrderDetailClient({ orderId, status, onCancelled }: OrderDetailClientProps) {
  const [isCancelling, setIsCancelling] = useState(false);
  const router = useRouter();
  const toast = useToast();

  const handleCancel = async () => {
    setIsCancelling(true);
    try {
      const response = await apiClient.fetch<CancelOrderResponse>(`/my/orders/${orderId}/cancel`, {
        method: "POST"
      });
      toast.push("주문이 취소되었습니다.", "success");
      onCancelled(response.cancelledAt);
      router.refresh();
    } catch (error) {
      toast.push(getErrorMessage(error, "취소에 실패했습니다."), "error");
    } finally {
      setIsCancelling(false);
    }
  };

  return (
    <div>
      {status === "COMPLETED" ? (
        <Button variant="danger" onClick={handleCancel} disabled={isCancelling}>
          {isCancelling ? "취소 중..." : "Cancel order"}
        </Button>
      ) : (
        <Button variant="outline" disabled>
          이미 취소됨
        </Button>
      )}
    </div>
  );
}
