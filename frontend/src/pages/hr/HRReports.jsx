import { useMemo, useState } from "react";
import "../../styles/hrReports.css";

const reportData = {
  "This Month": {
    jobs: 18,
    applications: 286,
    shortlisted: 74,
    interviews: 38,
    offers: 17,
    hired: 12,
    trend: [
      { label: "Week 1", applications: 48 },
      { label: "Week 2", applications: 65 },
      { label: "Week 3", applications: 79 },
      { label: "Week 4", applications: 94 },
    ],
  },

  "Last 3 Months": {
    jobs: 46,
    applications: 764,
    shortlisted: 218,
    interviews: 126,
    offers: 53,
    hired: 39,
    trend: [
      { label: "May", applications: 216 },
      { label: "June", applications: 262 },
      { label: "July", applications: 286 },
    ],
  },

  "Last 6 Months": {
    jobs: 92,
    applications: 1486,
    shortlisted: 436,
    interviews: 248,
    offers: 104,
    hired: 78,
    trend: [
      { label: "February", applications: 196 },
      { label: "March", applications: 224 },
      { label: "April", applications: 256 },
      { label: "May", applications: 262 },
      { label: "June", applications: 262 },
      { label: "July", applications: 286 },
    ],
  },

  "This Year": {
    jobs: 138,
    applications: 2254,
    shortlisted: 682,
    interviews: 392,
    offers: 168,
    hired: 124,
    trend: [
      { label: "Q1", applications: 482 },
      { label: "Q2", applications: 694 },
      { label: "Q3", applications: 706 },
      { label: "Q4", applications: 372 },
    ],
  },
};

const topJobs = [
  {
    id: 1,
    title: "Java Developer",
    applications: 86,
    shortlisted: 24,
    interviews: 12,
    hired: 4,
    status: "Active",
  },
  {
    id: 2,
    title: "React Developer",
    applications: 72,
    shortlisted: 19,
    interviews: 10,
    hired: 3,
    status: "Active",
  },
  {
    id: 3,
    title: "Backend Developer",
    applications: 54,
    shortlisted: 16,
    interviews: 8,
    hired: 2,
    status: "Active",
  },
  {
    id: 4,
    title: "Software Tester",
    applications: 43,
    shortlisted: 9,
    interviews: 5,
    hired: 2,
    status: "Closed",
  },
  {
    id: 5,
    title: "Frontend Intern",
    applications: 31,
    shortlisted: 6,
    interviews: 3,
    hired: 1,
    status: "Closed",
  },
];

