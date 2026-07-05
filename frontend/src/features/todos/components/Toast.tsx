import { CheckCircle2, X, XCircle } from "lucide-react";

type ToastProps = {
  toast: {
    type: "success" | "error";
    message: string;
  } | null;
  onClose: () => void;
};

export function Toast({ toast, onClose }: ToastProps) {
  if (!toast) {
    return null;
  }

  return (
    <div className={`toast ${toast.type}`} role="status">
      {toast.type === "success" ? <CheckCircle2 size={19} /> : <XCircle size={19} />}
      <span>{toast.message}</span>
      <button type="button" title="Đóng thông báo" onClick={onClose}>
        <X size={16} />
      </button>
    </div>
  );
}
