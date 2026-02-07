export function Spinner({ label = "Loading" }: { label?: string }) {
  return (
    <div className="spinner" role="status" aria-live="polite">
      <span className="spinner-circle" />
      <span className="spinner-label">{label}</span>
    </div>
  );
}
