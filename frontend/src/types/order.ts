import type { Shoe } from "./shoe";

export type OrderStatus = "COMPLETED" | "CANCELLED";

export interface Order {
  id: number;
  shoe: Pick<Shoe, "id" | "name" | "brand"> & {
    imageUrl: string;
  };
  size: number;
  price: number;
  status: OrderStatus;
  orderedAt: string;
}

export interface OrderDetail extends Order {
  shoe: Pick<Shoe, "id" | "name" | "brand" | "modelNumber"> & {
    imageUrl: string;
  };
  cancelledAt: string | null;
}

export interface CreateOrderRequest {
  releaseSizeId: number;
}

export interface CreateOrderResponse {
  id: number;
  releaseSizeId: number;
  size: number;
  shoe: Pick<Shoe, "id" | "name" | "brand">;
  price: number;
  status: OrderStatus;
  orderedAt: string;
}

export interface CancelOrderResponse {
  id: number;
  status: "CANCELLED";
  cancelledAt: string;
}
