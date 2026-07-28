import { useState } from "react";

const CandidateManagement = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("All");

  const [candidates, setCandidates] = useState([
    {
      id: 1,
      name: "Rahul Sharma",
      email: "rahul@gmail.com",
      qualification: "B.Tech Computer Science",
      location: "Pune",
      applications: 8,
      profileCompletion: 90,
      status: "Active",
    },
    {
      id: 2,
      name: "Sneha Patil",
      email: "sneha@gmail.com",
      qualification: "MCA",
      location: "Mumbai",
      applications: 5,
      profileCompletion: 100,
      status: "Active",
    },
    {
      id: 3,
      name: "Amit Kumar",
      email: "amit@gmail.com",
      qualification: "B.E. Information Technology",
      location: "Nashik",
      applications: 3,
      profileCompletion: 75,
      status: "Inactive",
    },
    {
      id: 4,
      name: "Neha Deshmukh",
      email: "neha@gmail.com",
      qualification: "B.Tech Electronics",
      location: "Pune",
      applications: 11,
      profileCompletion: 85,
      status: "Active",
    },
    {
      id: 5,
      name: "Rohit More",
      email: "rohit@gmail.com",
      qualification: "M.Sc Computer Science",
      location: "Chhatrapati Sambhajinagar",
      applications: 2,
      profileCompletion: 60,
      status: "Inactive",
    },
    {
      id: 6,
      name: "Priyanka Jadhav",
      email: "priyanka@gmail.com",
      qualification: "BCA",
      location: "Kolhapur",
      applications: 6,
      profileCompletion: 95,
      status: "Active",
    },
  ]);

  const filteredCandidates = candidates.filter((candidate) => {
    const search = searchTerm.toLowerCase();

    const matchesSearch =
      candidate.name.toLowerCase().includes(search) ||
      candidate.email.toLowerCase().includes(search) ||
      candidate.qualification.toLowerCase().includes(search) ||
      candidate.location.toLowerCase().includes(search);

    const matchesStatus =
      statusFilter === "All" ||
      candidate.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const toggleStatus = (id) => {
    setCandidates((currentCandidates) =>
      currentCandidates.map((candidate) =>
        candidate.id === id
          ? {
              ...candidate,
              status:
                candidate.status === "Active"
                  ? "Inactive"
                  : "Active",
            }
          : candidate
      )
    );
  };

  const deleteCandidate = (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this candidate?"
    );

    if (!confirmed) {
      return;
    }

    setCandidates((currentCandidates) =>
      currentCandidates.filter(
        (candidate) => candidate.id !== id
      )
    );
  };

  const activeCandidates = candidates.filter(
    (candidate) => candidate.status === "Active"
  ).length;

  const inactiveCandidates = candidates.filter(
    (candidate) => candidate.status === "Inactive"
  ).length;

  const totalApplications = candidates.reduce(
    (total, candidate) => total + candidate.applications,
    0
  );

  return (
    <div className="admin-candidates-page">
      <div className="admin-page-header">
        <div>
          <h2>Candidate Management</h2>

          <p>
            View and manage candidates registered on the
            recruitment platform.
          </p>
        </div>

        <div className="admin-user-count">
          <i className="bi bi-person-vcard"></i>

          <span>
            {candidates.length} Candidates
          </span>
        </div>
      </div>

      {/* Summary Cards */}

      <section className="admin-user-summary">
        <div className="admin-summary-card">
          <span>Total Candidates</span>
          <strong>{candidates.length}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Active Candidates</span>
          <strong>{activeCandidates}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Inactive Candidates</span>
          <strong>{inactiveCandidates}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Total Applications</span>
          <strong>{totalApplications}</strong>
        </div>
      </section>

      {/* Candidate Management */}

      <section className="admin-management-card">

        {/* Search and Filter */}

        <div className="admin-management-toolbar">
          <div className="admin-search-box">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search candidate, qualification or location..."
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
              <option value="All">
                All Status
              </option>

              <option value="Active">
                Active
              </option>

              <option value="Inactive">
                Inactive
              </option>
            </select>
          </div>
        </div>

        {/* Candidate Table */}

        <div className="admin-table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Candidate</th>
                <th>Qualification</th>
                <th>Location</th>
                <th>Applications</th>
                <th>Profile</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredCandidates.length > 0 ? (
                filteredCandidates.map((candidate) => (
                  <tr key={candidate.id}>

                    {/* Candidate */}

                    <td>
                      <div className="admin-user-cell">
                        <div className="admin-table-avatar">
                          {candidate.name.charAt(0)}
                        </div>

                        <div>
                          <strong>
                            {candidate.name}
                          </strong>

                          <span>
                            {candidate.email}
                          </span>
                        </div>
                      </div>
                    </td>

                    {/* Qualification */}

                    <td>
                      <span className="admin-candidate-qualification">
                        {candidate.qualification}
                      </span>
                    </td>

                    {/* Location */}

                    <td>
                      <div className="admin-location-cell">
                        <i className="bi bi-geo-alt"></i>

                        <span>
                          {candidate.location}
                        </span>
                      </div>
                    </td>

                    {/* Applications */}

                    <td>
                      <span className="admin-application-count">
                        {candidate.applications}
                      </span>
                    </td>

                    {/* Profile Completion */}

                    <td>
                      <div className="admin-profile-progress">
                        <div className="admin-progress-info">
                          <span>
                            {candidate.profileCompletion}%
                          </span>
                        </div>

                        <div className="admin-progress-bar">
                          <div
                            className="admin-progress-fill"
                            style={{
                              width: `${candidate.profileCompletion}%`,
                            }}
                          ></div>
                        </div>
                      </div>
                    </td>

                    {/* Status */}

                    <td>
                      <span
                        className={`admin-status ${
                          candidate.status === "Active"
                            ? "admin-status-active"
                            : "admin-status-inactive"
                        }`}
                      >
                        {candidate.status}
                      </span>
                    </td>

                    {/* Actions */}

                    <td>
                      <div className="admin-action-buttons">

                        <button
                          className="admin-action-button admin-view-button"
                          type="button"
                          title="View Candidate"
                        >
                          <i className="bi bi-eye"></i>
                        </button>

                        <button
                          className="admin-action-button admin-status-button"
                          type="button"
                          title={
                            candidate.status === "Active"
                              ? "Deactivate Candidate"
                              : "Activate Candidate"
                          }
                          onClick={() =>
                            toggleStatus(candidate.id)
                          }
                        >
                          <i
                            className={`bi ${
                              candidate.status === "Active"
                                ? "bi-person-x"
                                : "bi-person-check"
                            }`}
                          ></i>
                        </button>

                        <button
                          className="admin-action-button admin-delete-button"
                          type="button"
                          title="Delete Candidate"
                          onClick={() =>
                            deleteCandidate(candidate.id)
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
                    className="admin-empty-state"
                  >
                    <i className="bi bi-search"></i>

                    <p>
                      No candidates found.
                    </p>
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

export default CandidateManagement;