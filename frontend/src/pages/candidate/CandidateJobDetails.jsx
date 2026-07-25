import { useState } from "react";
import { useParams } from "react-router-dom";

const CandidateJobDetails = () => {
  const { id } = useParams();

  const [isSaved, setIsSaved] = useState(false);
  const [showApplication, setShowApplication] = useState(false);
  const [applicationSubmitted, setApplicationSubmitted] =
    useState(false);

  const [application, setApplication] = useState({
    coverLetter: "",
    resumeName: "",
  });

  const jobs = [
    {
      id: 1,
      title: "Java Developer",
      company: "TechCorp Solutions",
      location: "Pune",
      type: "Full Time",
      experience: "0-2 Years",
      salary: "₹4 - ₹6 LPA",
      posted: "2 days ago",
      applicants: 42,
      description:
        "We are looking for a Java Developer to join our development team. The candidate will work on scalable web applications and backend services using Java and Spring Boot.",
      responsibilities: [
        "Develop and maintain Java applications.",
        "Build RESTful APIs using Spring Boot.",
        "Work with MySQL databases.",
        "Collaborate with frontend developers.",
        "Write clean and maintainable code.",
      ],
      requirements: [
        "Strong knowledge of Core Java.",
        "Understanding of OOP concepts.",
        "Knowledge of Spring Boot.",
        "Basic understanding of REST APIs.",
        "Knowledge of SQL and MySQL.",
      ],
      skills: [
        "Java",
        "Spring Boot",
        "MySQL",
        "REST API",
        "Git",
      ],
    },
    {
      id: 2,
      title: "React Developer",
      company: "SoftWave Technologies",
      location: "Mumbai",
      type: "Full Time",
      experience: "0-1 Year",
      salary: "₹3.5 - ₹5 LPA",
      posted: "1 day ago",
      applicants: 35,
      description:
        "We are looking for a React Developer who can build responsive and user-friendly web applications.",
      responsibilities: [
        "Develop reusable React components.",
        "Build responsive user interfaces.",
        "Integrate frontend applications with REST APIs.",
        "Work with the development team.",
      ],
      requirements: [
        "Knowledge of JavaScript.",
        "Understanding of React.",
        "Knowledge of HTML and CSS.",
        "Basic Git knowledge.",
      ],
      skills: [
        "React",
        "JavaScript",
        "HTML",
        "CSS",
        "Git",
      ],
    },
    {
      id: 3,
      title: "Full Stack Developer",
      company: "CloudTech India",
      location: "Pune",
      type: "Full Time",
      experience: "Fresher",
      salary: "₹4 - ₹6.5 LPA",
      posted: "3 days ago",
      applicants: 51,
      description:
        "Join our engineering team as a Full Stack Developer and work on modern frontend and backend applications.",
      responsibilities: [
        "Develop frontend applications using React.",
        "Develop backend services using Spring Boot.",
        "Create and consume REST APIs.",
        "Work with relational databases.",
      ],
      requirements: [
        "Knowledge of Java.",
        "Understanding of React.",
        "Basic Spring Boot knowledge.",
        "Knowledge of SQL.",
      ],
      skills: [
        "Java",
        "React",
        "Spring Boot",
        "MySQL",
      ],
    },
  ];

  const job =
    jobs.find((item) => item.id === Number(id)) || jobs[0];

  const handleApplicationChange = (event) => {
    const { name, value } = event.target;

    setApplication((currentApplication) => ({
      ...currentApplication,
      [name]: value,
    }));
  };

  const handleResume = (event) => {
    const file = event.target.files[0];

    if (file) {
      setApplication((currentApplication) => ({
        ...currentApplication,
        resumeName: file.name,
      }));
    }
  };

  const submitApplication = (event) => {
    event.preventDefault();

    setApplicationSubmitted(true);
    setShowApplication(false);
  };

  return (
    <div className="candidate-job-details-page">

      {/* Back */}

      <div className="candidate-job-back">
        <a href="/candidate/jobs">
          <i className="bi bi-arrow-left"></i>
          Back to Jobs
        </a>
      </div>

      {/* Job Header */}

      <section className="candidate-job-detail-header">
        <div className="candidate-job-detail-company">
          <div className="candidate-job-detail-logo">
            {job.company.charAt(0)}
          </div>

          <div>
            <h2>{job.title}</h2>
            <p>{job.company}</p>

            <div className="candidate-job-detail-meta">
              <span>
                <i className="bi bi-geo-alt"></i>
                {job.location}
              </span>

              <span>
                <i className="bi bi-briefcase"></i>
                {job.experience}
              </span>

              <span>
                <i className="bi bi-clock"></i>
                {job.type}
              </span>
            </div>
          </div>
        </div>

        <div className="candidate-job-detail-actions">
          <button
            type="button"
            className={`candidate-detail-save ${
              isSaved ? "candidate-detail-saved" : ""
            }`}
            onClick={() => setIsSaved(!isSaved)}
          >
            <i
              className={`bi ${
                isSaved
                  ? "bi-bookmark-fill"
                  : "bi-bookmark"
              }`}
            ></i>

            {isSaved ? "Saved" : "Save Job"}
          </button>

          {!applicationSubmitted ? (
            <button
              type="button"
              className="candidate-apply-button"
              onClick={() => setShowApplication(true)}
            >
              Apply Now
            </button>
          ) : (
            <span className="candidate-applied-badge">
              <i className="bi bi-check-circle"></i>
              Applied
            </span>
          )}
        </div>
      </section>

      {/* Main */}

      <div className="candidate-job-detail-grid">

        <main className="candidate-job-detail-content">

          {/* Description */}

          <section className="candidate-job-detail-card">
            <h3>Job Description</h3>

            <p>{job.description}</p>
          </section>

          {/* Responsibilities */}

          <section className="candidate-job-detail-card">
            <h3>Responsibilities</h3>

            <ul>
              {job.responsibilities.map(
                (responsibility) => (
                  <li key={responsibility}>
                    {responsibility}
                  </li>
                )
              )}
            </ul>
          </section>

          {/* Requirements */}

          <section className="candidate-job-detail-card">
            <h3>Requirements</h3>

            <ul>
              {job.requirements.map((requirement) => (
                <li key={requirement}>
                  {requirement}
                </li>
              ))}
            </ul>
          </section>

          {/* Skills */}

          <section className="candidate-job-detail-card">
            <h3>Required Skills</h3>

            <div className="candidate-detail-skills">
              {job.skills.map((skill) => (
                <span key={skill}>{skill}</span>
              ))}
            </div>
          </section>
        </main>

        {/* Sidebar */}

        <aside className="candidate-job-info-card">
          <h3>Job Overview</h3>

          <div className="candidate-job-info-item">
            <i className="bi bi-geo-alt"></i>

            <div>
              <span>Location</span>
              <strong>{job.location}</strong>
            </div>
          </div>

          <div className="candidate-job-info-item">
            <i className="bi bi-briefcase"></i>

            <div>
              <span>Experience</span>
              <strong>{job.experience}</strong>
            </div>
          </div>

          <div className="candidate-job-info-item">
            <i className="bi bi-clock"></i>

            <div>
              <span>Job Type</span>
              <strong>{job.type}</strong>
            </div>
          </div>

          <div className="candidate-job-info-item">
            <i className="bi bi-currency-rupee"></i>

            <div>
              <span>Salary</span>
              <strong>{job.salary}</strong>
            </div>
          </div>

          <div className="candidate-job-info-item">
            <i className="bi bi-calendar3"></i>

            <div>
              <span>Posted</span>
              <strong>{job.posted}</strong>
            </div>
          </div>

          <div className="candidate-job-info-item">
            <i className="bi bi-people"></i>

            <div>
              <span>Applicants</span>
              <strong>{job.applicants}</strong>
            </div>
          </div>
        </aside>
      </div>

      {/* Application Modal */}

      {showApplication && (
        <div className="candidate-application-overlay">
          <div className="candidate-application-modal">

            <div className="candidate-application-header">
              <div>
                <h3>Apply for {job.title}</h3>
                <p>{job.company}</p>
              </div>

              <button
                type="button"
                onClick={() =>
                  setShowApplication(false)
                }
              >
                ×
              </button>
            </div>

            <form
              className="candidate-application-form"
              onSubmit={submitApplication}
            >
              <div className="candidate-form-group">
                <label>Resume</label>

                <input
                  type="file"
                  accept=".pdf,.doc,.docx"
                  onChange={handleResume}
                  required
                />

                {application.resumeName && (
                  <small>
                    Selected: {application.resumeName}
                  </small>
                )}
              </div>

              <div className="candidate-form-group">
                <label>Cover Letter</label>

                <textarea
                  name="coverLetter"
                  rows="6"
                  placeholder="Tell the recruiter why you are interested in this role..."
                  value={application.coverLetter}
                  onChange={handleApplicationChange}
                  required
                ></textarea>
              </div>

              <div className="candidate-application-buttons">
                <button
                  type="button"
                  className="candidate-cancel-button"
                  onClick={() =>
                    setShowApplication(false)
                  }
                >
                  Cancel
                </button>

                <button
                  type="submit"
                  className="candidate-submit-application"
                >
                  Submit Application
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default CandidateJobDetails;