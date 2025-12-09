// src/services/talentFeedback.service.js
import api from './base.service';

// 피드백 목록 조회
export const getTalentFeedbacks = (talentId) => {
  
  return api.get(`/talents/${talentId}/feedback`);
};

// 피드백 등록
export const createTalentFeedback = (talentId, feedback) => {
  return api.post(`/talents/${talentId}/feedback`, feedback);
};

export const updateTalentFeedback = (talentId, feedbackId, data) => {
  return api.put(`/talents/${talentId}/feedback/${feedbackId}`, data);
};

export const deleteTalentFeedback = (talentId, feedbackId) => {
  return api.delete(`/talents/${talentId}/feedback/${feedbackId}`);
};

