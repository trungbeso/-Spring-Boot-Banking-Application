# Trungbeso's Banking Application

This project is a backend RESTful API for a banking application. It provides various functionalities for user account management, transactions, and security using Spring Boot and JWT.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [License](#license)

## Features

- **User Account Management**: Create new user accounts, login, and retrieve account details.
- **Transactions**: Perform balance enquiries, credit, debit, and transfer money between accounts.
- **Email Notifications**: Send email alerts for account activities.
- **Security**: Secure endpoints using JWT authentication and Spring Security.

## Technologies Used

- **Spring Boot**: Framework for building the application.
- **Spring Security**: For securing the application.
- **JWT (JSON Web Token)**: For authentication and authorization.
- **Lombok**: To reduce boilerplate code.
- **Swagger**: For API documentation.
- **MySQL**: Database for storing user and transaction data.
- **Maven**: For project management and build.

## Setup

1. **Clone the repository**:

   ```sh
   git clone https://github.com/trungbeso/Banking-Application.git
   cd Banking-Application
   ```

2. **Configure the database**:
   Update the [application.properties](http://_vscodecontentref_/1) file in [resources](http://_vscodecontentref_/2) with your MySQL database details.

3. **Load environment variables**:
   Create a [local.env](http://_vscodecontentref_/3) file with the necessary environment variables (e.g., `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `SECRET_KEY`).

4. **Run the application**:
   ```sh
   ./mvnw spring-boot:run
   ```

## API Endpoints

### User Account Management

- **Login**: `[POST /api/v1/users/login]`
- **Create Account**: `[POST /api/v1/users]`
- **Balance Enquiry**: `[GET /api/v1/users/balanceEnquiry]`
- **Name Enquiry**: `[GET /api/v1/users/nameEnquiry]`
- **Credit Account**: `[POST /api/v1/users/credit]`
- **Debit Account**: `[POST /api/v1/users/debit]`
- **Transfer**: `[POST /api/v1/users/transfer]`

### Transactions

- **Save Transaction**: Automatically handled during credit, debit, and transfer operations.

## Security

The application uses JWT for authentication and authorization. The following classes handle security:

- [JwtAuthenticationFilter](http://_vscodecontentref_/9): Filters incoming requests and validates JWT tokens.
- [JwtTokenProvider](http://_vscodecontentref_/10): Generates and validates JWT tokens.
- [SecurityConfiguration](http://_vscodecontentref_/11): Configures security settings for the application.
- [CustomUserDetailsService](http://_vscodecontentref_/12): Loads user details from the database.

## License

This project is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
