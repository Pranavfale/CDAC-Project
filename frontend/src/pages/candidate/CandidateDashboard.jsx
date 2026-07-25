const CandidateDashboard = () => {
  const stats = [
    {
      title: "Applications",
      value: 12,
      icon: "bi-file-earmark-text",
      description: "Total applications",
    },
    {
      title: "Shortlisted",
      value: 4,
      icon: "bi-person-check",
      description: "Applications shortlisted",
    },
    {
      title: "Interviews",
      value: 2,
      icon: "bi-calendar-check",
      description: "Upcoming interviews",
    },
    {
      title: "Saved Jobs",
      value: 8,
      icon: "bi-bookmark",
      description: "Jobs saved",
    },
  ];

  const recentApplications = [
    {
      id: 1,
      job: "Java Developer",
      company: "TechCorp Solutions",
      location: "Pune",
      appliedDate: "23 Jul 2026",
      status: "Shortlisted",
    },
    {
      id: 2,
      job: "React Developer",
      company: "SoftWave Technologies",
      location: "Mumbai",
      appliedDate: "21 Jul 2026",
      status: "Applied",
    },
    {
      id: 3,
      job: "Backend Developer",
      company: "NexGen Software",
      location: "Bengaluru",
      appliedDate: "19 Jul 2026",
      status: "Interview",
    },
    {
      id: 4,
      job: "Software Engineer",
      company: "Info Systems",
      location: "Pune",
      appliedDate: "17 Jul 2026",
      status: "Rejected",
    },
  ];

  const recommendedJobs = [
    {
      id: 1,
      title: "Java Developer",
      company: "TechCorp Solutions",
      location: "Pune",
      type: "Full Time",
      experience: "0-2 Years",
    },
    {
      id: 2,
      title: "Full Stack Developer",
      company: "CloudTech India",
      location: "Pune",
      type: "Full Time",
      experience: "Fresher",
    },
    {
      id: 3,
      title: "React Developer",
      company: "SoftWave Technologies",
      location: "Mumbai",
      type: "Full Time",
      experience: "0-1 Year",
    },
  ];

  const getStatusClass = (status) => {
    switch (status) {
      case "Shortlisted":
        return "candidate-status-shortlisted";

      case "Interview":
        return "candidate-status-interview";

      case "Rejected":
        return "candidate-status-rejected";

      default:
        return "candidate-status-applied";
    }
  };

  return (
    <div className="candidate-dashboard">
      {/* Header */}

      <div className="candidate-page-header">
        <div>
          <h2>Welcome Back!</h2>
          <p>
            Track your applications and discover new job
            opportunities.
          </p>
        </div>

        <a
          href="/candidate/jobs"
          className="candidate-primary-button"
        >
          <i className="bi bi-search"></i>
          Browse Jobs
        </a>
      </div>

      {/* Statistics */}

      <section className="candidate-stats-grid">
        {stats.map((stat) => (
          <article
            className="candidate-stat-card"
            key={stat.title}
          >
            <div className="candidate-stat-icon">
              <i className={`bi ${stat.icon}`}></i>
            </div>

            <div>
              <h3>{stat.value}</h3>
              <strong>{stat.title}</strong>
              <span>{stat.description}</span>
            </div>
          </article>
        ))}
      </section>

      {/* Main Dashboard */}

      <section className="candidate-dashboard-grid">

        {/* Recent Applications */}

        <div className="candidate-dashboard-card">
          <div className="candidate-card-header">
            <div>
              <h3>Recent Applications</h3>
              <p>
                Track your latest job applications
              </p>
            </div>

            <a href="/candidate/applications">
              View All
            </a>
          </div>

          <div className="candidate-table-wrapper">
            <table className="candidate-table">
              <thead>
                <tr>
                  <th>Job</th>
                  <th>Applied</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {recentApplications.map(
                  (application) => (
                    <tr key={application.id}>
                      <td>
                        <div className="candidate-job-cell">
                          <div className="candidate-company-icon">
                            {application.company.charAt(0)}
                          </div>

                          <div>
                            <strong>
                              {application.job}
                            </strong>

                            <span>
                              {application.company}
                              {" • "}
                              {application.location}
                            </span>
                          </div>
                        </div>
                      </td>

                      <td>
                        {application.appliedDate}
                      </td>

                      <td>
                        <span
                          className={`candidate-status ${getStatusClass(
                            application.status
                          )}`}
                        >
                          {application.status}
                        </span>
                      </td>
                    </tr>
                  )
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Profile Completion */}

        <div className="candidate-dashboard-card">
          <div className="candidate-card-header">
            <div>
              <h3>Profile Completion</h3>
              <p>
                Complete your profile to improve visibility
              </p>
            </div>
          </div>

          <div className="candidate-profile-completion">
            <div className="candidate-profile-circle">
              <strong>75%</strong>
              <span>Complete</span>
            </div>

            <div className="candidate-profile-progress">
              <div className="candidate-profile-progress-info">
                <span>Profile Progress</span>
                <strong>75%</strong>
              </div>

              <div className="candidate-progress-track">
                <div
                  className="candidate-progress-fill"
                  style={{ width: "75%" }}
                ></div>
              </div>
            </div>

            <div className="candidate-profile-tasks">
              <div>
                <i className="bi bi-check-circle-fill"></i>
                Personal Information
              </div>

              <div>
                <i className="bi bi-check-circle-fill"></i>
                Education
              </div>

              <div>
                <i className="bi bi-check-circle-fill"></i>
                Skills
              </div>

              <div className="candidate-task-pending">
                <i className="bi bi-circle"></i>
                Upload Resume
              </div>
            </div>

            <a
              href="/candidate/profile"
              className="candidate-outline-button"
            >
              Complete Profile
            </a>
          </div>
        </div>
      </section>

      {/* Recommended Jobs */}

      <section className="candidate-dashboard-card candidate-recommended-section">
        <div className="candidate-card-header">
          <div>
            <h3>Recommended Jobs</h3>
            <p>
              Jobs matching your profile and skills
            </p>
          </div>

          <a href="/candidate/jobs">
            View All Jobs
          </a>
        </div>

        <div className="candidate-recommended-grid">
          {recommendedJobs.map((job) => (
            <article
              className="candidate-job-card"
              key={job.id}
            >
              <div className="candidate-job-card-header">
                <div className="candidate-company-icon candidate-company-large">
                  {job.company.charAt(0)}
                </div>

                <button
                  type="button"
                  className="candidate-save-button"
                  title="Save job"
                >
                  <i className="bi bi-bookmark"></i>
                </button>
              </div>

              <h4>{job.title}</h4>

              <p>{job.company}</p>

              <div className="candidate-job-details">
                <span>
                  <i className="bi bi-geo-alt"></i>
                  {job.location}
                </span>

                <span>
                  <i className="bi bi-briefcase"></i>
                  {job.experience}
                </span>
              </div>

              <span className="candidate-job-type">
                {job.type}
              </span>

              <a
                href={`/candidate/jobs/${job.id}`}
                className="candidate-view-job-button"
              >
                View Job
              </a>
            </article>
          ))}
        </div>
      </section>
    </div>
  );
};

export default CandidateDashboard;