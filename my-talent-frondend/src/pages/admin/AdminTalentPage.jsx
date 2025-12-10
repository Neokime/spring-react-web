import { useEffect, useState } from "react";
import api from "../../services/base.service";

export default function AdminTalentPage() {
  const [talents, setTalents] = useState([]);

  useEffect(() => {
    api.get("/admin/talents").then((res) => setTalents(res.data));
  }, []);

  const closeTalent = (id) => {
    api.post(`/admin/talents/${id}/close`).then(() => {
      setTalents((prev) =>
        prev.map((t) =>
          t.id === id ? { ...t, status: "CLOSED" } : t
        )
      );
    });
  };

  return (
    <div>
      <h3>관리자 - 재능 관리</h3>

      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>제목</th>
            <th>상태</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {talents.map((t) => (
            <tr key={t.id}>
              <td>{t.id}</td>
              <td>{t.title}</td>
              <td>{t.status}</td>
              <td>
                <button
                  className="btn btn-sm btn-danger"
                  onClick={() => closeTalent(t.id)}
                >
                  비공개 처리
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

    </div>
  );
}
