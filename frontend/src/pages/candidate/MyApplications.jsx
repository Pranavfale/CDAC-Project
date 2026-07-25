import { useState } from "react";

const MyApplications = () => {
  const [statusFilter, setStatusFilter] = useState("All");

  const applications = [
    {
      id: 1,
      jobTitle: "Java Developer",
      company: "TechCorp Solutions",
      location: "Pune",
      appliedDate: "23 Jul 2026",
      status: "Shortlisted",
      type: "Full Time",
    },
    {
      id: 2,
      jobTitle: "React Developer",
      company: "SoftWave Technologies",
      location: "Mumbai",
      appliedDate: "21 Jul 2026",
      status: "Applied",
      type: "Full Time",
    },
    {
      id: 3,
      jobTitle: "Backend Developer",
      company: "NexGen Software",
      location: "Bengaluru",
      appliedDate: "19 Jul 2026",
      status: "Interview",
      type: "Full Time",
    },
    {
      id: 4,
      jobTitle: "Software Engineer",
      company: "Info Systems",
      location: "Pune",
      appliedDate: "17 Jul 2026",
      status: "Rejected",
      type: "Full Time",
    },
    {
      id: 5,
      jobTitle: "Full Stack Developer",
      company: "CloudTech India",
      location: "Pune",
      appliedDate: "15 Jul 2026",
      status: "Under Review",
      type: "Full Time",
    },
    {
      id: 6,
      jobTitle: "Frontend Developer Intern",
      company: "WebCraft Solutions",
      location: "Remote",
      appliedDate: "12 Jul 2026",
      status: "Selected",
      type: "Internship",
    },
  ];

  const statuses = [
    "All",
    "Applied",
    "Under Review",
    "Shortlisted",
    "Interview",
    "Selected",
    "Rejected",
  ];

  const filteredApplications =
    statusFilter === "All"
      ? applications
      : applications.filter(
          (application) =>
            application.status === statusFilter
        );

  const getStatusClass = (status) => {
    switch (status) {
      case "Under Review":
        return "candidate-app-status-review";

      case "Shortlisted":
        return "candidate-app-status-shortlisted";

      case "Interview":
        return "candidate-app-status-interview";

      case "Selected":
        return "candidate-app-status-selected";

      case "Rejected":
        return "candidate-app-status-rejected";

      default:
        return "candidate-app-status-applied";
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "Under Review":
        return "bi-hourglass-split";

      case "Shortlisted":
        return "bi-person-check";

      case "Interview":
        return "bi-calendar-check";

      case "Selected":
        return "bi-check-circle";

      case "Rejected":
        return "bi-x-circle";

      default:
        return "bi-send-check";
    }
  };

  const getStatusCount = (status) => {
    return applications.filter(
      (application) => application.status === status
    ).length;
  };

  return (
    <div className="candidate-applications-page">
      <div className="candidate-page-header">
        <div>
          <h2>My Applications</h2>

          <p>
            Track the progress of your job applications.
          </p>
        </div>

        <a
          href="/candidate/jobs"
          className="candidate-primary-button"
        >
          <i className="bi bi-search"></i>
          Find More Jobs
        </a>
      </div>

      {/* Summary */}

      <section className="candidate-application-summary">
        <article>
          <div className="candidate-app-summary-icon">
            <i className="bi bi-file-earmark-text"></i>
          </div>

          <div>
            <strong>{applications.length}</strong>
            <span>Total Applications</span>
          </div>
        </article>

        <article>
          <div className="candidate-app-summary-icon">
            <i className="bi bi-hourglass-split"></i>
          </div>

          <div>
            <strong>
              {getStatusCount("Under Review")}
            </strong>
            <span>Under Review</span>
          </div>
        </article>

        <article>
          <div className="candidate-app-summary-icon">
            <i className="bi bi-person-check"></i>
          </div>

          <div>
            <strong>
              {getStatusCount("Shortlisted")}
            </strong>
            <span>Shortlisted</span>
          </div>
        </article>

        <article>
          <div className="candidate-app-summary-icon">
            <i className="bi bi-calendar-check"></i>
          </div>

          <div>
            <strong>
              {getStatusCount("Interview")}
            </strong>
            <span>Interviews</span>
          </div>
        </article>
      </section>

      {/* Filter */}

      <section className="candidate-application-filter-card">
        <div>
          <h3>Application History</h3>
          <p>
            View and track all your submitted applications.
          </p>
        </div>

        <div className="candidate-application-filter">
          <i className="bi bi-funnel"></i>

          <select
            value={statusFilter}
            onChange={(event) =>
              setStatusFilter(event.target.value)
            }
          >
            {statuses.map((status) => (
              <option key={status} value={status}>
                {status === "All"
                  ? "All Statuses"
                  : status}
              </option>
            ))}
          </select>
        </div>
      </section>

      {/* Applications */}

      {filteredApplications.length > 0 ? (
        <section className="candidate-applications-list">
          {filteredApplications.map((application) => (
            <article
              className="candidate-application-card"
              key={application.id}
            >
              <div className="candidate-application-company">
                <div className="candidate-application-logo">
                  {application.company.charAt(0)}
                </div>

                <div>
                  <h3>{application.jobTitle}</h3>
                  <p>{application.company}</p>

                  <div className="candidate-application-meta">
                    <span>
                      <i className="bi bi-geo-alt"></i>
                      {application.location}
                    </span>

                    <span>
                      <i className="bi bi-briefcase"></i>
                      {application.type}
                    </span>

                    <span>
                      <i className="bi bi-calendar3"></i>
                      Applied {application.appliedDate}
                    </span>
                  </div>
                </div>
              </div>

              <div className="candidate-application-card-right">
                <span
                  className={`candidate-app-status ${getStatusClass(
                    application.status
                  )}`}
                >
                  <i
                    className={`bi ${getStatusIcon(
                      application.status
                    )}`}
                  ></i>

                  {application.status}
                </span>

                <a
                  href={`/candidate/jobs/${application.id}`}
                  className="candidate-application-view"
                >
                  View Job
                </a>
              </div>
            </article>
          ))}
        </section>
      ) : (
        <div className="candidate-no-applications">
          <i className="bi bi-file-earmark-x"></i>

          <h3>No applications found</h3>

          <p>
            You don't have applications with this status.
          </p>

          <button
            type="button"
            onClick={() => setStatusFilter("All")}
          >
            Show All Applications
          </button>
        </div>
      )}
    </div>
  );
};

export default MyApplications;