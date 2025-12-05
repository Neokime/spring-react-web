// src/services/base.service.js
import axios from "axios";
import useUserStore from "../store/useUserStroe";
import { BASE_API_URL } from "../common/constants";

const api = axios.create({
  baseURL: BASE_API_URL + "/api", // ✅ 여기서 /api까지
});

export const authHeader = () => {
  const { user } = useUserStore.getState();

  const headers = {
    "Content-Type": "application/json",
  };

  if (user?.token) {
    headers.Authorization = "Bearer " + user.token;
  }

  return headers;
};

const handleResponseWithLoginCheck = () => {
  api.interceptors.response.use(
    (response) => response,
    (error) => {
      const { user, clearCurrentUser } = useUserStore.getState();
      const isLoggedIn = user?.token;
      const status = error?.response?.status;

      if (isLoggedIn && [401, 403].includes(status)) {
        clearCurrentUser();
        window.location.href = "/login";
      }

      return Promise.reject(error);
    }
  );
};

handleResponseWithLoginCheck();

export default api;
