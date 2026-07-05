# Todo Manager Frontend

React + TypeScript frontend for the Todo List backend.

## Structure

- `src/app`: application shell
- `src/shared/api`: shared HTTP client and `ApiResponse` handling
- `src/features/todos`: todo feature API, types, and UI page
- `src/styles.css`: global responsive styling

## Run

Install dependencies:

```powershell
npm.cmd install --cache .npm-cache
```

Start the dev server:

```powershell
npm.cmd run dev
```

Open:

```text
http://127.0.0.1:5173
```

The frontend expects the backend at:

```text
http://localhost:8080/api/v1
```

To override it, create `.env` from `.env.example` and update `VITE_API_BASE_URL`.

## Build

```powershell
npm.cmd run build
```

## Docker

Frontend production chạy bằng Nginx và proxy `/api` sang backend trong Docker Compose.

```powershell
docker compose up --build
```

Mở:

```text
http://localhost:3000
```
