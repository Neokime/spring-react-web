// src/pages/trade/TradeDetailPage.jsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import tradeService from "../../services/trade.service";
import useUserStore from "../../store/useUserStroe";

const TradeDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const currentUser = useUserStore((state) => state.user);

  const [trade, setTrade] = useState(null);
  const [requests, setRequests] = useState([]);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [reqError, setReqError] = useState("");

  const isOwner =
    currentUser && trade && Number(currentUser.id) === Number(trade.userId);

  const loadDetail = () => {
    tradeService
      .getTrade(id)
      .then((res) => setTrade(res.data))
      .catch((err) => {
        console.error(err);
        setError("교환 글 정보를 불러오는 중 오류가 발생했습니다.");
      });
  };

  const loadRequests = () => {
    tradeService
      .getTradeRequests(id)
      .then((res) => setRequests(res.data))
      .catch((err) => {
        console.error(err);
        setReqError("신청 목록을 불러오는 중 오류가 발생했습니다.");
      });
  };

  useEffect(() => {
    loadDetail();
    loadRequests();
  }, [id]);

  const handleDelete = () => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    tradeService
      .deleteTrade(id)
      .then(() => navigate("/trades"))
      .catch((err) => {
        console.error(err);
        alert("삭제 중 오류가 발생했습니다.");
      });
  };

  const handleRequestSubmit = (e) => {
    e.preventDefault();
    setReqError("");

    if (!currentUser) {
      setReqError("로그인이 필요합니다.");
      return;
    }
    if (!message.trim()) {
      setReqError("메시지를 입력해주세요.");
      return;
    }

    tradeService
      .requestTrade(id, { message })
      .then(() => {
        alert("교환 신청이 등록되었습니다.");
        setMessage("");
        loadRequests();
      })
      .catch((err) => {
        console.error(err);
        setReqError("신청 중 오류가 발생했습니다.");
      });
  };

  const handleAccept = (requestId) => {
    if (!window.confirm("이 신청을 수락하시겠습니까?")) return;

    tradeService
      .acceptTradeRequest(id, requestId)
      .then(() => {
        alert("신청을 수락했습니다.");
        loadDetail();
        loadRequests();
      })
      .catch((err) => {
        console.error(err);
        alert("수락 처리 중 오류가 발생했습니다.");
      });
  };

  if (!trade) return <div className="mt-4">Loading...</div>;

  return (
    <div className="mt-4">
      {error && <div className="alert alert-danger py-2">{error}</div>}

      <h3>재능 교환 상세</h3>
      <div className="card mt-3">
        <div className="card-body">
          <p>
            <strong>내 재능:</strong> {trade.giveTalent}
          </p>
          <p>
            <strong>원하는 재능:</strong> {trade.wantTalent}
          </p>
          <p>
            <strong>설명:</strong>
            <br />
            {trade.description}
          </p>
          <p>
            <strong>상태:</strong> {trade.status}
          </p>

          {isOwner && (
            <div className="mt-3">
              <button
                className="btn btn-danger btn-sm"
                onClick={handleDelete}
              >
                삭제하기
              </button>
            </div>
          )}
        </div>
      </div>

      {/* 신청 목록 & 수락 (작성자만) */}
      {isOwner && (
        <div className="mt-4">
          <h5>들어온 교환 신청</h5>
          {reqError && (
            <div className="alert alert-danger py-2">{reqError}</div>
          )}

          {requests.length === 0 ? (
            <p>아직 들어온 신청이 없습니다.</p>
          ) : (
            <ul className="list-group">
              {requests.map((r) => (
                <li key={r.id} className="list-group-item">
                  <div>
                    <div>
                      <strong>신청자 ID:</strong> {r.requesterId}
                    </div>
                    <div>
                      <strong>메시지:</strong> {r.message}
                    </div>
                    <div>
                      <strong>상태:</strong> {r.status}
                    </div>
                  </div>

                  {r.status === "PENDING" && trade.status === "OPEN" && (
                    <button
                      className="btn btn-success btn-sm mt-2"
                      onClick={() => handleAccept(r.id)}
                    >
                      수락하기
                    </button>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      )}

      {/* 신청 폼 (작성자가 아니고 로그인한 경우) */}
      {!isOwner && currentUser && (
        <div className="mt-4">
          <h5>교환 신청하기</h5>
          {reqError && (
            <div className="alert alert-danger py-2">{reqError}</div>
          )}

          <form onSubmit={handleRequestSubmit}>
            <div className="mb-2">
              <textarea
                className="form-control"
                rows="3"
                placeholder="본인 재능, 가능한 시간 등 자유롭게 적어주세요."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
              />
            </div>
            <button type="submit" className="btn btn-primary btn-sm">
              신청 보내기
            </button>
          </form>
        </div>
      )}

      {!currentUser && (
        <p className="mt-3 text-muted">
          교환을 신청하려면 먼저 로그인 해주세요.
        </p>
      )}
    </div>
  );
};

export default TradeDetailPage;
