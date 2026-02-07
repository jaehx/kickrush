import Link from "next/link";
import { Card, CardBody } from "@/components/ui/Card";
import { formatCurrency } from "@/lib/format";
import type { Shoe } from "@/types";

export function ShoeCard({ shoe }: { shoe: Shoe }) {
  return (
    <Card className="shoe-card">
      <CardBody>
        <div className="shoe-card-header">
          <div className="shoe-thumb">{shoe.brand.slice(0, 1)}</div>
          <div>
            <p className="meta">{shoe.brand}</p>
            <h3>{shoe.name}</h3>
          </div>
        </div>
        <p className="meta">{shoe.modelNumber}</p>
        <p className="price">{formatCurrency(shoe.price)}</p>
        <Link href={`/shoes/${shoe.id}`} className="link-button">
          View Shoe
        </Link>
      </CardBody>
    </Card>
  );
}
