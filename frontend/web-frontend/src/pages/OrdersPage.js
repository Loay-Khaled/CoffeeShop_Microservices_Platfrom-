import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import toast from 'react-hot-toast';
import { orderApi, paymentApi } from '../services/api';
import OrderCard from '../components/OrderCard';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/OrdersPage.css';

const OrdersPage = () => {
  const navigate = useNavigate();
  const { keycloak } = useKeycloak();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchOrders = useCallback(async () => {
    if (!keycloak.authenticated) {
      navigate('/');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const response = await orderApi.getMyOrders();
      setOrders(response.data);
    } catch (err) {
      console.error('Error fetching orders:', err);
      setError('Failed to load orders. Please try again later.');
    } finally {
      setLoading(false);
    }
  }, [keycloak.authenticated, navigate]);

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

  const handleViewDetails = (orderId) => {
    navigate(`/orders/${orderId}`);
  };

  const handlePayNow = async (orderId) => {
    try {
      await paymentApi.processPayment(orderId);
      toast.success('Payment processed successfully!');
      fetchOrders(); // Refresh orders
    } catch (error) {
      console.error('Payment error:', error);
      toast.error('Payment failed. Please try again.');
    }
  };

  if (loading) {
    return <LoadingSpinner message="Loading your orders..." />;
  }

  return (
    <div className="orders-page">
      <div className="container">
        <div className="orders-header">
          <h1>My Orders</h1>
          <button onClick={fetchOrders} className="btn btn-outline">
            Refresh
          </button>
        </div>

        {error && (
          <div className="error-message">
            <p>{error}</p>
            <button onClick={fetchOrders} className="btn btn-primary">
              Retry
            </button>
          </div>
        )}

        {!error && orders.length === 0 ? (
          <div className="empty-orders">
            <span className="empty-icon">📦</span>
            <h3>No orders yet</h3>
            <p>Start by adding some items to your cart!</p>
            <button
              className="btn btn-primary"
              onClick={() => navigate('/catalog')}
            >
              Browse Menu
            </button>
          </div>
        ) : (
          <div className="orders-grid">
            {orders.map((order) => (
              <OrderCard
                key={order.id}
                order={order}
                onViewDetails={handleViewDetails}
                onPayNow={handlePayNow}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default OrdersPage;
