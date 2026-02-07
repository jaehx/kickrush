import type { InputHTMLAttributes } from "react";
import clsx from "./clsx";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  helperText?: string;
  error?: string;
}

export function Input({ label, helperText, error, className, ...props }: InputProps) {
  return (
    <label className="field">
      {label ? <span className="field-label">{label}</span> : null}
      <input className={clsx("input", error && "input-error", className)} {...props} />
      {error ? <span className="field-error">{error}</span> : null}
      {!error && helperText ? <span className="field-help">{helperText}</span> : null}
    </label>
  );
}
