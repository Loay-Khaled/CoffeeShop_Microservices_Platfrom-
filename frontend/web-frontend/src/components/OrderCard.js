import React from "react";
import "../styles/OrderCard.css";

const OrderCard = ({ order, onViewDetails, onPayNow, onCancelOrder }) => {
  const getStatusClass = (status) => {
    switch (status?.toUpperCase()) {
      case "CREATED":
        return "status-created";
      case "PAID":
        return "status-paid";
      case "PROCESSING":
        return "status-processing";
      case "COMPLETED":
        return "status-completed";
      case "CANCELLED":
        return "status-cancelled";
      default:
        return "status-default";
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div className="order-card">
      <div className="order-header">
        <span className="order-id">Order #{order.id}</span>
        <span className={`order-status ${getStatusClass(order.status)}`}>
          {order.status}
        </span>
      </div>

      <div className="order-details">
        <p className="order-date">
          <strong>Date:</strong> {formatDate(order.createdAt)}
        </p>
        <p className="order-total">
          <strong>Total:</strong> ${parseFloat(order.total || 0).toFixed(2)}
        </p>
        <p className="order-items-count">
          <strong>Items:</strong> {order.items?.length || 0}
        </p>
      </div>

      {order.items && order.items.length > 0 && (
        <div className="order-items-preview">
          <p className="items-title">Items:</p>
          <ul className="items-list">
            {order.items.slice(0, 3).map((item, index) => (
              <li key={index} className="item-preview">
                {item.productName} x{item.quantity}
              </li>
            ))}
            {order.items.length > 3 && (
              <li className="item-more">+{order.items.length - 3} more</li>
            )}
          </ul>
        </div>
      )}

      <div className="order-actions">
        {onViewDetails && (
          <button
            className="btn btn-outline"
            onClick={() => onViewDetails(order.id)}
          >
            View Details
          </button>
        )}
        {order.status === "CREATED" && onPayNow && (
          <button
            className="btn btn-primary"
            onClick={() => onPayNow(order.id)}
          >
            Pay Now
          </button>
        )}
        {(order.status === "CREATED" ||
          order.status === "PENDING" ||
          order.status === "PAID") &&
          onCancelOrder && (
            <button
              className="btn btn-danger"
              onClick={() => onCancelOrder(order.id)}
            >
              Cancel Order
            </button>
          )}
      </div>
    </div>
  );
};

export default OrderCard;
