# Dream Shops

Dream Shops is a full-featured e-commerce backend built with Spring Boot, JPA, and MySQL. It supports user authentication, product management, cart and order processing, and image handling.

## Features

- User registration, authentication (JWT), and role-based authorization
- Product CRUD with category and image management
- Shopping cart with add, update, remove, and clear operations
- Order placement and order history
- RESTful API design with clear endpoints
- Exception handling and meaningful API responses
- Secure endpoints for admin and user roles

## Tech Stack

- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- MySQL
- Lombok
- ModelMapper

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- MySQL

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/dream-shops.git
   cd dream-shops
   ```

2. **Configure the database:**

   Update `src/main/resources/application.properties` with your MySQL credentials:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/dream_shops_db?createDatabaseIfNotExist=true
   spring.datasource.username=YOUR_DB_USER
   spring.datasource.password=YOUR_DB_PASSWORD
   ```

3. **Build and run the application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **API will be available at:**  
   `http://localhost:8080/api/v1/`

### Default Users

On first run, the app creates default users:
- Admins: `admin1@email.com` / `123456`
- Users: `user1@email.com` / `123456`
- All users have roles assigned.

## API Endpoints

### Authentication

- `POST /api/v1/auth/login` — Login and receive JWT

### Users

- `POST /api/v1/users/add` — Register new user
- `GET /api/v1/users/{userId}/user` — Get user by ID

### Products

- `GET /api/v1/products/all` — List all products
- `POST /api/v1/products/add` — Add product (admin only)
- `PUT /api/v1/products/product/{productId}/update` — Update product (admin only)
- `DELETE /api/v1/products/product/{productId}/delete` — Delete product (admin only)

### Categories

- `GET /api/v1/categories/all` — List all categories
- `POST /api/v1/categories/add` — Add category

### Cart

- `GET /api/v1/carts/{cartId}/my-cart` — Get cart by ID
- `DELETE /api/v1/carts/{cartId}/clear` — Clear cart

### Cart Items

- `POST /api/v1/cartItems/item/add` — Add item to cart
- `DELETE /api/v1/cartItems/cart/{cartId}/item/{itemId}/remove` — Remove item from cart
- `PUT /api/v1/cartItems/cart/{cartId}/item/{itemId}/update` — Update item quantity

### Orders

- `POST /api/v1/orders/order?userId={userId}` — Place order
- `GET /api/v1/orders/user/{userId}/order` — Get user orders

### Images

- `POST /api/v1/images/upload` — Upload product images
- `GET /api/v1/images/image/download/{imageId}` — Download image

## Security

- JWT-based authentication
- Role-based access for admin/user endpoints
- Secure password storage (BCrypt)

<img width="604" height="803" alt="image" src="https://github.com/user-attachments/assets/be980985-250f-4eb9-9f88-1bd80a251c26" />
