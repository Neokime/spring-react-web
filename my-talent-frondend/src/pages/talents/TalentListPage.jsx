// src/pages/talents/TalentListPage.jsx
import React, { useEffect, useState } from "react";
import talentService from "../../services/talent.service";
import useUserStore from "../../store/useUserStroe";
import { Link } from "react-router-dom";
import "./talent.css";
import { BASE_API_URL } from "../../common/constants";

const TalentListPage = () => {
  const [pageData, setPageData] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const currentUser = useUserStore((state) => state.user);

  const loadPage = (page = 1) => {
    setLoading(true);
    talentService
      .getTalentList(page, 10)
      .then((res) => {
        setPageData(res.data);
        setErrorMessage("");
      })
      .catch(() => {
        setErrorMessage("재능 목록을 불러오는데 실패했습니다.");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    loadPage(1);
  }, []);

  if (loading && !pageData) {
    return (
      <div className="container mt-4">
        <div>로딩 중...</div>
      </div>
    );
  }

  return (
    <div className="container mt-4 talent-page">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>재능 목록</h2>

        <Link to="/talents/create" className="btn btn-primary">
          재능 등록
        </Link>

        {currentUser && (
          <div className="text-muted">
            안녕하세요, <strong>{currentUser.nickname || currentUser.email}</strong> 님
          </div>
        )}
      </div>

      {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

      {!pageData || pageData.dtoList.length === 0 ? (
        <div className="alert alert-info">등록된 재능이 없습니다.</div>
      ) : (
        <>
          {/* 카드 리스트 */}
          <div className="talent-grid">
            {pageData.dtoList.map((talent) => (
              <Link
                key={talent.id}
                to={`/talents/${talent.id}`}
                className="talent-card text-decoration-none text-dark"
              >
                {talent.thumbnailUrl && (
                  <img
                    src={`${BASE_API_URL}${talent.thumbnailUrl}`}
                    alt={talent.title}
                  />
                )}

                <div className="talent-card-body">
                  <h5 className="talent-card-title">{talent.title}</h5>
                  <h6 className="talent-card-subtitle">
                    {talent.category} · {talent.creditPerHour} 크레딧
                  </h6>
                  <p className="talent-card-desc">{talent.description}</p>
                  <span className="talent-card-status">상태: {talent.status}</span>
                </div>
              </Link>
            ))}
          </div>

          {/* 페이지네이션 */}
          <nav aria-label="Talent pagination">
            <ul className="pagination justify-content-center mt-3">
              {pageData.prev && (
                <li className="page-item">
                  <button
                    className="page-link"
                    onClick={() => loadPage(pageData.start - 1)}
                  >
                    이전
                  </button>
                </li>
              )}

              {Array.from({ length: pageData.end - pageData.start + 1 }, (_, i) => {
                const p = pageData.start + i;
                return (
                  <li
                    key={p}
                    className={`page-item ${p === pageData.page ? "active" : ""}`}
                  >
                    <button className="page-link" onClick={() => loadPage(p)}>
                      {p}
                    </button>
                  </li>
                );
              })}

              {pageData.next && (
                <li className="page-item">
                  <button
                    className="page-link"
                    onClick={() => loadPage(pageData.end + 1)}
                  >
                    다음
                  </button>
                </li>
              )}
            </ul>
          </nav>
        </>
      )}
    </div>
  );
};

export default TalentListPage;
