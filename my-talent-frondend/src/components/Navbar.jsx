// src/components/Navbar.jsx
import { NavLink, useNavigate } from "react-router-dom";
import logo from "../assets/react.svg";

import useUserStore from "../store/useUserStroe";
import { Role } from "../models/Role";

const Navbar = () => {
  const currentUser = useUserStore((state) => state.user);
  const clearCurrentUser = useUserStore((state) => state.clearCurrentUser);
  const navigate = useNavigate();

  console.log("Navbar currentUser:", currentUser);
  console.log("Role.ADMIN:", Role.ADMIN);

  const logout = () => {
    clearCurrentUser();
    navigate("/login");
  };

  return (
    <nav className="navbar navbar-dark bg-dark">
      <div className="container d-flex align-items-center justify-content-between">
        {/* 왼쪽: 로고 */}
        <div className="d-flex align-items-center">
          <NavLink to="/" className="navbar-brand d-flex align-items-center">
            <img
              src={logo}
              alt="logo"
              style={{ height: "30px", marginRight: "8px" }}
            />
            MY TALENT
          </NavLink>
        </div>

        {/* 가운데: 메뉴들 */}
        <ul className="navbar-nav flex-row">
          {/* 🔥 관리자 전용 메뉴 */}
          {currentUser?.role === Role.ADMIN && (
            <li className="nav-item mx-2">
              <NavLink to="/admin/users" className="nav-link">
                관리자
              </NavLink>
            </li>
          )}

          {/* 공통 메뉴 */}
          <li className="nav-item mx-2">
            <NavLink to="/talents" className="nav-link">
              목록
            </NavLink>
          </li>

          <li className="nav-item mx-2">
            <NavLink to="/trades" className="nav-link">
              교환
            </NavLink>
          </li>

          {/* ⭐ 스토어 메뉴 추가 */}
          <li className="nav-item mx-2">
            <NavLink to="/store" className="nav-link">
              스토어
            </NavLink>
          </li>
        </ul>

        {/* 오른쪽: 로그인/프로필 */}
        {!currentUser ? (
          <ul className="navbar-nav flex-row">
            <li className="nav-item mx-2">
              <NavLink to="/login" className="nav-link">
                로그인
              </NavLink>
            </li>
            <li className="nav-item mx-2">
              <NavLink to="/register" className="nav-link">
                가입하기
              </NavLink>
            </li>
          </ul>
        ) : (
          <ul className="navbar-nav flex-row">
            <li className="nav-item mx-2">
              <NavLink to="/profile" className="nav-link">
                {currentUser.nickname}
              </NavLink>
            </li>
            <li className="nav-item mx-2">
              <button
                className="btn btn-outline-light btn-sm"
                onClick={logout}
              >
                로그아웃
              </button>
            </li>
          </ul>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
