import { OrderNewClient } from "@/components/OrderNewClient";
import { fetchApi } from "@/lib/api";
import type { Page, Release, ReleaseDetail } from "@/types";

interface OrderNewPageProps {
  searchParams?: { releaseId?: string; sizeId?: string };
}

export default async function OrderNewPage({ searchParams }: OrderNewPageProps) {
  const releases = await fetchApi<Page<Release>>("/releases");
  const releaseId = searchParams?.releaseId ? Number(searchParams.releaseId) : null;
  const sizeId = searchParams?.sizeId ? Number(searchParams.sizeId) : null;

  let releaseDetail: ReleaseDetail | null = null;
  if (releaseId) {
    releaseDetail = await fetchApi<ReleaseDetail>(`/releases/${releaseId}`);
  }

  return (
    <OrderNewClient
      releases={releases.content}
      releaseDetail={releaseDetail}
      initialSizeId={sizeId}
    />
  );
}
