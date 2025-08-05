# Stage2025 Angular Frontend

A modern Angular 17+ frontend application for the Stage2025 e-commerce platform.

## 🚀 Features

- **Pure Angular 17+** with standalone components
- **Angular Material** for UI components
- **NgRx** for state management
- **JWT Authentication** with OTP verification
- **Responsive Design** for all devices
- **TypeScript Strict Mode** for better code quality
- **Lazy Loading** for optimal performance

## 📁 Project Structure

\`\`\`
frontend/
├── src/
│   ├── app/
│   │   ├── core/                 # Core functionality
│   │   │   ├── guards/           # Route guards
│   │   │   ├── interceptors/     # HTTP interceptors
│   │   │   ├── models/           # TypeScript interfaces
│   │   │   ├── services/         # Core services
│   │   │   └── store/            # NgRx store
│   │   ├── features/             # Feature modules
│   │   │   ├── auth/             # Authentication
│   │   │   ├── products/         # Products
│   │   │   ├── cart/             # Shopping cart
│   │   │   ├── orders/           # Orders
│   │   │   ├── profile/          # User profile
│   │   │   └── admin/            # Admin panel
│   │   ├── app.component.ts      # Root component
│   │   └── app.routes.ts         # Application routes
│   ├── environments/             # Environment configs
│   └── assets/                   # Static assets
├── angular.json                  # Angular CLI config
├── package.json                  # Dependencies
└── tsconfig.json                 # TypeScript config
\`\`\`

## 🛠️ Getting Started

### Prerequisites

- Node.js 18+ and npm
- Angular CLI 17+

### Installation

1. Navigate to the frontend directory:
\`\`\`bash
cd frontend
\`\`\`

2. Install dependencies:
\`\`\`bash
npm install
\`\`\`

3. Configure environment:
\`\`\`bash
# Update src/environments/environment.ts with your backend API URL
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'  // Your Spring Boot backend URL
};
\`\`\`

4. Start the development server:
\`\`\`bash
ng serve
\`\`\`

The application will be available at `http://localhost:4200`

### Build for Production

\`\`\`bash
ng build --configuration production
\`\`\`

## 🎯 Features Overview

### Authentication
- User registration with email verification
- Login with OTP verification
- Password reset functionality
- JWT token management
- Role-based access control (Admin/Client)

### Products
- Product catalog with search and filtering
- Category-based browsing
- Product details with image gallery
- Real-time stock and pricing information
- Add to cart functionality

### Shopping Cart
- Add/remove items
- Update quantities
- Cart persistence
- Checkout process

### Orders
- Order history
- Order tracking
- Order details view
- Admin order management

### User Profile
- Profile management
- Address management
- Order history

### Admin Panel
- Dashboard with statistics
- Product management
- Order management
- User management

## 🔧 API Integration

The frontend integrates with your Spring Boot backend through:

- **AuthService**: Authentication and user management
- **ProductService**: Product and category operations
- **CartService**: Shopping cart operations
- **OrderService**: Order management

All API calls use Angular's HttpClient with proper error handling.

## 🛡️ Security

- JWT token storage and management
- HTTP interceptors for authentication
- Route guards for protected routes
- Role-based access control
- XSS protection through Angular's built-in sanitization

## 📱 Responsive Design

Works perfectly on:
- Desktop computers
- Tablets
- Mobile phones
- All modern browsers

## 🧪 Testing

\`\`\`bash
# Run unit tests
ng test

# Run e2e tests
ng e2e

# Run linting
ng lint
\`\`\`

## 🚀 Deployment

1. Build for production: `ng build --configuration production`
2. Deploy the `dist/` folder to your hosting service
3. Configure your web server to serve `index.html` for all routes (SPA routing)

## 📄 License

This project is licensed under the MIT License.
