// src/services/store.service.js
import api from "./base.service";

const getItems = () => api.get("/store/items");

const createItem = (payload) => api.post("/store/items", payload);

const updateItem = (id, payload) => api.put(`/store/items/${id}`, payload);

const deleteItem = (id) => api.delete(`/store/items/${id}`);


const purchaseItem = (id) => api.post(`/store/items/${id}/purchase`);


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
