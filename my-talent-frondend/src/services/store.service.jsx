// src/services/store.service.js
import api from "./base.service";

const getItems = () => api.get("/store/items");

const createItem = (payload) => api.post("/store/items", payload);

const updateItem = (id, payload) => api.put(`/store/items/${id}`, payload);

const deleteItem = (id) => api.delete(`/store/items/${id}`);

// 구매
const purchaseItem = (id) => api.post(`/store/items/${id}/purchase`);

// 내 구매 내역
const getMyPurchases = () => api.get("/store/purchases");

const storeService = {
  getItems,
  createItem,
  updateItem,
  deleteItem,
  purchaseItem,
  getMyPurchases,
};

export default storeService;
