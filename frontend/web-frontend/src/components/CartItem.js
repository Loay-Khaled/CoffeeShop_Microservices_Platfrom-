import React from 'react';
import '../styles/CartItem.css';

const CartItem = ({ item, onUpdateQuantity, onRemove }) => {
  return (
    <div className="cart-item">
      <div className="cart-item-image">
        <span className="coffee-icon">☕</span>
      </div>
      
      <div className="cart-item-info">
        <h4 className="cart-item-name">{item.name}</h4>
        <p className="cart-item-price">${parseFloat(item.price).toFixed(2)}</p>
      </div>
      
      <div className="cart-item-quantity">
        <button
          className="qty-btn"
          onClick={() => onUpdateQuantity(item.id, item.quantity - 1)}
        >
          -
        </button>
        <span className="qty-value">{item.quantity}</span>
        <button
          className="qty-btn"
          onClick={() => onUpdateQuantity(item.id, item.quantity + 1)}
        >
          +
        </button>
      </div>
      
      <div className="cart-item-subtotal">
        ${(parseFloat(item.price) * item.quantity).toFixed(2)}
      </div>
      
      <button
        className="cart-item-remove"
        onClick={() => onRemove(item.id)}
        title="Remove item"
      >
        ✕
      </button>
    </div>
  );
};

export default CartItem;
