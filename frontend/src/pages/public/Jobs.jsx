import { useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";

import {
  ArrowLeft,
  ArrowRight,
  BriefcaseBusiness,
  Building2,
  Clock3,
  MapPin,
  Search,
  SlidersHorizontal,
} from "lucide-react";

import { hrJobs } from "../../data/hrData";

function Jobs() {
  const [searchParams] = useSearchParams();

  const initialSearch = searchParams.get("search") || "";
  const initialLocation = searchParams.get("location") || "";

  const [search, setSearch] = useState(initialSearch);
  const [location, setLocation] = useState(initialLocation);
  const [jobType, setJobType] = useState("ALL");

  const publishedJobs = useMemo(() => {
    return hrJobs.filter((job) => {
      const status = String(job.status || "").toUpperCase();

      return status === "ACTIVE" || status === "PUBLISHED" || status === "OPEN";
    });
  }, []);

  const filteredJobs = useMemo(() => {
    return publishedJobs.filter((job) => {
      const searchableText = [
        job.title,
        job.department,
        job.location,
        ...(job.skills || []),
      ]
        .join(" ")
        .toLowerCase();

      const matchesSearch = searchableText.includes(
        search.toLowerCase().trim(),
      );

      const matchesLocation = String(job.location || "")
        .toLowerCase()
        .includes(location.toLowerCase().trim());

      const currentType = String(
        job.employmentType || job.type || "",
      ).toUpperCase();

      const matchesType = jobType === "ALL" || currentType === jobType;

      return matchesSearch && matchesLocation && matchesType;
    });
  }, [publishedJobs, search, location, jobType]);

  const clearFilters = () => {
    setSearch("");
    setLocation("");
    setJobType("ALL");
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
        {/* PAGE HEADER */}

        <section className="public-jobs-header">
          <div className="public-container">
            <Link to="/" className="public-jobs-back">
              <ArrowLeft size={15} />
              Back to home
            </Link>

            <div className="public-jobs-heading">
              <div>
                <span className="public-section-label">
                  CAREER OPPORTUNITIES
                </span>

                <h1>Find your next opportunity</h1>

                <p>
                  Explore available positions and discover opportunities that
                  match your skills, experience and career goals.
                </p>
              </div>

              <div className="public-jobs-count">
                <BriefcaseBusiness size={20} />

                <div>
                  <strong>{publishedJobs.length}</strong>
                  <span>Open positions</span>
                </div>
              </div>
            </div>

            {/* SEARCH */}

            <div className="public-jobs-search-panel">
              <div className="public-jobs-search-field">
                <Search size={18} />

                <div>
                  <label>What</label>

                  <input
                    type="text"
                    placeholder="Job title, skill or keyword"
                    value={search}
                    onChange={(event) => setSearch(event.target.value)}
                  />
                </div>
              </div>

              <div className="public-jobs-search-field">
                <MapPin size={18} />

                <div>
                  <label>Where</label>

                  <input
                    type="text"
                    placeholder="City or location"
                    value={location}
                    onChange={(event) => setLocation(event.target.value)}
                  />
                </div>
              </div>

              <div className="public-jobs-search-field">
                <SlidersHorizontal size={18} />

                <div>
                  <label>Job Type</label>

                  <select
                    value={jobType}
                    onChange={(event) => setJobType(event.target.value)}
                  >
                    <option value="ALL">All Types</option>

                    <option value="FULL_TIME">Full Time</option>

                    <option value="PART_TIME">Part Time</option>

                    <option value="INTERNSHIP">Internship</option>

                    <option value="CONTRACT">Contract</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* JOB LIST */}

        <section className="public-jobs-content">
          <div className="public-container">
            <div className="public-jobs-toolbar">
              <div>
                <strong>
                  {filteredJobs.length}{" "}
                  {filteredJobs.length === 1 ? "job" : "jobs"} found
                </strong>

                <span>Showing currently available opportunities</span>
              </div>

              {(search || location || jobType !== "ALL") && (
                <button
                  type="button"
                  className="public-clear-filters"
                  onClick={clearFilters}
                >
                  Clear filters
                </button>
              )}
            </div>

            {filteredJobs.length > 0 ? (
              <div className="public-job-list">
                {filteredJobs.map((job) => (
                  <article className="public-job-card" key={job.id}>
                    <div className="public-job-company-icon">
                      <BriefcaseBusiness size={22} />
                    </div>

                    <div className="public-job-card-content">
                      <div className="public-job-card-top">
                        <div>
                          <span className="public-job-department">
                            {job.department || "Recruitment"}
                          </span>

                          <h2>{job.title}</h2>
                        </div>

                        <span className="public-job-status">Open</span>
                      </div>

                      <div className="public-job-meta">
                        <span>
                          <Building2 size={14} />
                          RecruitFlow Hiring Partner
                        </span>

                        <span>
                          <MapPin size={14} />
                          {job.location || "Location not specified"}
                        </span>

                        <span>
                          <BriefcaseBusiness size={14} />
                          {String(
                            job.employmentType || job.type || "Full Time",
                          ).replaceAll("_", " ")}
                        </span>

                        <span>
                          <Clock3 size={14} />
                          {job.experience || "Experience based on role"}
                        </span>
                      </div>

                      {job.description && (
                        <p className="public-job-description">
                          {job.description}
                        </p>
                      )}

                      {job.skills && job.skills.length > 0 && (
                        <div className="public-job-skills">
                          {job.skills.slice(0, 5).map((skill) => (
                            <span key={skill}>{skill}</span>
                          ))}
                        </div>
                      )}
                    </div>

                    <div className="public-job-card-action">
                      <Link
                        to={`/jobs/${job.id}`}
                        className="btn btn-secondary"
                      >
                        View Details
                        <ArrowRight size={15} />
                      </Link>
                    </div>
                  </article>
                ))}
              </div>
            ) : (
              <div className="public-no-jobs">
                <div>
                  <Search size={28} />
                </div>

                <h2>No jobs found</h2>

                <p>
                  We couldn't find opportunities matching your current search
                  criteria.
                </p>

                <button
                  type="button"
                  className="btn btn-primary"
                  onClick={clearFilters}
                >
                  Clear Filters
                </button>
              </div>
            )}
          </div>
        </section>
      </main>

      {/* SIMPLE FOOTER */}

      <footer className="public-jobs-footer">
        <div className="public-container">
          <div>
            <strong>RecruitFlow</strong>

            <span>Find the right opportunity. Hire the right talent.</span>
          </div>

          <div>
            <Link to="/">Home</Link>
            <Link to="/login">Sign In</Link>
            <Link to="/register">Register</Link>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default Jobs;
