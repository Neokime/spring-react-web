// src/components/Navbar.jsx
import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import useUserStore from "../store/useUserStroe";
import api from "../services/base.service";
import "./navbar.css";

const Navbar = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const currentUser = useUserStore((state) => state.user);
  const clearUser = useUserStore((state) => state.clearUser);

  const isActive = (path) => location.pathname.startsWith(path);

  // 로그아웃
  const handleLogout = () => {
    clearUser();
    try {
      localStorage.removeItem("currentUser");
    } catch (e) {}
    navigate("/login");
  };

  // 회원 탈퇴
  const handleDeleteAccount = async () => {
    if (!window.confirm("정말 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
      return;
    }

    try {
      await api.delete("/user/me");

      alert("회원 탈퇴가 완료되었습니다.");

      clearUser();
      try {
        localStorage.removeItem("currentUser");
      } catch (e) {}
      navigate("/login");
    } catch (err) {
      console.error(err);
      alert("회원 탈퇴 중 오류가 발생했습니다.");
    }
  };

  return (
    <nav className="navbar navbar-expand-lg">
      <div className="container">
        {/* 로고 → index */}
        <Link className="navbar-brand" to="/">
          MY TALENT
        </Link>

        {/* 모바일 토글 */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarMain"
        >
          <span className="navbar-toggler-icon" />
        </button>

        <div className="collapse navbar-collapse" id="navbarMain">
          {/* 가운데 메뉴 */}
          <ul className="navbar-nav mx-auto">
            <li className="nav-item">
              <Link
                className={`nav-link ${isActive("/talents") ? "active" : ""}`}
                to="/talents"
              >
                목록
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className={`nav-link ${isActive("/trades") ? "active" : ""}`}
                to="/trades"
              >
                교환
              </Link>
            </li>
            <li className="nav-item">
              <Link
                className={`nav-link ${isActive("/store") ? "active" : ""}`}
                to="/store"
              >
                스토어
              </Link>
            </li>

            {currentUser?.role === "ADMIN" && (
              <li className="nav-item">
                <Link
                  className={`nav-link ${isActive("/admin") ? "active" : ""}`}
                  to="/admin/users"
                >
                  관리자
                </Link>
              </li>
            )}
          </ul>

          {/* 오른쪽 영역 */}
          <ul className="navbar-nav ms-auto">
            {!currentUser && (
              <>
                <li className="nav-item me-2">
                  <Link className="btn btn-outline-dark" to="/login">
                    로그인
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="btn btn-dark" to="/register">
                    회원가입
                  </Link>
                </li>
              </>
            )}

            {currentUser && (
              <li className="nav-item dropdown">
                <button
                  className="btn btn-dark dropdown-toggle"
                  data-bs-toggle="dropdown"
                >
                  {currentUser.nickname || currentUser.userId}
                </button>
                <ul className="dropdown-menu dropdown-menu-end">
                  <li>
                    <button
                      className="dropdown-item"
                      onClick={() => navigate("/profile")}
                    >
                      프로필
                    </button>
                  </li>
                  <li><hr className="dropdown-divider" /></li>
                  <li>
                    <button
                      className="dropdown-item"
                      onClick={handleLogout}
                    >
                      로그아웃
                    </button>
                  </li>
                  <li>
                    <button
                      className="dropdown-item text-danger"
                      onClick={handleDeleteAccount}
                    >
                      회원 탈퇴
                    </button>
                  </li>
                </ul>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
