# Stage2025 Frontend

A modern Angular 17+ frontend application for the Stage2025 e-commerce platform.

## Features

- **Modern Angular 17+** with standalone components
- **Angular Material** for UI components
- **NgRx** for state management
- **JWT Authentication** with OTP verification
- **Responsive Design** for all devices
- **TypeScript Strict Mode** for better code quality
- **Lazy Loading** for optimal performance
- **PWA Ready** (can be extended)

## Architecture

\`\`\`
src/
├── app/
│   ├── core/                 # Core functionality (singleton services, guards, interceptors)
│   │   ├── guards/           # Route guards
│   │   ├── interceptors/     # HTTP interceptors
│   │   ├── models/           # TypeScript interfaces/models
│   │   ├── services/         # Core services
│   │   └── store/            # NgRx store (auth)
│   ├── shared/               # Shared components, pipes, directives
│   ├── features/             # Feature modules
│   │   ├── auth/             # Authentication feature
│   │   ├── products/         # Products feature
│   │   ├── cart/             # Shopping cart feature
│   │   ├── orders/           # Orders feature
│   │   ├── profile/          # User profile feature
│   │   └── admin/            # Admin panel feature
│   ├── app.component.ts      # Root component
│   ├── app.routes.ts         # Application routes
│   └── main.ts               # Application bootstrap
├── environments/             # Environment configurations
└── assets/                   # Static assets
\`\`\`

## Getting Started

### Prerequisites

- Node.js 18+ and npm
- Angular CLI 17+

### Installation

1. Clone the repository
\`\`\`bash
git clone <repository-url>
cd stage2025-frontend
\`\`\`

2. Install dependencies
\`\`\`bash
npm install
\`\`\`

3. Configure environment
\`\`\`bash
# Update src/environments/environment.ts with your backend API URL
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080'  // Your Spring Boot backend URL
};
\`\`\`

4. Start the development server
\`\`\`bash
ng serve
\`\`\`

The application will be available at `http://localhost:4200`

### Build for Production

\`\`\`bash
ng build --configuration production
\`\`\`

## Features Overview

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
- Supplier management

## API Integration

The frontend integrates with the Spring Boot backend through the following services:

- **AuthService**: Authentication and user management
- **ProductService**: Product and category operations
- **CartService**: Shopping cart operations
- **OrderService**: Order management
- **UserService**: User profile operations

All API calls are handled through Angular's HttpClient with proper error handling and loading states.

## State Management

The application uses NgRx for state management, currently implemented for:

- **Auth State**: User authentication, login status, user information
- Easily extensible for other features (cart, products, etc.)

## Styling

- **Angular Material** for consistent UI components
- **Custom SCSS** for additional styling
- **Responsive design** with mobile-first approach
- **Material Design** principles

## Security

- JWT token storage and management
- HTTP interceptors for authentication
- Route guards for protected routes
- Role-based access control
- XSS protection through Angular's built-in sanitization

## Performance

- **Lazy loading** for feature modules
- **OnPush change detection** where applicable
- **TrackBy functions** for efficient list rendering
- **Optimized bundle size** through tree shaking

## Testing

\`\`\`bash
# Run unit tests
ng test

# Run e2e tests
ng e2e

# Run linting
ng lint
\`\`\`

## Deployment

The application can be deployed to any static hosting service:

1. Build for production: `ng build --configuration production`
2. Deploy the `dist/` folder to your hosting service
3. Configure your web server to serve `index.html` for all routes (SPA routing)

## Contributing

1. Follow Angular style guide
2. Use TypeScript strict mode
3. Write unit tests for new features
4. Follow the established folder structure
5. Use conventional commits

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

This project is licensed under the MIT License.
