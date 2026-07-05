# Chạy dự án bằng Docker

Project đã có Docker cho đủ 3 phần:

- `db`: MariaDB 11.4
- `backend`: Spring Boot chạy cổng `8080`
- `frontend`: React build production chạy qua Nginx cổng `3000`

## Chạy toàn bộ app

Tại thư mục gốc project:

```powershell
docker compose up --build
```

Mở giao diện:

```text
http://localhost:3000
```

Backend API vẫn được expose nếu cần test trực tiếp:

```text
http://localhost:8080/api/v1/todos
```

Khi chạy bằng Docker, frontend gọi API qua Nginx proxy:

```text
/api/v1 -> backend:8080/api/v1
```

## Dừng app

```powershell
docker compose down
```

Nếu muốn xóa luôn dữ liệu database:

```powershell
docker compose down -v
```

## Build riêng từng service

Backend:

```powershell
docker build -t intern-todo-backend ./backend
```

Frontend:

```powershell
docker build -t intern-todo-frontend ./frontend
```
