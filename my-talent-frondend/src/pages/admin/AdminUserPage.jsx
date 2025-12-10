import { useEffect, useState } from "react";
import api from "../../services/base.service";

export default function AdminUserPage() {

  console.log("🔵 AdminUserPage 렌더링됨"); // ← 렌더링 체크

  const [users, setUsers] = useState([]);

  useEffect(() => {
    console.log("🟢 useEffect 실행 → /api/admin/users 호출"); // ← useEffect 호출 확인

    api.get("/admin/users")
      .then((res) => {
        console.log("🟢 /api/admin/users 응답:", res.data); // ← 정상 응답 확인
        setUsers(res.data);
      })
      .catch((err) => {
        console.error("🔴 /api/admin/users 에러:", err); // ← 에러 확인
      });
  }, []);

  const toggleRole = (id) => {
    console.log("🟠 toggleRole 실행, id:", id);

    api.post(`/admin/users/${id}/toggle-role`)
      .then(() => {
        console.log("🟢 toggle-role 성공");

        setUsers((prev) =>
          prev.map((u) =>
            u.id === id
              ? { ...u, role: u.role === "ADMIN" ? "USER" : "ADMIN" }
              : u
          )
        );
      })
      .catch((err) => {
        console.error("🔴 toggle-role 에러:", err);
      });
  };

  return (
    <div>
      <h3>관리자 - 유저 관리</h3>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>닉네임</th>
            <th>ROLE</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {users.map((u) => (
            <tr key={u.id}>
              <td>{u.id}</td>
              <td>{u.nickname}</td>
              <td>{u.role}</td>
              <td>
                <button
                  className="btn btn-sm btn-warning"
                  onClick={() => toggleRole(u.id)}
                >
                  권한변경
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
