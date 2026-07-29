import { useState } from "react";

const JobManagement = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("All");

  const [jobs, setJobs] = useState([
    {
      id: 1,
      title: "Java Developer",
      company: "TechCorp Solutions",
      hr: "Priya Patil",
      location: "Pune",
      type: "Full Time",
      applicants: 42,
      postedDate: "20 Jul 2026",
      status: "Active",
    },
    {
      id: 2,
      title: "React Developer",
      company: "SoftWave Technologies",
      hr: "Sneha Kulkarni",
      location: "Mumbai",
      type: "Full Time",
      applicants: 35,
      postedDate: "19 Jul 2026",
      status: "Active",
    },
    {
      id: 3,
      title: "Software Tester",
      company: "Info Systems",
      hr: "Rohan Deshmukh",
      location: "Pune",
      type: "Full Time",
      applicants: 27,
      postedDate: "18 Jul 2026",
      status: "Inactive",
    },
    {
      id: 4,
      title: "Backend Developer",
      company: "NexGen Software",
      hr: "Aakash More",
      location: "Bengaluru",
      type: "Full Time",
      applicants: 31,
      postedDate: "17 Jul 2026",
      status: "Active",
    },
    {
      id: 5,
      title: "Frontend Intern",
      company: "CloudTech India",
      hr: "Neha Joshi",
      location: "Remote",
      type: "Internship",
      applicants: 56,
      postedDate: "16 Jul 2026",
      status: "Active",
    },
    {
      id: 6,
      title: "Database Administrator",
      company: "DataCore Systems",
      hr: "Amit Shah",
      location: "Hyderabad",
      type: "Full Time",
      applicants: 18,
      postedDate: "15 Jul 2026",
      status: "Inactive",
    },
  ]);

  const filteredJobs = jobs.filter((job) => {
    const search = searchTerm.toLowerCase();

    const matchesSearch =
      job.title.toLowerCase().includes(search) ||
      job.company.toLowerCase().includes(search) ||
      job.hr.toLowerCase().includes(search) ||
      job.location.toLowerCase().includes(search);

    const matchesStatus =
      statusFilter === "All" ||
      job.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const toggleJobStatus = (id) => {
    setJobs((currentJobs) =>
      currentJobs.map((job) =>
        job.id === id
          ? {
              ...job,
              status:
                job.status === "Active"
                  ? "Inactive"
                  : "Active",
            }
          : job
      )
    );
  };

  const deleteJob = (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to remove this job posting?"
    );

    if (!confirmed) {
      return;
    }

    setJobs((currentJobs) =>
      currentJobs.filter((job) => job.id !== id)
    );
  };

  const activeJobs = jobs.filter(
    (job) => job.status === "Active"
  ).length;

  const inactiveJobs = jobs.filter(
    (job) => job.status === "Inactive"
  ).length;

  const totalApplicants = jobs.reduce(
    (total, job) => total + job.applicants,
    0
  );

  return (
    <div className="admin-jobs-page">
      <div className="admin-page-header">
        <div>
          <h2>Job Management</h2>
          <p>
            Monitor and manage job postings across the
            recruitment platform.
          </p>
        </div>

        <div className="admin-user-count">
          <i className="bi bi-briefcase"></i>
          <span>{jobs.length} Jobs</span>
        </div>
      </div>

      {/* Summary */}

      <section className="admin-user-summary">
        <div className="admin-summary-card">
          <span>Total Jobs</span>
          <strong>{jobs.length}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Active Jobs</span>
          <strong>{activeJobs}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Inactive Jobs</span>
          <strong>{inactiveJobs}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Total Applicants</span>
          <strong>{totalApplicants}</strong>
        </div>
      </section>

      <section className="admin-management-card">

        {/* Search + Filter */}

        <div className="admin-management-toolbar">
          <div className="admin-search-box">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search job, company, HR or location..."
              value={searchTerm}
              onChange={(event) =>
                setSearchTerm(event.target.value)
              }
            />
          </div>

          <div className="admin-filter-box">
            <i className="bi bi-funnel"></i>

            <select
              value={statusFilter}
              onChange={(event) =>
                setStatusFilter(event.target.value)
              }
            >
              <option value="All">All Status</option>
              <option value="Active">Active</option>
              <option value="Inactive">Inactive</option>
            </select>
          </div>
        </div>

        {/* Job Table */}

        <div className="admin-table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Job</th>
                <th>Company / HR</th>
                <th>Location</th>
                <th>Type</th>
                <th>Applicants</th>
                <th>Posted</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredJobs.length > 0 ? (
                filteredJobs.map((job) => (
                  <tr key={job.id}>
                    <td>
                      <div className="admin-job-title-cell">
                        <div className="admin-job-icon">
                          <i className="bi bi-briefcase"></i>
                        </div>

                        <strong>{job.title}</strong>
                      </div>
                    </td>

                    <td>
                      <div className="admin-company-cell">
                        <strong>{job.company}</strong>
                        <span>HR: {job.hr}</span>
                      </div>
                    </td>

                    <td>
                      <div className="admin-location-cell">
                        <i className="bi bi-geo-alt"></i>
                        <span>{job.location}</span>
                      </div>
                    </td>

                    <td>
                      <span
                        className={`admin-job-type ${
                          job.type === "Internship"
                            ? "admin-job-internship"
                            : ""
                        }`}
                      >
                        {job.type}
                      </span>
                    </td>

                    <td>
                      <span className="admin-application-count">
                        {job.applicants}
                      </span>
                    </td>

                    <td>{job.postedDate}</td>

                    <td>
                      <span
                        className={`admin-status ${
                          job.status === "Active"
                            ? "admin-status-active"
                            : "admin-status-inactive"
                        }`}
                      >
                        {job.status}
                      </span>
                    </td>

                    <td>
                      <div className="admin-action-buttons">
                        <button
                          className="admin-action-button admin-view-button"
                          type="button"
                          title="View Job"
                        >
                          <i className="bi bi-eye"></i>
                        </button>

                        <button
                          className="admin-action-button admin-status-button"
                          type="button"
                          title={
                            job.status === "Active"
                              ? "Deactivate Job"
                              : "Activate Job"
                          }
                          onClick={() =>
                            toggleJobStatus(job.id)
                          }
                        >
                          <i
                            className={`bi ${
                              job.status === "Active"
                                ? "bi-pause-circle"
                                : "bi-play-circle"
                            }`}
                          ></i>
                        </button>

                        <button
                          className="admin-action-button admin-delete-button"
                          type="button"
                          title="Remove Job"
                          onClick={() =>
                            deleteJob(job.id)
                          }
                        >
                          <i className="bi bi-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan="8"
                    className="admin-empty-state"
                  >
                    <i className="bi bi-briefcase"></i>
                    <p>No jobs found.</p>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
};

export default JobManagement;