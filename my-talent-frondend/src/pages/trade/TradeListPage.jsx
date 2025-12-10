// src/pages/trade/TradeListPage.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import tradeService from "../../services/trade.service";

const TradeListPage = () => {
  const [trades, setTrades] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    tradeService
      .getTrades()
      .then((res) => setTrades(res.data))
      .catch((err) => {
        console.error(err);
        setError("교환 글 목록을 불러오는 중 오류가 발생했습니다.");
      });
  }, []);

  return (
    <div className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h3>재능 교환 게시판</h3>
        <Link to="/trades/create" className="btn btn-primary btn-sm">
          교환 글 올리기
        </Link>
      </div>

      {error && <div className="alert alert-danger py-2">{error}</div>}

      <table className="table table-striped">
        <thead>
          <tr>
            <th>#</th>
            <th>내 재능</th>
            <th>원하는 재능</th>
            <th>상태</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {trades.length === 0 ? (
            <tr>
              <td colSpan="5" className="text-center">
                등록된 교환 글이 없습니다.
              </td>
            </tr>
          ) : (
            trades.map((t, idx) => (
              <tr key={t.id}>
                <td>{idx + 1}</td>
                <td>{t.giveTalent}</td>
                <td>{t.wantTalent}</td>
                <td>{t.status}</td>
                <td>
                  <Link
                    to={`/trades/${t.id}`}
                    className="btn btn-sm btn-outline-primary"
                  >
                    상세보기
                  </Link>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default TradeListPage;
