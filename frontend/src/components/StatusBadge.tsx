import clsx from "@/components/ui/clsx";
import type { ReleaseStatus } from "@/types";

const labels: Record<ReleaseStatus, string> = {
  UPCOMING: "Coming Soon",
  ONGOING: "Live",
  ENDED: "Ended"
};

export function StatusBadge({ status }: { status: ReleaseStatus }) {
  return (
    <span className={clsx("status", `status-${status.toLowerCase()}`)}>
      {labels[status]}
    </span>
  );
}
