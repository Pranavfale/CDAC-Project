import { Link, useNavigate, useParams } from "react-router-dom";

import {
  ArrowLeft,
  ArrowRight,
  BriefcaseBusiness,
  Building2,
  CalendarDays,
  CheckCircle2,
  Clock3,
  MapPin,
  Share2,
  UserRound,
} from "lucide-react";

import { hrJobs } from "../../data/hrData";

function JobDetails() {
  const { jobId } = useParams();
  const navigate = useNavigate();

  const job = hrJobs.find((item) => String(item.id) === String(jobId));

  if (!job) {
    return (
      <div className="public-site">
        <header className="public-navbar">
          <div className="public-container public-navbar-inner">
            <Link to="/" className="public-brand">
              <div className="public-brand-icon">
                <BriefcaseBusiness size={21} />
              </div>

              <div>
                <strong>RecruitFlow</strong>
                <span>Recruitment Platform</span>
              </div>
            </Link>

            <div className="public-navbar-actions">
              <Link to="/jobs" className="btn btn-secondary">
                Browse Jobs
              </Link>
            </div>
          </div>
        </header>

        <main className="public-job-not-found">
          <div className="public-container">
            <div className="public-no-jobs">
              <div>
                <BriefcaseBusiness size={28} />
              </div>

              <h2>Job not found</h2>

              <p>
                The job you are looking for does not exist or is no longer
                available.
              </p>

              <Link to="/jobs" className="btn btn-primary">
                Browse Available Jobs
              </Link>
            </div>
          </div>
        </main>
      </div>
    );
  }

  const skills = job.skills || [];

  const responsibilities =
    job.responsibilities && job.responsibilities.length > 0
      ? job.responsibilities
      : [
          "Collaborate with the development team to design and implement application features.",
          "Write clean, maintainable and reusable code.",
          "Participate in code reviews, testing and debugging.",
          "Work with team members to deliver features within project timelines.",
        ];

  const requirements =
    job.requirements && job.requirements.length > 0
      ? job.requirements
      : [
          `Relevant knowledge and skills for the ${job.title} position.`,
          "Good understanding of software development fundamentals.",
          "Strong problem-solving and communication skills.",
          "Ability to work effectively in a collaborative team environment.",
        ];

  const handleApply = () => {
    navigate(`/login?redirect=/candidate/jobs/${job.id}`);
  };

  return (
    <div className="public-site">
      {/* NAVBAR */}

      <header className="public-navbar">
        <div className="public-container public-navbar-inner">
          <Link to="/" className="public-brand">
            <div className="public-brand-icon">
              <BriefcaseBusiness size={21} />
            </div>

            <div>
              <strong>RecruitFlow</strong>
              <span>Recruitment Platform</span>
            </div>
          </Link>

          <nav className="public-nav-links">
            <Link to="/">Home</Link>

            <Link to="/jobs" className="active">
              Find Jobs
            </Link>

            <Link to="/#about">About</Link>

            <Link to="/#contact">Contact</Link>
          </nav>

          <div className="public-navbar-actions">
            <Link to="/login" className="public-signin-link">
              Sign In
            </Link>

            <Link to="/register" className="btn btn-primary">
              Get Started
              <ArrowRight size={15} />
            </Link>
          </div>
        </div>
      </header>

      <main>
        {/* JOB HEADER */}

        <section className="public-detail-header">
          <div className="public-container">
            <Link to="/jobs" className="public-jobs-back">
              <ArrowLeft size={15} />
              Back to all jobs
            </Link>

            <div className="public-detail-heading">
              <div className="public-detail-title-area">
                <div className="public-detail-company-icon">
                  <BriefcaseBusiness size={27} />
                </div>

                <div>
                  <div className="public-detail-label-row">
                    <span>{job.department || "Career Opportunity"}</span>

                    <span className="public-job-status">Open</span>
                  </div>

                  <h1>{job.title}</h1>

                  <div className="public-detail-meta">
                    <span>
                      <Building2 size={15} />
                      RecruitFlow Hiring Partner
                    </span>

                    <span>
                      <MapPin size={15} />
                      {job.location || "Location not specified"}
                    </span>

                    <span>
                      <BriefcaseBusiness size={15} />
                      {String(
                        job.employmentType || job.type || "Full Time",
                      ).replaceAll("_", " ")}
                    </span>
                  </div>
                </div>
              </div>

              <div className="public-detail-header-actions">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => {
                    if (navigator.share) {
                      navigator.share({
                        title: job.title,
                        text: `Check out this ${job.title} opportunity on RecruitFlow.`,
                        url: window.location.href,
                      });
                    }
                  }}
                >
                  <Share2 size={15} />
                  Share
                </button>

                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={handleApply}
                >
                  Apply Now
                  <ArrowRight size={15} />
                </button>
              </div>
            </div>
          </div>
        </section>

        {/* DETAILS */}

        <section className="public-detail-content">
          <div className="public-container public-detail-grid">
            <div className="public-detail-main">
              <article className="public-detail-card">
                <h2>About the role</h2>

                <p className="public-detail-description">
                  {job.description ||
                    `We are looking for a motivated ${job.title} to join our growing team. The successful candidate will contribute to projects while collaborating with other members of the organization.`}
                </p>
              </article>

              <article className="public-detail-card">
                <h2>Key responsibilities</h2>

                <ul className="public-detail-list">
                  {responsibilities.map((responsibility, index) => (
                    <li key={index}>
                      <CheckCircle2 size={17} />
                      <span>{responsibility}</span>
                    </li>
                  ))}
                </ul>
              </article>

              <article className="public-detail-card">
                <h2>Requirements</h2>

                <ul className="public-detail-list">
                  {requirements.map((requirement, index) => (
                    <li key={index}>
                      <CheckCircle2 size={17} />
                      <span>{requirement}</span>
                    </li>
                  ))}
                </ul>
              </article>

              {skills.length > 0 && (
                <article className="public-detail-card">
                  <h2>Skills</h2>

                  <p className="public-detail-small-text">
                    Skills relevant to this opportunity:
                  </p>

                  <div className="public-detail-skills">
                    {skills.map((skill) => (
                      <span key={skill}>{skill}</span>
                    ))}
                  </div>
                </article>
              )}
            </div>

            {/* SIDEBAR */}

            <aside className="public-detail-sidebar">
              <div className="public-detail-summary-card">
                <h2>Job overview</h2>

                <div className="public-detail-summary-item">
                  <div>
                    <MapPin size={18} />
                  </div>

                  <span>
                    <small>Location</small>

                    <strong>{job.location || "Not specified"}</strong>
                  </span>
                </div>

                <div className="public-detail-summary-item">
                  <div>
                    <BriefcaseBusiness size={18} />
                  </div>

                  <span>
                    <small>Employment Type</small>

                    <strong>
                      {String(
                        job.employmentType || job.type || "Full Time",
                      ).replaceAll("_", " ")}
                    </strong>
                  </span>
                </div>

                <div className="public-detail-summary-item">
                  <div>
                    <UserRound size={18} />
                  </div>

                  <span>
                    <small>Experience</small>

                    <strong>{job.experience || "Based on role"}</strong>
                  </span>
                </div>

                <div className="public-detail-summary-item">
                  <div>
                    <Building2 size={18} />
                  </div>

                  <span>
                    <small>Department</small>

                    <strong>{job.department || "Not specified"}</strong>
                  </span>
                </div>

                <div className="public-detail-summary-item">
                  <div>
                    <Clock3 size={18} />
                  </div>

                  <span>
                    <small>Status</small>
                    <strong>Open</strong>
                  </span>
                </div>

                <div className="public-detail-summary-item">
                  <div>
                    <CalendarDays size={18} />
                  </div>

                  <span>
                    <small>Application</small>
                    <strong>Accepting Applications</strong>
                  </span>
                </div>

                <button
                  type="button"
                  className="btn btn-primary public-detail-apply"
                  onClick={handleApply}
                >
                  Apply for this position
                  <ArrowRight size={15} />
                </button>

                <p className="public-detail-login-note">
                  Already registered? <Link to="/login">Sign in</Link>
                </p>
              </div>

              <div className="public-detail-help">
                <strong>Interested in this role?</strong>

                <p>
                  Create your RecruitFlow candidate account to apply and track
                  your recruitment progress.
                </p>

                <Link to="/register">
                  Create an account
                  <ArrowRight size={14} />
                </Link>
              </div>
            </aside>
          </div>
        </section>

        {/* BOTTOM CTA */}

        <section className="public-detail-bottom-cta">
          <div className="public-container">
            <div>
              <h2>Ready to take the next step?</h2>

              <p>
                Apply for {job.title} and manage your application through
                RecruitFlow.
              </p>
            </div>

            <button
              type="button"
              className="btn btn-primary"
              onClick={handleApply}
            >
              Apply Now
              <ArrowRight size={15} />
            </button>
          </div>
        </section>
      </main>

      <footer className="public-jobs-footer">
        <div className="public-container">
          <div>
            <strong>RecruitFlow</strong>
            <span>Web-Based Recruitment System</span>
          </div>

          <div>
            <Link to="/">Home</Link>
            <Link to="/jobs">Jobs</Link>
            <Link to="/login">Sign In</Link>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default JobDetails;
