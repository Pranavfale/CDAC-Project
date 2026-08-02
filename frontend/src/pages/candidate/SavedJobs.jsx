import { useState } from "react";

const SavedJobs = () => {
  const [savedJobs, setSavedJobs] = useState([
    {
      id: 1,
      title: "Java Developer",
      company: "TechCorp Solutions",
      location: "Pune",
      type: "Full Time",
      experience: "0-2 Years",
      salary: "₹4 - ₹6 LPA",
      skills: ["Java", "Spring Boot", "MySQL"],
      posted: "2 days ago",
    },
    {
      id: 2,
      title: "React Developer",
      company: "SoftWave Technologies",
      location: "Mumbai",
      type: "Full Time",
      experience: "0-1 Year",
      salary: "₹3.5 - ₹5 LPA",
      skills: ["React", "JavaScript", "CSS"],
      posted: "1 day ago",
    },
    {
      id: 3,
      title: "Full Stack Developer",
      company: "CloudTech India",
      location: "Pune",
      type: "Full Time",
      experience: "Fresher",
      salary: "₹4 - ₹6.5 LPA",
      skills: ["Java", "React", "Spring Boot"],
      posted: "3 days ago",
    },
    {
      id: 4,
      title: "Frontend Developer Intern",
      company: "WebCraft Solutions",
      location: "Remote",
      type: "Internship",
      experience: "Fresher",
      salary: "₹15,000 / month",
      skills: ["HTML", "CSS", "JavaScript"],
      posted: "Today",
    },
  ]);

  const [searchTerm, setSearchTerm] = useState("");

  const removeSavedJob = (jobId) => {
    setSavedJobs((currentJobs) =>
      currentJobs.filter((job) => job.id !== jobId)
    );
  };

  const filteredJobs = savedJobs.filter((job) => {
    const search = searchTerm.toLowerCase();

    return (
      job.title.toLowerCase().includes(search) ||
      job.company.toLowerCase().includes(search) ||
      job.location.toLowerCase().includes(search) ||
      job.skills.some((skill) =>
        skill.toLowerCase().includes(search)
      )
    );
  });

  return (
    <div className="candidate-saved-jobs-page">
      <div className="candidate-page-header">
        <div>
          <h2>Saved Jobs</h2>

          <p>
            Review jobs you saved and apply when you're
            ready.
          </p>
        </div>

        <a
          href="/candidate/jobs"
          className="candidate-primary-button"
        >
          <i className="bi bi-search"></i>
          Browse More Jobs
        </a>
      </div>

      {/* SUMMARY */}

      <section className="candidate-saved-summary">
        <div className="candidate-saved-summary-icon">
          <i className="bi bi-bookmark-fill"></i>
        </div>

        <div>
          <strong>{savedJobs.length}</strong>
          <span>Saved Jobs</span>
        </div>
      </section>

      {/* SEARCH */}

      {savedJobs.length > 0 && (
        <section className="candidate-saved-search-card">
          <div className="candidate-main-search">
            <i className="bi bi-search"></i>

            <input
              type="text"
              placeholder="Search your saved jobs..."
              value={searchTerm}
              onChange={(event) =>
                setSearchTerm(event.target.value)
              }
            />
          </div>
        </section>
      )}

      {/* JOBS */}

      {filteredJobs.length > 0 ? (
        <>
          <div className="candidate-saved-result-header">
            <strong>
              {filteredJobs.length}{" "}
              {filteredJobs.length === 1
                ? "Job"
                : "Jobs"}
            </strong>

            <span>
              in your saved jobs
            </span>
          </div>

          <section className="candidate-saved-jobs-grid">
            {filteredJobs.map((job) => (
              <article
                className="candidate-saved-job-card"
                key={job.id}
              >
                <div className="candidate-saved-job-top">
                  <div className="candidate-job-company-info">
                    <div className="candidate-company-icon candidate-company-large">
                      {job.company.charAt(0)}
                    </div>

                    <div>
                      <h3>{job.title}</h3>
                      <p>{job.company}</p>
                    </div>
                  </div>

                  <button
                    type="button"
                    className="candidate-remove-saved"
                    onClick={() =>
                      removeSavedJob(job.id)
                    }
                    title="Remove saved job"
                  >
                    <i className="bi bi-bookmark-x"></i>
                  </button>
                </div>

                <div className="candidate-saved-job-meta">
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

                <div className="candidate-saved-salary">
                  <i className="bi bi-currency-rupee"></i>
                  <strong>{job.salary}</strong>
                </div>

                <div className="candidate-job-skills">
                  {job.skills.map((skill) => (
                    <span key={skill}>
                      {skill}
                    </span>
                  ))}
                </div>

                <div className="candidate-saved-job-footer">
                  <span>
                    <i className="bi bi-clock-history"></i>
                    Posted {job.posted}
                  </span>

                  <div className="candidate-saved-actions">
                    <button
                      type="button"
                      onClick={() =>
                        removeSavedJob(job.id)
                      }
                      className="candidate-unsave-button"
                    >
                      <i className="bi bi-trash"></i>
                      Remove
                    </button>

                    <a
                      href={`/candidate/jobs/${job.id}`}
                      className="candidate-saved-view-button"
                    >
                      View & Apply
                    </a>
                  </div>
                </div>
              </article>
            ))}
          </section>
        </>
      ) : savedJobs.length === 0 ? (
        <div className="candidate-saved-empty">
          <div className="candidate-saved-empty-icon">
            <i className="bi bi-bookmark"></i>
          </div>

          <h3>No saved jobs</h3>

          <p>
            Jobs you save while browsing will appear here.
          </p>

          <a href="/candidate/jobs">
            Browse Jobs
          </a>
        </div>
      ) : (
        <div className="candidate-saved-empty">
          <div className="candidate-saved-empty-icon">
            <i className="bi bi-search"></i>
          </div>

          <h3>No matching saved jobs</h3>

          <p>
            Try searching with another job title, company,
            location or skill.
          </p>

          <button
            type="button"
            onClick={() => setSearchTerm("")}
          >
            Clear Search
          </button>
        </div>
      )}
    </div>
  );
};

export default SavedJobs;