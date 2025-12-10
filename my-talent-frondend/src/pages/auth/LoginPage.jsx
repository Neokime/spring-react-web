import React, { useState, useEffect } from 'react'; // 👈 useEffect 추가
import { Link, useNavigate } from 'react-router-dom';
import { loginService } from '../../services/auth.service';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
import useUserStore from '../../store/useUserStroe';

const Login = () => {
  const [form, setForm] = useState({
    userId: '',
    password: '',
  });

  const [loading, setLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const currentUser = useUserStore((state) => state.user);     
  const setCurrentUser = useUserStore((state) => state.setCurrentUser);
  const navigate = useNavigate();

  
  useEffect(() => {
    if (currentUser?.id) {
      navigate('/talents');
    }
  }, [currentUser, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setSubmitted(true);

    if (!form.userId || !form.password) return;

    setLoading(true);

    try {
      const response = await loginService(form);

      console.log("🚀 login response:", response.data);  // 테스트

      const user = response.data;

      setCurrentUser({
        id: user.id,
        userId: user.userId,
        email: user.email,
        nickname: user.nickname,
        role: user.role,      // ⭐ 이거 추가!
        token: user.token,
      });


      navigate('/talents');
    } catch (error) {
      console.error(error);
      setErrorMessage('아이디 또는 비밀번호가 올바르지 않습니다.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card ms-auto me-auto p-3 shadow-lg custom-card">
        <FontAwesomeIcon icon={faUserCircle} className="ms-auto me-auto user-icon" />

        {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

        <form onSubmit={handleLogin} noValidate className={submitted ? 'was-validated' : ''}>
          <div className="form-group my-2">
            <label htmlFor="userId">아이디</label>
            <input
              type="text"
              name="userId"
              className="form-control"
              placeholder="아이디를 입력하세요"
              value={form.userId}
              onChange={handleChange}
              required
            />
            <div className="invalid-feedback">아이디를 입력해주세요</div>
          </div>

          <div className="form-group my-2">
            <label htmlFor="password">비밀번호</label>
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="비밀번호를 입력하세요"
              value={form.password}
              onChange={handleChange}
              required
            />
            <div className="invalid-feedback">비밀번호를 입력해주세요</div>
          </div>

          <button className="btn btn-info text-white w-100 mt-3" disabled={loading}>
            로그인
          </button>
        </form>

        <Link to="/register" className="btn btn-link" style={{ color: 'darkgray' }}>
          새 계정 만들기
        </Link>
      </div>
    </div>
  );
};

export default Login;
