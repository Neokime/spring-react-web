// src/App.jsx
import { BrowserRouter, Routes, Route } from "react-router-dom";

import LoginPage from "./pages/auth/LoginPage";
import TalentListPage from "./pages/talents/TalentListPage";
import Register from "./pages/register/Register";
import Navbar from "./components/Navbar";
import TalentCreatePage from "./pages/talents/TalentCreatePage";
import TalentDetail from "./pages/talents/TalentDetail";
import TalentEditPage from "./pages/talents/TalentEditPage";
import Profile from "./pages/profile/Profile";
import AdminUserPage from "./pages/admin/AdminUserPage";
import AdminTalentPage from "./pages/admin/AdminTalentPage";
import TradeListPage from "./pages/trade/TradeListPage";
import TradeCreatePage from "./pages/trade/TradeCreatePage";
import TradeDetailPage from "./pages/trade/TradeDetailPage";
import StorePage from "./pages/store/StorePage";
import IndexPage from "./pages/index/IndexPage";

import "./index.css";

function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <main className="container py-4">
        <Routes>
          
          <Route path="/" element={<IndexPage />} />

          {/* 인증 */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<Register />} />

          {/* 관리자 */}
          <Route path="/admin/users" element={<AdminUserPage />} />
          <Route path="/admin/talents" element={<AdminTalentPage />} />

          {/* 재능 */}
          <Route path="/talents" element={<TalentListPage />} />
          <Route path="/talents/create" element={<TalentCreatePage />} />
          <Route path="/talents/:id" element={<TalentDetail />} />
          <Route path="/talents/:id/edit" element={<TalentEditPage />} />

          {/* 교환 */}
          <Route path="/trades" element={<TradeListPage />} />
          <Route path="/trades/create" element={<TradeCreatePage />} />
          <Route path="/trades/:id" element={<TradeDetailPage />} />

          {/* 스토어 */}
          <Route path="/store" element={<StorePage />} />

          {/* 프로필 */}
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
