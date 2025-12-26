import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://localhost:8081',
  realm: 'coffeeshop',
  clientId: 'web-frontend',
});

export default keycloak;
