// src/services/talentRequest.service.js
import api from "./base.service";

const TALENT_API = "/talents";

class TalentRequestService {
  // 재능 신청 생성
  createRequest(talentId, { userId, message, hours }) {
    return api.post(`${TALENT_API}/${talentId}/requests`, {
      userId,
      message,
      hours,
    });
  }

  // 내 재능에 들어온 신청 목록
  getRequestsForTalent(talentId) {
    return api.get(`${TALENT_API}/${talentId}/requests`);
  }

  // 신청 수락
  acceptRequest(talentId, requestId) {
    return api.post(`${TALENT_API}/${talentId}/requests/${requestId}/accept`);
  }
}

const talentRequestService = new TalentRequestService();
export default talentRequestService;
