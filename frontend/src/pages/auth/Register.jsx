import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import {
  BriefcaseBusiness,
  Check,
  Eye,
  EyeOff,
  Info,
  LockKeyhole,
  Mail,
  Phone,
  User,
} from "lucide-react";

function Register() {
  const navigate = useNavigate();

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] =
    useState(false);

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
  });

  const [errors, setErrors] = useState({});

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((previousData) => ({
      ...previousData,
      [name]: value,
    }));

    setErrors((previousErrors) => ({
      ...previousErrors,
      [name]: "",
    }));
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.firstName.trim()) {
      newErrors.firstName = "First name is required.";
    }

    if (!formData.lastName.trim()) {
      newErrors.lastName = "Last name is required.";
    }

    if (!formData.email.trim()) {
      newErrors.email = "Email is required.";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Enter a valid email address.";
    }

    if (!formData.phone.trim()) {
      newErrors.phone = "Phone number is required.";
    } else if (!/^[6-9]\d{9}$/.test(formData.phone)) {
      newErrors.phone =
        "Enter a valid 10-digit mobile number.";
    }

    if (!formData.password) {
      newErrors.password = "Password is required.";
    } else if (formData.password.length < 8) {
      newErrors.password =
        "Password must contain at least 8 characters.";
    }

    if (!formData.confirmPassword) {
      newErrors.confirmPassword =
        "Please confirm your password.";
    } else if (
      formData.password !== formData.confirmPassword
    ) {
      newErrors.confirmPassword =
        "Passwords do not match.";
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

    /*
      FRONTEND DEMO ONLY

      Real registration will later call ASP.NET Core.
      The backend will create the user and automatically
      assign the CANDIDATE role.
    */

    alert(
      "Candidate account created successfully. You can now sign in."
    );

    navigate("/login");
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
          <h1>Start your next career journey.</h1>

          <p>
            Create your candidate profile, discover open
            opportunities and track your recruitment progress
            from application to hiring.
          </p>

          <div className="auth-features">
            <div className="auth-feature">
              <span className="auth-feature-icon">
                <Check size={16} />
              </span>
              Build your professional candidate profile
            </div>

            <div className="auth-feature">
              <span className="auth-feature-icon">
                <Check size={16} />
              </span>
              Search and apply for open positions
            </div>

            <div className="auth-feature">
              <span className="auth-feature-icon">
                <Check size={16} />
              </span>
              Track interviews and job offers
            </div>
          </div>
        </div>

        <div className="auth-brand-footer">
          Web-Based Recruitment System
        </div>
      </section>

      <section className="auth-form-panel">
        <div className="auth-form-container register-form-container">
          <div className="auth-form-header">
            <h2>Create candidate account</h2>

            <p>
              Enter your information to get started.
            </p>
          </div>

          <div className="candidate-role-notice">
            <Info size={18} />

            <span>
              Public registration creates a Candidate account.
              HR accounts are created by the Administrator.
            </span>
          </div>

          <form onSubmit={handleSubmit} noValidate>
            <div className="form-grid">
              <div className="form-group">
                <label
                  className="form-label"
                  htmlFor="firstName"
                >
                  First Name
                </label>

                <div className="auth-input-wrapper">
                  <User
                    className="auth-input-icon"
                    size={18}
                  />

                  <input
                    id="firstName"
                    name="firstName"
                    type="text"
                    className="form-control"
                    placeholder="First name"
                    value={formData.firstName}
                    onChange={handleChange}
                  />
                </div>

                {errors.firstName && (
                  <span className="field-error">
                    {errors.firstName}
                  </span>
                )}
              </div>

              <div className="form-group">
                <label
                  className="form-label"
                  htmlFor="lastName"
                >
                  Last Name
                </label>

                <div className="auth-input-wrapper">
                  <User
                    className="auth-input-icon"
                    size={18}
                  />

                  <input
                    id="lastName"
                    name="lastName"
                    type="text"
                    className="form-control"
                    placeholder="Last name"
                    value={formData.lastName}
                    onChange={handleChange}
                  />
                </div>

                {errors.lastName && (
                  <span className="field-error">
                    {errors.lastName}
                  </span>
                )}
              </div>

              <div className="form-group form-grid-full">
                <label
                  className="form-label"
                  htmlFor="registerEmail"
                >
                  Email Address
                </label>

                <div className="auth-input-wrapper">
                  <Mail
                    className="auth-input-icon"
                    size={18}
                  />

                  <input
                    id="registerEmail"
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

              <div className="form-group form-grid-full">
                <label
                  className="form-label"
                  htmlFor="phone"
                >
                  Phone Number
                </label>

                <div className="auth-input-wrapper">
                  <Phone
                    className="auth-input-icon"
                    size={18}
                  />

                  <input
                    id="phone"
                    name="phone"
                    type="tel"
                    className="form-control"
                    placeholder="9876543210"
                    maxLength="10"
                    value={formData.phone}
                    onChange={handleChange}
                  />
                </div>

                {errors.phone && (
                  <span className="field-error">
                    {errors.phone}
                  </span>
                )}
              </div>

              <div className="form-group">
                <label
                  className="form-label"
                  htmlFor="registerPassword"
                >
                  Password
                </label>

                <div className="auth-input-wrapper has-password-action">
                  <LockKeyhole
                    className="auth-input-icon"
                    size={18}
                  />

                  <input
                    id="registerPassword"
                    name="password"
                    type={showPassword ? "text" : "password"}
                    className="form-control"
                    placeholder="Minimum 8 characters"
                    value={formData.password}
                    onChange={handleChange}
                  />

                  <button
                    type="button"
                    className="password-toggle"
                    onClick={() =>
                      setShowPassword(
                        (previousValue) => !previousValue
                      )
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

              <div className="form-group">
                <label
                  className="form-label"
                  htmlFor="confirmPassword"
                >
                  Confirm Password
                </label>

                <div className="auth-input-wrapper has-password-action">
                  <LockKeyhole
                    className="auth-input-icon"
                    size={18}
                  />

                  <input
                    id="confirmPassword"
                    name="confirmPassword"
                    type={
                      showConfirmPassword
                        ? "text"
                        : "password"
                    }
                    className="form-control"
                    placeholder="Repeat password"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                  />

                  <button
                    type="button"
                    className="password-toggle"
                    onClick={() =>
                      setShowConfirmPassword(
                        (previousValue) => !previousValue
                      )
                    }
                  >
                    {showConfirmPassword ? (
                      <EyeOff size={18} />
                    ) : (
                      <Eye size={18} />
                    )}
                  </button>
                </div>

                {errors.confirmPassword && (
                  <span className="field-error">
                    {errors.confirmPassword}
                  </span>
                )}
              </div>
            </div>

            <button
              type="submit"
              className="btn btn-primary auth-submit"
            >
              Create Account
            </button>
          </form>

          <div className="auth-switch">
            Already have an account?{" "}
            <Link
              className="auth-link"
              to="/login"
            >
              Sign in
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Register;