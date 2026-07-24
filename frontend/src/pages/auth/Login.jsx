import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import {
  BriefcaseBusiness,
  Check,
  Eye,
  EyeOff,
  LockKeyhole,
  Mail,
} from "lucide-react";

import { mockUsers } from "../../data/mockUsers";

function Login() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
    rememberMe: false,
  });

  const [errors, setErrors] = useState({});
  const [loginError, setLoginError] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: type === "checkbox" ? checked : value,
    }));

    setErrors((previousErrors) => ({
      ...previousErrors,
      [name]: "",
    }));

    setLoginError("");
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.email.trim()) {
      newErrors.email = "Email is required.";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Enter a valid email address.";
    }

    if (!formData.password) {
      newErrors.password = "Password is required.";
    }

    return newErrors;
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    const validationErrors = validateForm();

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const user = mockUsers.find(
      (currentUser) =>
        currentUser.email.toLowerCase() ===
          formData.email.trim().toLowerCase() &&
        currentUser.password === formData.password
    );

    if (!user) {
      setLoginError("Invalid email or password.");
      return;
    }

    if (!user.isActive) {
      setLoginError(
        "Your account is inactive. Please contact the administrator."
      );
      return;
    }


    const authenticatedUser = {
      id: user.id,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      role: user.role,
    };

    localStorage.setItem(
      "recruitflowUser",
      JSON.stringify(authenticatedUser)
    );

    if (user.role === "ADMIN") {
      navigate("/admin/dashboard");
    } else if (user.role === "HR") {
      navigate("/hr/dashboard");
    } else {
      navigate("/candidate/dashboard");
    }
  };

  return (
    <div className="auth-page">
      <section className="auth-brand-panel">
        <div className="auth-logo">
          <span className="auth-logo-icon">
            <BriefcaseBusiness size={23} />
          </span>

          RecruitFlow
        </div>

        <div className="auth-brand-content">
          <h1>Connecting talent with opportunity.</h1>

          <p>
            Manage the complete recruitment journey from job discovery
            and applications to interviews, offers and hiring.
          </p>

          <div className="auth-features">
            <div className="auth-feature">
              <span className="auth-feature-icon">
                <Check size={16} />
              </span>
              Discover relevant job opportunities
            </div>

            <div className="auth-feature">
              <span className="auth-feature-icon">
                <Check size={16} />
              </span>
              Track every application in one place
            </div>

            <div className="auth-feature">
              <span className="auth-feature-icon">
                <Check size={16} />
              </span>
              Simplify recruitment for HR teams
            </div>
          </div>
        </div>

        <div className="auth-brand-footer">
          Web-Based Recruitment System
        </div>
      </section>

      <section className="auth-form-panel">
        <div className="auth-form-container">
          <div className="auth-form-header">
            <h2>Welcome back</h2>

            <p>
              Enter your credentials to access your account.
            </p>
          </div>

          {loginError && (
            <div className="auth-alert">
              {loginError}
            </div>
          )}

          <form onSubmit={handleSubmit} noValidate>
            <div className="form-group">
              <label className="form-label" htmlFor="email">
                Email Address
              </label>

              <div className="auth-input-wrapper">
                <Mail
                  className="auth-input-icon"
                  size={18}
                />

                <input
                  id="email"
                  name="email"
                  type="email"
                  className="form-control"
                  placeholder="you@example.com"
                  value={formData.email}
                  onChange={handleChange}
                />
              </div>

              {errors.email && (
                <span className="field-error">
                  {errors.email}
                </span>
              )}
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="password">
                Password
              </label>

              <div className="auth-input-wrapper has-password-action">
                <LockKeyhole
                  className="auth-input-icon"
                  size={18}
                />

                <input
                  id="password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  className="form-control"
                  placeholder="Enter your password"
                  value={formData.password}
                  onChange={handleChange}
                />

                <button
                  type="button"
                  className="password-toggle"
                  onClick={() =>
                    setShowPassword((previousValue) => !previousValue)
                  }
                  aria-label={
                    showPassword
                      ? "Hide password"
                      : "Show password"
                  }
                >
                  {showPassword ? (
                    <EyeOff size={18} />
                  ) : (
                    <Eye size={18} />
                  )}
                </button>
              </div>

              {errors.password && (
                <span className="field-error">
                  {errors.password}
                </span>
              )}
            </div>

            <div className="auth-row">
              <label className="checkbox-label">
                <input
                  name="rememberMe"
                  type="checkbox"
                  checked={formData.rememberMe}
                  onChange={handleChange}
                />

                Remember me
              </label>

              <button
                type="button"
                className="auth-link"
                style={{
                  border: "none",
                  background: "transparent",
                }}
                onClick={() =>
                  alert(
                    "Forgot password will be connected to the authentication backend later."
                  )
                }
              >
                Forgot password?
              </button>
            </div>

            <button
              type="submit"
              className="btn btn-primary auth-submit"
            >
              Sign In
            </button>
          </form>

          <div className="auth-switch">
            New candidate?{" "}
            <Link
              className="auth-link"
              to="/register"
            >
              Create an account
            </Link>
          </div>

          <div className="demo-accounts">
            <div className="demo-accounts-title">
              Evaluation Demo Accounts
            </div>

            <div className="demo-account">
              <span className="demo-role">
                Candidate
              </span>

              <span className="demo-email">
                candidate@recruitflow.com
              </span>
            </div>

            <div className="demo-account">
              <span className="demo-role">
                HR
              </span>

              <span className="demo-email">
                hr@recruitflow.com
              </span>
            </div>

            <div className="demo-account">
              <span className="demo-role">
                Admin
              </span>

              <span className="demo-email">
                admin@recruitflow.com
              </span>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Login;