"use client";

import { useState } from "react";
import clsx from "@/components/ui/clsx";
import type { ReleaseSize } from "@/types";

interface SizeGridProps {
  sizes: ReleaseSize[];
  onSelect?: (size: ReleaseSize) => void;
}

export function SizeGrid({ sizes, onSelect }: SizeGridProps) {
  const [activeId, setActiveId] = useState<number | null>(null);

  return (
    <div className="size-grid">
      {sizes.map((size) => {
        const isDisabled = size.stock <= 0;
        const isActive = activeId === size.id;
        return (
          <button
            key={size.id}
            type="button"
            className={clsx(
              "size-item",
              isActive && "size-item-active",
              isDisabled && "size-item-disabled"
            )}
            onClick={() => {
              if (isDisabled) return;
              setActiveId(size.id);
              onSelect?.(size);
            }}
            disabled={isDisabled}
          >
            <span>{size.size}</span>
            <small>{isDisabled ? "Sold out" : `${size.stock} left`}</small>
          </button>
        );
      })}
    </div>
  );
}
