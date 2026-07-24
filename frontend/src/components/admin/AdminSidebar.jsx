import { NavLink } from "react-router-dom";

function AdminSidebar({ sidebarOpen, closeSidebar }) {
  const menuItems = [
    {
      name: "Dashboard",
      path: "/admin/dashboard",
      icon: "bi-speedometer2",
    },
    {
      name: "User Management",
      path: "/admin/users",
      icon: "bi-people",
    },
    {
      name: "HR Management",
      path: "/admin/hr",
      icon: "bi-person-badge",
    },
    {
      name: "Candidates",
      path: "/admin/candidates",
      icon: "bi-person-vcard",
    },
    {
      name: "Job Oversight",
      path: "/admin/jobs",
      icon: "bi-briefcase",
    },
    {
      name: "Reports",
      path: "/admin/reports",
      icon: "bi-bar-chart",
    },
  ];

  return (
    <>
      {sidebarOpen && (
        <div
          className="admin-sidebar-overlay"
          onClick={closeSidebar}
        ></div>
      )}

      <aside
        className={`admin-sidebar ${
          sidebarOpen ? "admin-sidebar-open" : ""
        }`}
      >
        <div className="admin-sidebar-header">
          <div className="admin-logo">
            <span className="admin-logo-icon">R</span>

            <div>
              <h2>RecruitPro</h2>
              <span>Admin Portal</span>
            </div>
          </div>

          <button
            className="admin-sidebar-close"
            onClick={closeSidebar}
            type="button"
          >
            ×
          </button>
        </div>

        <nav className="admin-sidebar-nav">
          {menuItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              onClick={closeSidebar}
              className={({ isActive }) =>
                `admin-nav-link ${isActive ? "active" : ""}`
              }
            >
              <i className={`bi ${item.icon}`}></i>
              <span>{item.name}</span>
            </NavLink>
          ))}
        </nav>

        <div className="admin-sidebar-footer">
          <div className="admin-profile-mini">
            <div className="admin-avatar">A</div>

            <div>
              <strong>Administrator</strong>
              <span>System Admin</span>
            </div>
          </div>
        </div>
      </aside>
    </>
  );
}

export default AdminSidebar;