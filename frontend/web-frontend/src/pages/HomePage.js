import React from 'react';
import { Link } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import '../styles/HomePage.css';

const HomePage = () => {
  const { keycloak } = useKeycloak();

  return (
    <div className="home-page">
      <section className="hero">
        <div className="hero-content">
          <h1 className="hero-title">Welcome to Coffee Shop</h1>
          <p className="hero-subtitle">
            Experience the finest coffee brewed with passion and served with love.
          </p>
          <div className="hero-actions">
            <Link to="/catalog" className="btn btn-primary btn-large">
              View Menu
            </Link>
            {!keycloak.authenticated && (
              <button
                onClick={() => keycloak.login()}
                className="btn btn-outline btn-large"
              >
                Sign In
              </button>
            )}
          </div>
        </div>
      </section>

      <section className="features">
        <div className="container">
          <h2 className="section-title">Why Choose Us?</h2>
          <div className="features-grid">
            <div className="feature-card">
              <span className="feature-icon">☕</span>
              <h3>Premium Quality</h3>
              <p>We source the finest coffee beans from around the world.</p>
            </div>
            <div className="feature-card">
              <span className="feature-icon">🚀</span>
              <h3>Fast Service</h3>
              <p>Quick and efficient ordering with real-time status updates.</p>
            </div>
            <div className="feature-card">
              <span className="feature-icon">💳</span>
              <h3>Secure Payment</h3>
              <p>Safe and secure payment processing for your peace of mind.</p>
            </div>
            <div className="feature-card">
              <span className="feature-icon">📱</span>
              <h3>Easy Ordering</h3>
              <p>Order from anywhere with our simple and intuitive interface.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="cta">
        <div className="container">
          <h2>Ready to Order?</h2>
          <p>Browse our menu and place your order today!</p>
          <Link to="/catalog" className="btn btn-primary btn-large">
            Order Now
          </Link>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
