import { Check, Circle, ClipboardList, Loader2, Pencil, Trash2 } from "lucide-react";
import type { Todo, TodoStatus } from "../types";

type TodoListProps = {
  todos: Todo[];
  loading: boolean;
  actionId: number | null;
  onDelete: (todo: Todo) => void;
  onEdit: (todo: Todo) => void;
  onToggle: (todo: Todo) => void;
};

export function TodoList({ actionId, loading, todos, onDelete, onEdit, onToggle }: TodoListProps) {
  if (loading) {
    return (
      <div className="todo-list" aria-label="Đang tải">
        {Array.from({ length: 5 }).map((_, index) => (
          <div className="todo-skeleton" key={index}>
            <span />
            <div>
              <strong />
              <p />
            </div>
          </div>
        ))}
      </div>
    );
  }

  if (todos.length === 0) {
    return (
      <div className="todo-list">
        <div className="empty-state">
          <ClipboardList size={34} />
          <strong>Chưa có công việc phù hợp</strong>
          <span>Thử đổi bộ lọc hoặc tạo một task mới.</span>
        </div>
      </div>
    );
  }

  return (
    <div className="todo-list">
      {todos.map((todo) => (
        <article className={`todo-item ${todo.status === "COMPLETED" ? "is-done" : ""}`} key={todo.id}>
          <button
            className={`status-button ${todo.status === "COMPLETED" ? "done" : ""}`}
            type="button"
            title={todo.status === "COMPLETED" ? "Chuyển về đang làm" : "Đánh dấu hoàn thành"}
            onClick={() => onToggle(todo)}
            disabled={actionId === todo.id}
          >
            {actionId === todo.id ? (
              <Loader2 className="spin" size={18} />
            ) : todo.status === "COMPLETED" ? (
              <Check size={18} />
            ) : (
              <Circle size={18} />
            )}
          </button>

          <div className="todo-content">
            <div className="todo-title-row">
              <h3>{todo.title}</h3>
              <span className={`status-pill ${todo.status.toLowerCase()}`}>{formatStatus(todo.status)}</span>
            </div>
            {todo.description && <p>{todo.description}</p>}
            <time dateTime={todo.updatedAt}>Cập nhật {formatDate(todo.updatedAt)}</time>
          </div>

          <div className="row-actions">
            <button className="icon-button" type="button" title="Sửa" onClick={() => onEdit(todo)}>
              <Pencil size={17} />
            </button>
            <button
              className="icon-button danger"
              type="button"
              title="Xóa"
              onClick={() => onDelete(todo)}
              disabled={actionId === todo.id}
            >
              <Trash2 size={17} />
            </button>
          </div>
        </article>
      ))}
    </div>
  );
}

function formatStatus(status: TodoStatus) {
  return status === "COMPLETED" ? "Hoàn thành" : "Đang làm";
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat("vi-VN", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(value));
}
