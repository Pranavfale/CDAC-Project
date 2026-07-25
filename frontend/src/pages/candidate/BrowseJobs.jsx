import { useState } from "react";

const BrowseJobs = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [locationFilter, setLocationFilter] = useState("All");
  const [typeFilter, setTypeFilter] = useState("All");
  const [savedJobs, setSavedJobs] = useState([]);

  const jobs = [
    {
      id: 1,
      title: "Java Developer",
      company: "TechCorp Solutions",
      location: "Pune",
      type: "Full Time",
      experience: "0-2 Years",
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
      skills: ["Java", "React", "Spring Boot"],
      posted: "3 days ago",
    },
    {
      id: 4,
      title: "Software Testing Intern",
      company: "Info Systems",
      location: "Mumbai",
      type: "Internship",
      experience: "Fresher",
      skills: ["Testing", "Selenium", "Java"],
      posted: "4 days ago",
    },
    {
      id: 5,
      title: "Backend Developer",
      company: "NexGen Software",
      location: "Bengaluru",
      type: "Full Time",
      experience: "1-3 Years",
      skills: ["Java", "Spring Boot", "REST API"],
      posted: "5 days ago",
    },
    {
      id: 6,
      title: "Frontend Developer Intern",
      company: "WebCraft Solutions",
      location: "Remote",
      type: "Internship",
      experience: "Fresher",
      skills: ["HTML", "CSS", "JavaScript"],
      posted: "Today",
    },
  ];

  const filteredJobs = jobs.filter((job) => {
    const search = searchTerm.toLowerCase();

    const matchesSearch =
      job.title.toLowerCase().includes(search) ||
      job.company.toLowerCase().includes(search) ||
      job.skills.some((skill) =>
        skill.toLowerCase().includes(search)
      );

    const matchesLocation =
      locationFilter === "All" ||
      job.location === locationFilter;

    const matchesType =
      typeFilter === "All" ||
      job.type === typeFilter;

    return matchesSearch && matchesLocation && matchesType;
  });

  const toggleSavedJob = (jobId) => {
    setSavedJobs((currentSavedJobs) => {
      if (currentSavedJobs.includes(jobId)) {
        return currentSavedJobs.filter(
          (id) => id !== jobId
        );
      }

      return [...currentSavedJobs, jobId];
    });
  };

  const clearFilters = () => {
    setSearchTerm("");
    setLocationFilter("All");
    setTypeFilter("All");
  };

  return (
    <div className="candidate-jobs-page">
      <div className="candidate-page-header">
        <div>
          <h2>Browse Jobs</h2>
          <p>
            Search and discover opportunities that match
            your skills.
          </p>
        </div>
      </div>

      {/* Search and Filters */}

      <section className="candidate-job-search-card">
        <div className="candidate-main-search">
          <i className="bi bi-search"></i>

          <input
            type="text"
            placeholder="Search by job title, company or skill..."
            value={searchTerm}
            onChange={(event) =>
              setSearchTerm(event.target.value)
            }
          />
        </div>

        <div className="candidate-job-filters">
          <div className="candidate-filter-control">
            <i className="bi bi-geo-alt"></i>

            <select
              value={locationFilter}
              onChange={(event) =>
                setLocationFilter(event.target.value)
              }
            >
              <option value="All">All Locations</option>
              <option value="Pune">Pune</option>
              <option value="Mumbai">Mumbai</option>
              <option value="Bengaluru">Bengaluru</option>
              <option value="Remote">Remote</option>
            </select>
          </div>

          <div className="candidate-filter-control">
            <i className="bi bi-briefcase"></i>

            <select
              value={typeFilter}
              onChange={(event) =>
                setTypeFilter(event.target.value)
              }
            >
              <option value="All">All Job Types</option>
              <option value="Full Time">Full Time</option>
              <option value="Internship">Internship</option>
            </select>
          </div>

          <button
            className="candidate-clear-filter"
            type="button"
            onClick={clearFilters}
          >
            Clear Filters
          </button>
        </div>
      </section>

      {/* Results */}

      <div className="candidate-jobs-result-header">
        <div>
          <strong>
            {filteredJobs.length} Jobs Found
          </strong>

          <span>
            Showing opportunities matching your search
          </span>
        </div>
      </div>

      {/* Job Cards */}

      {filteredJobs.length > 0 ? (
        <section className="candidate-browse-jobs-grid">
          {filteredJobs.map((job) => {
            const isSaved = savedJobs.includes(job.id);

            return (
              <article
                className="candidate-browse-job-card"
                key={job.id}
              >
                <div className="candidate-browse-job-header">
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
                    className={`candidate-save-button ${
                      isSaved
                        ? "candidate-job-saved"
                        : ""
                    }`}
                    onClick={() =>
                      toggleSavedJob(job.id)
                    }
                    title={
                      isSaved
                        ? "Remove from saved jobs"
                        : "Save job"
                    }
                  >
                    <i
                      className={`bi ${
                        isSaved
                          ? "bi-bookmark-fill"
                          : "bi-bookmark"
                      }`}
                    ></i>
                  </button>
                </div>

                <div className="candidate-browse-job-meta">
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

                <div className="candidate-job-skills">
                  {job.skills.map((skill) => (
                    <span key={skill}>
                      {skill}
                    </span>
                  ))}
                </div>

                <div className="candidate-browse-job-footer">
                  <span>
                    <i className="bi bi-clock-history"></i>
                    {job.posted}
                  </span>

                  <a
                    href={`/candidate/jobs/${job.id}`}
                    className="candidate-view-job-button"
                  >
                    View Details
                  </a>
                </div>
              </article>
            );
          })}
        </section>
      ) : (
        <div className="candidate-no-jobs">
          <i className="bi bi-search"></i>

          <h3>No jobs found</h3>

          <p>
            Try changing your search or filters.
          </p>

          <button
            type="button"
            onClick={clearFilters}
          >
            Clear Filters
          </button>
        </div>
      )}
    </div>
  );
};

export default BrowseJobs;