import React from 'react';
import { Link } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import { useCart } from '../context/CartContext';
import '../styles/Navbar.css';

const Navbar = () => {
  const { keycloak, initialized } = useKeycloak();
  const { getItemCount } = useCart();
  const itemCount = getItemCount();

  const handleLogin = () => keycloak.login();
  const handleLogout = () => keycloak.logout();
  const handleRegister = () => keycloak.register();

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          ☕ Coffee Shop
        </Link>

        <ul className="navbar-menu">
          <li>
            <Link to="/" className="navbar-link">Home</Link>
          </li>
          <li>
            <Link to="/catalog" className="navbar-link">Menu</Link>
          </li>
          {keycloak.authenticated && (
            <li>
              <Link to="/orders" className="navbar-link">My Orders</Link>
            </li>
          )}
        </ul>

        <div className="navbar-actions">
          <Link to="/cart" className="cart-link">
            🛒
            {itemCount > 0 && <span className="cart-badge">{itemCount}</span>}
          </Link>

          {initialized && (
            <>
              {keycloak.authenticated ? (
                <div className="user-menu">
                  <span className="user-name">
                    👋 {keycloak.tokenParsed?.preferred_username}
                  </span>
                  <button onClick={handleLogout} className="btn btn-outline">
                    Logout
                  </button>
                </div>
              ) : (
                <div className="auth-buttons">
                  <button onClick={handleLogin} className="btn btn-primary">
                    Login
                  </button>
                  <button onClick={handleRegister} className="btn btn-outline">
                    Register
                  </button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
