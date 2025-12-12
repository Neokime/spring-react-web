// src/services/base.service.js
import axios from "axios";
import useUserStore from "../store/useUserStroe";
import { BASE_API_URL } from "../common/constants";

const api = axios.create({
  baseURL: BASE_API_URL + "/api",
});

// ---------------------
// 요청 인터셉터
// ---------------------
api.interceptors.request.use((config) => {
  const { user } = useUserStore.getState();

  const token =
    user?.token ||
    user?.accessToken ||
    user?.jwt ||
    user?.access_token;

  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = "Bearer " + token;
  }

  return config;
});

// ---------------------
// 응답 인터셉터
// ---------------------
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const store = useUserStore.getState();
    const user = store.user;
    const clearUser = store.clearUser;

    console.log("🟡 interceptor user:", user);

    const token =
      user?.token ||
      user?.accessToken ||
      user?.jwt ||
      user?.access_token;

    const isLoggedIn = !!token;
    const status = error?.response?.status;

    // 인증 실패 → 강제 로그아웃
    if (isLoggedIn && (status === 401 || status === 403)) {
      clearUser();

      try {
        localStorage.removeItem("currentUser");
      } catch (e) {}

      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

export const authHeader = () => {
  const { user } = useUserStore.getState();

  const token =
    user?.token ||
    user?.accessToken ||
    user?.jwt ||
    user?.access_token;

  if (token) {
    return { Authorization: "Bearer " + token };
  }
  return {};
};

export default api;
