import api from "./base.service";

export const authService = {
  login: (email, password) => api.post("/auth/sign-in", { email, password }),
};
