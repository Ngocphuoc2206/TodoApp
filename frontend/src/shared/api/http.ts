export type ApiResponse<T> = {
  success: boolean;
  code: string;
  message: string;
  data: T | null;
  errors: Record<string, string>;
  timestamp: string;
};

export class ApiError extends Error {
  code: string;
  errors: Record<string, string>;
  status: number;

  constructor(message: string, code: string, errors: Record<string, string>, status: number) {
    super(message);
    this.name = "ApiError";
    this.code = code;
    this.errors = errors;
    this.status = status;
  }
}

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api/v1";

type RequestOptions = {
  method?: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
  body?: unknown;
};

export async function apiRequest<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: options.method ?? "GET",
    headers: {
      "Content-Type": "application/json",
    },
    body: options.body === undefined ? undefined : JSON.stringify(options.body),
  });

  const payload = (await response.json()) as ApiResponse<T>;

  if (!response.ok || !payload.success) {
    throw new ApiError(
      payload.message || "Request failed",
      payload.code || "UNKNOWN_ERROR",
      payload.errors ?? {},
      response.status,
    );
  }

  return payload.data as T;
}
