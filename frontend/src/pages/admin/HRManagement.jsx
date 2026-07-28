import { useState } from "react";

const HRManagement = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("All");

  const [hrUsers, setHrUsers] = useState([
    {
      id: 1,
      name: "Priya Patil",
      email: "priya@techcorp.com",
      company: "TechCorp Solutions",
      designation: "HR Manager",
      jobsPosted: 12,
      status: "Active",
      verification: "Verified",
    },
    {
      id: 2,
      name: "Rohan Deshmukh",
      email: "rohan@infosystems.com",
      company: "Info Systems",
      designation: "Technical Recruiter",
      jobsPosted: 8,
      status: "Active",
      verification: "Verified",
    },
    {
      id: 3,
      name: "Sneha Kulkarni",
      email: "sneha@softwave.com",
      company: "SoftWave Technologies",
      designation: "Talent Acquisition",
      jobsPosted: 5,
      status: "Inactive",
      verification: "Verified",
    },
    {
      id: 4,
      name: "Aakash More",
      email: "aakash@nexgen.com",
      company: "NexGen Software",
      designation: "HR Executive",
      jobsPosted: 3,
      status: "Active",
      verification: "Pending",
    },
    {
      id: 5,
      name: "Neha Joshi",
      email: "neha@cloudtech.com",
      company: "CloudTech India",
      designation: "Recruitment Manager",
      jobsPosted: 9,
      status: "Active",
      verification: "Pending",
    },
  ]);

  const filteredHR = hrUsers.filter((hr) => {
    const search = searchTerm.toLowerCase();

    const matchesSearch =
      hr.name.toLowerCase().includes(search) ||
      hr.email.toLowerCase().includes(search) ||
      hr.company.toLowerCase().includes(search);

    const matchesStatus =
      statusFilter === "All" ||
      hr.status === statusFilter;

    return matchesSearch && matchesStatus;
  });

  const toggleStatus = (id) => {
    setHrUsers((currentUsers) =>
      currentUsers.map((hr) =>
        hr.id === id
          ? {
              ...hr,
              status:
                hr.status === "Active"
                  ? "Inactive"
                  : "Active",
            }
          : hr
      )
    );
  };

  const verifyHR = (id) => {
    setHrUsers((currentUsers) =>
      currentUsers.map((hr) =>
        hr.id === id
          ? {
              ...hr,
              verification: "Verified",
            }
          : hr
      )
    );
  };

  const deleteHR = (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this HR account?"
    );

    if (!confirmed) {
      return;
    }

    setHrUsers((currentUsers) =>
      currentUsers.filter((hr) => hr.id !== id)
    );
  };

  const activeHRCount = hrUsers.filter(
    (hr) => hr.status === "Active"
  ).length;

  const verifiedCount = hrUsers.filter(
    (hr) => hr.verification === "Verified"
  ).length;

  const pendingCount = hrUsers.filter(
    (hr) => hr.verification === "Pending"
  ).length;

  return (
    <div className="admin-hr-page">
      <div className="admin-page-header">
        <div>
          <h2>HR Management</h2>
          <p>
            Manage HR accounts, companies and verification
            status.
          </p>
        </div>

        <div className="admin-user-count">
          <i className="bi bi-person-badge"></i>
          <span>{hrUsers.length} HR Accounts</span>
        </div>
      </div>

      {/* Summary Cards */}

      <section className="admin-user-summary">
        <div className="admin-summary-card">
          <span>Total HR</span>
          <strong>{hrUsers.length}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Active HR</span>
          <strong>{activeHRCount}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Verified</span>
          <strong>{verifiedCount}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Pending Verification</span>
          <strong>{pendingCount}</strong>
        </div>
      </section>

      <section className="admin-management-card">

        {/* Search and Filter */}

        <div className="admin-management-toolbar">
          <div className="admin-search-box">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search HR, email or company..."
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

        {/* HR Table */}

        <div className="admin-table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>HR</th>
                <th>Company</th>
                <th>Jobs</th>
                <th>Verification</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredHR.length > 0 ? (
                filteredHR.map((hr) => (
                  <tr key={hr.id}>
                    <td>
                      <div className="admin-user-cell">
                        <div className="admin-table-avatar">
                          {hr.name.charAt(0)}
                        </div>

                        <div>
                          <strong>{hr.name}</strong>
                          <span>{hr.email}</span>
                        </div>
                      </div>
                    </td>

                    <td>
                      <div className="admin-company-cell">
                        <strong>{hr.company}</strong>
                        <span>
                          {hr.designation}
                        </span>
                      </div>
                    </td>

                    <td>
                      <span className="admin-job-count">
                        {hr.jobsPosted}
                      </span>
                    </td>

                    <td>
                      <span
                        className={`admin-verification-badge ${
                          hr.verification === "Verified"
                            ? "admin-verified"
                            : "admin-pending"
                        }`}
                      >
                        {hr.verification}
                      </span>
                    </td>

                    <td>
                      <span
                        className={`admin-status ${
                          hr.status === "Active"
                            ? "admin-status-active"
                            : "admin-status-inactive"
                        }`}
                      >
                        {hr.status}
                      </span>
                    </td>

                    <td>
                      <div className="admin-action-buttons">

                        <button
                          className="admin-action-button admin-view-button"
                          type="button"
                          title="View HR"
                        >
                          <i className="bi bi-eye"></i>
                        </button>

                        {hr.verification === "Pending" && (
                          <button
                            className="admin-action-button admin-verify-button"
                            type="button"
                            title="Verify HR"
                            onClick={() =>
                              verifyHR(hr.id)
                            }
                          >
                            <i className="bi bi-patch-check"></i>
                          </button>
                        )}

                        <button
                          className="admin-action-button admin-status-button"
                          type="button"
                          title={
                            hr.status === "Active"
                              ? "Deactivate HR"
                              : "Activate HR"
                          }
                          onClick={() =>
                            toggleStatus(hr.id)
                          }
                        >
                          <i
                            className={`bi ${
                              hr.status === "Active"
                                ? "bi-person-x"
                                : "bi-person-check"
                            }`}
                          ></i>
                        </button>

                        <button
                          className="admin-action-button admin-delete-button"
                          type="button"
                          title="Delete HR"
                          onClick={() =>
                            deleteHR(hr.id)
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
                    colSpan="6"
                    className="admin-empty-state"
                  >
                    <i className="bi bi-search"></i>
                    <p>No HR accounts found.</p>
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

export default HRManagement;