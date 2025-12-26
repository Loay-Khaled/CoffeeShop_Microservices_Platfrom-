import React, { useState, useEffect, useCallback } from 'react';
import { catalogApi } from '../services/api';
import ProductCard from '../components/ProductCard';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/CatalogPage.css';

const CatalogPage = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  const fetchProducts = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await catalogApi.getProducts();
      setProducts(response.data);
    } catch (err) {
      console.error('Error fetching products:', err);
      setError('Failed to load products. Please try again later.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  const filteredProducts = products.filter((product) =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <LoadingSpinner message="Loading menu..." />;
  }

  return (
    <div className="catalog-page">
      <div className="container">
        <div className="catalog-header">
          <h1>Our Menu</h1>
          <p>Discover our selection of premium coffees and beverages</p>
        </div>

        <div className="catalog-controls">
          <div className="search-box">
            <input
              type="text"
              placeholder="Search products..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>
        </div>

        {error && (
          <div className="error-message">
            <p>{error}</p>
            <button onClick={fetchProducts} className="btn btn-primary">
              Retry
            </button>
          </div>
        )}

        {!error && filteredProducts.length === 0 && (
          <div className="empty-state">
            <span className="empty-icon">☕</span>
            <h3>No products found</h3>
            <p>
              {searchTerm
                ? 'Try adjusting your search terms'
                : 'Check back later for new items!'}
            </p>
          </div>
        )}

        <div className="products-grid">
          {filteredProducts.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default CatalogPage;
