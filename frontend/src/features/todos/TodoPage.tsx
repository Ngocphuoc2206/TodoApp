import { useEffect, useMemo, useState } from "react";
import { ApiError } from "../../shared/api/http";
import { ConfirmDialog } from "./components/ConfirmDialog";
import { SummaryGrid } from "./components/SummaryGrid";
import { TodoForm, type TodoFormState } from "./components/TodoForm";
import { TodoList } from "./components/TodoList";
import { TodoPagination } from "./components/TodoPagination";
import { TodoToolbar } from "./components/TodoToolbar";
import { Toast } from "./components/Toast";
import { createTodo, deleteTodo, getTodos, toggleTodo, updateTodo } from "./todoApi";
import type { PageResponse, Todo, TodoQuery, TodoRequest } from "./types";

const initialQuery: TodoQuery = {
  keyword: "",
  status: "",
  page: 0,
  size: 8,
  sort: "createdAt,desc",
};

const emptyPage: PageResponse<Todo> = {
  content: [],
  page: 0,
  size: initialQuery.size,
  totalElements: 0,
  totalPages: 0,
  first: true,
  last: true,
};

const initialForm: TodoFormState = {
  id: null,
  title: "",
  description: "",
};

type ToastState = {
  type: "success" | "error";
  message: string;
} | null;

export function TodoPage() {
  const [query, setQuery] = useState<TodoQuery>(initialQuery);
  const [pageData, setPageData] = useState<PageResponse<Todo>>(emptyPage);
  const [form, setForm] = useState<TodoFormState>(initialForm);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [actionId, setActionId] = useState<number | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [todoToDelete, setTodoToDelete] = useState<Todo | null>(null);
  const [toast, setToast] = useState<ToastState>(null);

  const completedCount = useMemo(
    () => pageData.content.filter((todo) => todo.status === "COMPLETED").length,
    [pageData.content],
  );

  const pendingCount = pageData.content.length - completedCount;

  async function loadTodos(nextQuery = query, showSuccess = false) {
    setLoading(true);

    try {
      const data = await getTodos(nextQuery);
      setPageData(data);
      if (showSuccess) {
        setToast({ type: "success", message: "Danh sách đã được cập nhật." });
      }
    } catch (exception) {
      setToast({ type: "error", message: getErrorMessage(exception) });
      setPageData(emptyPage);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void loadTodos(query);
  }, [query]);

  function updateQuery(patch: Partial<TodoQuery>) {
    setQuery((current) => ({
      ...current,
      ...patch,
      page: patch.page ?? 0,
    }));
  }

  function resetForm() {
    setFieldErrors({});
    setForm(initialForm);
  }

  function startEdit(todo: Todo) {
    setFieldErrors({});
    setForm({
      id: todo.id,
      title: todo.title,
      description: todo.description ?? "",
    });
  }

  async function handleSubmit(payload: TodoRequest) {
    setSubmitting(true);
    setFieldErrors({});

    try {
      if (form.id === null) {
        await createTodo(payload);
        setToast({ type: "success", message: "Đã tạo công việc mới." });
      } else {
        await updateTodo(form.id, payload);
        setToast({ type: "success", message: "Đã lưu thay đổi." });
      }

      resetForm();
      await loadTodos(query);
    } catch (exception) {
      if (exception instanceof ApiError) {
        setFieldErrors(exception.errors);
      }
      setToast({ type: "error", message: getErrorMessage(exception) });
    } finally {
      setSubmitting(false);
    }
  }

  async function handleToggle(todo: Todo) {
    setActionId(todo.id);

    try {
      await toggleTodo(todo.id);
      setToast({
        type: "success",
        message: todo.status === "COMPLETED" ? "Đã chuyển về trạng thái đang làm." : "Đã hoàn thành công việc.",
      });
      await loadTodos(query);
    } catch (exception) {
      setToast({ type: "error", message: getErrorMessage(exception) });
    } finally {
      setActionId(null);
    }
  }

  async function confirmDelete() {
    if (!todoToDelete) {
      return;
    }

    setActionId(todoToDelete.id);

    try {
      await deleteTodo(todoToDelete.id);
      const nextPage = pageData.content.length === 1 && query.page > 0 ? query.page - 1 : query.page;
      const nextQuery = { ...query, page: nextPage };
      setTodoToDelete(null);
      setToast({ type: "success", message: "Đã xóa công việc." });
      setQuery(nextQuery);
    } catch (exception) {
      setToast({ type: "error", message: getErrorMessage(exception) });
    } finally {
      setActionId(null);
    }
  }

  return (
    <main className="app-shell">
      <header className="app-header">
        <div>
          <p className="eyebrow">Intern Todo List</p>
          <h1>Quản lý công việc</h1>
          <p className="header-copy">Theo dõi, lọc và cập nhật các task trong một màn hình gọn gàng.</p>
        </div>
      </header>

      <SummaryGrid completed={completedCount} pending={pendingCount} total={pageData.totalElements} />

      <section className="workspace">
        <TodoForm
          form={form}
          fieldErrors={fieldErrors}
          submitting={submitting}
          onChange={setForm}
          onCancel={resetForm}
          onSubmit={handleSubmit}
        />

        <section className="list-panel" aria-label="Danh sách công việc">
          <TodoToolbar
            query={query}
            loading={loading}
            onRefresh={() => void loadTodos(query, true)}
            onQueryChange={updateQuery}
          />

          <TodoList
            actionId={actionId}
            loading={loading}
            todos={pageData.content}
            onDelete={setTodoToDelete}
            onEdit={startEdit}
            onToggle={(todo) => void handleToggle(todo)}
          />

          <TodoPagination loading={loading} pageData={pageData} onPageChange={(page) => updateQuery({ page })} />
        </section>
      </section>

      <ConfirmDialog
        open={todoToDelete !== null}
        busy={actionId === todoToDelete?.id}
        title="Xóa công việc?"
        description={todoToDelete ? `Công việc "${todoToDelete.title}" sẽ bị xóa khỏi danh sách.` : ""}
        onCancel={() => setTodoToDelete(null)}
        onConfirm={() => void confirmDelete()}
      />

      <Toast toast={toast} onClose={() => setToast(null)} />
    </main>
  );
}

function getErrorMessage(exception: unknown) {
  if (exception instanceof ApiError) {
    return exception.message;
  }

  if (exception instanceof Error) {
    return exception.message;
  }

  return "Đã có lỗi xảy ra. Vui lòng thử lại.";
}
