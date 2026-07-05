import { CheckCircle2, CircleDot, ListTodo } from "lucide-react";
import type { ReactNode } from "react";

type SummaryGridProps = {
  total: number;
  completed: number;
  pending: number;
};

export function SummaryGrid({ total, completed, pending }: SummaryGridProps) {
  return (
    <section className="summary-grid" aria-label="Tổng quan công việc">
      <SummaryTile icon={<ListTodo size={20} />} label="Tổng task" value={total} tone="neutral" />
      <SummaryTile icon={<CheckCircle2 size={20} />} label="Hoàn thành trang này" value={completed} tone="success" />
      <SummaryTile icon={<CircleDot size={20} />} label="Đang làm trang này" value={pending} tone="warning" />
    </section>
  );
}

function SummaryTile({
  icon,
  label,
  value,
  tone,
}: {
  icon: ReactNode;
  label: string;
  value: number;
  tone: "neutral" | "success" | "warning";
}) {
  return (
    <div className={`summary-tile ${tone}`}>
      <div className="summary-icon">{icon}</div>
      <span>{label}</span>
      <strong>{value}</strong>
    </div>
  );
}
