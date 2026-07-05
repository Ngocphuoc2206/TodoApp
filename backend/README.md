# Todo List Backend

Backend API cho bài test Intern Developer: quản lý công việc, thêm/sửa/xóa, đánh dấu hoàn thành, tìm kiếm/lọc theo trạng thái, phân trang và sắp xếp.

## Công Nghệ

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Flyway
- MariaDB/MySQL

## Yêu Cầu

- Java 21
- Maven 3.9+
- MariaDB hoặc MySQL

## Cấu Hình Database

Tạo database:

```sql
CREATE DATABASE todoList CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Ứng dụng đọc cấu hình qua biến môi trường, nếu không có sẽ dùng giá trị mặc định:

```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="todoList"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="password123"
```

## Chạy Dự Án

```powershell
mvn spring-boot:run
```

API mặc định chạy tại:

```text
http://localhost:8080/api/v1/todos
```

## ApiResponse Và ErrorCode

Tất cả response thành công và thất bại đều dùng cùng một format. Field `code` là mã ổn định để frontend xử lý logic mà không phụ thuộc vào text `message`.

```json
{
  "success": true,
  "code": "SUCCESS",
  "message": "Tạo công việc thành công",
  "data": {
    "id": 1,
    "title": "Hoàn thành bài test",
    "description": "Kiểm tra backend và frontend",
    "status": "PENDING",
    "createdAt": "2026-07-05T02:00:00Z",
    "updatedAt": "2026-07-05T02:00:00Z"
  },
  "errors": {},
  "timestamp": "2026-07-05T02:00:00Z"
}
```

Response lỗi validation:

```json
{
  "success": false,
  "code": "VALIDATION_FAILED",
  "message": "Dữ liệu đầu vào không hợp lệ",
  "data": null,
  "errors": {
    "title": "Tiêu đề không được để trống"
  },
  "timestamp": "2026-07-05T02:00:00Z"
}
```

Một số `ErrorCode` chính:

- `TODO_NOT_FOUND`
- `VALIDATION_FAILED`
- `INVALID_REQUEST_PARAMETER`
- `INVALID_JSON`
- `INVALID_PAGE`
- `INVALID_SIZE`
- `INVALID_SORT_FORMAT`
- `INVALID_SORT_FIELD`
- `INVALID_SORT_DIRECTION`
- `INTERNAL_SERVER_ERROR`

## API Chính

- `GET /api/v1/todos`: danh sách todo, hỗ trợ `keyword`, `status`, `page`, `size`, `sort`
- `GET /api/v1/todos/{id}`: xem chi tiết todo
- `POST /api/v1/todos`: tạo todo
- `PUT /api/v1/todos/{id}`: cập nhật todo
- `PATCH /api/v1/todos/{id}/toggle`: đổi trạng thái hoàn thành/chưa hoàn thành
- `DELETE /api/v1/todos/{id}`: xóa todo

Ví dụ tạo todo:

```json
{
  "title": "Hoàn thành bài test",
  "description": "Kiểm tra backend và frontend"
}
```

## Chạy Test

Test dùng H2 in-memory database với profile `test`.

```powershell
mvn test
```
