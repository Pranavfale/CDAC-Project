const AdminDashboard = () => {
  const stats = [
    {
      title: "Total Users",
      value: "1,248",
      icon: "bi-people",
      change: "+12%",
      description: "from last month",
    },
    {
      title: "Active Jobs",
      value: "86",
      icon: "bi-briefcase",
      change: "+8%",
      description: "from last month",
    },
    {
      title: "HR Accounts",
      value: "42",
      icon: "bi-person-badge",
      change: "+5%",
      description: "from last month",
    },
    {
      title: "Candidates",
      value: "1,120",
      icon: "bi-person-vcard",
      change: "+15%",
      description: "from last month",
    },
  ];

  const recentUsers = [
    {
      id: 1,
      name: "Rahul Sharma",
      email: "rahul@example.com",
      role: "Candidate",
      status: "Active",
    },
    {
      id: 2,
      name: "Priya Patil",
      email: "priya@example.com",
      role: "HR",
      status: "Active",
    },
    {
      id: 3,
      name: "Amit Kumar",
      email: "amit@example.com",
      role: "Candidate",
      status: "Active",
    },
    {
      id: 4,
      name: "Sneha Joshi",
      email: "sneha@example.com",
      role: "Candidate",
      status: "Inactive",
    },
  ];

  return (
    <div className="admin-dashboard">
      <div className="admin-page-header">
        <div>
          <h2>Dashboard</h2>
          <p>Overview of your recruitment system.</p>
        </div>

        <div className="admin-dashboard-date">
          <i className="bi bi-calendar3"></i>
          <span>Admin Overview</span>
        </div>
      </div>

      <section className="admin-stats-grid">
        {stats.map((stat) => (
          <article className="admin-stat-card" key={stat.title}>
            <div className="admin-stat-card-top">
              <div className="admin-stat-icon">
                <i className={`bi ${stat.icon}`}></i>
              </div>

              <span className="admin-stat-change">
                {stat.change}
              </span>
            </div>

            <h3>{stat.value}</h3>

            <p className="admin-stat-title">
              {stat.title}
            </p>

            <span className="admin-stat-description">
              {stat.description}
            </span>
          </article>
        ))}
      </section>

      <section className="admin-dashboard-grid">
        <div className="admin-dashboard-card">
          <div className="admin-card-header">
            <div>
              <h3>Recent Users</h3>
              <p>Recently registered users</p>
            </div>

            <button className="admin-text-button" type="button">
              View All
            </button>
          </div>

          <div className="admin-table-wrapper">
            <table className="admin-table">
              <thead>
                <tr>
                  <th>User</th>
                  <th>Role</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {recentUsers.map((user) => (
                  <tr key={user.id}>
                    <td>
                      <div className="admin-user-cell">
                        <div className="admin-table-avatar">
                          {user.name.charAt(0)}
                        </div>

                        <div>
                          <strong>{user.name}</strong>
                          <span>{user.email}</span>
                        </div>
                      </div>
                    </td>

                    <td>{user.role}</td>

                    <td>
                      <span
                        className={`admin-status ${
                          user.status === "Active"
                            ? "admin-status-active"
                            : "admin-status-inactive"
                        }`}
                      >
                        {user.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="admin-dashboard-card">
          <div className="admin-card-header">
            <div>
              <h3>System Overview</h3>
              <p>Current platform status</p>
            </div>
          </div>

          <div className="admin-overview-list">
            <div className="admin-overview-item">
              <div className="admin-overview-icon">
                <i className="bi bi-building"></i>
              </div>

              <div>
                <strong>32</strong>
                <span>Registered Companies</span>
              </div>
            </div>

            <div className="admin-overview-item">
              <div className="admin-overview-icon">
                <i className="bi bi-file-earmark-text"></i>
              </div>

              <div>
                <strong>486</strong>
                <span>Total Applications</span>
              </div>
            </div>

            <div className="admin-overview-item">
              <div className="admin-overview-icon">
                <i className="bi bi-person-check"></i>
              </div>

              <div>
                <strong>74</strong>
                <span>Shortlisted Candidates</span>
              </div>
            </div>

            <div className="admin-overview-item">
              <div className="admin-overview-icon">
                <i className="bi bi-check-circle"></i>
              </div>

              <div>
                <strong>System Online</strong>
                <span>All services operational</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default AdminDashboard;