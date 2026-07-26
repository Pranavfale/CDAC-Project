import { useState } from "react";

const UserManagement = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [roleFilter, setRoleFilter] = useState("All");
  const [users, setUsers] = useState([
    {
      id: 1,
      name: "Rahul Sharma",
      email: "rahul@example.com",
      role: "Candidate",
      status: "Active",
      joined: "20 Jul 2026",
    },
    {
      id: 2,
      name: "Priya Patil",
      email: "priya@example.com",
      role: "HR",
      status: "Active",
      joined: "19 Jul 2026",
    },
    {
      id: 3,
      name: "Amit Kumar",
      email: "amit@example.com",
      role: "Candidate",
      status: "Inactive",
      joined: "18 Jul 2026",
    },
    {
      id: 4,
      name: "Sneha Joshi",
      email: "sneha@example.com",
      role: "Candidate",
      status: "Active",
      joined: "17 Jul 2026",
    },
    {
      id: 5,
      name: "Rohit More",
      email: "rohit@example.com",
      role: "HR",
      status: "Inactive",
      joined: "16 Jul 2026",
    },
    {
      id: 6,
      name: "Neha Deshmukh",
      email: "neha@example.com",
      role: "Candidate",
      status: "Active",
      joined: "15 Jul 2026",
    },
  ]);

  const filteredUsers = users.filter((user) => {
    const search = searchTerm.toLowerCase();

    const matchesSearch =
      user.name.toLowerCase().includes(search) ||
      user.email.toLowerCase().includes(search);

    const matchesRole =
      roleFilter === "All" || user.role === roleFilter;

    return matchesSearch && matchesRole;
  });

  const toggleStatus = (id) => {
    setUsers((currentUsers) =>
      currentUsers.map((user) =>
        user.id === id
          ? {
              ...user,
              status:
                user.status === "Active"
                  ? "Inactive"
                  : "Active",
            }
          : user
      )
    );
  };

  const deleteUser = (id) => {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this user?"
    );

    if (!confirmDelete) {
      return;
    }

    setUsers((currentUsers) =>
      currentUsers.filter((user) => user.id !== id)
    );
  };

  return (
    <div className="admin-users-page">
      <div className="admin-page-header">
        <div>
          <h2>User Management</h2>
          <p>
            View and manage registered users of the system.
          </p>
        </div>

        <div className="admin-user-count">
          <i className="bi bi-people"></i>
          <span>{users.length} Users</span>
        </div>
      </div>

      <section className="admin-user-summary">
        <div className="admin-summary-card">
          <span>Total Users</span>
          <strong>{users.length}</strong>
        </div>

        <div className="admin-summary-card">
          <span>Candidates</span>
          <strong>
            {
              users.filter(
                (user) => user.role === "Candidate"
              ).length
            }
          </strong>
        </div>

        <div className="admin-summary-card">
          <span>HR Users</span>
          <strong>
            {
              users.filter(
                (user) => user.role === "HR"
              ).length
            }
          </strong>
        </div>

        <div className="admin-summary-card">
          <span>Active Users</span>
          <strong>
            {
              users.filter(
                (user) => user.status === "Active"
              ).length
            }
          </strong>
        </div>
      </section>

      <section className="admin-management-card">
        <div className="admin-management-toolbar">
          <div className="admin-search-box">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search by name or email..."
              value={searchTerm}
              onChange={(event) =>
                setSearchTerm(event.target.value)
              }
            />
          </div>

          <div className="admin-filter-box">
            <i className="bi bi-funnel"></i>

            <select
              value={roleFilter}
              onChange={(event) =>
                setRoleFilter(event.target.value)
              }
            >
              <option value="All">All Roles</option>
              <option value="Candidate">
                Candidate
              </option>
              <option value="HR">HR</option>
            </select>
          </div>
        </div>

        <div className="admin-table-wrapper">
          <table className="admin-table admin-users-table">
            <thead>
              <tr>
                <th>User</th>
                <th>Role</th>
                <th>Joined</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredUsers.length > 0 ? (
                filteredUsers.map((user) => (
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

                    <td>
                      <span
                        className={`admin-role-badge ${
                          user.role === "HR"
                            ? "admin-role-hr"
                            : "admin-role-candidate"
                        }`}
                      >
                        {user.role}
                      </span>
                    </td>

                    <td>{user.joined}</td>

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

                    <td>
                      <div className="admin-action-buttons">
                        <button
                          className="admin-action-button admin-view-button"
                          type="button"
                          title="View user"
                        >
                          <i className="bi bi-eye"></i>
                        </button>

                        <button
                          className="admin-action-button admin-status-button"
                          type="button"
                          title="Change status"
                          onClick={() =>
                            toggleStatus(user.id)
                          }
                        >
                          <i
                            className={`bi ${
                              user.status === "Active"
                                ? "bi-person-x"
                                : "bi-person-check"
                            }`}
                          ></i>
                        </button>

                        <button
                          className="admin-action-button admin-delete-button"
                          type="button"
                          title="Delete user"
                          onClick={() =>
                            deleteUser(user.id)
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
                    colSpan="5"
                    className="admin-empty-state"
                  >
                    <i className="bi bi-search"></i>
                    <p>No users found.</p>
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

export default UserManagement;