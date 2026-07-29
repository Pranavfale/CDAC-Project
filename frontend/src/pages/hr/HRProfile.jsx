import { useState } from "react";
import "../../styles/hrProfile.css";

const initialProfile = {
  name: "Priya Patil",
  email: "priya@techcorp.com",
  phone: "9876543210",
  designation: "HR Manager",
  experience: "5 Years",
};

const initialCompany = {
  companyName: "TechCorp Solutions",
  industry: "Information Technology",
  website: "https://techcorp.example.com",
  location: "Pune, Maharashtra",
  employees: "201 - 500",
  description:
    "TechCorp Solutions is a software development company providing enterprise technology solutions.",
};

function HRProfile() {
  const [profile, setProfile] = useState(initialProfile);
  const [company, setCompany] = useState(initialCompany);

  const [profileForm, setProfileForm] =
    useState(initialProfile);

  const [companyForm, setCompanyForm] =
    useState(initialCompany);

  const [editingProfile, setEditingProfile] =
    useState(false);

  const [editingCompany, setEditingCompany] =
    useState(false);

  const handleProfileChange = (event) => {
    const { name, value } = event.target;

    setProfileForm((currentData) => ({
      ...currentData,
      [name]: value,
    }));
  };

  const handleCompanyChange = (event) => {
    const { name, value } = event.target;

    setCompanyForm((currentData) => ({
      ...currentData,
      [name]: value,
    }));
  };

  const saveProfile = (event) => {
    event.preventDefault();

    if (
      !profileForm.name.trim() ||
      !profileForm.email.trim() ||
      !profileForm.phone.trim() ||
      !profileForm.designation.trim()
    ) {
      window.alert("Please fill all required profile fields.");
      return;
    }

    setProfile(profileForm);
    setEditingProfile(false);

    window.alert("HR profile updated successfully.");
  };

  const saveCompany = (event) => {
    event.preventDefault();

    if (
      !companyForm.companyName.trim() ||
      !companyForm.industry.trim() ||
      !companyForm.location.trim()
    ) {
      window.alert("Please fill all required company fields.");
      return;
    }

    setCompany(companyForm);
    setEditingCompany(false);

    window.alert("Company profile updated successfully.");
  };

  const cancelProfileEdit = () => {
    setProfileForm(profile);
    setEditingProfile(false);
  };

  const cancelCompanyEdit = () => {
    setCompanyForm(company);
    setEditingCompany(false);
  };

  const profileFields = [
    profile.name,
    profile.email,
    profile.phone,
    profile.designation,
    profile.experience,
    company.companyName,
    company.industry,
    company.website,
    company.location,
    company.description,
  ];

  const completedFields = profileFields.filter(
    (field) => field && field.trim() !== ""
  ).length;

  const profileCompletion = Math.round(
    (completedFields / profileFields.length) * 100
  );

  return (
    <div className="hr-profile-page">
      <div className="hr-profile-page-header">
        <div>
          <h2>Profile Management</h2>
          <p>
            Manage your personal and company information.
          </p>
        </div>

        <div className="hr-verification-badge">
          <i className="bi bi-patch-check-fill"></i>
          Verified Company
        </div>
      </div>

      <section className="hr-profile-overview">
        <div className="hr-profile-cover">
          <div className="hr-profile-avatar-large">
            {profile.name.charAt(0).toUpperCase()}
          </div>

          <div className="hr-profile-overview-info">
            <h3>{profile.name}</h3>
            <p>{profile.designation}</p>

            <div className="hr-profile-company-name">
              <i className="bi bi-building"></i>
              <span>{company.companyName}</span>
            </div>
          </div>
        </div>

        <div className="hr-profile-completion">
          <div className="hr-completion-header">
            <div>
              <span>Profile Completion</span>
              <strong>{profileCompletion}%</strong>
            </div>
          </div>

          <div className="hr-completion-track">
            <div
              className="hr-completion-fill"
              style={{
                width: `${profileCompletion}%`,
              }}
            ></div>
          </div>

          <p>
            Complete your profile to improve company
            visibility.
          </p>
        </div>
      </section>

      <section className="hr-profile-grid">
        {/* HR Personal Profile */}

        <article className="hr-profile-card">
          <div className="hr-profile-card-header">
            <div>
              <h3>Personal Information</h3>
              <p>Your HR account details</p>
            </div>

            {!editingProfile && (
              <button
                type="button"
                className="hr-profile-edit-button"
                onClick={() => setEditingProfile(true)}
              >
                <i className="bi bi-pencil-square"></i>
                Edit
              </button>
            )}
          </div>

          {!editingProfile ? (
            <div className="hr-profile-details">
              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-person"></i>
                </div>

                <div>
                  <span>Full Name</span>
                  <strong>{profile.name}</strong>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-envelope"></i>
                </div>

                <div>
                  <span>Email Address</span>
                  <strong>{profile.email}</strong>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-telephone"></i>
                </div>

                <div>
                  <span>Phone Number</span>
                  <strong>{profile.phone}</strong>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-person-badge"></i>
                </div>

                <div>
                  <span>Designation</span>
                  <strong>{profile.designation}</strong>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-briefcase"></i>
                </div>

                <div>
                  <span>Experience</span>
                  <strong>{profile.experience}</strong>
                </div>
              </div>
            </div>
          ) : (
            <form
              className="hr-profile-form"
              onSubmit={saveProfile}
            >
              <div className="hr-profile-form-group">
                <label htmlFor="hrName">Full Name</label>

                <input
                  id="hrName"
                  type="text"
                  name="name"
                  value={profileForm.name}
                  onChange={handleProfileChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="hrEmail">
                  Email Address
                </label>

                <input
                  id="hrEmail"
                  type="email"
                  name="email"
                  value={profileForm.email}
                  onChange={handleProfileChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="hrPhone">
                  Phone Number
                </label>

                <input
                  id="hrPhone"
                  type="tel"
                  name="phone"
                  value={profileForm.phone}
                  onChange={handleProfileChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="hrDesignation">
                  Designation
                </label>

                <input
                  id="hrDesignation"
                  type="text"
                  name="designation"
                  value={profileForm.designation}
                  onChange={handleProfileChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="hrExperience">
                  Experience
                </label>

                <input
                  id="hrExperience"
                  type="text"
                  name="experience"
                  value={profileForm.experience}
                  onChange={handleProfileChange}
                />
              </div>

              <div className="hr-profile-form-actions">
                <button
                  type="button"
                  className="hr-profile-cancel-button"
                  onClick={cancelProfileEdit}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="hr-profile-save-button"
                >
                  Save Changes
                </button>
              </div>
            </form>
          )}
        </article>

        {/* Company Profile */}

        <article className="hr-profile-card">
          <div className="hr-profile-card-header">
            <div>
              <h3>Company Information</h3>
              <p>Company details visible to candidates</p>
            </div>

            {!editingCompany && (
              <button
                type="button"
                className="hr-profile-edit-button"
                onClick={() => setEditingCompany(true)}
              >
                <i className="bi bi-pencil-square"></i>
                Edit
              </button>
            )}
          </div>

          {!editingCompany ? (
            <div className="hr-profile-details">
              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-building"></i>
                </div>

                <div>
                  <span>Company Name</span>
                  <strong>{company.companyName}</strong>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-diagram-3"></i>
                </div>

                <div>
                  <span>Industry</span>
                  <strong>{company.industry}</strong>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-globe"></i>
                </div>

                <div>
                  <span>Website</span>

                  <a
                    href={company.website}
                    target="_blank"
                    rel="noreferrer"
                  >
                    {company.website}
                  </a>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-geo-alt"></i>
                </div>

                <div>
                  <span>Location</span>
                  <strong>{company.location}</strong>
                </div>
              </div>

              <div className="hr-profile-detail-row">
                <div className="hr-profile-detail-icon">
                  <i className="bi bi-people"></i>
                </div>

                <div>
                  <span>Company Size</span>
                  <strong>
                    {company.employees} Employees
                  </strong>
                </div>
              </div>

              <div className="hr-company-description">
                <span>About Company</span>
                <p>{company.description}</p>
              </div>
            </div>
          ) : (
            <form
              className="hr-profile-form"
              onSubmit={saveCompany}
            >
              <div className="hr-profile-form-group">
                <label htmlFor="companyName">
                  Company Name
                </label>

                <input
                  id="companyName"
                  type="text"
                  name="companyName"
                  value={companyForm.companyName}
                  onChange={handleCompanyChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="industry">
                  Industry
                </label>

                <input
                  id="industry"
                  type="text"
                  name="industry"
                  value={companyForm.industry}
                  onChange={handleCompanyChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="companyWebsite">
                  Website
                </label>

                <input
                  id="companyWebsite"
                  type="url"
                  name="website"
                  value={companyForm.website}
                  onChange={handleCompanyChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="companyLocation">
                  Location
                </label>

                <input
                  id="companyLocation"
                  type="text"
                  name="location"
                  value={companyForm.location}
                  onChange={handleCompanyChange}
                />
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="employees">
                  Company Size
                </label>

                <select
                  id="employees"
                  name="employees"
                  value={companyForm.employees}
                  onChange={handleCompanyChange}
                >
                  <option value="1 - 10">1 - 10</option>
                  <option value="11 - 50">11 - 50</option>
                  <option value="51 - 200">51 - 200</option>
                  <option value="201 - 500">
                    201 - 500
                  </option>
                  <option value="501 - 1000">
                    501 - 1000
                  </option>
                  <option value="1000+">1000+</option>
                </select>
              </div>

              <div className="hr-profile-form-group">
                <label htmlFor="companyDescription">
                  Company Description
                </label>

                <textarea
                  id="companyDescription"
                  name="description"
                  rows="5"
                  value={companyForm.description}
                  onChange={handleCompanyChange}
                ></textarea>
              </div>

              <div className="hr-profile-form-actions">
                <button
                  type="button"
                  className="hr-profile-cancel-button"
                  onClick={cancelCompanyEdit}
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="hr-profile-save-button"
                >
                  Save Changes
                </button>
              </div>
            </form>
          )}
        </article>
      </section>
    </div>
  );
}

export default HRProfile;