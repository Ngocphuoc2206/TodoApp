import { FormEvent } from "react";
import { Loader2, Plus, Save, X } from "lucide-react";
import type { TodoRequest } from "../types";

export type TodoFormState = {
  id: number | null;
  title: string;
  description: string;
};

type TodoFormProps = {
  form: TodoFormState;
  fieldErrors: Record<string, string>;
  submitting: boolean;
  onCancel: () => void;
  onChange: (nextForm: TodoFormState) => void;
  onSubmit: (payload: TodoRequest) => Promise<void>;
};

export function TodoForm({ form, fieldErrors, submitting, onCancel, onChange, onSubmit }: TodoFormProps) {
  const isEditing = form.id !== null;

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    await onSubmit({
      title: form.title.trim(),
      description: form.description.trim() ? form.description.trim() : null,
    });
  }

  return (
    <aside className="form-panel" aria-label="Form công việc">
      <div className="panel-heading">
        <div>
          <p className="section-kicker">{isEditing ? "Chỉnh sửa" : "Thêm mới"}</p>
          <h2>{isEditing ? "Cập nhật task" : "Tạo task"}</h2>
        </div>

        {isEditing && (
          <button className="ghost-button" type="button" onClick={onCancel}>
            <X size={16} />
            Hủy
          </button>
        )}
      </div>

      <form className="todo-form" onSubmit={handleSubmit}>
        <label>
          <span>Tiêu đề</span>
          <input
            value={form.title}
            maxLength={150}
            onChange={(event) => onChange({ ...form, title: event.target.value })}
            placeholder="Ví dụ: Hoàn thiện UI Todo"
          />
          {fieldErrors.title && <small className="field-error">{fieldErrors.title}</small>}
        </label>

        <label>
          <span>Mô tả</span>
          <textarea
            value={form.description}
            maxLength={1000}
            rows={5}
            onChange={(event) => onChange({ ...form, description: event.target.value })}
            placeholder="Ghi chú thêm nếu cần"
          />
          {fieldErrors.description && <small className="field-error">{fieldErrors.description}</small>}
        </label>

        <button className="primary-button" type="submit" disabled={submitting}>
          {submitting ? <Loader2 className="spin" size={18} /> : isEditing ? <Save size={18} /> : <Plus size={18} />}
          {isEditing ? "Lưu thay đổi" : "Tạo công việc"}
        </button>
      </form>
    </aside>
  );
}
