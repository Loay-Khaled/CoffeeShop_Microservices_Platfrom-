import React from 'react';
import { useCart } from '../context/CartContext';
import '../styles/ProductCard.css';

const ProductCard = ({ product }) => {
  const { addToCart } = useCart();

  const handleAddToCart = () => {
    addToCart(product);
  };

  return (
    <div className="product-card">
      <div className="product-image">
        <span className="coffee-icon">☕</span>
      </div>
      <div className="product-info">
        <h3 className="product-name">{product.name}</h3>
        <p className="product-price">${parseFloat(product.price).toFixed(2)}</p>
        <p className="product-stock">
          {product.stock > 0 ? (
            <span className="in-stock">In Stock ({product.stock})</span>
          ) : (
            <span className="out-of-stock">Out of Stock</span>
          )}
        </p>
        <button
          className="btn btn-primary add-to-cart"
          onClick={handleAddToCart}
          disabled={product.stock <= 0}
        >
          Add to Cart
        </button>
      </div>
    </div>
  );
};

export default ProductCard;
