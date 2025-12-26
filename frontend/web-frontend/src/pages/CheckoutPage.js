import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';
import { orderApi, paymentApi } from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/CheckoutPage.css';

const CheckoutPage = () => {
  const { orderId } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(false);
  const [paymentComplete, setPaymentComplete] = useState(false);

  const fetchOrder = useCallback(async () => {
    try {
      setLoading(true);
      const response = await orderApi.getOrder(orderId);
      setOrder(response.data);
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

  const handlePayment = async () => {
    setProcessing(true);
    try {
      await paymentApi.processPayment(orderId);
      setPaymentComplete(true);
      toast.success('Payment successful!');
    } catch (error) {
      console.error('Payment error:', error);
      toast.error('Payment failed. Please try again.');
    } finally {
      setProcessing(false);
    }
  };

  if (loading) {
    return <LoadingSpinner message="Loading order details..." />;
  }

  if (paymentComplete) {
    return (
      <div className="checkout-page">
        <div className="container">
          <div className="payment-success">
            <span className="success-icon">✓</span>
            <h1>Payment Successful!</h1>
            <p>Thank you for your order. Your coffee is being prepared.</p>
            <div className="order-confirmation">
              <p><strong>Order ID:</strong> #{orderId}</p>
              <p><strong>Total Paid:</strong> ${parseFloat(order?.total || 0).toFixed(2)}</p>
            </div>
            <div className="success-actions">
              <button
                className="btn btn-primary"
                onClick={() => navigate('/orders')}
              >
                View My Orders
              </button>
              <button
                className="btn btn-outline"
                onClick={() => navigate('/catalog')}
              >
                Order More
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="checkout-page">
      <div className="container">
        <h1 className="page-title">Checkout</h1>

        <div className="checkout-content">
          <div className="order-review">
            <h2>Order Review</h2>
            <div className="order-info">
              <p><strong>Order ID:</strong> #{order?.id}</p>
              <p><strong>Status:</strong> {order?.status}</p>
            </div>

            {order?.items && order.items.length > 0 && (
              <div className="order-items">
                <h3>Items</h3>
                <table className="items-table">
                  <thead>
                    <tr>
                      <th>Product</th>
                      <th>Quantity</th>
                      <th>Price</th>
                      <th>Subtotal</th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.items.map((item, index) => (
                      <tr key={index}>
                        <td>{item.productName}</td>
                        <td>{item.quantity}</td>
                        <td>${parseFloat(item.unitPrice).toFixed(2)}</td>
                        <td>
                          ${(parseFloat(item.unitPrice) * item.quantity).toFixed(2)}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>

          <div className="payment-section">
            <h2>Payment</h2>
            <div className="payment-summary">
              <div className="summary-row">
                <span>Subtotal</span>
                <span>${parseFloat(order?.total || 0).toFixed(2)}</span>
              </div>
              <div className="summary-row">
                <span>Tax (Included)</span>
                <span>-</span>
              </div>
              <div className="summary-row total">
                <span>Total</span>
                <span>${parseFloat(order?.total || 0).toFixed(2)}</span>
              </div>
            </div>

            <div className="payment-methods">
              <h3>Select Payment Method</h3>
              <div className="payment-options">
                <label className="payment-option selected">
                  <input type="radio" name="payment" defaultChecked />
                  <span className="option-icon">💳</span>
                  <span>Credit/Debit Card</span>
                </label>
                <label className="payment-option">
                  <input type="radio" name="payment" disabled />
                  <span className="option-icon">📱</span>
                  <span>Mobile Payment (Coming Soon)</span>
                </label>
              </div>
            </div>

            <button
              className="btn btn-primary btn-block btn-large"
              onClick={handlePayment}
              disabled={processing}
            >
              {processing ? 'Processing...' : `Pay $${parseFloat(order?.total || 0).toFixed(2)}`}
            </button>

            <button
              className="btn btn-outline btn-block"
              onClick={() => navigate('/cart')}
              disabled={processing}
            >
              Back to Cart
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CheckoutPage;
