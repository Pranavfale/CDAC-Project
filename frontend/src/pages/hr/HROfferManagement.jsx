import { useMemo, useState } from "react";
import "../../styles/hrOffers.css";

const emptyOfferForm = {
  candidateName: "",
  candidateEmail: "",
  jobTitle: "",
  department: "",
  annualCTC: "",
  joiningDate: "",
  expiryDate: "",
  location: "",
  employmentType: "Full Time",
};

const initialOffers = [
  {
    id: 1,
    candidateName: "Rahul Sharma",
    candidateEmail: "rahul@gmail.com",
    jobTitle: "Java Developer",
    department: "Engineering",
    annualCTC: "8.5 LPA",
    joiningDate: "2026-08-18",
    expiryDate: "2026-08-05",
    location: "Pune",
    employmentType: "Full Time",
    createdDate: "2026-07-26",
    status: "Sent",
  },
  {
    id: 2,
    candidateName: "Sneha Patil",
    candidateEmail: "sneha@gmail.com",
    jobTitle: "React Developer",
    department: "Engineering",
    annualCTC: "7.8 LPA",
    joiningDate: "2026-08-20",
    expiryDate: "2026-08-07",
    location: "Mumbai",
    employmentType: "Full Time",
    createdDate: "2026-07-25",
    status: "Accepted",
  },
  {
    id: 3,
    candidateName: "Amit Kumar",
    candidateEmail: "amit@gmail.com",
    jobTitle: "Software Tester",
    department: "Quality Assurance",
    annualCTC: "6.2 LPA",
    joiningDate: "2026-08-22",
    expiryDate: "2026-08-08",
    location: "Pune",
    employmentType: "Full Time",
    createdDate: "2026-07-24",
    status: "Draft",
  },
  {
    id: 4,
    candidateName: "Neha Deshmukh",
    candidateEmail: "neha@gmail.com",
    jobTitle: "Backend Developer",
    department: "Engineering",
    annualCTC: "9.2 LPA",
    joiningDate: "2026-08-15",
    expiryDate: "2026-08-02",
    location: "Bengaluru",
    employmentType: "Full Time",
    createdDate: "2026-07-22",
    status: "Rejected",
  },
];

