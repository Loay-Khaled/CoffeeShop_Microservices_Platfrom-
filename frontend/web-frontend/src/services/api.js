import axios from 'axios';

// Base URLs for microservices
const CATALOG_SERVICE_URL = 'http://localhost:8082/api';
const ORDER_SERVICE_URL = 'http://localhost:8084/api';
const PAYMENT_SERVICE_URL = 'http://localhost:8085/api';

// Create axios instance with auth header
const createAuthenticatedClient = (baseURL) => {
  const client = axios.create({ baseURL });
  
  client.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });

  client.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        localStorage.removeItem('token');
        window.location.reload();
      }
      return Promise.reject(error);
    }
  );

  return client;
};

const catalogClient = createAuthenticatedClient(CATALOG_SERVICE_URL);
const orderClient = createAuthenticatedClient(ORDER_SERVICE_URL);
const paymentClient = createAuthenticatedClient(PAYMENT_SERVICE_URL);

// Catalog Service API
export const catalogApi = {
  getProducts: () => catalogClient.get('/catalog/items'),
  getProduct: (id) => catalogClient.get(`/catalog/items/${id}`),
  createProduct: (product) => catalogClient.post('/catalog/items', product),
};

// Order Service API
export const orderApi = {
  createOrder: (customer) => orderClient.post(`/orders?customer=${customer}`),
  getOrder: (id) => orderClient.get(`/orders/${id}`),
  getMyOrders: () => orderClient.get('/orders/me'),
  addItemToOrder: (orderId, productId, quantity) => 
    orderClient.post(`/orders/${orderId}/items`, { productId, quantity }),
};

// Payment Service API
export const paymentApi = {
  processPayment: (orderId) => paymentClient.post(`/payments/process/${orderId}`),
  getPaymentStatus: (orderId) => paymentClient.get(`/payments/status/${orderId}`),
};

export { catalogClient, orderClient, paymentClient };
