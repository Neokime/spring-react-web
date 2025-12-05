// src/services/auth.service.js
import api from "./base.service";

const loginService = (user) => {
  return api.post("/auth/sign-in", user);   
};

const registerService = (user) => {
  return api.post("/auth/sign-up", user);
};

export { loginService, registerService };
