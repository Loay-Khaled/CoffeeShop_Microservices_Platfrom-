import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import keycloak from './keycloak';
import App from './App';
import './styles/index.css';

const root = ReactDOM.createRoot(document.getElementById('root'));

const eventLogger = (event, error) => {
  console.log('Keycloak event:', event, error);
};

const tokenLogger = (tokens) => {
  if (tokens?.token) {
    localStorage.setItem('token', tokens.token);
  }
};

root.render(
  <ReactKeycloakProvider
    authClient={keycloak}
    onEvent={eventLogger}
    onTokens={tokenLogger}
    initOptions={{
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      pkceMethod: 'S256',
    }}
  >
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </ReactKeycloakProvider>
);
