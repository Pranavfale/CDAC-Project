import { Link, useNavigate } from "react-router-dom";
import { useState } from "react";

import {
  ArrowRight,
  BriefcaseBusiness,
  Building2,
  CheckCircle2,
  Code2,
  Headphones,
  MapPin,
  Search,
  ShieldCheck,
  TrendingUp,
  UserRound,
  Users,
} from "lucide-react";

function LandingPage() {
  const navigate = useNavigate();

  const [keyword, setKeyword] = useState("");
  const [location, setLocation] = useState("");

  const handleSearch = (event) => {
    event.preventDefault();

    navigate(
      `/jobs?search=${encodeURIComponent(
        keyword
      )}&location=${encodeURIComponent(location)}`
    );
  };

  const categories = [
    {
      icon: Code2,
      title: "Software Development",
      jobs: "24 Open Positions",
    },
    {
      icon: TrendingUp,
      title: "Sales & Marketing",
      jobs: "12 Open Positions",
    },
    {
      icon: Building2,
      title: "Finance & Banking",
      jobs: "8 Open Positions",
    },
    {
      icon: Headphones,
      title: "Customer Support",
      jobs: "10 Open Positions",
    },
  ];

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
            <Link to="/" className="active">
              Home
            </Link>

            <Link to="/jobs">
              Find Jobs
            </Link>

            <a href="#about">
              About
            </a>

            <a href="#contact">
              Contact
            </a>
          </nav>

          <div className="public-navbar-actions">
            <Link
              to="/login"
              className="public-signin-link"
            >
              Sign In
            </Link>

            <Link
              to="/register"
              className="btn btn-primary"
            >
              Get Started
              <ArrowRight size={15} />
            </Link>
          </div>
        </div>
      </header>

      {/* HERO */}

      <main>
        <section className="public-hero">
          <div className="public-container public-hero-grid">
            <div className="public-hero-content">
              <div className="public-hero-badge">
                <Users size={15} />
                Connecting talent with opportunity
              </div>

              <h1>
                Find the right job.
                <span> Build your future.</span>
              </h1>

              <p>
                Discover opportunities that match your
                skills and career goals. RecruitFlow
                connects talented candidates with growing
                organizations through one streamlined
                recruitment platform.
              </p>

              <form
                className="public-job-search"
                onSubmit={handleSearch}
              >
                <div className="public-search-field">
                  <Search size={18} />

                  <input
                    type="text"
                    placeholder="Job title, skill or keyword"
                    value={keyword}
                    onChange={(event) =>
                      setKeyword(event.target.value)
                    }
                  />
                </div>

                <div className="public-search-divider" />

                <div className="public-search-field">
                  <MapPin size={18} />

                  <input
                    type="text"
                    placeholder="Location"
                    value={location}
                    onChange={(event) =>
                      setLocation(event.target.value)
                    }
                  />
                </div>

                <button
                  type="submit"
                  className="btn btn-primary public-search-button"
                >
                  <Search size={16} />
                  Search Jobs
                </button>
              </form>

              <div className="public-popular-search">
                <span>Popular:</span>
                <button
                  onClick={() => {
                    setKeyword("Java Developer");
                  }}
                >
                  Java Developer
                </button>

                <button
                  onClick={() => {
                    setKeyword("React Developer");
                  }}
                >
                  React
                </button>

                <button
                  onClick={() => {
                    setKeyword("Software Engineer");
                  }}
                >
                  Software Engineer
                </button>
              </div>
            </div>

            <div className="public-hero-visual">
              <div className="public-hero-main-card">
                <div className="public-hero-card-header">
                  <div className="public-company-icon">
                    <Code2 size={22} />
                  </div>

                  <div>
                    <span>Featured Opportunity</span>
                    <strong>
                      Java Full Stack Developer
                    </strong>
                  </div>
                </div>

                <div className="public-hero-job-meta">
                  <span>
                    <Building2 size={14} />
                    TechNova Solutions
                  </span>

                  <span>
                    <MapPin size={14} />
                    Pune, Maharashtra
                  </span>
                </div>

                <div className="public-hero-skills">
                  <span>Java</span>
                  <span>Spring Boot</span>
                  <span>React</span>
                  <span>MySQL</span>
                </div>

                <div className="public-hero-card-footer">
                  <div>
                    <small>Experience</small>
                    <strong>0 - 2 Years</strong>
                  </div>

                  <button
                    className="btn btn-primary"
                    onClick={() => navigate("/jobs")}
                  >
                    View Job
                  </button>
                </div>
              </div>

              <div className="public-floating-card public-floating-top">
                <div className="public-floating-icon">
                  <CheckCircle2 size={18} />
                </div>

                <div>
                  <strong>Application submitted</strong>
                  <span>Successfully sent to HR</span>
                </div>
              </div>

              <div className="public-floating-card public-floating-bottom">
                <div className="public-floating-icon">
                  <TrendingUp size={18} />
                </div>

                <div>
                  <strong>100+ Opportunities</strong>
                  <span>Explore growing careers</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* STATS */}

        <section className="public-stats-section">
          <div className="public-container public-stats">
            <div>
              <strong>100+</strong>
              <span>Active Jobs</span>
            </div>

            <div>
              <strong>500+</strong>
              <span>Candidates</span>
            </div>

            <div>
              <strong>50+</strong>
              <span>Companies</span>
            </div>

            <div>
              <strong>200+</strong>
              <span>Successful Hires</span>
            </div>
          </div>
        </section>

        {/* CATEGORIES */}

        <section className="public-section">
          <div className="public-container">
            <div className="public-section-heading">
              <div>
                <span className="public-section-label">
                  EXPLORE OPPORTUNITIES
                </span>

                <h2>Popular job categories</h2>

                <p>
                  Explore opportunities across different
                  career paths and find the role that
                  matches your skills.
                </p>
              </div>

              <Link
                to="/jobs"
                className="public-text-link"
              >
                View all jobs
                <ArrowRight size={15} />
              </Link>
            </div>

            <div className="public-category-grid">
              {categories.map((category) => {
                const Icon = category.icon;

                return (
                  <article
                    className="public-category-card"
                    key={category.title}
                    onClick={() => navigate("/jobs")}
                  >
                    <div className="public-category-icon">
                      <Icon size={22} />
                    </div>

                    <h3>{category.title}</h3>

                    <p>{category.jobs}</p>

                    <ArrowRight
                      className="public-category-arrow"
                      size={18}
                    />
                  </article>
                );
              })}
            </div>
          </div>
        </section>

        {/* HOW IT WORKS */}

        <section className="public-section public-section-muted">
          <div className="public-container">
            <div className="public-centered-heading">
              <span className="public-section-label">
                SIMPLE RECRUITMENT
              </span>

              <h2>How RecruitFlow works</h2>

              <p>
                From discovering an opportunity to getting
                hired, manage your complete recruitment
                journey in one place.
              </p>
            </div>

            <div className="public-steps">
              <article>
                <div className="public-step-number">
                  01
                </div>

                <div className="public-step-icon">
                  <UserRound size={24} />
                </div>

                <h3>Create your profile</h3>

                <p>
                  Register and build your professional
                  profile with skills, education and
                  experience.
                </p>
              </article>

              <div className="public-step-line" />

              <article>
                <div className="public-step-number">
                  02
                </div>

                <div className="public-step-icon">
                  <Search size={24} />
                </div>

                <h3>Find opportunities</h3>

                <p>
                  Search available jobs and discover roles
                  that match your career goals.
                </p>
              </article>

              <div className="public-step-line" />

              <article>
                <div className="public-step-number">
                  03
                </div>

                <div className="public-step-icon">
                  <BriefcaseBusiness size={24} />
                </div>

                <h3>Apply & track</h3>

                <p>
                  Submit applications and monitor every
                  stage of your recruitment process.
                </p>
              </article>

              <div className="public-step-line" />

              <article>
                <div className="public-step-number">
                  04
                </div>

                <div className="public-step-icon">
                  <CheckCircle2 size={24} />
                </div>

                <h3>Get hired</h3>

                <p>
                  Attend interviews, receive offers and
                  start your next career opportunity.
                </p>
              </article>
            </div>
          </div>
        </section>

        {/* CANDIDATE + HR */}

        <section
          className="public-section"
          id="about"
        >
          <div className="public-container public-role-grid">
            <article className="public-role-card public-candidate-role">
              <div className="public-role-icon">
                <UserRound size={26} />
              </div>

              <span className="public-section-label">
                FOR CANDIDATES
              </span>

              <h2>
                Take control of your career journey
              </h2>

              <p>
                Discover opportunities, manage your
                applications and track every stage from
                application to offer.
              </p>

              <ul>
                <li>
                  <CheckCircle2 size={16} />
                  Search and explore job opportunities
                </li>

                <li>
                  <CheckCircle2 size={16} />
                  Build your professional profile
                </li>

                <li>
                  <CheckCircle2 size={16} />
                  Track application progress
                </li>

                <li>
                  <CheckCircle2 size={16} />
                  Manage interviews and offers
                </li>
              </ul>

              <Link
                to="/register"
                className="btn btn-primary"
              >
                Create Candidate Account
                <ArrowRight size={15} />
              </Link>
            </article>

            <article className="public-role-card public-hr-role">
              <div className="public-role-icon">
                <Users size={26} />
              </div>

              <span className="public-section-label">
                FOR RECRUITERS
              </span>

              <h2>
                Manage your complete hiring pipeline
              </h2>

              <p>
                RecruitFlow provides HR teams with the
                tools needed to manage vacancies,
                candidates and recruitment decisions.
              </p>

              <ul>
                <li>
                  <CheckCircle2 size={16} />
                  Create and manage job vacancies
                </li>

                <li>
                  <CheckCircle2 size={16} />
                  Review candidate applications
                </li>

                <li>
                  <CheckCircle2 size={16} />
                  Schedule and manage interviews
                </li>

                <li>
                  <CheckCircle2 size={16} />
                  Offers and recruitment analytics
                </li>
              </ul>

              <Link
                to="/login"
                className="btn btn-secondary"
              >
                Recruiter Sign In
                <ArrowRight size={15} />
              </Link>
            </article>
          </div>
        </section>

        {/* CTA */}

        <section className="public-cta-section">
          <div className="public-container">
            <div className="public-cta">
              <div>
                <span>START YOUR JOURNEY</span>

                <h2>
                  Your next opportunity starts here.
                </h2>

                <p>
                  Create your RecruitFlow account and
                  explore opportunities tailored to your
                  career.
                </p>
              </div>

              <div className="public-cta-actions">
                <Link
                  to="/register"
                  className="btn public-cta-primary"
                >
                  Create Free Account
                  <ArrowRight size={16} />
                </Link>

                <Link
                  to="/jobs"
                  className="btn public-cta-secondary"
                >
                  Browse Jobs
                </Link>
              </div>
            </div>
          </div>
        </section>
      </main>

      {/* FOOTER */}

      <footer
        className="public-footer"
        id="contact"
      >
        <div className="public-container">
          <div className="public-footer-grid">
            <div>
              <Link to="/" className="public-brand public-footer-brand">
                <div className="public-brand-icon">
                  <BriefcaseBusiness size={20} />
                </div>

                <strong>RecruitFlow</strong>
              </Link>

              <p>
                A centralized recruitment platform
                connecting talented candidates with the
                right opportunities.
              </p>
            </div>

            <div>
              <h3>For Candidates</h3>

              <Link to="/jobs">Browse Jobs</Link>
              <Link to="/register">Create Account</Link>
              <Link to="/login">Sign In</Link>
            </div>

            <div>
              <h3>RecruitFlow</h3>

              <a href="#about">About</a>
              <a href="#contact">Contact</a>
              <Link to="/login">Recruiter Login</Link>
            </div>

            <div>
              <h3>Platform</h3>

              <span>
                <ShieldCheck size={14} />
                Secure Recruitment
              </span>

              <span>
                <Users size={14} />
                Role-Based Access
              </span>

              <span>
                <BriefcaseBusiness size={14} />
                End-to-End Hiring
              </span>
            </div>
          </div>

          <div className="public-footer-bottom">
            <span>
              © 2026 RecruitFlow. Web-Based Recruitment
              System.
            </span>

            <span>
              Built for efficient recruitment management.
            </span>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default LandingPage;