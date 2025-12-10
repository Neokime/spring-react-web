// src/services/trade.service.js
import api from "./base.service";

const getTrades = () => api.get("/trades");

const getTrade = (id) => api.get(`/trades/${id}`);

const createTrade = (payload) => api.post("/trades", payload);

const updateTrade = (id, payload) => api.put(`/trades/${id}`, payload);

const deleteTrade = (id) => api.delete(`/trades/${id}`);

// 교환 신청 관련
const getTradeRequests = (tradeId) =>
  api.get(`/trades/${tradeId}/requests`);

const requestTrade = (tradeId, payload) =>
  api.post(`/trades/${tradeId}/requests`, payload);

const acceptTradeRequest = (tradeId, requestId) =>
  api.post(`/trades/${tradeId}/requests/${requestId}/accept`);

const tradeService = {
  getTrades,
  getTrade,
  createTrade,
  updateTrade,
  deleteTrade,
  getTradeRequests,
  requestTrade,
  acceptTradeRequest,
};

export default tradeService;
