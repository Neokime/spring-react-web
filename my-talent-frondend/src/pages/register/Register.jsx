import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { registerService } from '../../services/auth.service';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
import api from '../../services/base.service'; // ⭐ 중복확인용
import '../auth/auth.css';

const Register = () => {
  const [form, setForm] = useState({
    userId: '',
    email: '',
    nickname: '',
    password: '',
  });

  const [loading, setLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  // ⭐ 아이디 중복확인 상태
  const [userIdChecked, setUserIdChecked] = useState(false);
  const [userIdError, setUserIdError] = useState('');

  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));

    // 아이디 변경 시 중복확인 다시 필요
    if (name === 'userId') {
      setUserIdChecked(false);
      setUserIdError('');
    }
  };

  // ⭐ 아이디 중복확인
  const checkUserId = async () => {
    if (!form.userId) return;

    try {
      const res = await api.get('/user/check-userid', {
        params: { userId: form.userId },
      });

      if (res.data === true) {
        setUserIdError('이미 사용 중인 아이디입니다.');
        setUserIdChecked(false);
      } else {
        setUserIdError('');
        setUserIdChecked(true);
        alert('사용 가능한 아이디입니다.');
      }
    } catch (err) {
      setUserIdError('아이디 확인 중 오류가 발생했습니다.');
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setSubmitted(true);
    setErrorMessage('');

    if (!form.userId || !form.email || !form.nickname || !form.password) return;

    // ⭐ 중복확인 필수
    if (!userIdChecked) {
      setErrorMessage('아이디 중복확인을 해주세요.');
      return;
    }

    setLoading(true);

    try {
      await registerService(form);
      navigate('/login');
    } catch (error) {
      if (error?.response?.status === 409) {
        setErrorMessage(error.response.data || '이미 존재하는 아이디/이메일입니다.');
      } else {
        setErrorMessage('예상하지 못한 에러가 발생했습니다.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card ms-auto me-auto p-3 shadow-lg custom-card">
        <FontAwesomeIcon icon={faUserCircle} className="ms-auto me-auto user-icon" />

        {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

        <form onSubmit={handleRegister} noValidate className={submitted ? 'was-validated' : ''}>

          {/* 아이디 */}
          <div className="form-group mb-2">
            <label>아이디</label>
            <div className="d-flex gap-2">
              <input
                type="text"
                name="userId"
                className="form-control"
                placeholder="아이디 입력"
                value={form.userId}
                onChange={handleChange}
                required
              />
              <button
                type="button"
                className="btn btn-outline-secondary"
                onClick={checkUserId}
              >
                중복확인
              </button>
            </div>
            {userIdError && <small className="text-danger">{userIdError}</small>}
            <div className="invalid-feedback">아이디를 입력해주세요</div>
          </div>

          {/* 이메일 */}
          <div className="form-group mb-2">
            <label>이메일</label>
            <input
              type="email"
              name="email"
              className="form-control"
              placeholder="email@example.com"
              value={form.email}
              onChange={handleChange}
              required
            />
            <div className="invalid-feedback">이메일을 입력해주세요</div>
          </div>

          {/* 닉네임 */}
          <div className="form-group mb-2">
            <label>닉네임</label>
            <input
              type="text"
              name="nickname"
              className="form-control"
              placeholder="닉네임 입력"
              value={form.nickname}
              onChange={handleChange}
              required
            />
            <div className="invalid-feedback">닉네임을 입력해주세요</div>
          </div>

          {/* 비밀번호 */}
          <div className="form-group mb-2">
            <label>비밀번호</label>
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="password"
              value={form.password}
              onChange={handleChange}
              required
            />
            <div className="invalid-feedback">비밀번호를 입력해주세요</div>
          </div>

          <button className="btn btn-info text-white w-100 mt-3" disabled={loading}>
            가입하기
          </button>
        </form>

        <Link to="/login" className="btn btn-link" style={{ color: 'darkgray' }}>
          이미 계정이 있습니다.
        </Link>
      </div>
    </div>
  );
};

export default Register;
