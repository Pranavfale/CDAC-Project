import { useState } from "react";
import { Outlet } from "react-router-dom";
import CandidateSidebar from "./CandidateSidebar";
import "../../styles/candidate.css";

const CandidateLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const openSidebar = () => {
    setSidebarOpen(true);
  };

  const closeSidebar = () => {
    setSidebarOpen(false);
  };

  return (
    <div className="candidate-layout">
      <CandidateSidebar
        sidebarOpen={sidebarOpen}
        closeSidebar={closeSidebar}
      />

      <div className="candidate-main">
        <header className="candidate-topbar">
          <div className="candidate-topbar-left">
            <button
              className="candidate-menu-button"
              type="button"
              onClick={openSidebar}
              aria-label="Open candidate menu"
            >
              <i className="bi bi-list"></i>
            </button>

            <div>
              <h1>Candidate Portal</h1>
              <p>Find your next opportunity</p>
            </div>
          </div>

          <div className="candidate-topbar-right">
            <button
              className="candidate-icon-button"
              type="button"
              aria-label="Notifications"
            >
              <i className="bi bi-bell"></i>
            </button>

            <div className="candidate-topbar-profile">
              <div className="candidate-avatar">
                C
              </div>

              <div className="candidate-topbar-user">
                <strong>Candidate</strong>
                <span>Job Seeker</span>
              </div>
            </div>
          </div>
        </header>

        <main className="candidate-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default CandidateLayout;