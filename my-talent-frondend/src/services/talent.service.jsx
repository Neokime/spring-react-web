// src/services/talent.service.js
import api from "./base.service";
import { authHeader } from "./base.service";

const TALENT_API = "/talents";

class TalentService {
  // 재능 등록
  createTalent(talent) {
    return api.post(TALENT_API, talent, { headers: authHeader() });
  }

  // 재능 상세 조회
  getTalent(id) {
    return api.get(`${TALENT_API}/${id}`);
  }

  // 재능 목록 조회 (pageRequestDTO 기반)
  getTalentList(page = 1, size = 10) {
  return api.get(`${TALENT_API}?page=${page}&size=${size}`, {
    headers: authHeader(),
  });
}


  // 재능 수정
  updateTalent(id, talent) {
    return api.put(`${TALENT_API}/${id}`, talent, { headers: authHeader() });
  }

  // 재능 삭제
  deleteTalent(id) {
    return api.delete(`${TALENT_API}/${id}`, { headers: authHeader() });
  }
}

const talentService = new TalentService();
export default talentService;
