const HRDashboard = () => {
  const stats = [
    {
      title: "Active Vacancies",
      value: 18,
      icon: "bi-briefcase",
      description: "Currently open positions",
    },
    {
      title: "Total Applicants",
      value: 246,
      icon: "bi-people",
      description: "Across all vacancies",
    },
    {
      title: "Interviews Scheduled",
      value: 14,
      icon: "bi-calendar-event",
      description: "Upcoming interviews",
    },
    {
      title: "Offers Released",
      value: 8,
      icon: "bi-file-earmark-check",
      description: "Offers sent this month",
    },
  ];

  const recentApplicants = [
    {
      id: 1,
      name: "Rahul Sharma",
      position: "Java Developer",
      appliedDate: "24 Jul 2026",
      status: "Shortlisted",
    },
    {
      id: 2,
      name: "Sneha Patil",
      position: "React Developer",
      appliedDate: "23 Jul 2026",
      status: "Applied",
    },
    {
      id: 3,
      name: "Amit Kumar",
      position: "Software Tester",
      appliedDate: "22 Jul 2026",
      status: "Interview",
    },
    {
      id: 4,
      name: "Neha Deshmukh",
      position: "Backend Developer",
      appliedDate: "21 Jul 2026",
      status: "Rejected",
    },
  ];

  const upcomingInterviews = [
    {
      id: 1,
      candidate: "Rahul Sharma",
      position: "Java Developer",
      date: "26 Jul 2026",
      time: "10:30 AM",
      mode: "Online",
    },
    {
      id: 2,
      candidate: "Amit Kumar",
      position: "Software Tester",
      date: "26 Jul 2026",
      time: "2:00 PM",
      mode: "Offline",
    },
    {
      id: 3,
      candidate: "Priyanka Jadhav",
      position: "React Developer",
      date: "27 Jul 2026",
      time: "11:00 AM",
      mode: "Online",
    },
  ];

  const getStatusClass = (status) => {
    switch (status) {
      case "Shortlisted":
        return "hr-status-shortlisted";
      case "Interview":
        return "hr-status-interview";
      case "Rejected":
        return "hr-status-rejected";
      default:
        return "hr-status-applied";
    }
  };

  return (
    <div className="hr-dashboard-page">
      <div className="hr-page-header">
        <div>
          <h2>HR Dashboard</h2>
          <p>Welcome back, Priya. Here is your recruitment overview.</p>
        </div>

        <div className="hr-dashboard-label">
          <i className="bi bi-calendar3"></i>
          <span>July 2026</span>
        </div>
      </div>

      <section className="hr-stats-grid">
        {stats.map((stat) => (
          <article className="hr-stat-card" key={stat.title}>
            <div className="hr-stat-icon">
              <i className={`bi ${stat.icon}`}></i>
            </div>

            <div>
              <h3>{stat.value}</h3>
              <p>{stat.title}</p>
              <span>{stat.description}</span>
            </div>
          </article>
        ))}
      </section>

      <section className="hr-dashboard-grid">
        <div className="hr-dashboard-card">
          <div className="hr-card-header">
            <div>
              <h3>Recent Applicants</h3>
              <p>Latest candidates who applied</p>
            </div>

            <button type="button" className="hr-text-button">
              View All
            </button>
          </div>

          <div className="hr-table-wrapper">
            <table className="hr-table">
              <thead>
                <tr>
                  <th>Candidate</th>
                  <th>Position</th>
                  <th>Applied Date</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {recentApplicants.map((applicant) => (
                  <tr key={applicant.id}>
                    <td>
                      <div className="hr-user-cell">
                        <div className="hr-table-avatar">
                          {applicant.name.charAt(0)}
                        </div>

                        <strong>{applicant.name}</strong>
                      </div>
                    </td>

                    <td>{applicant.position}</td>
                    <td>{applicant.appliedDate}</td>

                    <td>
                      <span
                        className={`hr-status-badge ${getStatusClass(
                          applicant.status
                        )}`}
                      >
                        {applicant.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="hr-dashboard-card">
          <div className="hr-card-header">
            <div>
              <h3>Upcoming Interviews</h3>
              <p>Scheduled candidate interviews</p>
            </div>
          </div>

          <div className="hr-interview-list">
            {upcomingInterviews.map((interview) => (
              <div className="hr-interview-item" key={interview.id}>
                <div className="hr-interview-date">
                  <i className="bi bi-calendar-event"></i>
                </div>

                <div className="hr-interview-details">
                  <strong>{interview.candidate}</strong>
                  <span>{interview.position}</span>

                  <div className="hr-interview-meta">
                    <span>
                      <i className="bi bi-calendar3"></i>
                      {interview.date}
                    </span>

                    <span>
                      <i className="bi bi-clock"></i>
                      {interview.time}
                    </span>

                    <span>
                      <i className="bi bi-camera-video"></i>
                      {interview.mode}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <section className="hr-dashboard-card hr-quick-actions-card">
        <div className="hr-card-header">
          <div>
            <h3>Quick Actions</h3>
            <p>Frequently used recruitment actions</p>
          </div>
        </div>

        <div className="hr-quick-actions">
          <button type="button">
            <i className="bi bi-plus-circle"></i>
            <span>Create Vacancy</span>
          </button>

          <button type="button">
            <i className="bi bi-people"></i>
            <span>Review Applicants</span>
          </button>

          <button type="button">
            <i className="bi bi-calendar-plus"></i>
            <span>Schedule Interview</span>
          </button>

          <button type="button">
            <i className="bi bi-file-earmark-check"></i>
            <span>Create Offer</span>
          </button>
        </div>
      </section>
    </div>
  );
};

export default HRDashboard;