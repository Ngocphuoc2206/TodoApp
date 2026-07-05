import { ChevronLeft, ChevronRight } from "lucide-react";
import type { PageResponse, Todo } from "../types";

type TodoPaginationProps = {
  pageData: PageResponse<Todo>;
  loading: boolean;
  onPageChange: (page: number) => void;
};

export function TodoPagination({ pageData, loading, onPageChange }: TodoPaginationProps) {
  return (
    <footer className="pagination">
      <span>
        Trang {pageData.totalPages === 0 ? 0 : pageData.page + 1} / {pageData.totalPages}
      </span>
      <div>
        <button
          className="icon-button"
          type="button"
          title="Trang trước"
          disabled={loading || pageData.first}
          onClick={() => onPageChange(Math.max(pageData.page - 1, 0))}
        >
          <ChevronLeft size={18} />
        </button>
        <button
          className="icon-button"
          type="button"
          title="Trang sau"
          disabled={loading || pageData.last}
          onClick={() => onPageChange(pageData.page + 1)}
        >
          <ChevronRight size={18} />
        </button>
      </div>
    </footer>
  );
}
