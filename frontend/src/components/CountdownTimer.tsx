"use client";

import { useEffect, useMemo, useState } from "react";

interface CountdownTimerProps {
  target: string;
  label?: string;
}

const formatDuration = (ms: number) => {
  const totalSeconds = Math.max(0, Math.floor(ms / 1000));
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;

  return [hours, minutes, seconds].map((value) => String(value).padStart(2, "0")).join(":");
};

export function CountdownTimer({ target, label = "Drop opens in" }: CountdownTimerProps) {
  const targetTime = useMemo(() => new Date(target).getTime(), [target]);
  const [now, setNow] = useState(() => Date.now());

  useEffect(() => {
    const timer = setInterval(() => setNow(Date.now()), 1000);
    return () => clearInterval(timer);
  }, []);

  const remaining = targetTime - now;
  const isLive = remaining <= 0;

  return (
    <div className="countdown">
      <span className="countdown-label">{isLive ? "Drop live" : label}</span>
      <strong className="countdown-value">{isLive ? "00:00:00" : formatDuration(remaining)}</strong>
    </div>
  );
}
