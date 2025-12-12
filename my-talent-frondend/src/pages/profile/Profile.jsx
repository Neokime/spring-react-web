import React, { useEffect, useState } from 'react';
import talentService from '../../services/talent.service';
import useUserStore from '../../store/useUserStroe';
import "./profile.css";
import api from "../../services/base.service";
import { useNavigate } from "react-router-dom";

const Profile = () => {
  const [myTalentList, setMyTalentList] = useState([]);
  const [errorMessage, setErrorMessage] = useState('');

  const currentUser = useUserStore((state) => state.user);
  const clearUser = useUserStore((state) => state.clearUser); // 로그아웃용
  const navigate = useNavigate();

  // 🔥 회원 탈퇴 기능
  const handleDelete = async () => {
    if (!window.confirm("정말 탈퇴하시겠습니까?\n모든 데이터가 삭제됩니다.")) return;

    try {
      await api.delete("/user/me");  

      alert("회원 탈퇴가 완료되었습니다.");

      clearUser();      // 스토어 초기화
      navigate("/");    // 홈으로 이동
    } catch (err) {
      console.log(err);
      alert("회원 탈퇴 중 오류가 발생했습니다.");
    }
  };


  // 내가 등록한 재능 목록 불러오기
  useEffect(() => {
    if (!currentUser?.id) return;

    talentService.getTalentsByUser(currentUser.id)
      .then((res) => setMyTalentList(res.data))
      .catch(() => setErrorMessage("내 재능 목록을 불러올 수 없습니다."));
  }, [currentUser]);


  return (
    <div className="profile-page mt-5">

      {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

      <div className="card profile-card">

        {/* ----------------- 헤더 ----------------- */}
        <div className="profile-card-header">
          <div className="row align-items-center">

            {/* 좌측 : 제목 + 옵션 */}
            <div className="col-6 d-flex align-items-center">
              <h3 className="m-0">내 프로필</h3>

              {/* ⚙ 드롭다운 메뉴 */}
              <div className="dropdown ms-2">
                <button
                  className="btn btn-light btn-sm dropdown-toggle"
                  type="button"
                  data-bs-toggle="dropdown"
                >
                  ⚙
                </button>

                <ul className="dropdown-menu">
                  <li>
                    <button className="dropdown-item" disabled>
                      프로필 수정 (준비중)
                    </button>
                  </li>

                  <li>
                    <button
                      className="dropdown-item"
                      onClick={() => {
                        clearUser();
                        navigate("/login");
                      }}
                    >
                      로그아웃
                    </button>
                  </li>

                  <li><hr className="dropdown-divider" /></li>

                  <li>
                    <button
                      className="dropdown-item text-danger"
                      onClick={handleDelete}
                    >
                      회원 탈퇴
                    </button>
                  </li>
                </ul>
              </div>
            </div>

            {/* 우측 : 유저 정보 */}
            <div className="col-6 profile-user-info text-end">
              현재 유저: <strong>{currentUser?.email}</strong><br />
              Role: <strong>{currentUser?.role}</strong><br />
              남은 크레딧: <strong>{currentUser?.credit ?? 0}</strong>
            </div>

          </div>
        </div>


        {/* ----------------- 본문 ----------------- */}
        <div className="profile-card-body">
          <h4>내가 등록한 재능</h4>

          <table className="table profile-table mt-3">
            <thead>
              <tr>
                <th>#</th>
                <th>제목</th>
                <th>카테고리</th>
                <th>시급(credit)</th>
                <th>상태</th>
                <th>관리</th>
              </tr>
            </thead>

            <tbody>
              {myTalentList.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center">
                    등록한 재능이 없습니다.
                  </td>
                </tr>
              ) : (
                myTalentList.map((item, index) => (
                  <tr key={item.id}>
                    <td>{index + 1}</td>
                    <td>{item.title}</td>
                    <td>{item.category}</td>
                    <td>{item.creditPerHour}</td>
                    <td>{item.status}</td>
                    <td>
                      <button
                        onClick={() => window.location.href = `/talents/${item.id}`}
                        className="btn btn-sm btn-primary"
                      >
                        상세보기
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Profile;
