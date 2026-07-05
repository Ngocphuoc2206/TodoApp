import { Loader2, Trash2, X } from "lucide-react";

type ConfirmDialogProps = {
  open: boolean;
  busy: boolean;
  title: string;
  description: string;
  onCancel: () => void;
  onConfirm: () => void;
};

export function ConfirmDialog({ busy, description, open, title, onCancel, onConfirm }: ConfirmDialogProps) {
  if (!open) {
    return null;
  }

  return (
    <div className="dialog-backdrop" role="presentation">
      <section className="dialog" role="dialog" aria-modal="true" aria-labelledby="delete-dialog-title">
        <button className="dialog-close" type="button" title="Đóng" onClick={onCancel} disabled={busy}>
          <X size={18} />
        </button>
        <div className="dialog-icon">
          <Trash2 size={22} />
        </div>
        <h2 id="delete-dialog-title">{title}</h2>
        <p>{description}</p>
        <div className="dialog-actions">
          <button className="ghost-button" type="button" onClick={onCancel} disabled={busy}>
            Hủy
          </button>
          <button className="danger-button" type="button" onClick={onConfirm} disabled={busy}>
            {busy ? <Loader2 className="spin" size={17} /> : <Trash2 size={17} />}
            Xóa
          </button>
        </div>
      </section>
    </div>
  );
}
