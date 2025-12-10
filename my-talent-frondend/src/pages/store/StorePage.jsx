// src/pages/store/StorePage.jsx
import { useEffect, useState } from "react";
import storeService from "../../services/store.service";
import useUserStore from "../../store/useUserStroe";
import { Role } from "../../models/Role";

const EMPTY_FORM = {
  title: "",
  description: "",
  price: "",
  itemType: "AD_SLOT",
};

const StorePage = () => {
  const currentUser = useUserStore((state) => state.user);

  const [items, setItems] = useState([]);
  const [purchases, setPurchases] = useState([]);

  const [form, setForm] = useState(EMPTY_FORM);
  const [editingId, setEditingId] = useState(null);

  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");

  const isAdmin = currentUser?.role === Role.ADMIN;

  // ========== 초기 로딩 ==========
  useEffect(() => {
    loadItems();

    if (currentUser) {
      loadPurchases();
    }
  }, [currentUser]);

  const loadItems = () => {
    setErrorMsg("");
    storeService
      .getItems()
      .then((res) => setItems(res.data))
      .catch((err) => {
        console.error(err);
        setErrorMsg("스토어 아이템을 불러오는 중 오류가 발생했습니다.");
      });
  };

  const loadPurchases = () => {
    storeService
      .getMyPurchases()
      .then((res) => setPurchases(res.data))
      .catch((err) => {
        console.error(err);
        // 굳이 에러 띄우지 않아도 됨
      });
  };

  // ========== 폼 입력 ==========
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // ========== 아이템 저장 (생성/수정 공용) ==========
  const handleSubmit = (e) => {
    e.preventDefault();
    setErrorMsg("");

    if (!form.title || !form.price) {
      setErrorMsg("제목과 가격은 필수입니다.");
      return;
    }

    const payload = {
      title: form.title,
      description: form.description,
      price: parseInt(form.price, 10),
      itemType: form.itemType,
    };

    setLoading(true);

    const apiCall = editingId
      ? storeService.updateItem(editingId, payload)
      : storeService.createItem(payload);

    apiCall
      .then(() => {
        setForm(EMPTY_FORM);
        setEditingId(null);
        loadItems();
      })
      .catch((err) => {
        console.error(err);
        setErrorMsg("아이템 저장 중 오류가 발생했습니다.");
      })
      .finally(() => setLoading(false));
  };

  // ========== 수정 모드 진입 ==========
  const handleEdit = (item) => {
    setEditingId(item.id);
    setForm({
      title: item.title,
      description: item.description || "",
      price: item.price,
      itemType: item.itemType || "AD_SLOT",
    });
  };

  // ========== 삭제 ==========
  const handleDelete = (id) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    storeService
      .deleteItem(id)
      .then(() => loadItems())
      .catch((err) => {
        console.error(err);
        alert("삭제 중 오류가 발생했습니다.");
      });
  };

  // ========== 구매 ==========
  const handlePurchase = (item) => {
    if (!currentUser) {
      alert("로그인이 필요합니다.");
      return;
    }

    if (!window.confirm(`'${item.title}'를 구매하시겠습니까?`)) return;

    storeService
      .purchaseItem(item.id)
      .then(() => {
        alert("구매가 완료되었습니다.");
        loadPurchases();
      })
      .catch((err) => {
        console.error(err);
        alert("구매 중 오류가 발생했습니다.");
      });
  };

  return (
    <div className="mt-4">
      <h2>스토어</h2>
      <p className="text-muted">
        크레딧으로 광고권 / 노출권 등 다양한 아이템을 구매할 수 있습니다.
      </p>

      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}

      {/* ======= 관리자 전용 영역 (상품 등록/수정) ======= */}
      {isAdmin && (
        <div className="card mb-4">
          <div className="card-header">
            <strong>{editingId ? "스토어 아이템 수정" : "스토어 아이템 등록"}</strong>
          </div>
          <div className="card-body">
            <form onSubmit={handleSubmit}>
              <div className="mb-2">
                <label className="form-label">제목</label>
                <input
                  type="text"
                  name="title"
                  className="form-control"
                  value={form.title}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="mb-2">
                <label className="form-label">설명</label>
                <textarea
                  name="description"
                  className="form-control"
                  rows="2"
                  value={form.description}
                  onChange={handleChange}
                />
              </div>

              <div className="mb-2">
                <label className="form-label">가격 (크레딧)</label>
                <input
                  type="number"
                  name="price"
                  min="0"
                  className="form-control"
                  value={form.price}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="mb-2">
                <label className="form-label">아이템 타입</label>
                <select
                  name="itemType"
                  className="form-select"
                  value={form.itemType}
                  onChange={handleChange}
                >
                  <option value="AD_SLOT">광고 슬롯</option>
                  <option value="HIGHLIGHT">상단 노출</option>
                  <option value="BADGE">뱃지</option>
                </select>
              </div>

              <button className="btn btn-primary" type="submit" disabled={loading}>
                {editingId ? "수정 완료" : "등록하기"}
              </button>

              {editingId && (
                <button
                  type="button"
                  className="btn btn-secondary ms-2"
                  onClick={() => {
                    setEditingId(null);
                    setForm(EMPTY_FORM);
                  }}
                >
                  취소
                </button>
              )}
            </form>
          </div>
        </div>
      )}

      {/* ======= 상품 목록 ======= */}
      <div className="card mb-4">
        <div className="card-header">
          <strong>판매 중인 아이템</strong>
        </div>
        <div className="card-body">
          {items.length === 0 ? (
            <p className="text-muted">등록된 아이템이 없습니다.</p>
          ) : (
            <table className="table table-hover">
              <thead>
                <tr>
                  <th>제목</th>
                  <th>설명</th>
                  <th>가격</th>
                  <th>타입</th>
                  <th>상태</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {items.map((item) => (
                  <tr key={item.id}>
                    <td>{item.title}</td>
                    <td>{item.description}</td>
                    <td>{item.price} 크레딧</td>
                    <td>{item.itemType}</td>
                    <td>{item.status}</td>
                    <td className="text-end">
                      <button
                        className="btn btn-sm btn-success me-2"
                        onClick={() => handlePurchase(item)}
                      >
                        구매
                      </button>

                      {isAdmin && (
                        <>
                          <button
                            className="btn btn-sm btn-outline-primary me-2"
                            onClick={() => handleEdit(item)}
                          >
                            수정
                          </button>
                          <button
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => handleDelete(item.id)}
                          >
                            삭제
                          </button>
                        </>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {/* ======= 내 구매 내역 ======= */}
      {currentUser && (
        <div className="card">
          <div className="card-header">
            <strong>내 구매 내역</strong>
          </div>
          <div className="card-body">
            {purchases.length === 0 ? (
              <p className="text-muted">아직 구매한 아이템이 없습니다.</p>
            ) : (
              <table className="table table-sm">
                <thead>
                  <tr>
                    <th>아이템</th>
                    <th>사용 크레딧</th>
                    <th>구매 시각</th>
                  </tr>
                </thead>
                <tbody>
                  {purchases.map((p) => (
                    <tr key={p.id}>
                      <td>{p.itemTitle}</td>
                      <td>{p.usedCredit}</td>
                      <td>{p.purchasedAt}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default StorePage;
