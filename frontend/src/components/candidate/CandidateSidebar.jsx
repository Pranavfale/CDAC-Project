import { NavLink } from "react-router-dom";

const CandidateSidebar = ({ sidebarOpen, closeSidebar }) => {
  const menuItems = [
    {
      name: "Dashboard",
      path: "/candidate/dashboard",
      icon: "bi-speedometer2",
    },
    {
      name: "Browse Jobs",
      path: "/candidate/jobs",
      icon: "bi-search",
    },
    {
      name: "My Applications",
      path: "/candidate/applications",
      icon: "bi-file-earmark-text",
    },
    {
      name: "Saved Jobs",
      path: "/candidate/saved-jobs",
      icon: "bi-bookmark",
    },
    {
      name: "My Profile",
      path: "/candidate/profile",
      icon: "bi-person",
    },
  ];

  return (
    <>
      {sidebarOpen && (
        <div
          className="candidate-sidebar-overlay"
          onClick={closeSidebar}
        ></div>
      )}

      <aside
        className={`candidate-sidebar ${
          sidebarOpen ? "candidate-sidebar-open" : ""
        }`}
      >
        <div className="candidate-sidebar-header">
          <div className="candidate-logo">
            <div className="candidate-logo-icon">
              R
            </div>

            <div>
              <h2>RecruitPro</h2>
              <span>Candidate Portal</span>
            </div>
          </div>

          <button
            className="candidate-sidebar-close"
            onClick={closeSidebar}
            type="button"
            aria-label="Close menu"
          >
            ×
          </button>
        </div>

        <nav className="candidate-sidebar-nav">
          {menuItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              onClick={closeSidebar}
              className={({ isActive }) =>
                `candidate-nav-link ${
                  isActive ? "active" : ""
                }`
              }
            >
              <i className={`bi ${item.icon}`}></i>
              <span>{item.name}</span>
            </NavLink>
          ))}
        </nav>

        <div className="candidate-sidebar-footer">
          <div className="candidate-profile-mini">
            <div className="candidate-avatar">
              C
            </div>

            <div>
              <strong>Candidate</strong>
              <span>Job Seeker</span>
            </div>
          </div>
        </div>
      </aside>
    </>
  );
};

export default CandidateSidebar;