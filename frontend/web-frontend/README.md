# Coffee Shop Frontend

React-based frontend application for the Coffee Shop Platform.

## Features

- **User Authentication** via Keycloak SSO
- **Product Catalog** - Browse and search coffee products
- **Shopping Cart** - Add/remove items with quantity management
- **Order Management** - Create and track orders
- **Payment Processing** - Secure payment integration
- **Responsive Design** - Works on desktop and mobile

## Prerequisites

- Node.js 18+
- npm or yarn
- Running backend services:
  - Keycloak (port 8081)
  - Catalog Service (port 8082)
  - Order Service (port 8084)
  - Payment Service (port 8085)

## Installation

```bash
cd frontend/web-frontend
npm install
```

## Configuration

The application connects to the following services:

- **Keycloak**: `http://localhost:8081` (realm: `coffeeshop`, client: `web-frontend`)
- **Catalog Service**: `http://localhost:8082/api`
- **Order Service**: `http://localhost:8084/api`
- **Payment Service**: `http://localhost:8085/api`

## Running the Application

```bash
npm start
```

The application will run on `http://localhost:3000`.

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── CartItem.js
│   ├── LoadingSpinner.js
│   ├── Navbar.js
│   ├── OrderCard.js
│   └── ProductCard.js
├── context/             # React context providers
│   └── CartContext.js
├── pages/               # Page components
│   ├── CartPage.js
│   ├── CatalogPage.js
│   ├── CheckoutPage.js
│   ├── HomePage.js
│   ├── OrderDetailsPage.js
│   └── OrdersPage.js
├── services/            # API service layer
│   └── api.js
├── styles/              # CSS styles
├── App.js               # Main application component
├── index.js             # Application entry point
└── keycloak.js          # Keycloak configuration
```

## Available Scripts

- `npm start` - Run in development mode
- `npm build` - Build for production
- `npm test` - Run tests

## Test Accounts

Default test accounts (from Keycloak realm):

- **Customer**: `customer1` / `123456`
- **Admin**: `admin1` / `123456`

## API Integration

### Catalog Service

- `GET /api/catalog/items` - List all products
- `GET /api/catalog/items/:id` - Get product by ID
- `POST /api/catalog/items` - Create new product

### Order Service

- `POST /api/orders?customer=:username` - Create new order
- `GET /api/orders/:id` - Get order by ID
- `GET /api/orders/me` - Get current user's orders
- `POST /api/orders/:id/items` - Add item to order

### Payment Service

- `POST /api/payments/process/:orderId` - Process payment
- `GET /api/payments/status/:orderId` - Get payment status

Open http://localhost:3000 in your browser
You should see the Coffee Shop homepage
Click "Login" to authenticate with Keycloak
Use credentials: customer1 / 123456 or admin1 / 123456
Note: The backend Spring Boot services (catalog, order, payment) still need to be started for the API calls to work. Would you like me to start them as well?
