// src/pages/talents/TalentDetail.jsx
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import talentService from "../../services/talent.service";
import useUserStore from "../../store/useUserStroe";
import { Modal } from "react-bootstrap";
import "./talent.css";


const TalentDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [data, setData] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const user = useUserStore((state) => state.user);

  // userId가 data.userId 또는 data.user.id 둘 중 하나일 수 있으니 둘 다 지원
  const ownerId = data?.userId ?? data?.user?.id;
  const isOwner =
    user && ownerId && Number(user.id) === Number(ownerId);

  console.log("TalentDetailPage 렌더링됨");
  console.log("data:", data);
  console.log("currentUser:", user);
  console.log("ownerId:", ownerId);
  console.log("isOwner:", isOwner);

  useEffect(() => {
    talentService
      .getTalent(id)
      .then((res) => setData(res.data))
      .catch((err) => console.log(err));
  }, [id]);

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

  if (!data) return <div>Loading...</div>;

  return (
    <div className="container mt-4">
      {/* 여기 텍스트가 보이면 상세 페이지에 “들어온 것” */}
      <h1>재능 상세 페이지</h1>

      <h2>{data.title}</h2>

      <p>카테고리: {data.category}</p>
      <p>설명: {data.description}</p>
      <p>크레딧: {data.creditPerHour}</p>
      <p>상태: {data.status}</p>

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
            onClick={() => setShowModal(true)}
          >
            삭제
          </button>
        </div>
      )}

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>삭제 확인</Modal.Title>
        </Modal.Header>

        <Modal.Body>정말 삭제하시겠습니까?</Modal.Body>

        <Modal.Footer>
          <button
            className="btn btn-secondary"
            onClick={() => setShowModal(false)}
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
