import { Navigate, Route, Routes } from "react-router-dom";
import AdminLayout from "./components/admin/AdminLayout.jsx";
import CandidateLayout from "./components/candidate/CandidateLayout";
import HRLayout from "./components/hr/HRLayout";
import AdminDashboard from "./pages/admin/AdminDashboard";

function AdminPlaceholder() {
  return (
    <div>
      <h2>Admin Portal</h2>
      <p>Select an Admin feature from the sidebar.</p>
    </div>
  );
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/admin" replace />} />

      <Route path="/admin" element={<AdminLayout />}>
        <Route index element={<AdminPlaceholder />} />
        <Route path="dashboard" element={<AdminDashboard />} />
        <Route path="*" element={<AdminPlaceholder />} />
      </Route>

      <Route path="/candidate" element={<CandidateLayout />}>
        {/* Candidate feature routes will go here */}
      </Route>
      <Route path="/hr" element={<HRLayout />}>
        {/* HR child routes will be added feature by feature */}
      </Route>
    </Routes>
  );
}

export default App;
