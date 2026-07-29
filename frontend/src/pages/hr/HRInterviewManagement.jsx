import { useState } from "react";
import "../../styles/hrInterview.css";

const initialForm = {
  candidateName: "",
  jobTitle: "",
  interviewer: "",
  date: "",
  time: "",
  mode: "Online",
};

function HRInterviewManagement() {
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("All");
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState(initialForm);

  const [interviews, setInterviews] = useState([
    {
      id: 1,
      candidateName: "Rahul Sharma",
      candidateEmail: "rahul@gmail.com",
      jobTitle: "Java Developer",
      interviewer: "Priya Patil",
      date: "2026-07-30",
      time: "11:00",
      mode: "Online",
      status: "Scheduled",
    },
    {
      id: 2,
      candidateName: "Sneha Joshi",
      candidateEmail: "sneha@gmail.com",
      jobTitle: "React Developer",
      interviewer: "Rohan Deshmukh",
      date: "2026-07-31",
      time: "14:30",
      mode: "Offline",
      status: "Scheduled",
    },
    {
      id: 3,
      candidateName: "Amit Kumar",
      candidateEmail: "amit@gmail.com",
      jobTitle: "Software Tester",
      interviewer: "Priya Patil",
      date: "2026-07-28",
      time: "10:00",
      mode: "Online",
      status: "Completed",
    },
    {
      id: 4,
      candidateName: "Neha More",
      candidateEmail: "neha@gmail.com",
      jobTitle: "Backend Developer",
      interviewer: "Rohan Deshmukh",
      date: "2026-07-29",
      time: "16:00",
      mode: "Offline",
      status: "Cancelled",
    },
  ]);

  const filteredInterviews = interviews.filter((interview) => {
    const search = searchTerm.toLowerCase();

    const matchesSearch =
      interview.candidateName.toLowerCase().includes(search) ||
      interview.candidateEmail.toLowerCase().includes(search) ||
      interview.jobTitle.toLowerCase().includes(search) ||
      interview.interviewer.toLowerCase().includes(search);

    const matchesStatus =
      statusFilter === "All" ||
      interview.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const handleInputChange = (event) => {
    const { name, value } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }));
  };

  const handleScheduleInterview = (event) => {
    event.preventDefault();

    if (
      !formData.candidateName.trim() ||
      !formData.jobTitle.trim() ||
      !formData.interviewer.trim() ||
      !formData.date ||
      !formData.time
    ) {
      window.alert("Please fill all interview details.");
      return;
    }

    const newInterview = {
      id: Date.now(),
      candidateName: formData.candidateName.trim(),
      candidateEmail: "candidate@example.com",
      jobTitle: formData.jobTitle.trim(),
      interviewer: formData.interviewer.trim(),
      date: formData.date,
      time: formData.time,
      mode: formData.mode,
      status: "Scheduled",
    };

    setInterviews((currentInterviews) => [
      newInterview,
      ...currentInterviews,
    ]);

    setFormData(initialForm);
    setShowForm(false);
  };

  const updateInterviewStatus = (id, newStatus) => {
    setInterviews((currentInterviews) =>
      currentInterviews.map((interview) =>
        interview.id === id
          ? {
              ...interview,
              status: newStatus,
            }
          : interview
      )
    );
  };

  const deleteInterview = (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this interview?"
    );

    if (!confirmed) {
      return;
    }

    setInterviews((currentInterviews) =>
      currentInterviews.filter(
        (interview) => interview.id !== id
      )
    );
  };

  const scheduledCount = interviews.filter(
    (interview) => interview.status === "Scheduled"
  ).length;

  const completedCount = interviews.filter(
    (interview) => interview.status === "Completed"
  ).length;

  const cancelledCount = interviews.filter(
    (interview) => interview.status === "Cancelled"
  ).length;

  return (
    <div className="hr-interview-page">
      <div className="hr-interview-header">
        <div>
          <h2>Interview Management</h2>
          <p>
            Schedule and manage candidate interviews.
          </p>
        </div>

        <button
          type="button"
          className="hr-schedule-button"
          onClick={() => setShowForm(true)}
        >
          <i className="bi bi-calendar-plus"></i>
          Schedule Interview
        </button>
      </div>

      <section className="hr-interview-summary">
        <article className="hr-interview-summary-card">
          <div className="hr-summary-icon">
            <i className="bi bi-calendar-event"></i>
          </div>

          <div>
            <span>Total Interviews</span>
            <strong>{interviews.length}</strong>
          </div>
        </article>

        <article className="hr-interview-summary-card">
          <div className="hr-summary-icon">
            <i className="bi bi-clock"></i>
          </div>

          <div>
            <span>Scheduled</span>
            <strong>{scheduledCount}</strong>
          </div>
        </article>

        <article className="hr-interview-summary-card">
          <div className="hr-summary-icon">
            <i className="bi bi-check-circle"></i>
          </div>

          <div>
            <span>Completed</span>
            <strong>{completedCount}</strong>
          </div>
        </article>

        <article className="hr-interview-summary-card">
          <div className="hr-summary-icon">
            <i className="bi bi-x-circle"></i>
          </div>

          <div>
            <span>Cancelled</span>
            <strong>{cancelledCount}</strong>
          </div>
        </article>
      </section>

      <section className="hr-interview-card">
        <div className="hr-interview-toolbar">
          <div className="hr-interview-search">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search candidate, job or interviewer..."
              value={searchTerm}
              onChange={(event) =>
                setSearchTerm(event.target.value)
              }
            />
          </div>

          <div className="hr-interview-filter">
            <i className="bi bi-funnel"></i>

            <select
              value={statusFilter}
              onChange={(event) =>
                setStatusFilter(event.target.value)
              }
            >
              <option value="All">All Status</option>
              <option value="Scheduled">Scheduled</option>
              <option value="Completed">Completed</option>
              <option value="Cancelled">Cancelled</option>
            </select>
          </div>
        </div>

        <div className="hr-interview-table-wrapper">
          <table className="hr-interview-table">
            <thead>
              <tr>
                <th>Candidate</th>
                <th>Job Position</th>
                <th>Interviewer</th>
                <th>Date & Time</th>
                <th>Mode</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredInterviews.length > 0 ? (
                filteredInterviews.map((interview) => (
                  <tr key={interview.id}>
                    <td>
                      <div className="hr-candidate-cell">
                        <div className="hr-candidate-avatar">
                          {interview.candidateName
                            .charAt(0)
                            .toUpperCase()}
                        </div>

                        <div>
                          <strong>
                            {interview.candidateName}
                          </strong>
                          <span>
                            {interview.candidateEmail}
                          </span>
                        </div>
                      </div>
                    </td>

                    <td>{interview.jobTitle}</td>

                    <td>{interview.interviewer}</td>

                    <td>
                      <div className="hr-date-cell">
                        <strong>{interview.date}</strong>
                        <span>{interview.time}</span>
                      </div>
                    </td>

                    <td>
                      <span
                        className={`hr-mode-badge ${
                          interview.mode === "Online"
                            ? "hr-mode-online"
                            : "hr-mode-offline"
                        }`}
                      >
                        {interview.mode}
                      </span>
                    </td>

                    <td>
                      <span
                        className={`hr-interview-status hr-status-${interview.status.toLowerCase()}`}
                      >
                        {interview.status}
                      </span>
                    </td>

                    <td>
                      <div className="hr-interview-actions">
                        {interview.status === "Scheduled" && (
                          <>
                            <button
                              type="button"
                              className="hr-action-button hr-complete-button"
                              title="Mark completed"
                              onClick={() =>
                                updateInterviewStatus(
                                  interview.id,
                                  "Completed"
                                )
                              }
                            >
                              <i className="bi bi-check-lg"></i>
                            </button>

                            <button
                              type="button"
                              className="hr-action-button hr-cancel-button"
                              title="Cancel interview"
                              onClick={() =>
                                updateInterviewStatus(
                                  interview.id,
                                  "Cancelled"
                                )
                              }
                            >
                              <i className="bi bi-x-lg"></i>
                            </button>
                          </>
                        )}

                        <button
                          type="button"
                          className="hr-action-button hr-delete-button"
                          title="Delete interview"
                          onClick={() =>
                            deleteInterview(interview.id)
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
                    colSpan="7"
                    className="hr-interview-empty"
                  >
                    <i className="bi bi-calendar-x"></i>
                    <p>No interviews found.</p>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {showForm && (
        <div className="hr-modal-overlay">
          <div className="hr-interview-modal">
            <div className="hr-modal-header">
              <div>
                <h3>Schedule Interview</h3>
                <p>Enter candidate interview details.</p>
              </div>

              <button
                type="button"
                className="hr-modal-close"
                onClick={() => {
                  setShowForm(false);
                  setFormData(initialForm);
                }}
              >
                ×
              </button>
            </div>

            <form onSubmit={handleScheduleInterview}>
              <div className="hr-modal-body">
                <div className="hr-form-group">
                  <label htmlFor="candidateName">
                    Candidate Name
                  </label>

                  <input
                    id="candidateName"
                    name="candidateName"
                    type="text"
                    placeholder="Enter candidate name"
                    value={formData.candidateName}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="jobTitle">
                    Job Position
                  </label>

                  <input
                    id="jobTitle"
                    name="jobTitle"
                    type="text"
                    placeholder="Enter job position"
                    value={formData.jobTitle}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="hr-form-group">
                  <label htmlFor="interviewer">
                    Interviewer
                  </label>

                  <input
                    id="interviewer"
                    name="interviewer"
                    type="text"
                    placeholder="Enter interviewer name"
                    value={formData.interviewer}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="hr-form-row">
                  <div className="hr-form-group">
                    <label htmlFor="date">Date</label>

                    <input
                      id="date"
                      name="date"
                      type="date"
                      value={formData.date}
                      onChange={handleInputChange}
                    />
                  </div>

                  <div className="hr-form-group">
                    <label htmlFor="time">Time</label>

                    <input
                      id="time"
                      name="time"
                      type="time"
                      value={formData.time}
                      onChange={handleInputChange}
                    />
                  </div>
                </div>

                <div className="hr-form-group">
                  <label htmlFor="mode">
                    Interview Mode
                  </label>

                  <select
                    id="mode"
                    name="mode"
                    value={formData.mode}
                    onChange={handleInputChange}
                  >
                    <option value="Online">Online</option>
                    <option value="Offline">Offline</option>
                  </select>
                </div>
              </div>

              <div className="hr-modal-footer">
                <button
                  type="button"
                  className="hr-secondary-button"
                  onClick={() => {
                    setShowForm(false);
                    setFormData(initialForm);
                  }}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="hr-primary-button"
                >
                  Schedule Interview
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default HRInterviewManagement;