import { useState } from "react";

const CandidateProfile = () => {
  const [isEditing, setIsEditing] = useState(false);

  const [profile, setProfile] = useState({
    fullName: "Candidate Name",
    email: "candidate@example.com",
    phone: "9876543210",
    location: "Pune, Maharashtra",
    title: "Java Full Stack Developer",
    about:
      "Aspiring Java Full Stack Developer with knowledge of Java, Spring Boot, React, MySQL and REST APIs.",
    degree: "B.Tech",
    specialization: "Electronics and Telecommunication Engineering",
    college: "ABC Institute of Technology",
    graduationYear: "2023",
    experience: "Fresher",
    resumeName: "Candidate_Resume.pdf",
  });

  const [skills, setSkills] = useState([
    "Java",
    "Spring Boot",
    "React",
    "JavaScript",
    "MySQL",
    "HTML",
    "CSS",
    "Git",
  ]);

  const [newSkill, setNewSkill] = useState("");

  const handleChange = (event) => {
    const { name, value } = event.target;

    setProfile((currentProfile) => ({
      ...currentProfile,
      [name]: value,
    }));
  };

  const addSkill = () => {
    const skill = newSkill.trim();

    if (
      skill &&
      !skills.some(
        (existingSkill) =>
          existingSkill.toLowerCase() ===
          skill.toLowerCase()
      )
    ) {
      setSkills([...skills, skill]);
      setNewSkill("");
    }
  };

  const removeSkill = (skillToRemove) => {
    setSkills(
      skills.filter(
        (skill) => skill !== skillToRemove
      )
    );
  };

  const handleResumeChange = (event) => {
    const file = event.target.files[0];

    if (file) {
      setProfile((currentProfile) => ({
        ...currentProfile,
        resumeName: file.name,
      }));
    }
  };

  const saveProfile = () => {
    setIsEditing(false);
  };

  return (
    <div className="candidate-profile-page">

      {/* PAGE HEADER */}

      <div className="candidate-page-header">
        <div>
          <h2>My Profile</h2>
          <p>
            Manage your personal information and
            professional details.
          </p>
        </div>

        {!isEditing ? (
          <button
            type="button"
            className="candidate-primary-button"
            onClick={() => setIsEditing(true)}
          >
            <i className="bi bi-pencil"></i>
            Edit Profile
          </button>
        ) : (
          <button
            type="button"
            className="candidate-primary-button"
            onClick={saveProfile}
          >
            <i className="bi bi-check-lg"></i>
            Save Changes
          </button>
        )}
      </div>

      <div className="candidate-profile-grid">

        {/* LEFT SIDE */}

        <aside className="candidate-profile-sidebar-card">

          <div className="candidate-profile-avatar-large">
            {profile.fullName.charAt(0)}
          </div>

          <h3>{profile.fullName}</h3>

          <p className="candidate-profile-title">
            {profile.title}
          </p>

          <div className="candidate-profile-contact">
            <span>
              <i className="bi bi-envelope"></i>
              {profile.email}
            </span>

            <span>
              <i className="bi bi-telephone"></i>
              {profile.phone}
            </span>

            <span>
              <i className="bi bi-geo-alt"></i>
              {profile.location}
            </span>
          </div>

          <div className="candidate-profile-completion-box">
            <div>
              <span>Profile Completion</span>
              <strong>85%</strong>
            </div>

            <div className="candidate-progress-track">
              <div
                className="candidate-progress-fill"
                style={{ width: "85%" }}
              ></div>
            </div>

            <small>
              Complete your profile to improve your
              visibility to recruiters.
            </small>
          </div>
        </aside>

        {/* RIGHT SIDE */}

        <div className="candidate-profile-sections">

          {/* PERSONAL INFORMATION */}

          <section className="candidate-profile-card">
            <div className="candidate-profile-card-header">
              <div>
                <h3>Personal Information</h3>
                <p>
                  Your basic personal and contact details.
                </p>
              </div>

              <i className="bi bi-person"></i>
            </div>

            <div className="candidate-profile-form-grid">

              <div className="candidate-profile-field">
                <label>Full Name</label>

                <input
                  type="text"
                  name="fullName"
                  value={profile.fullName}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field">
                <label>Email Address</label>

                <input
                  type="email"
                  name="email"
                  value={profile.email}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field">
                <label>Phone Number</label>

                <input
                  type="tel"
                  name="phone"
                  value={profile.phone}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field">
                <label>Location</label>

                <input
                  type="text"
                  name="location"
                  value={profile.location}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field candidate-profile-full-field">
                <label>Professional Title</label>

                <input
                  type="text"
                  name="title"
                  value={profile.title}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field candidate-profile-full-field">
                <label>About Me</label>

                <textarea
                  name="about"
                  rows="4"
                  value={profile.about}
                  onChange={handleChange}
                  disabled={!isEditing}
                ></textarea>
              </div>
            </div>
          </section>

          {/* EDUCATION */}

          <section className="candidate-profile-card">
            <div className="candidate-profile-card-header">
              <div>
                <h3>Education</h3>
                <p>
                  Your educational qualification.
                </p>
              </div>

              <i className="bi bi-mortarboard"></i>
            </div>

            <div className="candidate-profile-form-grid">

              <div className="candidate-profile-field">
                <label>Degree</label>

                <input
                  type="text"
                  name="degree"
                  value={profile.degree}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field">
                <label>Specialization</label>

                <input
                  type="text"
                  name="specialization"
                  value={profile.specialization}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field">
                <label>College / University</label>

                <input
                  type="text"
                  name="college"
                  value={profile.college}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>

              <div className="candidate-profile-field">
                <label>Graduation Year</label>

                <input
                  type="text"
                  name="graduationYear"
                  value={profile.graduationYear}
                  onChange={handleChange}
                  disabled={!isEditing}
                />
              </div>
            </div>
          </section>

          {/* EXPERIENCE */}

          <section className="candidate-profile-card">
            <div className="candidate-profile-card-header">
              <div>
                <h3>Experience</h3>
                <p>
                  Your professional experience level.
                </p>
              </div>

              <i className="bi bi-briefcase"></i>
            </div>

            <div className="candidate-profile-field">
              <label>Experience</label>

              <select
                name="experience"
                value={profile.experience}
                onChange={handleChange}
                disabled={!isEditing}
              >
                <option value="Fresher">
                  Fresher
                </option>

                <option value="0-1 Year">
                  0-1 Year
                </option>

                <option value="1-2 Years">
                  1-2 Years
                </option>

                <option value="2-3 Years">
                  2-3 Years
                </option>

                <option value="3+ Years">
                  3+ Years
                </option>
              </select>
            </div>
          </section>

          {/* SKILLS */}

          <section className="candidate-profile-card">
            <div className="candidate-profile-card-header">
              <div>
                <h3>Skills</h3>
                <p>
                  Add skills relevant to the jobs you are
                  looking for.
                </p>
              </div>

              <i className="bi bi-lightning"></i>
            </div>

            <div className="candidate-profile-skills">
              {skills.map((skill) => (
                <span key={skill}>
                  {skill}

                  {isEditing && (
                    <button
                      type="button"
                      onClick={() =>
                        removeSkill(skill)
                      }
                    >
                      ×
                    </button>
                  )}
                </span>
              ))}
            </div>

            {isEditing && (
              <div className="candidate-add-skill">
                <input
                  type="text"
                  placeholder="Enter a skill"
                  value={newSkill}
                  onChange={(event) =>
                    setNewSkill(event.target.value)
                  }
                  onKeyDown={(event) => {
                    if (event.key === "Enter") {
                      event.preventDefault();
                      addSkill();
                    }
                  }}
                />

                <button
                  type="button"
                  onClick={addSkill}
                >
                  <i className="bi bi-plus-lg"></i>
                  Add Skill
                </button>
              </div>
            )}
          </section>

          {/* RESUME */}

          <section className="candidate-profile-card">
            <div className="candidate-profile-card-header">
              <div>
                <h3>Resume</h3>
                <p>
                  Keep your latest resume available for job
                  applications.
                </p>
              </div>

              <i className="bi bi-file-earmark-person"></i>
            </div>

            <div className="candidate-resume-box">
              <div className="candidate-resume-info">
                <div className="candidate-resume-icon">
                  <i className="bi bi-file-earmark-pdf"></i>
                </div>

                <div>
                  <strong>
                    {profile.resumeName}
                  </strong>

                  <span>
                    Current Resume
                  </span>
                </div>
              </div>

              {isEditing && (
                <label className="candidate-resume-upload">
                  <i className="bi bi-upload"></i>
                  Replace Resume

                  <input
                    type="file"
                    accept=".pdf,.doc,.docx"
                    onChange={handleResumeChange}
                  />
                </label>
              )}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
};

export default CandidateProfile;