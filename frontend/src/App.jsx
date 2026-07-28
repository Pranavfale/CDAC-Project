import { Navigate, Route, Routes } from "react-router-dom";

// Layouts
import AdminLayout from "./components/admin/AdminLayout";
import CandidateLayout from "./components/candidate/CandidateLayout";
import HRLayout from "./components/hr/HRLayout";

// Admin Pages
import AdminDashboard from "./pages/admin/AdminDashboard";
import UserManagement from "./pages/admin/UserManagement";
import HRManagement from "./pages/admin/HRManagement";

// Candidate Pages
import CandidateDashboard from "./pages/candidate/CandidateDashboard";
import BrowseJobs from "./pages/candidate/BrowseJobs";
import CandidateJobDetails from "./pages/candidate/CandidateJobDetails";
import MyApplications from "./pages/candidate/MyApplications";
import CandidateProfile from "./pages/candidate/CandidateProfile";

// HR Pages
import HRDashboard from "./pages/hr/HRDashboard";
import HRJobManagement from "./pages/hr/HRJobManagement";
import HRApplicantManagement from "./pages/hr/HRApplicantManagement";


import LandingPage from './pages/public/LandingPage';
import PublicJobs from "./pages/public/Jobs";
import PublicJobDetails from "./pages/public/JobDetails";
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Unauthorized from './pages/public/Unauthorized';

function App() {
  return (
    <Routes>
  {/* ================= PUBLIC ================= */}

      <Route path="/" element={<LandingPage />} />
      <Route path="/jobs" element={<PublicJobs />} />
      <Route path="/jobs/:jobId" element={<PublicJobDetails />} />

      <Route path="/login" element={<Login />} />

      <Route path="/register" element={<Register />} />

      <Route path="/unauthorized" element={<Unauthorized />} />

      {/* ================= ADMIN ================= */}

      <Route path="/admin" element={<AdminLayout />}>
        <Route
          index
          element={<Navigate to="dashboard" replace />}
        />

        <Route
          path="dashboard"
          element={<AdminDashboard />}
        />

        <Route
          path="users"
          element={<UserManagement />}
        />

        <Route
          path="hr"
          element={<HRManagement />}
        />
      </Route>

      {/* ================= CANDIDATE ================= */}

      <Route path="/candidate" element={<CandidateLayout />}>
        <Route
          index
          element={<Navigate to="dashboard" replace />}
        />

        <Route
          path="dashboard"
          element={<CandidateDashboard />}
        />

        <Route
          path="jobs"
          element={<BrowseJobs />}
        />

        <Route
          path="jobs/:id"
          element={<CandidateJobDetails />}
        />

        <Route
          path="applications"
          element={<MyApplications />}
        />

        <Route
          path="profile"
          element={<CandidateProfile />}
        />
      </Route>

      {/* ================= HR ================= */}

      <Route path="/hr" element={<HRLayout />}>
        <Route
          index
          element={<Navigate to="dashboard" replace />}
        />

        <Route
          path="dashboard"
          element={<HRDashboard />}
        />

        <Route
          path="jobs"
          element={<HRJobManagement />}
        />

        <Route
          path="applicants"
          element={<HRApplicantManagement />}
        />
      </Route>

      {/* 404 */}

      <Route
        path="*"
        element={<Navigate to="/" replace />}
      />

    </Routes>
  );
}

export default App;