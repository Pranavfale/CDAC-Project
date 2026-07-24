import { useState } from "react";
import { Outlet } from "react-router-dom";
import AdminSidebar from "./AdminSidebar.jsx";
import "../../styles/admin.css";

function AdminLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const openSidebar = () => {
    setSidebarOpen(true);
  };

  const closeSidebar = () => {
    setSidebarOpen(false);
  };

  return (
    <div className="admin-layout">
      <AdminSidebar
        sidebarOpen={sidebarOpen}
        closeSidebar={closeSidebar}
      />

      <div className="admin-main">
        <header className="admin-topbar">
          <div className="admin-topbar-left">
            <button
              className="admin-menu-button"
              onClick={openSidebar}
              type="button"
              aria-label="Open admin menu"
            >
              <i className="bi bi-list"></i>
            </button>

            <div>
              <h1>Admin Portal</h1>
              <p>Web-Based Recruitment System</p>
            </div>
          </div>

          <div className="admin-topbar-right">
            <button
              className="admin-icon-button"
              type="button"
              aria-label="Notifications"
            >
              <i className="bi bi-bell"></i>
            </button>

            <div className="admin-topbar-profile">
              <div className="admin-avatar">A</div>

              <div className="admin-topbar-user-info">
                <strong>Admin</strong>
                <span>Administrator</span>
              </div>
            </div>
          </div>
        </header>

        <main className="admin-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

export default AdminLayout;