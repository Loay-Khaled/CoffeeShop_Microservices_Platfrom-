import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import toast from 'react-hot-toast';
import { useCart } from '../context/CartContext';
import { orderApi } from '../services/api';
import CartItem from '../components/CartItem';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/CartPage.css';

const CartPage = () => {
  const navigate = useNavigate();
  const { keycloak } = useKeycloak();
  const { items, updateQuantity, removeFromCart, clearCart, getTotal } = useCart();
  const [loading, setLoading] = useState(false);

  const handleCheckout = async () => {
    if (!keycloak.authenticated) {
      toast.error('Please login to place an order');
      keycloak.login();
      return;
    }

    if (items.length === 0) {
      toast.error('Your cart is empty');
      return;
    }

    setLoading(true);
    try {
      const username = keycloak.tokenParsed?.preferred_username;
      
      // Create order
      const orderResponse = await orderApi.createOrder(username);
      const orderId = orderResponse.data.id;

      // Add items to order
      for (const item of items) {
        await orderApi.addItemToOrder(orderId, item.id, item.quantity);
      }

      clearCart();
      toast.success('Order placed successfully!');
      navigate(`/checkout/${orderId}`);
    } catch (error) {
      console.error('Checkout error:', error);
      toast.error('Failed to place order. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <LoadingSpinner message="Processing your order..." />;
  }

  return (
    <div className="cart-page">
      <div className="container">
        <h1 className="page-title">Shopping Cart</h1>

        {items.length === 0 ? (
          <div className="empty-cart">
            <span className="empty-icon">🛒</span>
            <h3>Your cart is empty</h3>
            <p>Add some delicious coffee to get started!</p>
            <button
              className="btn btn-primary"
              onClick={() => navigate('/catalog')}
            >
              Browse Menu
            </button>
          </div>
        ) : (
          <div className="cart-content">
            <div className="cart-items">
              <div className="cart-header">
                <span>Product</span>
                <span>Price</span>
                <span>Quantity</span>
                <span>Subtotal</span>
                <span></span>
              </div>
              {items.map((item) => (
                <CartItem
                  key={item.id}
                  item={item}
                  onUpdateQuantity={updateQuantity}
                  onRemove={removeFromCart}
                />
              ))}
            </div>

            <div className="cart-summary">
              <h3>Order Summary</h3>
              <div className="summary-row">
                <span>Subtotal</span>
                <span>${getTotal().toFixed(2)}</span>
              </div>
              <div className="summary-row">
                <span>Tax (10%)</span>
                <span>${(getTotal() * 0.1).toFixed(2)}</span>
              </div>
              <div className="summary-row total">
                <span>Total</span>
                <span>${(getTotal() * 1.1).toFixed(2)}</span>
              </div>
              <button
                className="btn btn-primary btn-block"
                onClick={handleCheckout}
                disabled={items.length === 0}
              >
                Proceed to Checkout
              </button>
              <button
                className="btn btn-outline btn-block"
                onClick={() => navigate('/catalog')}
              >
                Continue Shopping
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default CartPage;
