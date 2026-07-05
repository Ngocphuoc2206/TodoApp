import { RefreshCw, Search } from "lucide-react";
import type { TodoQuery, TodoStatus } from "../types";

type TodoToolbarProps = {
  query: TodoQuery;
  loading: boolean;
  onRefresh: () => void;
  onQueryChange: (patch: Partial<TodoQuery>) => void;
};

export function TodoToolbar({ query, loading, onRefresh, onQueryChange }: TodoToolbarProps) {
  return (
    <div className="filters">
      <label className="search-box">
        <Search size={17} />
        <input
          value={query.keyword}
          onChange={(event) => onQueryChange({ keyword: event.target.value })}
          placeholder="Tìm theo tiêu đề hoặc mô tả"
        />
      </label>

      <select
        value={query.status}
        onChange={(event) => onQueryChange({ status: event.target.value as "" | TodoStatus })}
        aria-label="Lọc theo trạng thái"
      >
        <option value="">Tất cả</option>
        <option value="PENDING">Đang làm</option>
        <option value="COMPLETED">Hoàn thành</option>
      </select>

      <select value={query.sort} onChange={(event) => onQueryChange({ sort: event.target.value })} aria-label="Sắp xếp">
        <option value="createdAt,desc">Mới nhất</option>
        <option value="createdAt,asc">Cũ nhất</option>
        <option value="title,asc">Tên A-Z</option>
        <option value="title,desc">Tên Z-A</option>
        <option value="status,asc">Trạng thái</option>
      </select>

      <button className="icon-button" type="button" title="Tải lại" disabled={loading} onClick={onRefresh}>
        <RefreshCw className={loading ? "spin" : undefined} size={18} />
      </button>
    </div>
  );
}
