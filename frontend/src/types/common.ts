export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ApiError {
  code: string;
  message: string;
  timestamp: string;
}

export const ErrorCode = {
  INVALID_PARAMETER: "INVALID_PARAMETER",
  RELEASE_NOT_STARTED: "RELEASE_NOT_STARTED",
  RELEASE_ENDED: "RELEASE_ENDED",
  UNAUTHORIZED: "UNAUTHORIZED",
  INVALID_CREDENTIALS: "INVALID_CREDENTIALS",
  TOKEN_EXPIRED: "TOKEN_EXPIRED",
  FORBIDDEN: "FORBIDDEN",
  SHOE_NOT_FOUND: "SHOE_NOT_FOUND",
  RELEASE_NOT_FOUND: "RELEASE_NOT_FOUND",
  ORDER_NOT_FOUND: "ORDER_NOT_FOUND",
  STOCK_INSUFFICIENT: "STOCK_INSUFFICIENT",
  DUPLICATE_ORDER: "DUPLICATE_ORDER",
  DUPLICATE_EMAIL: "DUPLICATE_EMAIL",
  ORDER_NOT_CANCELLABLE: "ORDER_NOT_CANCELLABLE",
  LOCK_TIMEOUT: "LOCK_TIMEOUT"
} as const;

export type ErrorCodeType = (typeof ErrorCode)[keyof typeof ErrorCode];
