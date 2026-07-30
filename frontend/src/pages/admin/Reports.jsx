import { useState } from "react";

const Reports = () => {
  const [period, setPeriod] = useState("This Month");

  const monthlyData = [
    {
      month: "February",
      jobs: 42,
      applications: 210,
      candidates: 95,
      hires: 18,
    },
    {
      month: "March",
      jobs: 51,
      applications: 285,
      candidates: 120,
      hires: 24,
    },
    {
      month: "April",
      jobs: 48,
      applications: 260,
      candidates: 108,
      hires: 21,
    },
    {
      month: "May",
      jobs: 63,
      applications: 340,
      candidates: 145,
      hires: 31,
    },
    {
      month: "June",
      jobs: 71,
      applications: 390,
      candidates: 168,
      hires: 36,
    },
    {
      month: "July",
      jobs: 86,
      applications: 486,
      candidates: 192,
      hires: 44,
    },
  ];

  const companies = [
    {
      id: 1,
      company: "TechCorp Solutions",
      jobs: 18,
      applications: 124,
      hires: 15,
    },
    {
      id: 2,
      company: "SoftWave Technologies",
      jobs: 14,
      applications: 96,
      hires: 11,
    },
    {
      id: 3,
      company: "Info Systems",
      jobs: 12,
      applications: 82,
      hires: 9,
    },
    {
      id: 4,
      company: "NexGen Software",
      jobs: 9,
      applications: 71,
      hires: 7,
    },
    {
      id: 5,
      company: "CloudTech India",
      jobs: 8,
      applications: 65,
      hires: 6,
    },
  ];

  const maxApplications = Math.max(
    ...monthlyData.map((item) => item.applications)
  );

  return (
    <div className="admin-reports-page">
      <div className="admin-page-header">
        <div>
          <h2>Reports & Analytics</h2>
          <p>
            Monitor recruitment activity and platform
            performance.
          </p>
        </div>

        <div className="admin-report-filter">
          <i className="bi bi-calendar3"></i>

          <select
            value={period}
            onChange={(event) =>
              setPeriod(event.target.value)
            }
          >
            <option>This Week</option>
            <option>This Month</option>
            <option>Last 3 Months</option>
            <option>This Year</option>
          </select>
        </div>
      </div>

      {/* Statistics */}

      <section className="admin-stats-grid">
        <article className="admin-stat-card">
          <div className="admin-stat-card-top">
            <div className="admin-stat-icon">
              <i className="bi bi-briefcase"></i>
            </div>

            <span className="admin-stat-change">
              +18%
            </span>
          </div>

          <h3>86</h3>
          <p className="admin-stat-title">
            Jobs Posted
          </p>

          <span className="admin-stat-description">
            During {period.toLowerCase()}
          </span>
        </article>

        <article className="admin-stat-card">
          <div className="admin-stat-card-top">
            <div className="admin-stat-icon">
              <i className="bi bi-file-earmark-text"></i>
            </div>

            <span className="admin-stat-change">
              +24%
            </span>
          </div>

          <h3>486</h3>

          <p className="admin-stat-title">
            Applications
          </p>

          <span className="admin-stat-description">
            During {period.toLowerCase()}
          </span>
        </article>

        <article className="admin-stat-card">
          <div className="admin-stat-card-top">
            <div className="admin-stat-icon">
              <i className="bi bi-people"></i>
            </div>

            <span className="admin-stat-change">
              +15%
            </span>
          </div>

          <h3>192</h3>

          <p className="admin-stat-title">
            New Candidates
          </p>

          <span className="admin-stat-description">
            During {period.toLowerCase()}
          </span>
        </article>

        <article className="admin-stat-card">
          <div className="admin-stat-card-top">
            <div className="admin-stat-icon">
              <i className="bi bi-person-check"></i>
            </div>

            <span className="admin-stat-change">
              +12%
            </span>
          </div>

          <h3>44</h3>

          <p className="admin-stat-title">
            Successful Hires
          </p>

          <span className="admin-stat-description">
            During {period.toLowerCase()}
          </span>
        </article>
      </section>

      {/* Analytics */}

      <section className="admin-reports-grid">

        {/* Application Activity */}

        <div className="admin-dashboard-card">
          <div className="admin-card-header">
            <div>
              <h3>Application Activity</h3>

              <p>
                Applications received over the last
                six months
              </p>
            </div>
          </div>

          <div className="admin-chart-container">
            {monthlyData.map((item) => {
              const percentage =
                (item.applications / maxApplications) *
                100;

              return (
                <div
                  className="admin-chart-row"
                  key={item.month}
                >
                  <span className="admin-chart-label">
                    {item.month}
                  </span>

                  <div className="admin-chart-track">
                    <div
                      className="admin-chart-bar"
                      style={{
                        width: `${percentage}%`,
                      }}
                    ></div>
                  </div>

                  <strong>
                    {item.applications}
                  </strong>
                </div>
              );
            })}
          </div>
        </div>

        {/* Recruitment Funnel */}

        <div className="admin-dashboard-card">
          <div className="admin-card-header">
            <div>
              <h3>Recruitment Funnel</h3>

              <p>
                Current recruitment conversion
              </p>
            </div>
          </div>

          <div className="admin-funnel">
            <div className="admin-funnel-item">
              <div>
                <span>Applications</span>
                <strong>486</strong>
              </div>

              <div className="admin-funnel-track">
                <div
                  className="admin-funnel-fill"
                  style={{ width: "100%" }}
                ></div>
              </div>
            </div>

            <div className="admin-funnel-item">
              <div>
                <span>Reviewed</span>
                <strong>310</strong>
              </div>

              <div className="admin-funnel-track">
                <div
                  className="admin-funnel-fill"
                  style={{ width: "64%" }}
                ></div>
              </div>
            </div>

            <div className="admin-funnel-item">
              <div>
                <span>Shortlisted</span>
                <strong>148</strong>
              </div>

              <div className="admin-funnel-track">
                <div
                  className="admin-funnel-fill"
                  style={{ width: "30%" }}
                ></div>
              </div>
            </div>

            <div className="admin-funnel-item">
              <div>
                <span>Interviewed</span>
                <strong>82</strong>
              </div>

              <div className="admin-funnel-track">
                <div
                  className="admin-funnel-fill"
                  style={{ width: "17%" }}
                ></div>
              </div>
            </div>

            <div className="admin-funnel-item">
              <div>
                <span>Hired</span>
                <strong>44</strong>
              </div>

              <div className="admin-funnel-track">
                <div
                  className="admin-funnel-fill"
                  style={{ width: "9%" }}
                ></div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Monthly Report */}

      <section className="admin-dashboard-card admin-report-table-card">
        <div className="admin-card-header">
          <div>
            <h3>Monthly Recruitment Report</h3>
            <p>
              Recruitment statistics by month
            </p>
          </div>
        </div>

        <div className="admin-table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Month</th>
                <th>Jobs Posted</th>
                <th>Applications</th>
                <th>New Candidates</th>
                <th>Hires</th>
              </tr>
            </thead>

            <tbody>
              {monthlyData.map((item) => (
                <tr key={item.month}>
                  <td>
                    <strong>{item.month}</strong>
                  </td>

                  <td>{item.jobs}</td>
                  <td>{item.applications}</td>
                  <td>{item.candidates}</td>

                  <td>
                    <span className="admin-hire-count">
                      {item.hires}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>

      {/* Company Activity */}

      <section className="admin-dashboard-card admin-report-table-card">
        <div className="admin-card-header">
          <div>
            <h3>Company Activity</h3>

            <p>
              Recruitment activity by company
            </p>
          </div>
        </div>

        <div className="admin-table-wrapper">
          <table className="admin-table">
            <thead>
              <tr>
                <th>Company</th>
                <th>Jobs Posted</th>
                <th>Applications</th>
                <th>Hires</th>
              </tr>
            </thead>

            <tbody>
              {companies.map((company) => (
                <tr key={company.id}>
                  <td>
                    <div className="admin-company-report">
                      <div className="admin-company-report-icon">
                        <i className="bi bi-building"></i>
                      </div>

                      <strong>
                        {company.company}
                      </strong>
                    </div>
                  </td>

                  <td>{company.jobs}</td>
                  <td>{company.applications}</td>

                  <td>
                    <span className="admin-hire-count">
                      {company.hires}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
};

export default Reports;