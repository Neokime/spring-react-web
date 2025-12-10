// src/pages/talents/TalentDetail.jsx
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import talentService from "../../services/talent.service";
import useUserStore from "../../store/useUserStroe";
import { Modal } from "react-bootstrap";
import "./talent.css";
import { BASE_API_URL } from "../../common/constants"; 

import {
  getTalentFeedbacks,
  createTalentFeedback,
  updateTalentFeedback,
  deleteTalentFeedback,
} from "../../services/talentFeedback.service";

import talentRequestService from "../../services/talentRequest.service";
import uploadService from "../../services/upload.service"; // ⭐ 파일 업로드 서비스

const TalentDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [data, setData] = useState(null);
  const user = useUserStore((state) => state.user);

  // 삭제 모달
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  // ----- 피드백 상태 -----
  const [feedbacks, setFeedbacks] = useState([]);
  const [rating, setRating] = useState(5);
  const [content, setContent] = useState("");
  const [feedbackError, setFeedbackError] = useState("");
  const [editingFeedbackId, setEditingFeedbackId] = useState(null);

  // ----- 재능 신청 상태 -----
  const [showRequestModal, setShowRequestModal] = useState(false);
  const [requestHours, setRequestHours] = useState(1);
  const [requestMessage, setRequestMessage] = useState("");
  const [requestError, setRequestError] = useState("");
  const [requests, setRequests] = useState([]);
  const [requestListError, setRequestListError] = useState("");

  // ----- 썸네일 업로드 상태 -----
  const [uploading, setUploading] = useState(false);

  const ownerId = data?.userId ?? data?.user?.id;
  const isOwner = user && Number(user.id) === Number(ownerId);

  // ================= 초기 데이터 로드 =================
  useEffect(() => {
    talentService
      .getTalent(id)
      .then((res) => setData(res.data))
      .catch(console.log);

    loadFeedbacks(id);
  }, [id]);

  useEffect(() => {
    if (data && isOwner) {
      loadRequests(id);
    }
  }, [data, isOwner, id]);

  const loadFeedbacks = (talentId) => {
    getTalentFeedbacks(talentId)
      .then((res) => setFeedbacks(res.data))
      .catch(console.log);
  };

  const loadRequests = (talentId) => {
    setRequestListError("");
    talentRequestService
      .getRequestsForTalent(talentId)
      .then((res) => setRequests(res.data))
      .catch(() => setRequestListError("신청 목록을 불러오는 중 오류"));
  };

  // ================= 재능 삭제 =================
  const handleDelete = () => {
    talentService
      .deleteTalent(id)
      .then(() => navigate("/talents"))
      .catch(() => alert("삭제 실패"));
  };

  // ================= 피드백 등록 + 수정 =================
  const handleFeedbackSubmit = async (e) => {
    e.preventDefault();
    setFeedbackError("");

    if (!user) return setFeedbackError("로그인이 필요합니다.");
    if (!content.trim()) return setFeedbackError("내용을 입력하세요.");

    try {
      if (editingFeedbackId) {
        // 수정
        await updateTalentFeedback(id, editingFeedbackId, {
          content,
          rating,
        });
        setEditingFeedbackId(null);
      } else {
        // 새 피드백 작성
        await createTalentFeedback(id, {
          userId: user.id,
          rating,
          content,
        });
      }

      setContent("");
      setRating(5);
      loadFeedbacks(id);
    } catch (err) {
      console.error(err);
      setFeedbackError("피드백 처리 중 오류 발생");
    }
  };

  const handleEditFeedback = (fb) => {
    setEditingFeedbackId(fb.id);
    setContent(fb.content);
    setRating(fb.rating);
  };

  const handleDeleteFeedback = async (feedbackId) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    try {
      await deleteTalentFeedback(id, feedbackId);
      loadFeedbacks(id);
    } catch (err) {
      console.error(err);
      alert("삭제 실패");
    }
  };

  // ================= 재능 신청 =================
  const handleRequestSubmit = async (e) => {
    e.preventDefault();
    setRequestError("");

    if (!user) return setRequestError("로그인이 필요합니다.");

    try {
      await talentRequestService.createRequest(id, {
        userId: user.id,
        message: requestMessage,
        hours: requestHours,
      });

      alert("신청 완료");
      setShowRequestModal(false);
      setRequestMessage("");
      setRequestHours(1);
    } catch (err) {
      console.error(err);
      setRequestError("신청 처리 중 오류");
    }
  };

  const handleAccept = async (requestId) => {
    try {
      await talentRequestService.acceptRequest(id, requestId);
      loadRequests(id);
    } catch (err) {
      console.error(err);
      alert("수락 실패");
    }
  };

  // ================= 썸네일 변경 =================
  const handleChangeThumbnail = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    try {
      setUploading(true);

      // 1) 파일 업로드
      const formData = new FormData();
      formData.append("file", file);

      const res = await uploadService.uploadFile(formData);
      const thumbnailId = res.data.id; // 백엔드에서 id 로 내려온다고 가정

      // 2) 현재 재능 + thumbnailId 로 수정
      await talentService.updateTalent(id, {
        title: data.title,
        category: data.category,
        description: data.description,
        creditPerHour: data.creditPerHour,
        status: data.status,
        thumbnailId,
      });

      // 3) 다시 상세 조회
      const detailRes = await talentService.getTalent(id);
      setData(detailRes.data);

      alert("썸네일이 변경되었습니다.");
    } catch (err) {
      console.error(err);
      alert("썸네일 변경 중 오류가 발생했습니다.");
    } finally {
      setUploading(false);
      e.target.value = "";
    }
  };

  if (!data) return <div>Loading...</div>;

  // ================= 렌더링 =================
  return (
    <div className="container mt-4">
      <h1>재능 상세 페이지</h1>

      {/* 썸네일 이미지 */}
      {data.thumbnailUrl && (
        <div className="mb-3">
          <img
            src={`${BASE_API_URL}${data.thumbnailUrl}`}  
            alt="썸네일"
            style={{ maxWidth: "300px", borderRadius: "8px" }}
          />
        </div>
      )}


      <h2>{data.title}</h2>
      <p>카테고리: {data.category}</p>
      <p>설명: {data.description}</p>
      <p>크레딧: {data.creditPerHour}</p>
      <p>상태: {data.status}</p>

      {/* ----- 소유자만 수정/삭제/썸네일 변경 ----- */}
      {isOwner && (
        <div className="mt-3">
          <button
            className="btn btn-warning me-2"
            onClick={() => navigate(`/talents/${id}/edit`)}
          >
            수정
          </button>

          <button
            className="btn btn-danger me-2"
            onClick={() => setShowDeleteModal(true)}
          >
            삭제
          </button>

          {/* 숨겨진 파일 input */}
          <input
            type="file"
            accept="image/*"
            id="thumbnailInput"
            style={{ display: "none" }}
            onChange={handleChangeThumbnail}
          />

          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => document.getElementById("thumbnailInput").click()}
            disabled={uploading}
          >
            {uploading ? "썸네일 업로드 중..." : "썸네일 변경"}
          </button>
        </div>
      )}

      {/* ----- 재능 신청 버튼 (소유자 X + 로그인 O) ----- */}
      {!isOwner && user && (
        <div className="mt-3">
          <button
            className="btn btn-success"
            onClick={() => setShowRequestModal(true)}
          >
            신청하기
          </button>
        </div>
      )}

      {/* ----- 들어온 재능 신청 목록 (소유자만) ----- */}
      {isOwner && (
        <>
          <hr />
          <h3>들어온 재능 신청</h3>

          {requestListError && (
            <div className="alert alert-danger py-1">{requestListError}</div>
          )}

          {requests.length === 0 ? (
            <p>아직 들어온 신청이 없습니다.</p>
          ) : (
            <ul className="list-group mb-3">
              {requests.map((req) => (
                <li key={req.id} className="list-group-item">
                  <div>
                    <div>
                      <strong>신청자:</strong> {req.requesterId}
                    </div>
                    <div>
                      <strong>시간:</strong> {req.hours}시간 / 총{" "}
                      {req.totalCredits} 크레딧
                    </div>
                    {req.message && <div>메시지: {req.message}</div>}
                    <div>상태: {req.status}</div>
                  </div>

                  {req.status === "PENDING" && (
                    <button
                      className="btn btn-success btn-sm mt-2"
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

      {/* ----- 피드백 리스트 ----- */}
      <hr />
      <h3>피드백</h3>

      {feedbacks.length === 0 ? (
        <p>아직 등록된 피드백이 없습니다.</p>
      ) : (
        <ul className="list-group mb-3">
          {feedbacks.map((fb) => (
            <li key={fb.id} className="list-group-item">
              <strong>{fb.nickname}</strong> ({fb.rating}점)
              <div>{fb.content}</div>

              {user && Number(user.id) === Number(fb.userId) && (
                <div className="mt-2">
                  <button
                    className="btn btn-warning btn-sm me-2"
                    onClick={() => handleEditFeedback(fb)}
                  >
                    수정
                  </button>
                  <button
                    className="btn btn-danger btn-sm"
                    onClick={() => handleDeleteFeedback(fb.id)}
                  >
                    삭제
                  </button>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}

      {/* ----- 피드백 작성/수정 폼 ----- */}
      {user ? (
        <form onSubmit={handleFeedbackSubmit}>
          {feedbackError && (
            <div className="alert alert-danger">{feedbackError}</div>
          )}

          <div className="mb-2">
            <label>평점</label>
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
            <label>내용</label>
            <textarea
              className="form-control"
              rows="3"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
          </div>

          <button className="btn btn-primary" type="submit">
            {editingFeedbackId ? "수정 완료" : "피드백 남기기"}
          </button>
        </form>
      ) : (
        <p className="text-muted">로그인 후 작성 가능합니다.</p>
      )}

      {/* ----- 재능 신청 모달 ----- */}
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

      {/* ----- 삭제 모달 ----- */}
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
    </div>
  );
};

export default TalentDetailPage;
