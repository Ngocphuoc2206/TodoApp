export type TodoStatus = "PENDING" | "COMPLETED";

export type Todo = {
  id: number;
  title: string;
  description: string | null;
  status: TodoStatus;
  createdAt: string;
  updatedAt: string;
};

export type TodoRequest = {
  title: string;
  description: string | null;
};

export type PageResponse<T> = {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
};

export type TodoQuery = {
  keyword: string;
  status: "" | TodoStatus;
  page: number;
  size: number;
  sort: string;
};
