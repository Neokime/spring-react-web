import { NavLink, useNavigate } from 'react-router-dom';
import logo from '../assets/react.svg';

// 파일명이 실제로 useUserStroe.js 라면 여기 절대 바꾸지 말아야 함!
import useUserStore from '../store/useUserStroe';

import { Role } from '../models/Role';

const Navbar = () => {
  const currentUser = useUserStore((state) => state.user);
  const clearCurrentUser = useUserStore((state) => state.clearCurrentUser);
  const navigate = useNavigate();

  const logout = () => {
    clearCurrentUser();
    navigate('/login');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
      <div className="container">

        {/* 왼쪽 브랜드 */}
        <NavLink to="/" className="navbar-brand d-flex align-items-center">
          <img src={logo} alt="logo" style={{ height: "30px", marginRight: "8px" }} />
          MY TALENT
        </NavLink>

        {/* 가운데 메뉴 */}
        <div className="collapse navbar-collapse">

          <ul className="navbar-nav me-auto">
            {currentUser?.role === Role.ADMIN && (
              <li className="nav-item">
                <NavLink to="/admin" className="nav-link">
                  관리자
                </NavLink>
              </li>
            )}

            <li className="nav-item">
              <NavLink to="/talents" className="nav-link">
                목록
              </NavLink>
            </li>
          </ul>

          {/* 오른쪽 영역 */}
          {!currentUser && (
            <ul className="navbar-nav ms-auto">
              <li className="nav-item">
                <NavLink to="/login" className="nav-link">
                  로그인
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink to="/register" className="nav-link">
                  가입하기
                </NavLink>
              </li>
            </ul>
          )}

          {currentUser && (
            <ul className="navbar-nav ms-auto">
              <li className="nav-item">
                <NavLink to="/profile" className="nav-link">
                  {currentUser.nickname}
                </NavLink>
              </li>
              <li className="nav-item">
                <button
                  className="btn btn-outline-light btn-sm ms-2"
                  onClick={logout}
                >
                  로그아웃
                </button>
              </li>
            </ul>
          )}

        </div>
      </div>
    </nav>
  );
};

export default Navbar;
