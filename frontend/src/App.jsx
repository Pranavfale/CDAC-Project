import { Navigate, Route, Routes } from "react-router-dom";
import AdminLayout from "./components/admin/AdminLayout.jsx";
import CandidateLayout from "./components/candidate/CandidateLayout";
import CandidateDashboard from "./pages/candidate/CandidateDashboard";
import HRLayout from "./components/hr/HRLayout";
import AdminDashboard from "./pages/admin/AdminDashboard";
import BrowseJobs from "./pages/candidate/BrowseJobs";
import CandidateJobDetails from "./pages/candidate/CandidateJobDetails";
import HRDashboard from "./pages/hr/HRDashboard";
import MyApplications from "./pages/candidate/MyApplications";
import CandidateProfile from "./pages/candidate/CandidateProfile";
import UserManagement from "./pages/admin/UserManagement";

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
        <Route path="dashboard" element={<AdminDashboard />} />
        <Route path="users" element={<UserManagement />} />
      </Route>

      <Route path="/candidate" element={<CandidateLayout />}>
        <Route path="dashboard" element={<CandidateDashboard />} />
        <Route path="jobs" element={<BrowseJobs />} />
        <Route path="jobs/:id" element={<CandidateJobDetails />} />
        <Route path="applications" element={<MyApplications />} />
        <Route path="profile" element={<CandidateProfile />} />
      </Route>

      <Route path="/hr" element={<HRLayout />}>
        <Route index element={<Navigate to="dashboard" replace />} />

        <Route path="dashboard" element={<HRDashboard />} />
      </Route>
    </Routes>
  );
}

export default App;
