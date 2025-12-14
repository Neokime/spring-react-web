import { useEffect, useState } from "react";
import api from "../../services/base.service";

export default function AdminUserPage() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    api.get("/admin/users")
      .then((res) => setUsers(res.data))
      .catch(console.error);
  }, []);

  const toggleRole = (id) => {
    api.post(`/admin/users/${id}/toggle-role`)
      .then(() => {
        setUsers((prev) =>
          prev.map((u) =>
            u.id === id
              ? { ...u, role: u.role === "ADMIN" ? "USER" : "ADMIN" }
              : u
          )
        );
      });
  };

  // 🔴 유저 삭제 추가
  const deleteUser = (id) => {
    if (!window.confirm("정말 이 유저를 삭제하시겠습니까?")) return;

    api.delete(`/admin/users/${id}`)
      .then(() => {
        setUsers((prev) => prev.filter((u) => u.id !== id));
      })
      .catch(console.error);
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
            <th>관리</th>
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
                  className="btn btn-sm btn-warning me-2"
                  onClick={() => toggleRole(u.id)}
                >
                  권한변경
                </button>

                <button
                  className="btn btn-sm btn-danger"
                  onClick={() => deleteUser(u.id)}
                >
                  삭제
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
