import { useState } from "react";

const HRApplicantManagement = () => {
  const [applicants, setApplicants] = useState([
    {
      id: 1,
      name: "Rahul Sharma",
      job: "Java Developer",
      experience: "2 Years",
      status: "Applied",
    },
    {
      id: 2,
      name: "Priya Verma",
      job: "React Developer",
      experience: "1 Year",
      status: "Shortlisted",
    },
    {
      id: 3,
      name: "Amit Kumar",
      job: "Python Developer",
      experience: "3 Years",
      status: "Applied",
    },
  ]);

  const updateStatus = (id, status) => {
    setApplicants(
      applicants.map((applicant) =>
        applicant.id === id
          ? { ...applicant, status }
          : applicant
      )
    );
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Applicant Management</h2>

      <table className="table table-bordered table-hover">
        <thead className="table-dark">
          <tr>
            <th>Candidate</th>
            <th>Applied Job</th>
            <th>Experience</th>
            <th>Status</th>
            <th width="250">Actions</th>
          </tr>
        </thead>

        <tbody>
          {applicants.map((applicant) => (
            <tr key={applicant.id}>
              <td>{applicant.name}</td>
              <td>{applicant.job}</td>
              <td>{applicant.experience}</td>

              <td>
                <span
                  className={`badge ${
                    applicant.status === "Applied"
                      ? "bg-secondary"
                      : applicant.status === "Shortlisted"
                      ? "bg-success"
                      : applicant.status === "Rejected"
                      ? "bg-danger"
                      : "bg-primary"
                  }`}
                >
                  {applicant.status}
                </span>
              </td>

              <td>
                <button
                  className="btn btn-success btn-sm me-2"
                  onClick={() =>
                    updateStatus(applicant.id, "Shortlisted")
                  }
                >
                  Shortlist
                </button>

                <button
                  className="btn btn-danger btn-sm me-2"
                  onClick={() =>
                    updateStatus(applicant.id, "Rejected")
                  }
                >
                  Reject
                </button>

                <button
                  className="btn btn-primary btn-sm"
                  onClick={() =>
                    updateStatus(applicant.id, "Interview")
                  }
                >
                  Interview
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default HRApplicantManagement;