function HRReports() {
  const [selectedPeriod, setSelectedPeriod] =
    useState("This Month");

  const currentReport = useMemo(
    () => reportData[selectedPeriod],
    [selectedPeriod]
  );

  const maximumApplications = Math.max(
    ...currentReport.trend.map(
      (item) => item.applications
    )
  );

  const conversionRate =
    currentReport.applications === 0
      ? 0
      : (
          (currentReport.hired /
            currentReport.applications) *
          100
        ).toFixed(1);

  const interviewSuccessRate =
    currentReport.interviews === 0
      ? 0
      : (
          (currentReport.hired /
            currentReport.interviews) *
          100
        ).toFixed(1);

  const downloadReport = () => {
    const headingRows = [
      ["HR Recruitment Report"],
      ["Period", selectedPeriod],
      [],
      ["Metric", "Count"],
      ["Jobs Posted", currentReport.jobs],
      ["Applications", currentReport.applications],
      ["Shortlisted", currentReport.shortlisted],
      ["Interviews", currentReport.interviews],
      ["Offers", currentReport.offers],
      ["Hired", currentReport.hired],
      ["Conversion Rate", `${conversionRate}%`],
      [],
      [
        "Job Title",
        "Applications",
        "Shortlisted",
        "Interviews",
        "Hired",
        "Status",
      ],
    ];

    const jobRows = topJobs.map((job) => [
      job.title,
      job.applications,
      job.shortlisted,
      job.interviews,
      job.hired,
      job.status,
    ]);

    const csvRows = [...headingRows, ...jobRows];

    const csvContent = csvRows
      .map((row) =>
        row
          .map((value) => {
            const text = String(value ?? "");
            return `"${text.replaceAll('"', '""')}"`;
          })
          .join(",")
      )
      .join("\n");

    const blob = new Blob([csvContent], {
      type: "text/csv;charset=utf-8;",
    });

    const downloadUrl = URL.createObjectURL(blob);
    const link = document.createElement("a");

    link.href = downloadUrl;
    link.download = `hr-report-${selectedPeriod
      .toLowerCase()
      .replaceAll(" ", "-")}.csv`;

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

    URL.revokeObjectURL(downloadUrl);
  };

  return (
    <div className="hr-reports-page">
      <div className="hr-reports-header">
        <div>
          <h2>Reports & Analytics</h2>
          <p>
            Monitor recruitment performance and hiring
            activity.
          </p>
        </div>

        <div className="hr-report-header-actions">
          <div className="hr-report-period">
            <i className="bi bi-calendar3"></i>

            <select
              value={selectedPeriod}
              onChange={(event) =>
                setSelectedPeriod(event.target.value)
              }
            >
              {Object.keys(reportData).map((period) => (
                <option key={period} value={period}>
                  {period}
                </option>
              ))}
            </select>
          </div>

          <button
            type="button"
            className="hr-export-report-button"
            onClick={downloadReport}
          >
            <i className="bi bi-download"></i>
            Export Report
          </button>
        </div>
      </div>

      <section className="hr-report-summary-grid">
        <article className="hr-report-summary-card">
          <div className="hr-report-card-icon">
            <i className="bi bi-briefcase"></i>
          </div>

          <div>
            <span>Jobs Posted</span>
            <strong>{currentReport.jobs}</strong>
            <p>During {selectedPeriod.toLowerCase()}</p>
          </div>
        </article>

        <article className="hr-report-summary-card">
          <div className="hr-report-card-icon">
            <i className="bi bi-file-earmark-text"></i>
          </div>

          <div>
            <span>Applications</span>
            <strong>
              {currentReport.applications}
            </strong>
            <p>Total applications received</p>
          </div>
        </article>

        <article className="hr-report-summary-card">
          <div className="hr-report-card-icon">
            <i className="bi bi-person-check"></i>
          </div>

          <div>
            <span>Shortlisted</span>
            <strong>
              {currentReport.shortlisted}
            </strong>
            <p>Candidates shortlisted</p>
          </div>
        </article>

        <article className="hr-report-summary-card">
          <div className="hr-report-card-icon">
            <i className="bi bi-trophy"></i>
          </div>

          <div>
            <span>Successful Hires</span>
            <strong>{currentReport.hired}</strong>
            <p>{conversionRate}% conversion rate</p>
          </div>
        </article>
      </section>

      <section className="hr-report-main-grid">
        <article className="hr-report-panel">
          <div className="hr-report-panel-header">
            <div>
              <h3>Application Activity</h3>
              <p>
                Applications received during the selected
                period
              </p>
            </div>

            <i className="bi bi-bar-chart"></i>
          </div>

          <div className="hr-application-chart">
            {currentReport.trend.map((item) => {
              const percentage =
                maximumApplications === 0
                  ? 0
                  : (item.applications /
                      maximumApplications) *
                    100;

              return (
                <div
                  className="hr-chart-row"
                  key={item.label}
                >
                  <span className="hr-chart-label">
                    {item.label}
                  </span>

                  <div className="hr-chart-track">
                    <div
                      className="hr-chart-fill"
                      style={{
                        width: `${percentage}%`,
                      }}
                    ></div>
                  </div>

                  <strong>{item.applications}</strong>
                </div>
              );
            })}
          </div>
        </article>

        <article className="hr-report-panel">
          <div className="hr-report-panel-header">
            <div>
              <h3>Recruitment Funnel</h3>
              <p>Candidate progress through hiring</p>
            </div>

            <i className="bi bi-funnel"></i>
          </div>

          <div className="hr-recruitment-funnel">
            <FunnelItem
              label="Applications"
              value={currentReport.applications}
              total={currentReport.applications}
            />

            <FunnelItem
              label="Shortlisted"
              value={currentReport.shortlisted}
              total={currentReport.applications}
            />

            <FunnelItem
              label="Interviews"
              value={currentReport.interviews}
              total={currentReport.applications}
            />

            <FunnelItem
              label="Offers"
              value={currentReport.offers}
              total={currentReport.applications}
            />

            <FunnelItem
              label="Hired"
              value={currentReport.hired}
              total={currentReport.applications}
            />
          </div>
        </article>
      </section>

      <section className="hr-report-rates-grid">
        <article className="hr-rate-card">
          <div>
            <span>Application to Shortlist</span>

            <strong>
              {currentReport.applications === 0
                ? 0
                : (
                    (currentReport.shortlisted /
                      currentReport.applications) *
                    100
                  ).toFixed(1)}
              %
            </strong>
          </div>

          <i className="bi bi-person-lines-fill"></i>
        </article>

        <article className="hr-rate-card">
          <div>
            <span>Interview Success Rate</span>
            <strong>
              {interviewSuccessRate}%
            </strong>
          </div>

          <i className="bi bi-calendar-check"></i>
        </article>

        <article className="hr-rate-card">
          <div>
            <span>Offer Acceptance Rate</span>

            <strong>
              {currentReport.offers === 0
                ? 0
                : (
                    (currentReport.hired /
                      currentReport.offers) *
                    100
                  ).toFixed(1)}
              %
            </strong>
          </div>

          <i className="bi bi-file-earmark-check"></i>
        </article>
      </section>

      <section className="hr-report-panel hr-top-jobs-panel">
        <div className="hr-report-panel-header">
          <div>
            <h3>Job Performance</h3>
            <p>
              Recruitment performance for posted jobs
            </p>
          </div>
        </div>

        <div className="hr-report-table-wrapper">
          <table className="hr-report-table">
            <thead>
              <tr>
                <th>Job Position</th>
                <th>Applications</th>
                <th>Shortlisted</th>
                <th>Interviews</th>
                <th>Hired</th>
                <th>Hire Rate</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              {topJobs.map((job) => {
                const hireRate =
                  job.applications === 0
                    ? 0
                    : (
                        (job.hired /
                          job.applications) *
                        100
                      ).toFixed(1);

                return (
                  <tr key={job.id}>
                    <td>
                      <div className="hr-report-job-cell">
                        <div className="hr-report-job-icon">
                          <i className="bi bi-briefcase"></i>
                        </div>

                        <strong>{job.title}</strong>
                      </div>
                    </td>

                    <td>{job.applications}</td>
                    <td>{job.shortlisted}</td>
                    <td>{job.interviews}</td>

                    <td>
                      <span className="hr-hired-count">
                        {job.hired}
                      </span>
                    </td>

                    <td>{hireRate}%</td>

                    <td>
                      <span
                        className={`hr-job-report-status ${
                          job.status === "Active"
                            ? "hr-job-report-active"
                            : "hr-job-report-closed"
                        }`}
                      >
                        {job.status}
                      </span>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}

function FunnelItem({ label, value, total }) {
  const percentage =
    total === 0 ? 0 : (value / total) * 100;

  return (
    <div className="hr-funnel-item">
      <div className="hr-funnel-info">
        <span>{label}</span>
        <strong>{value}</strong>
      </div>

      <div className="hr-funnel-track">
        <div
          className="hr-funnel-fill"
          style={{
            width: `${percentage}%`,
          }}
        ></div>
      </div>
    </div>
  );
}

export default HRReports;