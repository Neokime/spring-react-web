// src/pages/index/IndexPage.jsx
import { Link } from "react-router-dom";
import "./indexPage.css";

const IndexPage = () => {
  return (
    <div className="index-page">
      {/* HERO */}
      <section className="hero">
        <h1>MY TALENT</h1>
        <p>재능을 나누고, 크레딧으로 교환하세요</p>

        <div className="hero-buttons">
          <Link to="/talents" className="btn btn-dark">
            재능 둘러보기
          </Link>
          <Link to="/talents/create" className="btn btn-outline-dark">
            재능 등록하기
          </Link>
        </div>
      </section>

      {/* FEATURES */}
      <section className="features">
        <div className="feature-card">
          <h4>🎯 재능 공유</h4>
          <p>내가 잘하는 것을 다른 사람과 나눠요</p>
        </div>

        <div className="feature-card">
          <h4>💳 크레딧 시스템</h4>
          <p>안전한 크레딧 기반 거래</p>
        </div>

        <div className="feature-card">
          <h4>🔄 재능 교환</h4>
          <p>재능 ↔ 재능도 가능해요</p>
        </div>
      </section>
    </div>
  );
};

export default IndexPage;