function HROfferManagement() {
  const [offers, setOffers] = useState(initialOffers);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("All");
  const [showOfferForm, setShowOfferForm] = useState(false);
  const [formData, setFormData] = useState(emptyOfferForm);
  const [editingOfferId, setEditingOfferId] = useState(null);

  const filteredOffers = useMemo(() => {
    const search = searchTerm.trim().toLowerCase();

    return offers.filter((offer) => {
      const matchesSearch =
        offer.candidateName.toLowerCase().includes(search) ||
        offer.candidateEmail.toLowerCase().includes(search) ||
        offer.jobTitle.toLowerCase().includes(search) ||
        offer.department.toLowerCase().includes(search) ||
        offer.location.toLowerCase().includes(search);

      const matchesStatus =
        statusFilter === "All" ||
        offer.status === statusFilter;

      return matchesSearch && matchesStatus;
    });
  }, [offers, searchTerm, statusFilter]);

  const handleInputChange = (event) => {
    const { name, value } = event.target;

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }));
  };

  const resetForm = () => {
    setFormData(emptyOfferForm);
    setEditingOfferId(null);
    setShowOfferForm(false);
  };

  const handleOfferSubmit = (event) => {
    event.preventDefault();

    const requiredFields = [
      formData.candidateName,
      formData.candidateEmail,
      formData.jobTitle,
      formData.department,
      formData.annualCTC,
      formData.joiningDate,
      formData.expiryDate,
      formData.location,
    ];

    const hasEmptyField = requiredFields.some(
      (field) => !field.trim()
    );

    if (hasEmptyField) {
      window.alert("Please fill all required offer details.");
      return;
    }

    if (
      new Date(formData.expiryDate) >=
      new Date(formData.joiningDate)
    ) {
      window.alert(
        "Offer expiry date must be before the joining date."
      );
      return;
    }

    if (editingOfferId !== null) {
      setOffers((currentOffers) =>
        currentOffers.map((offer) =>
          offer.id === editingOfferId
            ? {
                ...offer,
                ...formData,
              }
            : offer
        )
      );

      window.alert("Offer updated successfully.");
    } else {
      const newOffer = {
        id: Date.now(),
        ...formData,
        createdDate: new Date()
          .toISOString()
          .split("T")[0],
        status: "Draft",
      };

      setOffers((currentOffers) => [
        newOffer,
        ...currentOffers,
      ]);

      window.alert("Offer created successfully.");
    }

    resetForm();
  };

  const editOffer = (offer) => {
    setFormData({
      candidateName: offer.candidateName,
      candidateEmail: offer.candidateEmail,
      jobTitle: offer.jobTitle,
      department: offer.department,
      annualCTC: offer.annualCTC,
      joiningDate: offer.joiningDate,
      expiryDate: offer.expiryDate,
      location: offer.location,
      employmentType: offer.employmentType,
    });

    setEditingOfferId(offer.id);
    setShowOfferForm(true);
  };

  const updateOfferStatus = (id, newStatus) => {
    setOffers((currentOffers) =>
      currentOffers.map((offer) =>
        offer.id === id
          ? {
              ...offer,
              status: newStatus,
            }
          : offer
      )
    );
  };

  const sendOffer = (id) => {
    const confirmed = window.confirm(
      "Send this offer to the candidate?"
    );

    if (confirmed) {
      updateOfferStatus(id, "Sent");
    }
  };

  const withdrawOffer = (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to withdraw this offer?"
    );

    if (confirmed) {
      updateOfferStatus(id, "Withdrawn");
    }
  };

  const deleteOffer = (id) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this offer?"
    );

    if (!confirmed) {
      return;
    }

    setOffers((currentOffers) =>
      currentOffers.filter((offer) => offer.id !== id)
    );
  };

  const downloadOffer = (offer) => {
    const offerContent = `
RECRUITPRO - EMPLOYMENT OFFER

Candidate: ${offer.candidateName}
Email: ${offer.candidateEmail}

Job Position: ${offer.jobTitle}
Department: ${offer.department}
Employment Type: ${offer.employmentType}
Location: ${offer.location}

Annual CTC: ${offer.annualCTC}
Joining Date: ${offer.joiningDate}
Offer Expiry Date: ${offer.expiryDate}

Offer Status: ${offer.status}

We are pleased to offer you the position of ${
      offer.jobTitle
    } at RecruitPro.

Regards,
HR Department
`;

    const blob = new Blob([offerContent], {
      type: "text/plain;charset=utf-8",
    });

    const downloadUrl = URL.createObjectURL(blob);
    const link = document.createElement("a");

    link.href = downloadUrl;
    link.download = `${offer.candidateName
      .toLowerCase()
      .replaceAll(" ", "-")}-offer-letter.txt`;

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    URL.revokeObjectURL(downloadUrl);
  };

  const draftCount = offers.filter(
    (offer) => offer.status === "Draft"
  ).length;

  const sentCount = offers.filter(
    (offer) => offer.status === "Sent"
  ).length;

  const acceptedCount = offers.filter(
    (offer) => offer.status === "Accepted"
  ).length;

  const rejectedCount = offers.filter(
    (offer) =>
      offer.status === "Rejected" ||
      offer.status === "Withdrawn"
  ).length;

  return (
    <div className="hr-offers-page">
      <div className="hr-offers-header">
        <div>
          <h2>Offer Management</h2>
          <p>
            Create, send and manage candidate job offers.
          </p>
        </div>

        <button
          type="button"
          className="hr-create-offer-button"
          onClick={() => {
            setEditingOfferId(null);
            setFormData(emptyOfferForm);
            setShowOfferForm(true);
          }}
        >
          <i className="bi bi-file-earmark-plus"></i>
          Create Offer
        </button>
      </div>

      <section className="hr-offer-summary-grid">
        <OfferSummaryCard
          title="Total Offers"
          value={offers.length}
          icon="bi-file-earmark-text"
        />

        <OfferSummaryCard
          title="Draft Offers"
          value={draftCount}
          icon="bi-pencil-square"
        />

        <OfferSummaryCard
          title="Sent Offers"
          value={sentCount}
          icon="bi-send"
        />

        <OfferSummaryCard
          title="Accepted Offers"
          value={acceptedCount}
          icon="bi-check-circle"
        />

        <OfferSummaryCard
          title="Rejected / Withdrawn"
          value={rejectedCount}
          icon="bi-x-circle"
        />
      </section>

      <section className="hr-offers-card">
        <div className="hr-offers-toolbar">
          <div className="hr-offer-search">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search candidate, job, department or location..."
              value={searchTerm}
              onChange={(event) =>
                setSearchTerm(event.target.value)
              }
            />
          </div>

          <div className="hr-offer-filter">
            <i className="bi bi-funnel"></i>

            <select
              value={statusFilter}
              onChange={(event) =>
                setStatusFilter(event.target.value)
              }
            >
              <option value="All">All Status</option>
              <option value="Draft">Draft</option>
              <option value="Sent">Sent</option>
              <option value="Accepted">Accepted</option>
              <option value="Rejected">Rejected</option>
              <option value="Withdrawn">Withdrawn</option>
            </select>
          </div>
        </div>

        <div className="hr-offer-table-wrapper">
          <table className="hr-offer-table">
            <thead>
              <tr>
                <th>Candidate</th>
                <th>Job Position</th>
                <th>CTC</th>
                <th>Joining Date</th>
                <th>Expiry Date</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {filteredOffers.length > 0 ? (
                filteredOffers.map((offer) => (
                  <tr key={offer.id}>
                    <td>
                      <div className="hr-offer-candidate">
                        <div className="hr-offer-avatar">
                          {offer.candidateName
                            .charAt(0)
                            .toUpperCase()}
                        </div>

                        <div>
                          <strong>
                            {offer.candidateName}
                          </strong>
                          <span>
                            {offer.candidateEmail}
                          </span>
                        </div>
                      </div>
                    </td>

                    <td>
                      <div className="hr-offer-job">
                        <strong>{offer.jobTitle}</strong>
                        <span>
                          {offer.department} ·{" "}
                          {offer.location}
                        </span>
                      </div>
                    </td>

                    <td>
                      <span className="hr-offer-ctc">
                        {offer.annualCTC}
                      </span>
                    </td>

                    <td>{offer.joiningDate}</td>
                    <td>{offer.expiryDate}</td>

                    <td>
                      <span
                        className={`hr-offer-status hr-offer-status-${offer.status.toLowerCase()}`}
                      >
                        {offer.status}
                      </span>
                    </td>

                    <td>
                      <div className="hr-offer-actions">
                        <button
                          type="button"
                          className="hr-offer-action hr-offer-download"
                          title="Download offer letter"
                          onClick={() =>
                            downloadOffer(offer)
                          }
                        >
                          <i className="bi bi-download"></i>
                        </button>

                        {offer.status === "Draft" && (
                          <>
                            <button
                              type="button"
                              className="hr-offer-action hr-offer-edit"
                              title="Edit offer"
                              onClick={() =>
                                editOffer(offer)
                              }
                            >
                              <i className="bi bi-pencil"></i>
                            </button>

                            <button
                              type="button"
                              className="hr-offer-action hr-offer-send"
                              title="Send offer"
                              onClick={() =>
                                sendOffer(offer.id)
                              }
                            >
                              <i className="bi bi-send"></i>
                            </button>
                          </>
                        )}

                        {offer.status === "Sent" && (
                          <>
                            <button
                              type="button"
                              className="hr-offer-action hr-offer-accept"
                              title="Mark accepted"
                              onClick={() =>
                                updateOfferStatus(
                                  offer.id,
                                  "Accepted"
                                )
                              }
                            >
                              <i className="bi bi-check-lg"></i>
                            </button>

                            <button
                              type="button"
                              className="hr-offer-action hr-offer-reject"
                              title="Mark rejected"
                              onClick={() =>
                                updateOfferStatus(
                                  offer.id,
                                  "Rejected"
                                )
                              }
                            >
                              <i className="bi bi-x-lg"></i>
                            </button>

                            <button
                              type="button"
                              className="hr-offer-action hr-offer-withdraw"
                              title="Withdraw offer"
                              onClick={() =>
                                withdrawOffer(offer.id)
                              }
                            >
                              <i className="bi bi-arrow-counterclockwise"></i>
                            </button>
                          </>
                        )}

                        <button
                          type="button"
                          className="hr-offer-action hr-offer-delete"
                          title="Delete offer"
                          onClick={() =>
                            deleteOffer(offer.id)
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
                    className="hr-offers-empty"
                  >
                    <i className="bi bi-file-earmark-x"></i>
                    <p>No offers found.</p>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      {showOfferForm && (
        <div className="hr-offer-modal-overlay">
          <div className="hr-offer-modal">
            <div className="hr-offer-modal-header">
              <div>
                <h3>
                  {editingOfferId !== null
                    ? "Edit Offer"
                    : "Create New Offer"}
                </h3>

                <p>
                  Enter candidate and employment details.
                </p>
              </div>

              <button
                type="button"
                className="hr-offer-modal-close"
                onClick={resetForm}
                aria-label="Close offer form"
              >
                <i className="bi bi-x-lg"></i>
              </button>
            </div>

            <form onSubmit={handleOfferSubmit}>
              <div className="hr-offer-modal-body">
                <div className="hr-offer-form-row">
                  <OfferInput
                    label="Candidate Name"
                    name="candidateName"
                    value={formData.candidateName}
                    onChange={handleInputChange}
                    placeholder="Enter candidate name"
                  />

                  <OfferInput
                    label="Candidate Email"
                    name="candidateEmail"
                    type="email"
                    value={formData.candidateEmail}
                    onChange={handleInputChange}
                    placeholder="Enter candidate email"
                  />
                </div>

                <div className="hr-offer-form-row">
                  <OfferInput
                    label="Job Position"
                    name="jobTitle"
                    value={formData.jobTitle}
                    onChange={handleInputChange}
                    placeholder="Enter job position"
                  />

                  <OfferInput
                    label="Department"
                    name="department"
                    value={formData.department}
                    onChange={handleInputChange}
                    placeholder="Enter department"
                  />
                </div>

                <div className="hr-offer-form-row">
                  <OfferInput
                    label="Annual CTC"
                    name="annualCTC"
                    value={formData.annualCTC}
                    onChange={handleInputChange}
                    placeholder="Example: 8.5 LPA"
                  />

                  <OfferInput
                    label="Location"
                    name="location"
                    value={formData.location}
                    onChange={handleInputChange}
                    placeholder="Enter job location"
                  />
                </div>

                <div className="hr-offer-form-row">
                  <OfferInput
                    label="Joining Date"
                    name="joiningDate"
                    type="date"
                    value={formData.joiningDate}
                    onChange={handleInputChange}
                  />

                  <OfferInput
                    label="Offer Expiry Date"
                    name="expiryDate"
                    type="date"
                    value={formData.expiryDate}
                    onChange={handleInputChange}
                  />
                </div>

                <div className="hr-offer-form-group">
                  <label htmlFor="employmentType">
                    Employment Type
                  </label>

                  <select
                    id="employmentType"
                    name="employmentType"
                    value={formData.employmentType}
                    onChange={handleInputChange}
                  >
                    <option value="Full Time">
                      Full Time
                    </option>
                    <option value="Part Time">
                      Part Time
                    </option>
                    <option value="Internship">
                      Internship
                    </option>
                    <option value="Contract">
                      Contract
                    </option>
                  </select>
                </div>
              </div>

              <div className="hr-offer-modal-footer">
                <button
                  type="button"
                  className="hr-offer-cancel-button"
                  onClick={resetForm}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="hr-offer-save-button"
                >
                  {editingOfferId !== null
                    ? "Update Offer"
                    : "Create Offer"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

function OfferSummaryCard({ title, value, icon }) {
  return (
    <article className="hr-offer-summary-card">
      <div className="hr-offer-summary-icon">
        <i className={`bi ${icon}`}></i>
      </div>

      <div>
        <span>{title}</span>
        <strong>{value}</strong>
      </div>
    </article>
  );
}

function OfferInput({
  label,
  name,
  value,
  onChange,
  placeholder = "",
  type = "text",
}) {
  return (
    <div className="hr-offer-form-group">
      <label htmlFor={name}>{label}</label>

      <input
        id={name}
        name={name}
        type={type}
        value={value}
        placeholder={placeholder}
        onChange={onChange}
      />
    </div>
  );
}

export default HROfferManagement;