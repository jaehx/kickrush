import type { ApiError, ErrorCodeType } from "@/types";

const DEFAULT_MESSAGES: Record<ErrorCodeType, string> = {
  INVALID_PARAMETER: "요청 정보가 올바르지 않습니다.",
  RELEASE_NOT_STARTED: "발매가 아직 시작되지 않았습니다.",
  RELEASE_ENDED: "발매가 종료되었습니다.",
  UNAUTHORIZED: "로그인이 필요합니다.",
  INVALID_CREDENTIALS: "이메일 또는 비밀번호가 올바르지 않습니다.",
  TOKEN_EXPIRED: "세션이 만료되었습니다. 다시 로그인해주세요.",
  FORBIDDEN: "접근 권한이 없습니다.",
  SHOE_NOT_FOUND: "상품을 찾을 수 없습니다.",
  RELEASE_NOT_FOUND: "발매 정보를 찾을 수 없습니다.",
  ORDER_NOT_FOUND: "주문을 찾을 수 없습니다.",
  STOCK_INSUFFICIENT: "재고가 부족합니다.",
  DUPLICATE_ORDER: "이미 해당 상품을 주문하셨습니다.",
  DUPLICATE_EMAIL: "이미 사용 중인 이메일입니다.",
  ORDER_NOT_CANCELLABLE: "취소할 수 없는 주문입니다.",
  LOCK_TIMEOUT: "요청이 많아 처리 지연 중입니다. 잠시 후 다시 시도해주세요."
};

export const isApiError = (error: unknown): error is ApiError => {
  return (
    typeof error === "object" &&
    error !== null &&
    "code" in error &&
    "message" in error &&
    "timestamp" in error
  );
};

export const getErrorMessage = (error: unknown, fallback: string) => {
  if (isApiError(error)) {
    const code = error.code as ErrorCodeType;
    return DEFAULT_MESSAGES[code] ?? error.message ?? fallback;
  }

  if (error instanceof Error) {
    return error.message;
  }

  return fallback;
};
