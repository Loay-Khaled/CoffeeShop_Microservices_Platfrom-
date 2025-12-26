import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { orderApi, paymentApi } from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/OrderDetailsPage.css';

const OrderDetailsPage = () => {
  const { orderId } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [paymentStatus, setPaymentStatus] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchOrder = useCallback(async () => {
    try {
      setLoading(true);
      const [orderResponse, paymentResponse] = await Promise.all([
        orderApi.getOrder(orderId),
        paymentApi.getPaymentStatus(orderId).catch(() => null),
      ]);
      setOrder(orderResponse.data);
      if (paymentResponse) {
        setPaymentStatus(paymentResponse.data);
      }
    } catch (error) {
      console.error('Error fetching order:', error);
      toast.error('Failed to load order details');
      navigate('/orders');
    } finally {
      setLoading(false);
    }
  }, [orderId, navigate]);

  useEffect(() => {
    fetchOrder();
  }, [fetchOrder]);

  const handlePayNow = async () => {
    try {
      await paymentApi.processPayment(orderId);
      toast.success('Payment processed successfully!');
      fetchOrder();
    } catch (error) {
      console.error('Payment error:', error);
      toast.error('Payment failed. Please try again.');
    }
  };

  const getStatusClass = (status) => {
    switch (status?.toUpperCase()) {
      case 'CREATED':
        return 'status-created';
      case 'PAID':
      case 'PROCESSED':
      case 'COMPLETED':
        return 'status-paid';
      case 'PROCESSING':
        return 'status-processing';
      case 'CANCELLED':
        return 'status-cancelled';
      default:
        return 'status-default';
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  if (loading) {
    return <LoadingSpinner message="Loading order details..." />;
  }

  if (!order) {
    return (
      <div className="order-details-page">
        <div className="container">
          <div className="error-message">
            <p>Order not found</p>
            <button
              className="btn btn-primary"
              onClick={() => navigate('/orders')}
            >
              Back to Orders
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="order-details-page">
      <div className="container">
        <div className="page-header">
          <button
            className="btn btn-outline back-btn"
            onClick={() => navigate('/orders')}
          >
            ← Back to Orders
          </button>
          <h1>Order #{order.id}</h1>
        </div>

        <div className="order-details-content">
          <div className="order-main">
            <div className="order-info-card">
              <h2>Order Information</h2>
              <div className="info-grid">
                <div className="info-item">
                  <span className="label">Order ID</span>
                  <span className="value">#{order.id}</span>
                </div>
                <div className="info-item">
                  <span className="label">Status</span>
                  <span className={`value status ${getStatusClass(order.status)}`}>
                    {order.status}
                  </span>
                </div>
                <div className="info-item">
                  <span className="label">Customer</span>
                  <span className="value">{order.customerUsername}</span>
                </div>
                <div className="info-item">
                  <span className="label">Date</span>
                  <span className="value">{formatDate(order.createdAt)}</span>
                </div>
              </div>
            </div>

            <div className="order-items-card">
              <h2>Order Items</h2>
              {order.items && order.items.length > 0 ? (
                <table className="items-table">
                  <thead>
                    <tr>
                      <th>Product</th>
                      <th>Unit Price</th>
                      <th>Quantity</th>
                      <th>Subtotal</th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.items.map((item, index) => (
                      <tr key={index}>
                        <td>
                          <div className="product-info">
                            <span className="product-icon">☕</span>
                            <span>{item.productName}</span>
                          </div>
                        </td>
                        <td>${parseFloat(item.unitPrice).toFixed(2)}</td>
                        <td>{item.quantity}</td>
                        <td>
                          ${(parseFloat(item.unitPrice) * item.quantity).toFixed(2)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                  <tfoot>
                    <tr>
                      <td colSpan="3"><strong>Total</strong></td>
                      <td><strong>${parseFloat(order.total).toFixed(2)}</strong></td>
                    </tr>
                  </tfoot>
                </table>
              ) : (
                <p className="no-items">No items in this order</p>
              )}
            </div>
          </div>

          <div className="order-sidebar">
            <div className="payment-status-card">
              <h2>Payment Status</h2>
              {paymentStatus ? (
                <div className="payment-info">
                  <div className="info-item">
                    <span className="label">Status</span>
                    <span className={`value status ${getStatusClass(paymentStatus.status)}`}>
                      {paymentStatus.status}
                    </span>
                  </div>
                  {paymentStatus.amount && (
                    <div className="info-item">
                      <span className="label">Amount</span>
                      <span className="value">${paymentStatus.amount}</span>
                    </div>
                  )}
                </div>
              ) : (
                <p className="pending">Payment pending</p>
              )}

              {order.status === 'CREATED' && (
                <button
                  className="btn btn-primary btn-block"
                  onClick={handlePayNow}
                >
                  Pay Now
                </button>
              )}
            </div>

            <div className="order-summary-card">
              <h2>Order Summary</h2>
              <div className="summary-row">
                <span>Items ({order.items?.length || 0})</span>
                <span>${parseFloat(order.total).toFixed(2)}</span>
              </div>
              <div className="summary-row total">
                <span>Total</span>
                <span>${parseFloat(order.total).toFixed(2)}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderDetailsPage;
