// src/App.jsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/auth/LoginPage.jsx";
import TalentListPage from "./pages/talents/TalentListPage.jsx";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/talents" element={<TalentListPage />} />
        {/* 초기세팅 */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
