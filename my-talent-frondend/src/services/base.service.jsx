// src/services/base.service.js
import axios from "axios";
import useUserStore from "../store/useUserStroe";
import { BASE_API_URL } from "../common/constants";

const api = axios.create({
  baseURL: BASE_API_URL + "/api",
});

api.interceptors.request.use((config) => {
  const { user } = useUserStore.getState();

  
  const token =
    user?.token ||
    user?.accessToken ||   // 백엔드 응답이 accessToken 으로 올 때
    user?.jwt ||
    user?.access_token;

  if (token) {
    if (!config.headers) config.headers = {};
    config.headers.Authorization = "Bearer " + token;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const { user, clearCurrentUser } = useUserStore.getState();

    console.log("🟡 interceptor user:", user);
   

    // 🔹 위와 동일한 기준으로 토큰 존재 여부 판단
    const token =
      user?.token ||
      user?.accessToken ||
      user?.jwt ||
      user?.access_token;

    const isLoggedIn = !!token;
    const status = error?.response?.status;

    if (isLoggedIn && status === 401) {
      clearCurrentUser();
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
