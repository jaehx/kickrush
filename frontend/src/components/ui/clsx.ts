export default function clsx(...values: Array<string | undefined | false | null>) {
  return values.filter(Boolean).join(" ");
}
