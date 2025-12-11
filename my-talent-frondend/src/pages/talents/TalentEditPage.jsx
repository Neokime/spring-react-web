// src/pages/talents/TalentEditPage.jsx
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import talentService from "../../services/talent.service";
import useUserStore from "../../store/useUserStroe";
import "./talentForm.css";



const TalentEditPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const user = useUserStore((state) => state.user);

  const [form, setForm] = useState({
    title: "",
    category: "",
    description: "",
    creditPerHour: "",
    status: "OPEN",
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 1) 기존 데이터 불러오기
    talentService
      .getTalent(id)
      .then((res) => {
        const data = res.data;

        // 권한 체크: 작성자 아니면 막기
        if (user?.id && user.id !== data.userId) {
          alert("수정 권한이 없습니다.");
          navigate("/talents");
          return;
        }

        setForm({
          title: data.title,
          category: data.category,
          description: data.description,
          creditPerHour: data.creditPerHour,
          status: data.status,
        });
      })
      .catch((err) => {
        console.log(err);
        alert("데이터를 불러올 수 없습니다.");
        navigate("/talents");
      })
      .finally(() => setLoading(false));
  }, [id, user, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    talentService
      .updateTalent(id, form)
      .then(() => {
        alert("수정 완료!");
        navigate(`/talents/${id}`); // 수정 후 상세 페이지로 이동
      })
      .catch((err) => {
        console.log(err);
        alert("수정 실패. 입력 내용을 확인해주세요.");
      });
  };

  if (loading) return <div className="container mt-4">로딩 중...</div>;

  return (
    <div className="container talent-form-page">
      <h2 className="mb-4">재능 수정</h2>

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

        <button type="submit" className="btn btn-primary w-100 mt-3">
          수정하기
        </button>
      </form>
    </div>
  );
};

export default TalentEditPage;
