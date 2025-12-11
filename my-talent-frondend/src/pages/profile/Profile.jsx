import React, { useEffect, useState } from 'react';
import talentService from '../../services/talent.service';
import useUserStore from '../../store/useUserStroe';
import "./profile.css";


const Profile = () => {
  const [myTalentList, setMyTalentList] = useState([]);
  const [errorMessage, setErrorMessage] = useState('');

  const currentUser = useUserStore((state) => state.user);

  useEffect(() => {
    if (!currentUser?.id) return;

    // 내가 등록한 재능 목록 가져오기
    talentService.getTalentsByUser(currentUser.id)
      .then((res) => setMyTalentList(res.data))
      .catch((err) => {
        console.log(err);
        setErrorMessage("내 재능 목록을 불러오지 못했습니다.");
      });
  }, [currentUser]);

  return (
    <div className="profile-page mt-5">

  {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

  <div className="card profile-card">
    <div className="profile-card-header">
      <div className="row">
        <div className="col-6">
          <h3>내 프로필</h3>
        </div>

        <div className="col-6 profile-user-info">
          현재 유저: <strong>{currentUser?.email}</strong><br />
          Role: <strong>{currentUser?.role}</strong><br />
          남은 크레딧: <strong>{currentUser?.credit ?? 0}</strong>
        </div>
      </div>
    </div>

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
