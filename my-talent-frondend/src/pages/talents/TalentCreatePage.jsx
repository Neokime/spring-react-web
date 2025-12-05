// src/pages/talents/TalentCreatePage.jsx
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import talentService from "../../services/talent.service";
import useUserStore from "../../store/useUserStroe";
import "./talent.css";


const TalentCreatePage = () => {
  const navigate = useNavigate();
  const user = useUserStore((state) => state.user);

  // 로그인 안 한 사용자 접근 방지
  useEffect(() => {
    if (!user?.id) {
      alert("로그인이 필요합니다.");
      navigate("/login");
    }
  }, [user]);

  // 입력 폼 상태
  const [form, setForm] = useState({
    title: "",
    category: "",
    description: "",
    creditPerHour: "",
    status: "OPEN",
  });

  // 입력 변화 반영
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  // 저장 버튼
  const handleSubmit = (e) => {
    e.preventDefault();

    talentService
      .createTalent(form)
      .then(() => {
        alert("재능 등록 완료!");
        navigate("/talents");
      })
      .catch((err) => {
        console.log(err);
        alert("등록 실패. 입력 내용을 다시 확인해주세요.");
      });
  };

  return (
    <div className="container mt-4" style={{ maxWidth: "700px" }}>
      <h2 className="mb-4">재능 등록</h2>

      <form onSubmit={handleSubmit}>

        {/* 제목 */}
        <div className="mb-3">
          <label className="form-label">제목</label>
          <input
            type="text"
            className="form-control"
            name="title"
            value={form.title}
            onChange={handleChange}
            required
          />
        </div>

        {/* 카테고리 */}
        <div className="mb-3">
          <label className="form-label">카테고리</label>
          <select
            className="form-select"
            name="category"
            value={form.category}
            onChange={handleChange}
            required
          >
            <option value="">선택하세요</option>
            <option value="IT">IT / 개발</option>
            <option value="Design">디자인</option>
            <option value="Music">음악</option>
            <option value="Study">공부</option>
            <option value="Life">생활 스킬</option>
          </select>
        </div>

        {/* 설명 */}
        <div className="mb-3">
          <label className="form-label">설명</label>
          <textarea
            className="form-control"
            name="description"
            rows="4"
            value={form.description}
            onChange={handleChange}
            required
          ></textarea>
        </div>

        {/* 시간당 크레딧 */}
        <div className="mb-3">
          <label className="form-label">시간당 비용 (크레딧)</label>
          <input
            type="number"
            className="form-control"
            name="creditPerHour"
            value={form.creditPerHour}
            onChange={handleChange}
            required
          />
        </div>

        {/* 상태 */}
        <div className="mb-3">
          <label className="form-label">상태</label>
          <select
            className="form-select"
            name="status"
            value={form.status}
            onChange={handleChange}
            required
          >
            <option value="OPEN">OPEN</option>
            <option value="CLOSED">CLOSED</option>
          </select>
        </div>

        {/* 등록 버튼 */}
        <button type="submit" className="btn btn-primary w-100 mt-3">
          등록하기
        </button>
      </form>
    </div>
  );
};

export default TalentCreatePage;
