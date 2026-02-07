export interface Shoe {
  id: number;
  name: string;
  brand: string;
  modelNumber: string;
  price: number;
}

export interface ShoeDetail extends Shoe {
  description: string;
  imageUrl: string;
}

export interface ShoeListParams {
  brand?: string;
  page?: number;
  size?: number;
}
