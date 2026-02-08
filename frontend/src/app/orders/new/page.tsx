import { OrderNewClient } from "@/components/OrderNewClient";
import { fetchApi } from "@/lib/api";
import type { Page, Release, ReleaseDetail } from "@/types";

interface OrderNewPageProps {
  searchParams?: Promise<{ releaseId?: string; sizeId?: string }>;
}

export default async function OrderNewPage({ searchParams }: OrderNewPageProps) {
  const resolvedSearchParams = searchParams ? await searchParams : undefined;
  const releases = await fetchApi<Page<Release>>("/releases");
  const releaseId = resolvedSearchParams?.releaseId
    ? Number(resolvedSearchParams.releaseId)
    : null;
  const sizeId = resolvedSearchParams?.sizeId ? Number(resolvedSearchParams.sizeId) : null;

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
