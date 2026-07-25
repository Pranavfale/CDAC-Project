import { useState } from "react";
import { Outlet } from "react-router-dom";
import HRSidebar from "./HRSidebar";
import "../../styles/hr.css";

function HRLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const openSidebar = () => {
    setSidebarOpen(true);
  };

  const closeSidebar = () => {
    setSidebarOpen(false);
  };

  return (
    <div className="hr-layout">
      <HRSidebar
        sidebarOpen={sidebarOpen}
        closeSidebar={closeSidebar}
      />

      <div className="hr-main">
        <header className="hr-topbar">
          <div className="hr-topbar-left">
            <button
              type="button"
              className="hr-menu-button"
              onClick={openSidebar}
              aria-label="Open HR menu"
            >
              <i className="bi bi-list"></i>
            </button>

            <div>
              <h1>HR Portal</h1>
              <p>Manage recruitment activities</p>
            </div>
          </div>

          <div className="hr-topbar-right">
            <button
              type="button"
              className="hr-icon-button"
              aria-label="Notifications"
            >
              <i className="bi bi-bell"></i>
            </button>

            <div className="hr-topbar-profile">
              <div className="hr-avatar">H</div>

              <div className="hr-topbar-user">
                <strong>Priya Patil</strong>
                <span>HR Manager</span>
              </div>
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