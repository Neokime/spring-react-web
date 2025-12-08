// src/pages/talents/TalentDetail.jsx
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import talentService from "../../services/talent.service";
import useUserStore from "../../store/useUserStroe";
import { Modal } from "react-bootstrap";
import "./talent.css";
import {
  getTalentFeedbacks,
  createTalentFeedback,
} from "../../services/talentFeedback.service";
import talentRequestService from "../../services/talentRequest.service";

const TalentDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [data, setData] = useState(null);

  // 삭제 모달
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const user = useUserStore((state) => state.user);

  // 피드백 관련 상태
  const [feedbacks, setFeedbacks] = useState([]);
  const [rating, setRating] = useState(5);
  const [content, setContent] = useState("");
  const [feedbackError, setFeedbackError] = useState("");

  // 재능 신청 관련 상태 (신청하기 모달)
  const [showRequestModal, setShowRequestModal] = useState(false);
  const [requestHours, setRequestHours] = useState(1);
  const [requestMessage, setRequestMessage] = useState("");
  const [requestError, setRequestError] = useState("");

  // 내 재능에 들어온 신청 목록
  const [requests, setRequests] = useState([]);
  const [requestListError, setRequestListError] = useState("");

  // userId가 data.userId 또는 data.user.id 둘 중 하나일 수 있으니 둘 다 지원
  const ownerId = data?.userId ?? data?.user?.id;
  const isOwner = user && ownerId && Number(user.id) === Number(ownerId);

  useEffect(() => {
    // 재능 상세
    talentService
      .getTalent(id)
      .then((res) => setData(res.data))
      .catch((err) => console.log(err));

    // 피드백 목록
    loadFeedbacks(id);
  }, [id]);

  // data + isOwner 가 결정된 뒤에 신청 목록 로드
  useEffect(() => {
    if (data && isOwner) {
      loadRequests(id);
    }
  }, [data, isOwner, id]);

  const loadFeedbacks = (talentId) => {
    getTalentFeedbacks(talentId)
      .then((res) => setFeedbacks(res.data))
      .catch((err) => console.log(err));
  };

  const loadRequests = (talentId) => {
    setRequestListError("");
    talentRequestService
      .getRequestsForTalent(talentId)
      .then((res) => setRequests(res.data))
      .catch((err) => {
        console.log(err);
        setRequestListError("신청 목록을 불러오는 중 오류가 발생했습니다.");
      });
  };

  const handleDelete = () => {
    talentService
      .deleteTalent(id)
      .then(() => {
        alert("삭제 완료");
        navigate("/talents");
      })
      .catch((err) => {
        console.log(err);
        alert("삭제 실패");
      });
  };

  const handleFeedbackSubmit = async (e) => {
    e.preventDefault();
    setFeedbackError("");

    if (!user) {
      setFeedbackError("로그인 후 이용 가능합니다.");
      return;
    }

    if (!content.trim()) {
      setFeedbackError("내용을 입력해주세요.");
      return;
    }

    try {
      await createTalentFeedback(id, {
        userId: user.id,
        rating,
        content,
      });
      setContent("");
      setRating(5);
      loadFeedbacks(id);
    } catch (error) {
      console.error(error);
      setFeedbackError("피드백 등록 중 오류가 발생했습니다.");
    }
  };

  // 🔹 재능 신청 제출
  const handleRequestSubmit = async (e) => {
    e.preventDefault();
    setRequestError("");

    if (!user) {
      setRequestError("로그인 후 이용 가능합니다.");
      return;
    }

    if (!requestHours || requestHours <= 0) {
      setRequestError("신청 시간(hours)은 1 이상이어야 합니다.");
      return;
    }

    try {
      await talentRequestService.createRequest(id, {
        userId: user.id, // DTO로 같이 전송
        message: requestMessage,
        hours: requestHours,
      });

      alert("재능 신청이 완료되었습니다.");
      setShowRequestModal(false);
      setRequestMessage("");
      setRequestHours(1);
    } catch (error) {
      console.error(error);
      setRequestError("재능 신청 중 오류가 발생했습니다.");
    }
  };

  // 🔹 신청 수락
  const handleAccept = async (requestId) => {
    try {
      await talentRequestService.acceptRequest(id, requestId);
      alert("신청을 수락했습니다.");
      loadRequests(id);
    } catch (error) {
      console.error(error);
      alert("신청 수락 중 오류가 발생했습니다.");
    }
  };

  if (!data) return <div>Loading...</div>;

  return (
    <div className="container mt-4">
      <h1>재능 상세 페이지</h1>

      <h2>{data.title}</h2>

      <p>카테고리: {data.category}</p>
      <p>설명: {data.description}</p>
      <p>크레딧: {data.creditPerHour}</p>
      <p>상태: {data.status}</p>

      {/* 소유자일 때: 수정/삭제 버튼 */}
      {isOwner && (
        <div className="mt-3">
          <button
            className="btn btn-warning me-2"
            onClick={() => navigate(`/talents/${id}/edit`)}
          >
            수정
          </button>

          <button
            className="btn btn-danger"
            onClick={() => setShowDeleteModal(true)}
          >
            삭제
          </button>
        </div>
      )}

      {/* 소유자가 아니고 로그인한 경우: 재능 신청 버튼 */}
      {!isOwner && user && (
        <div className="mt-3">
          <button
            className="btn btn-success"
            onClick={() => setShowRequestModal(true)}
          >
            재능 신청하기
          </button>
        </div>
      )}

      {/* 🔻 (소유자용) 들어온 재능 신청 목록 */}
      {isOwner && (
        <>
          <hr className="my-4" />
          <h3>들어온 재능 신청</h3>

          {requestListError && (
            <div className="alert alert-danger py-1">{requestListError}</div>
          )}

          {requests.length === 0 ? (
            <p>아직 들어온 신청이 없습니다.</p>
          ) : (
            <ul className="list-group mb-3">
              {requests.map((req) => (
                <li
                  key={req.id}
                  className="list-group-item d-flex justify-content-between align-items-start"
                >
                  <div>
                    <div>
                      <strong>신청자 ID:</strong> {req.requesterId}
                    </div>
                    <div>
                      <strong>시간:</strong> {req.hours}시간 /{" "}
                      <strong>총 크레딧:</strong> {req.totalCredits}
                    </div>
                    {req.message && <div>메시지: {req.message}</div>}
                    <div>상태: {req.status}</div>
                  </div>

                  {req.status === "PENDING" && (
                    <button
                      className="btn btn-sm btn-success ms-3"
                      onClick={() => handleAccept(req.id)}
                    >
                      수락
                    </button>
                  )}
                </li>
              ))}
            </ul>
          )}
        </>
      )}

      {/* 피드백 영역 */}
      <hr className="my-4" />
      <h3>피드백</h3>

      {/* 피드백 리스트 */}
      {feedbacks.length === 0 ? (
        <p>아직 등록된 피드백이 없습니다.</p>
      ) : (
        <ul className="list-group mb-3">
          {feedbacks.map((fb) => (
            <li key={fb.id} className="list-group-item">
              <div>
                <strong>{fb.nickname}</strong> ({fb.rating}점)
              </div>
              <div>{fb.content}</div>
              {fb.createdAt && (
                <small className="text-muted">작성일: {fb.createdAt}</small>
              )}
            </li>
          ))}
        </ul>
      )}

      {/* 피드백 작성 폼 */}
      {user ? (
        <form onSubmit={handleFeedbackSubmit} className="mb-4">
          {feedbackError && (
            <div className="alert alert-danger py-1">{feedbackError}</div>
          )}
          <div className="mb-2">
            <label className="form-label">평점 (1~5)</label>
            <input
              type="number"
              min="1"
              max="5"
              className="form-control"
              value={rating}
              onChange={(e) => setRating(Number(e.target.value))}
            />
          </div>
          <div className="mb-2">
            <label className="form-label">피드백 내용</label>
            <textarea
              className="form-control"
              rows="3"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
          </div>
          <button type="submit" className="btn btn-primary">
            피드백 남기기
          </button>
        </form>
      ) : (
        <p className="text-muted">피드백을 남기려면 로그인해주세요.</p>
      )}

      {/* 삭제 모달 */}
      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>삭제 확인</Modal.Title>
        </Modal.Header>

        <Modal.Body>정말 삭제하시겠습니까?</Modal.Body>

        <Modal.Footer>
          <button
            className="btn btn-secondary"
            onClick={() => setShowDeleteModal(false)}
          >
            취소
          </button>
          <button className="btn btn-danger" onClick={handleDelete}>
            삭제
          </button>
        </Modal.Footer>
      </Modal>

      {/* 재능 신청 모달 */}
      <Modal show={showRequestModal} onHide={() => setShowRequestModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>재능 신청하기</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          {requestError && (
            <div className="alert alert-danger py-1">{requestError}</div>
          )}

          <form onSubmit={handleRequestSubmit}>
            <div className="mb-2">
              <label className="form-label">신청 시간(시간 단위)</label>
              <input
                type="number"
                min="1"
                className="form-control"
                value={requestHours}
                onChange={(e) => setRequestHours(Number(e.target.value))}
              />
            </div>

            <div className="mb-2">
              <label className="form-label">메시지 (선택)</label>
              <textarea
                className="form-control"
                rows="3"
                value={requestMessage}
                onChange={(e) => setRequestMessage(e.target.value)}
              />
            </div>

            <button type="submit" className="btn btn-primary mt-2">
              신청하기
            </button>
          </form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default TalentDetailPage;
