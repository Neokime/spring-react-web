import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
});

// 아직은 zustand 안 쓰니까 localStorage 기준으로만 처리
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
