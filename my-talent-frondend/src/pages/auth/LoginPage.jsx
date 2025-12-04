import { useState } from "react";
import { authService } from "../../services/auth.service";

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await authService.login(email, password);
      const token = res.data.token; // 백엔드 응답에 맞게 수정
      localStorage.setItem("accessToken", token);
      alert("로그인 성공");
      // 나중에: navigate("/talents");
    } catch (err) {
      console.error(err);
      alert("로그인 실패");
    }
  };

  return (
    <div>
      <h1>로그인</h1>
      <form onSubmit={handleSubmit}>
        <input
          placeholder="이메일"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          placeholder="비밀번호"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">로그인</button>
      </form>
    </div>
  );
}

export default LoginPage;
