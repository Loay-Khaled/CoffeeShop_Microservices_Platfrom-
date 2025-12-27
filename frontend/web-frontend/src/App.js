import React from "react";
import { Routes, Route } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";
import { Toaster } from "react-hot-toast";
import { CartProvider } from "./context/CartContext";
import Navbar from "./components/Navbar";
import HomePage from "./pages/HomePage";
import CatalogPage from "./pages/CatalogPage";
import CartPage from "./pages/CartPage";
import OrdersPage from "./pages/OrdersPage";
import OrderDetailsPage from "./pages/OrderDetailsPage";
import CheckoutPage from "./pages/CheckoutPage";
import AdminPage from "./pages/AdminPage";
import LoadingSpinner from "./components/LoadingSpinner";
import "./styles/App.css";

// Protected Route wrapper
const ProtectedRoute = ({ children }) => {
  const { keycloak, initialized } = useKeycloak();

  if (!initialized) {
    return <LoadingSpinner message="Initializing..." />;
  }

  if (!keycloak.authenticated) {
    keycloak.login();
    return <LoadingSpinner message="Redirecting to login..." />;
  }

  return children;
};

function App() {
  const { initialized } = useKeycloak();

  if (!initialized) {
    return <LoadingSpinner message="Loading application..." />;
  }

  return (
    <CartProvider>
      <div className="app">
        <Navbar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/catalog" element={<CatalogPage />} />
            <Route path="/cart" element={<CartPage />} />
            <Route
              path="/orders"
              element={
                <ProtectedRoute>
                  <OrdersPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/orders/:orderId"
              element={
                <ProtectedRoute>
                  <OrderDetailsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/checkout/:orderId"
              element={
                <ProtectedRoute>
                  <CheckoutPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin"
              element={
                <ProtectedRoute>
                  <AdminPage />
                </ProtectedRoute>
              }
            />
          </Routes>
        </main>
        <footer className="footer">
          <div className="container">
            <p>&copy; 2024 Coffee Shop. All rights reserved.</p>
          </div>
        </footer>
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 3000,
            style: {
              background: "#333",
              color: "#fff",
            },
            success: {
              iconTheme: {
                primary: "#6F4E37",
                secondary: "#fff",
              },
            },
          }}
        />
      </div>
    </CartProvider>
  );
}

export default App;
