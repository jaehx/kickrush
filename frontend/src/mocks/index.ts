import shoesData from "./data/shoes.json";
import releasesData from "./data/releases.json";
import ordersData from "./data/orders.json";
import membersData from "./data/members.json";

import type {
  ApiError,
  CreateOrderRequest,
  CreateOrderResponse,
  LoginRequest,
  LoginResponse,
  Member,
  Order,
  OrderDetail,
  Page,
  RefreshTokenRequest,
  RefreshTokenResponse,
  RegisterRequest,
  RegisterResponse,
  Release,
  ReleaseDetail,
  ReleaseStatus,
  Shoe,
  ShoeDetail
} from "@/types";

const delay = (ms: number) => new Promise((resolve) => setTimeout(resolve, ms));

const nowIso = () => new Date().toISOString();

let shoes = [...shoesData.shoes] as ShoeDetail[];
let releases = [...releasesData.releases] as Array<
  Omit<ReleaseDetail, "shoe"> & { shoeId: number }
>;
let orders = [...ordersData.orders] as Array<
  OrderDetail & { memberId: number; releaseSizeId: number }
>;
let members = [...membersData.members] as Array<Member & { password: string }>;

const buildPage = <T>(content: T[], page = 0, size = 20): Page<T> => ({
  content,
  page,
  size,
  totalElements: content.length,
  totalPages: content.length === 0 ? 0 : 1
});

const findShoe = (id: number) => shoes.find((item) => item.id === id);

const error = (code: string, message: string): ApiError => ({
  code,
  message,
  timestamp: nowIso()
});

