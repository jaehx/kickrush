import type { Shoe, ShoeDetail } from "./shoe";

export type ReleaseStatus = "UPCOMING" | "ONGOING" | "ENDED";

export interface ReleaseSize {
  id: number;
  size: number;
  stock: number;
  price: number;
}

export interface Release {
  id: number;
  shoe: Pick<Shoe, "id" | "name" | "brand" | "modelNumber"> & {
    imageUrl: string;
  };
  releaseDateTime: string;
  endDateTime: string;
  status: ReleaseStatus;
  totalStock: number;
}

export interface ReleaseDetail {
  id: number;
  shoe: ShoeDetail;
  releaseDateTime: string;
  endDateTime: string;
  status: ReleaseStatus;
  totalStock: number;
  sizes: ReleaseSize[];
}

export interface ReleaseListParams {
  status?: ReleaseStatus;
  page?: number;
  size?: number;
}
