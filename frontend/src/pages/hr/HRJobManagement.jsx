import { useMemo, useState } from "react";

const emptyJob = {
  title: "",
  department: "",
  location: "",
  jobType: "Full Time",
  experience: "",
  vacancies: 1,
  closingDate: "",
  status: "Open",
};

function HRJobManagement() {
  const [jobs, setJobs] = useState([
    {
      id: 1,
      title: "Java Developer",
      department: "Engineering",
      location: "Pune",
      jobType: "Full Time",
      experience: "2-4 Years",
      vacancies: 4,
      applicants: 38,
      closingDate: "2026-08-10",
      status: "Open",
    },
    {
      id: 2,
      title: "React Developer",
      department: "Engineering",
      location: "Mumbai",
      jobType: "Full Time",
      experience: "1-3 Years",
      vacancies: 3,
      applicants: 27,
      closingDate: "2026-08-15",
      status: "Open",
    },
    {
      id: 3,
      title: "Software Tester",
      department: "Quality Assurance",
      location: "Pune",
      jobType: "Full Time",
      experience: "1-2 Years",
      vacancies: 2,
      applicants: 19,
      closingDate: "2026-07-30",
      status: "Closed",
    },
    {
      id: 4,
      title: "Frontend Intern",
      department: "Engineering",
      location: "Remote",
      jobType: "Internship",
      experience: "Fresher",
      vacancies: 5,
      applicants: 46,
      closingDate: "2026-08-20",
      status: "Open",
    },
  ]);

  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("All");
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState(emptyJob);
  const [formError, setFormError] = useState("");

  const filteredJobs = useMemo(() => {
    const search = searchTerm.trim().toLowerCase();

    return jobs.filter((job) => {
      const matchesSearch =
        job.title.toLowerCase().includes(search) ||
        job.department.toLowerCase().includes(search) ||
        job.location.toLowerCase().includes(search);

      const matchesStatus =
        statusFilter === "All" || job.status === statusFilter;

      return matchesSearch && matchesStatus;
    });
  }, [jobs, searchTerm, statusFilter]);

  const openJobs = jobs.filter((job) => job.status === "Open").length;

  const closedJobs = jobs.filter(
    (job) => job.status === "Closed"
  ).length;

  const totalApplicants = jobs.reduce(
    (total, job) => total + job.applicants,
    0
  );

  const handleInputChange = (event) => {
    const { name, value } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]:
        name === "vacancies"
          ? Number(value)
          : value,
    }));
  };

  const openAddForm = () => {
    setEditingId(null);
    setFormData(emptyJob);
    setFormError("");
    setShowForm(true);
  };

  const openEditForm = (job) => {
    setEditingId(job.id);

    setFormData({
      title: job.title,
      department: job.department,
      location: job.location,
      jobType: job.jobType,
      experience: job.experience,
      vacancies: job.vacancies,
      closingDate: job.closingDate,
      status: job.status,
    });

    setFormError("");
    setShowForm(true);
  };

  const closeForm = () => {
    setShowForm(false);
    setEditingId(null);
    setFormData(emptyJob);
    setFormError("");
  };

  const validateForm = () => {
    if (
      !formData.title.trim() ||
      !formData.department.trim() ||
      !formData.location.trim() ||
      !formData.experience.trim() ||
      !formData.closingDate
    ) {
      setFormError("Please complete all required fields.");
      return false;
    }

    if (formData.vacancies < 1) {
      setFormError("Vacancies must be at least 1.");
      return false;
    }

    return true;
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!validateForm()) {
      return;
    }

    if (editingId) {
      setJobs((currentJobs) =>
        currentJobs.map((job) =>
          job.id === editingId
            ? {
                ...job,
                ...formData,
              }
            : job
        )
      );
    } else {
      const newJob = {
        id: Date.now(),
        ...formData,
        applicants: 0,
      };

      setJobs((currentJobs) => [newJob, ...currentJobs]);
    }

    closeForm();
  };

  const toggleJobStatus = (id) => {
    setJobs((currentJobs) =>
      currentJobs.map((job) =>
        job.id === id
          ? {
              ...job,
              status:
                job.status === "Open"
                  ? "Closed"
                  : "Open",
            }
          : job
      )
    );
  };

  const deleteJob = (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this vacancy?"
    );

    if (!confirmed) {
      return;
    }

    setJobs((currentJobs) =>
      currentJobs.filter((job) => job.id !== id)
    );
  };

  return (
    <div className="hr-job-page">
      <div className="hr-page-header">
        <div>
          <h2>Job Management</h2>
          <p>Create and manage recruitment vacancies.</p>
        </div>

        <button
          className="hr-primary-button"
          type="button"
          onClick={openAddForm}
        >
          <i className="bi bi-plus-lg"></i>
          Add Vacancy
        </button>
      </div>

      <section className="hr-job-stats">
        <article className="hr-stat-card">
          <span>Total Vacancies</span>
          <strong>{jobs.length}</strong>
        </article>

        <article className="hr-stat-card">
          <span>Open Vacancies</span>
          <strong>{openJobs}</strong>
        </article>

        <article className="hr-stat-card">
          <span>Closed Vacancies</span>
          <strong>{closedJobs}</strong>
        </article>

        <article className="hr-stat-card">
          <span>Total Applicants</span>
          <strong>{totalApplicants}</strong>
        </article>
      </section>

      <section className="hr-management-card">
        <div className="hr-management-toolbar">
          <div className="hr-search-box">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search title, department or location..."
              value={searchTerm}
              onChange={(event) =>
                setSearchTerm(event.target.value)
              }
            />
          </div>

          <select
            className="hr-filter-select"
            value={statusFilter}
            onChange={(event) =>
              setStatusFilter(event.target.value)
            }
          >
            <option value="All">All Status</option>
            <option value="Open">Open</option>
            <option value="Closed">Closed</option>
          </select>
        </div>

        <div className="hr-table-wrapper">
          <table className="hr-job-table">
            <thead>
              <tr>
                <th>Job</th>
                <th>Location</th>
                <th>Type</th>
                <th>Vacancies</th>
                <th>Applicants</th>
                <th>Closing Date</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredJobs.length > 0 ? (
                filteredJobs.map((job) => (
                  <tr key={job.id}>
                    <td>
                      <div className="hr-job-title">
                        <strong>{job.title}</strong>
                        <span>
                          {job.department} · {job.experience}
                        </span>
                      </div>
                    </td>

                    <td>{job.location}</td>

                    <td>
                      <span className="hr-job-type">
                        {job.jobType}
                      </span>
                    </td>

                    <td>{job.vacancies}</td>
                    <td>{job.applicants}</td>
                    <td>{job.closingDate}</td>

                    <td>
                      <span
                        className={`hr-status-badge ${
                          job.status === "Open"
                            ? "hr-status-open"
                            : "hr-status-closed"
                        }`}
                      >
                        {job.status}
                      </span>
                    </td>

                    <td>
                      <div className="hr-action-buttons">
                        <button
                          type="button"
                          className="hr-action-button hr-edit-button"
                          title="Edit job"
                          onClick={() => openEditForm(job)}
                        >
                          <i className="bi bi-pencil"></i>
                        </button>

                        <button
                          type="button"
                          className="hr-action-button hr-status-button"
                          title={
                            job.status === "Open"
                              ? "Close job"
                              : "Reopen job"
                          }
                          onClick={() =>
                            toggleJobStatus(job.id)
                          }
                        >
                          <i
                            className={`bi ${
                              job.status === "Open"
                                ? "bi-pause-circle"
                                : "bi-play-circle"
                            }`}
                          ></i>
                        </button>

                        <button
                          type="button"
                          className="hr-action-button hr-delete-button"
                          title="Delete job"
                          onClick={() => deleteJob(job.id)}
                        >
                          <i className="bi bi-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="8" className="hr-empty-state">
                    No vacancies found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {showForm && (
        <div
          className="hr-modal-overlay"
          onMouseDown={closeForm}
        >
          <div
            className="hr-job-modal"
            onMouseDown={(event) => event.stopPropagation()}
          >
            <div className="hr-modal-header">
              <div>
                <h3>
                  {editingId
                    ? "Edit Vacancy"
                    : "Create Vacancy"}
                </h3>

                <p>
                  Enter the vacancy information below.
                </p>
              </div>

              <button
                type="button"
                className="hr-modal-close"
                onClick={closeForm}
              >
                ×
              </button>
            </div>

            <form
              className="hr-job-form"
              onSubmit={handleSubmit}
            >
              {formError && (
                <div className="hr-form-error">
                  {formError}
                </div>
              )}

              <div className="hr-form-grid">
                <div className="hr-form-group">
                  <label htmlFor="title">Job Title *</label>

                  <input
                    id="title"
                    name="title"
                    type="text"
                    value={formData.title}
                    onChange={handleInputChange}
                    placeholder="Java Developer"
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="department">
                    Department *
                  </label>

                  <input
                    id="department"
                    name="department"
                    type="text"
                    value={formData.department}
                    onChange={handleInputChange}
                    placeholder="Engineering"
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="location">
                    Location *
                  </label>

                  <input
                    id="location"
                    name="location"
                    type="text"
                    value={formData.location}
                    onChange={handleInputChange}
                    placeholder="Pune"
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="jobType">
                    Job Type
                  </label>

                  <select
                    id="jobType"
                    name="jobType"
                    value={formData.jobType}
                    onChange={handleInputChange}
                  >
                    <option>Full Time</option>
                    <option>Part Time</option>
                    <option>Internship</option>
                    <option>Contract</option>
                  </select>
                </div>

                <div className="hr-form-group">
                  <label htmlFor="experience">
                    Experience *
                  </label>

                  <input
                    id="experience"
                    name="experience"
                    type="text"
                    value={formData.experience}
                    onChange={handleInputChange}
                    placeholder="2-4 Years"
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="vacancies">
                    Number of Vacancies
                  </label>

                  <input
                    id="vacancies"
                    name="vacancies"
                    type="number"
                    min="1"
                    value={formData.vacancies}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="closingDate">
                    Closing Date *
                  </label>

                  <input
                    id="closingDate"
                    name="closingDate"
                    type="date"
                    value={formData.closingDate}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="status">Status</label>

                  <select
                    id="status"
                    name="status"
                    value={formData.status}
                    onChange={handleInputChange}
                  >
                    <option>Open</option>
                    <option>Closed</option>
                  </select>
                </div>
              </div>

              <div className="hr-form-actions">
                <button
                  className="hr-secondary-button"
                  type="button"
                  onClick={closeForm}
                >
                  Cancel
                </button>

                <button
                  className="hr-primary-button"
                  type="submit"
                >
                  {editingId
                    ? "Update Vacancy"
                    : "Create Vacancy"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default HRJobManagement;