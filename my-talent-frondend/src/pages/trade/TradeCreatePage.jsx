// src/pages/trade/TradeCreatePage.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import tradeService from "../../services/trade.service";

const TradeCreatePage = () => {
  const [form, setForm] = useState({
    giveTalent: "",
    wantTalent: "",
    description: "",
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!form.giveTalent || !form.wantTalent) {
      setError("내 재능과 원하는 재능을 모두 입력해주세요.");
      return;
    }

    tradeService
      .createTrade(form)
      .then(() => {
        navigate("/trades");
      })
      .catch((err) => {
        console.error(err);
        setError("교환 글 등록 중 오류가 발생했습니다.");
      });
  };

  return (
    <div className="mt-4">
      <h3>재능 교환 글 올리기</h3>

      {error && <div className="alert alert-danger py-2">{error}</div>}

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">내 재능 (내가 가진 것)</label>
          <input
            type="text"
            name="giveTalent"
            className="form-control"
            value={form.giveTalent}
            onChange={handleChange}
            placeholder="예) 백엔드 스프링 강의"
          />
        </div>

        <div className="mb-3">
          <label className="form-label">원하는 재능</label>
          <input
            type="text"
            name="wantTalent"
            className="form-control"
            value={form.wantTalent}
            onChange={handleChange}
            placeholder="예) 사업/스타트업 코칭"
          />
        </div>

        <div className="mb-3">
          <label className="form-label">상세 설명</label>
          <textarea
            name="description"
            className="form-control"
            rows="4"
            value={form.description}
            onChange={handleChange}
            placeholder="교환 조건, 시간, 방식 등을 적어주세요."
          />
        </div>

        <button type="submit" className="btn btn-primary">
          등록하기
        </button>
      </form>
    </div>
  );
};

export default TradeCreatePage;
