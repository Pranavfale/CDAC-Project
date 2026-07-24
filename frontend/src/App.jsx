import { Navigate, Route, Routes } from "react-router-dom";
import AdminLayout from "./components/admin/AdminLayout.jsx";

function AdminPlaceholder() {
  return (
    <div>
      <h2>Admin Portal</h2>
      <p>Select an Admin feature from the sidebar.</p>
    </div>
  );
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/admin" replace />} />

      <Route path="/admin" element={<AdminLayout />}>
        <Route index element={<AdminPlaceholder />} />

        <Route path="*" element={<AdminPlaceholder />} />
      </Route>
    </Routes>
  );
}

export default App;