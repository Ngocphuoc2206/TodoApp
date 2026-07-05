import { apiRequest } from "../../shared/api/http";
import type { PageResponse, Todo, TodoQuery, TodoRequest } from "./types";

export function getTodos(query: TodoQuery) {
  const params = new URLSearchParams({
    page: String(query.page),
    size: String(query.size),
    sort: query.sort,
  });

  if (query.keyword.trim()) {
    params.set("keyword", query.keyword.trim());
  }

  if (query.status) {
    params.set("status", query.status);
  }

  return apiRequest<PageResponse<Todo>>(`/todos?${params.toString()}`);
}

export function createTodo(payload: TodoRequest) {
  return apiRequest<Todo>("/todos", {
    method: "POST",
    body: payload,
  });
}

export function updateTodo(id: number, payload: TodoRequest) {
  return apiRequest<Todo>(`/todos/${id}`, {
    method: "PUT",
    body: payload,
  });
}

export function toggleTodo(id: number) {
  return apiRequest<Todo>(`/todos/${id}/toggle`, {
    method: "PATCH",
  });
}

export function deleteTodo(id: number) {
  return apiRequest<void>(`/todos/${id}`, {
    method: "DELETE",
  });
}
