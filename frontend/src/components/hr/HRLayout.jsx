import { useState } from "react";
import {
  Outlet,
  useLocation,
} from "react-router-dom";
import HRSidebar from "./HRSidebar";
import "../../styles/hr.css";

function HRLayout() {
  const [sidebarOpen, setSidebarOpen] =
    useState(false);

  const location = useLocation();

  const getPageTitle = () => {
    if (location.pathname.includes("/jobs")) {
      return "Job Management";
    }

    if (location.pathname.includes("/applicants")) {
      return "Applicant Management";
    }

    if (location.pathname.includes("/interviews")) {
      return "Interview Management";
    }

    if (location.pathname.includes("/offers")) {
      return "Offer Management";
    }

    if (location.pathname.includes("/reports")) {
      return "Reports & Analytics";
    }

    if (location.pathname.includes("/profile")) {
      return "My Profile";
    }

    return "HR Dashboard";
  };

  return (
    <div className="hr-layout">
      <HRSidebar
        sidebarOpen={sidebarOpen}
        closeSidebar={() => setSidebarOpen(false)}
      />

      <div className="hr-main">
        <header className="hr-topbar">
          <div className="hr-topbar-left">
            <button
              type="button"
              className="hr-menu-button"
              onClick={() => setSidebarOpen(true)}
              aria-label="Open HR menu"
            >
              <i className="bi bi-list"></i>
            </button>

            <div>
              <h1>{getPageTitle()}</h1>
              <p>RecruitPro HR Portal</p>
            </div>
          </div>

          <div className="hr-topbar-profile">
            <div className="hr-avatar">P</div>

            <div>
              <strong>Priya Patil</strong>
              <span>HR Manager</span>
            </div>
          </div>
        </header>

        <main className="hr-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default HRLayout;