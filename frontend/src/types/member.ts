export type MemberRole = "USER" | "ADMIN";

export interface Member {
  id: number;
  email: string;
  name: string;
  role: MemberRole;
}

export interface RegisterRequest {
  email: string;
  password: string;
  name: string;
}

export interface RegisterResponse {
  id: number;
  email: string;
  name: string;
}