export const mockApi = {
  async getShoes(params?: { brand?: string; page?: number; size?: number }) {
    await delay(200);
    let result = shoes.map((shoe) => ({
      id: shoe.id,
      name: shoe.name,
      brand: shoe.brand,
      modelNumber: shoe.modelNumber,
      price: shoe.price
    })) as Shoe[];

    if (params?.brand) {
      result = result.filter((shoe) => shoe.brand === params.brand);
    }

    return buildPage(result, params?.page, params?.size);
  },

  async getShoe(id: number) {
    await delay(180);
    const shoe = findShoe(id);
    if (!shoe) {
      throw error("SHOE_NOT_FOUND", "상품을 찾을 수 없습니다.");
    }
    return shoe;
  },

  async getReleases(params?: { status?: ReleaseStatus; page?: number; size?: number }) {
    await delay(200);

    let result = releases.map((release) => {
      const shoe = findShoe(release.shoeId);
      return {
        id: release.id,
        shoe: shoe
          ? {
              id: shoe.id,
              name: shoe.name,
              brand: shoe.brand,
              modelNumber: shoe.modelNumber,
              imageUrl: shoe.imageUrl
            }
          : {
              id: release.shoeId,
              name: "Unknown",
              brand: "Unknown",
              modelNumber: "",
              imageUrl: ""
            },
        releaseDateTime: release.releaseDateTime,
        endDateTime: release.endDateTime,
        status: release.status,
        totalStock: release.totalStock
      } as Release;
    });

    if (params?.status) {
      result = result.filter((release) => release.status === params.status);
    }

    return buildPage(result, params?.page, params?.size);
  },

  async getRelease(id: number) {
    await delay(180);
    const release = releases.find((item) => item.id === id);
    if (!release) {
      throw error("RELEASE_NOT_FOUND", "발매 정보를 찾을 수 없습니다.");
    }

    const shoe = findShoe(release.shoeId);

    return {
      id: release.id,
      shoe: shoe ?? {
        id: release.shoeId,
        name: "Unknown",
        brand: "Unknown",
        modelNumber: "",
        price: 0,
        description: "",
        imageUrl: ""
      },
      releaseDateTime: release.releaseDateTime,
      endDateTime: release.endDateTime,
      status: release.status,
      totalStock: release.totalStock,
      sizes: release.sizes
    } satisfies ReleaseDetail;
  },

  async createOrder(payload: CreateOrderRequest): Promise<CreateOrderResponse> {
    await delay(400);

    const release = releases.find((item) =>
      item.sizes.some((size) => size.id === payload.releaseSizeId)
    );
    if (!release) {
      throw error("RELEASE_NOT_FOUND", "발매 정보를 찾을 수 없습니다.");
    }

    const size = release.sizes.find((item) => item.id === payload.releaseSizeId);
    if (!size) {
      throw error("INVALID_PARAMETER", "사이즈 정보가 올바르지 않습니다.");
    }

    if (size.stock <= 0) {
      throw error("STOCK_INSUFFICIENT", "재고가 부족합니다.");
    }

    size.stock -= 1;

    const shoe = findShoe(release.shoeId);
    const newOrder: CreateOrderResponse = {
      id: Date.now(),
      releaseSizeId: payload.releaseSizeId,
      size: size.size,
      shoe: shoe
        ? {
            id: shoe.id,
            name: shoe.name,
            brand: shoe.brand
          }
        : {
            id: release.shoeId,
            name: "Unknown",
            brand: "Unknown"
          },
      price: size.price,
      status: "COMPLETED",
      orderedAt: nowIso()
    };

    orders = [
      ...orders,
      {
        ...newOrder,
        shoe: {
          ...newOrder.shoe,
          modelNumber: shoe?.modelNumber ?? "",
          imageUrl: shoe?.imageUrl ?? ""
        },
        memberId: 1,
        releaseSizeId: payload.releaseSizeId,
        cancelledAt: null
      }
    ];

    return newOrder;
  },

  async getMyOrders(): Promise<Page<Order>> {
    await delay(220);
    const list = orders.map((order) => ({
      id: order.id,
      shoe: {
        id: order.shoe.id,
        name: order.shoe.name,
        brand: order.shoe.brand,
        imageUrl: order.shoe.imageUrl
      },
      size: order.size,
      price: order.price,
      status: order.status,
      orderedAt: order.orderedAt
    }));

    return buildPage(list, 0, 20);
  },

  async getMyOrder(id: number): Promise<OrderDetail> {
    await delay(200);
    const order = orders.find((item) => item.id === id);
    if (!order) {
      throw error("ORDER_NOT_FOUND", "주문을 찾을 수 없습니다.");
    }

    return {
      id: order.id,
      shoe: {
        id: order.shoe.id,
        name: order.shoe.name,
        brand: order.shoe.brand,
        modelNumber: order.shoe.modelNumber,
        imageUrl: order.shoe.imageUrl
      },
      size: order.size,
      price: order.price,
      status: order.status,
      orderedAt: order.orderedAt,
      cancelledAt: order.cancelledAt ?? null
    };
  },

  async cancelOrder(id: number) {
    await delay(220);
    const order = orders.find((item) => item.id === id);
    if (!order) {
      throw error("ORDER_NOT_FOUND", "주문을 찾을 수 없습니다.");
    }
    if (order.status === "CANCELLED") {
      throw error("ORDER_NOT_CANCELLABLE", "취소할 수 없는 주문입니다.");
    }

    order.status = "CANCELLED";
    order.cancelledAt = nowIso();

    return {
      id: order.id,
      status: "CANCELLED",
      cancelledAt: order.cancelledAt
    };
  },

  async register(payload: RegisterRequest): Promise<RegisterResponse> {
    await delay(280);
    const exists = members.some((member) => member.email === payload.email);
    if (exists) {
      throw error("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.");
    }

    const newMember = {
      id: Date.now(),
      email: payload.email,
      name: payload.name,
      password: payload.password,
      role: "USER" as const
    };
    members = [...members, newMember];

    return {
      id: newMember.id,
      email: newMember.email,
      name: newMember.name
    };
  },

  async login(payload: LoginRequest): Promise<LoginResponse> {
    await delay(280);
    const member = members.find((item) => item.email === payload.email);
    if (!member) {
      throw error("INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다.");
    }

    return {
      accessToken: "mock-access-token",
      refreshToken: "mock-refresh-token",
      expiresIn: 900,
      tokenType: "Bearer"
    };
  },

  async refreshToken(_payload: RefreshTokenRequest): Promise<RefreshTokenResponse> {
    await delay(200);
    return {
      accessToken: "mock-access-token",
      expiresIn: 900,
      tokenType: "Bearer"
    };
  },

  async getProfile(): Promise<Member> {
    await delay(200);
    const member = members[0];
    return {
      id: member.id,
      email: member.email,
      name: member.name,
      role: member.role
    };
  }
};
