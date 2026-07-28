import { Navigate, Route, Routes } from "react-router-dom";

import AdminLayout from "./components/admin/AdminLayout.jsx";
import CandidateLayout from "./components/candidate/CandidateLayout";
import HRLayout from "./components/hr/HRLayout";
import LandingPage from './pages/public/LandingPage.jsx'
import PublicJobs from "./pages/public/Jobs";

import AdminDashboard from "./pages/admin/AdminDashboard";
import UserManagement from "./pages/admin/UserManagement";
import PublicJobDetails from "./pages/public/JobDetails";

import CandidateDashboard from "./pages/candidate/CandidateDashboard";
import BrowseJobs from "./pages/candidate/BrowseJobs";
import CandidateJobDetails from "./pages/candidate/CandidateJobDetails";
import MyApplications from "./pages/candidate/MyApplications";
import CandidateProfile from "./pages/candidate/CandidateProfile";

import HRDashboard from "./pages/hr/HRDashboard";
import HRJobManagement from "./pages/hr/HRJobManagement";
import Unauthorized from './pages/public/Unauthorized';
import Register from './pages/auth/Register';
import Login from './pages/auth/Login';

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

     

      {/* Admin Routes */}
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
      </Route>

      {/* Candidate Routes */}
      <Route
        path="/candidate"
        element={<CandidateLayout />}
      >
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

      {/* HR Routes */}
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
          path="vacancies"
          element={<HRJobManagement />}
        />
      </Route>

      {/* Invalid route */}
      <Route
        path="*"
        element={<Navigate to="/" replace />}
      />
    </Routes>
  );
}

export default App;