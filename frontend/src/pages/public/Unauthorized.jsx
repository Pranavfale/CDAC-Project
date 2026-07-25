function Unauthorized() {
  return (
    <div className="page">
      <div className="card">
        <h1>403</h1>

        <h2>Access Denied</h2>

        <p style={{ marginTop: "10px" }}>
          You do not have permission to access this page.
        </p>
      </div>
    </div>
  );
}

export default Unauthorized;