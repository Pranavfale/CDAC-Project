import { NavLink } from "react-router-dom";

function HRSidebar({ sidebarOpen, closeSidebar }) {
  const menuItems = [
    {
      name: "Dashboard",
      path: "/hr/dashboard",
      icon: "bi-speedometer2",
    },
    {
      name: "Vacancies",
      path: "/hr/vacancies",
      icon: "bi-briefcase",
    },
    {
      name: "Applicants",
      path: "/hr/applicants",
      icon: "bi-people",
    },
    {
      name: "Interviews",
      path: "/hr/interviews",
      icon: "bi-calendar-event",
    },
    {
      name: "Offers",
      path: "/hr/offers",
      icon: "bi-file-earmark-check",
    },
    {
      name: "My Profile",
      path: "/hr/profile",
      icon: "bi-person-circle",
    },
  ];

  return (
    <>
      {sidebarOpen && (
        <div
          className="hr-sidebar-overlay"
          onClick={closeSidebar}
        ></div>
      )}

      <aside
        className={`hr-sidebar ${
          sidebarOpen ? "hr-sidebar-open" : ""
        }`}
      >
        <div className="hr-sidebar-header">
          <div className="hr-logo">
            <div className="hr-logo-icon">R</div>

            <div>
              <h2>RecruitPro</h2>
              <span>HR Portal</span>
            </div>
          </div>

          <button
            type="button"
            className="hr-sidebar-close"
            onClick={closeSidebar}
            aria-label="Close HR menu"
          >
            ×
          </button>
        </div>

        <nav className="hr-sidebar-nav">
          {menuItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              onClick={closeSidebar}
              className={({ isActive }) =>
                `hr-nav-link ${isActive ? "active" : ""}`
              }
            >
              <i className={`bi ${item.icon}`}></i>
              <span>{item.name}</span>
            </NavLink>
          ))}
        </nav>

        <div className="hr-sidebar-footer">
          <div className="hr-profile-mini">
            <div className="hr-avatar">H</div>

            <div>
              <strong>Priya Patil</strong>
              <span>HR Manager</span>
            </div>
          </div>
        </div>
      </aside>
    </>
  );
}

export default HRSidebar;