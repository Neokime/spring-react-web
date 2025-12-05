import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { registerService } from '../../services/auth.service';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
// import './Register.css';
import useUserStore from '../../store/useUserStroe';

const Register = () => {
  const [form, setForm] = useState({
    email: '',
    nickname: '',
    password: '',
  });
  const [loading, setLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  // const currentUser = useUserStore((state) => state.user);
  const navigate = useNavigate();

  // useEffect(() => {
  //   if (currentUser?.id) {
  //     navigate('/talents');
  //   }
  // }, [currentUser, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleRegister = (e) => {
    e.preventDefault();
    setSubmitted(true);

    if (!form.email || !form.nickname || !form.password) {
      return;
    }

    setLoading(true);

    registerService(form)
      .then(() => {
        navigate('/login');
      })
      .catch((error) => {
        console.log(error);
        if (error?.response?.status === 409) {
          setErrorMessage('이미 존재하는 이메일입니다.');
        } else {
          setErrorMessage('예상하지 못한 에러가 발생했습니다.');
        }
        setLoading(false);
      });
  };

  return (
    <div className="container mt-5">
      <div className="card ms-auto me-auto p-3 shadow-lg custom-card">
        <FontAwesomeIcon icon={faUserCircle} className="ms-auto me-auto user-icon" />

        {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

        <form onSubmit={handleRegister} noValidate className={submitted ? 'was-validated' : ''}>
          <div className="form-group mb-2">
            <label htmlFor="email">이메일</label>
            <input
              type="email"
              name="email"
              className="form-control"
              placeholder="email"
              value={form.email}
              onChange={handleChange}
              required
            />
            <div className="invalid-feedback">이메일을 입력해주세요</div>
          </div>

          <div className="form-group mb-2">
            <label htmlFor="nickname">닉네임</label>
            <input
              type="text"
              name="nickname"
              className="form-control"
              placeholder="nickname"
              value={form.nickname}
              onChange={handleChange}
              required
            />
            <div className="invalid-feedback">닉네임을 입력해주세요</div>
          </div>

          <div className="form-group mb-2">
            <label htmlFor="password">비밀번호</label>
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
