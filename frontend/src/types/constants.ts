export const SHOE_SIZE = {
  MIN: 220,
  MAX: 300,
  STEP: 5
} as const;

export const AVAILABLE_SIZES = Array.from(
  { length: (SHOE_SIZE.MAX - SHOE_SIZE.MIN) / SHOE_SIZE.STEP + 1 },
  (_, i) => SHOE_SIZE.MIN + i * SHOE_SIZE.STEP
);

export const BRANDS = ["Nike", "Adidas", "New Balance", "Asics", "Converse"] as const;
export type Brand = (typeof BRANDS)[number];
