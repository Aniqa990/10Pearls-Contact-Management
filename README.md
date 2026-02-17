# Contact Management Application - Backend

## 📦 WHAT'S INCLUDED

✅ **User Authentication**
- User registration with email/password
- Secure login with JWT tokens
- Password change functionality
- Automatic token validation on requests

✅ **Contact Management**
- Create, read, update, delete (CRUD) contacts
- Each contact can have multiple emails and phones
- Search/filter contacts by name
- Paginated contact lists

✅ **Security Features**
- Password hashing with BCrypt (never stored in plain text)
- JWT authentication (tokens expire after 1 hour)
- Email duplicate prevention
- Global exception handling

✅ **Code Quality**
- Comprehensive unit tests (UserServiceImplTest, ContactServiceTest)
- Integration tests (AuthControllerTest, ContactControllerTest)
- Detailed logging with Slf4j
- Clean 3-layer architecture (Controller → Service → Repository)
- Professional error responses

---

## 🚀 QUICK START

### 1. Start the Backend
```bash
mvn spring-boot:run
```

Server starts on: `http://localhost:8080`



## 🏗️ PROJECT STRUCTURE

```
backend/
├── src/main/java/com/aniqa/contact_mgt/
│   ├── model/                   # Database entities
│   │   ├── User.java
│   │   ├── Contact.java
│   │   ├── ContactEmails.java
│   │   ├── ContactPhones.java
│   │   └── enums/
│   │       ├── EmailType.java
│   │       └── PhoneType.java
│   │
│   ├── dto/                     # Request/Response objects
│   │   ├── UserResponse.java
│   │   ├── ContactDTO.java
│   │   ├── EmailDTO.java
│   │   ├── PhoneDTO.java
│   │
│   ├── repository/              # Database access (Data Layer)
│   │   ├── UserRepository.java
│   │   ├── ContactRepository.java
│   │   ├── ContactEmailRepository.java
│   │   └── ContactPhoneRepository.java
│   │
│   ├── service/                 # Business logic (Service Layer)
│   │   ├── UserService.java
│   │   ├── ContactService.java
│   │   └── impl/
│   │       └── UserServiceImpl.java
│   │
│   ├── controller/              # HTTP endpoints (Controller Layer)
│   │   ├── AuthController.java
│   │   ├── ContactController.java
│   │   └── UserController.java
│   │
│   ├── security/                # JWT Authentication
│   │   ├── JwtTokenProvider.java
│   │   ├── JwtFilter.java
│   │   └── SecurityConfig.java
│   │
│   ├── exception/               # Error Handling
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ErrorResponse.java
│   │   └── Custom exceptions
│   
│
├── src/test/java/               # Unit Tests
│   ├── service/
│   │   ├── UserServiceImplTest.java
│   │   └── ContactServiceTest.java
│   └── controller/
│       ├── AuthControllerTest.java
│       └── ContactControllerTest.java
│
├── src/main/resources/
│   ├── application.properties    # Configuration
│
├── pom.xml                       # Dependencies

```

---

## 🔑 KEY ENDPOINTS


### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### User Profile
- `GET /api/users/profile` - Get user profile
- `POST /api/users/{userId}/change-password` - Change password
- `POST /api/users/logout` - Logout

### Contacts (CRUD)
- `POST /api/contacts` - Create contact
- `GET /api/contacts?page=0&size=10` - Get all contacts (paginated)
- `GET /api/contacts/{contactId}` - Get single contact
- `PUT /api/contacts/{contactId}` - Update contact
- `DELETE /api/contacts/{contactId}` - Delete contact

### Search & Photos
- `GET /api/contacts/search?keyword=John` - Search contacts
- `POST /api/contacts/{contactId}/photo` - Upload contact photo

---

## 🔐 SECURITY IMPLEMENTED

| Feature | How It Works |
|---------|-------------|
| **Password Hashing** | BCrypt with salt - passwords never stored in plain text |
| **JWT Tokens** | Issued on login, validated on every request |
| **Token Expiration** | Tokens expire after 1 hour (must login again) |
| **Permission Checks** | Users can only access their own contacts |
| **Email Validation** | Prevents duplicate registrations |
| **HTTPS Ready** | Can be deployed with SSL/TLS |

---

## 🧪 RUNNING TESTS

### Run All Tests
```bash
mvn test
```

### Run Specific Test
```bash
mvn test -Dtest=UserServiceImplTest
```

---

## 📊 DATABASE TABLES

The application creates 4 tables automatically:

```
┌──────────────────────┐
│      USER            │  ← Each user has a login account
│─────────────────────┤
│ id, email, password  │
│ first_name, last_name│
└──────────────────────┘
           │
           ├────────────────────┬──────────────────────┐
           │                    │                      │
        (1 user → many         (1 contact → many       (1 contact → many
         contacts)             emails)                 phones)
           │                    │                      │
           ↓                    ↓                      ↓
┌──────────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│    CONTACT           │  │ CONTACT_EMAILS   │  │ CONTACT_PHONES   │
│─────────────────────┤  │──────────────────┤  │──────────────────┤
│ id, user_id          │  │ id, contact_id   │  │ id, contact_id   │
│ first_name, last_name│  │ email, type      │  │ number, type     │
│ title, photoUrl      │  │ (WORK/PERSONAL)  │  │ (WORK/HOME/...)  │
└──────────────────────┘  └──────────────────┘  └──────────────────┘
```

---

## 💻 TECHNOLOGY STACK

| Layer | Technology |
|-------|-----------|
| **Backend** | Spring Boot 4.0 |
| **Language** | Java 21 |
| **Database** | SQL Server |
| **Authentication** | JWT (jjwt 0.12.3) |
| **ORM** | Hibernate / JPA |
| **Testing** | JUnit 5, Mockito |
| **Logging** | SLF4J / Logback |
| **Build Tool** | Maven |
| **Password Security** | BCrypt |


## ✅ FEATURES CHECKLIST

Backend Features Implemented:

**User Management**
- ✅ User registration (email + password)
- ✅ User login with JWT token
- ✅ Change password
- ✅ Get user profile
- ✅ User logout

**Contact Management**
- ✅ Create contacts
- ✅ Get all contacts (paginated)
- ✅ Get single contact
- ✅ Update contacts
- ✅ Delete contacts
- ✅ Search contacts by name

**Contact Details**
- ✅ Multiple email addresses per contact (labeled: WORK, PERSONAL, etc.)
- ✅ Multiple phone numbers per contact (labeled: WORK, HOME, PERSONAL, etc.)
- ✅ First name, last name, title fields

**Technical Requirements**
- ✅ Spring Boot framework
- ✅ Spring Data JPA + Hibernate
- ✅ SQL Server database
- ✅ JWT authentication
- ✅ JUnit & Mockito tests
- ✅ SLF4J logging with Logback
- ✅ Global exception handling
- ✅ Input validation
- ✅ Pagination support
- ✅ SonarQube ready (configuration files present)


### Environment Variables
```bash
export JWT_SECRET="your-256-bit-secret"
export JWT_EXPIRATION="3600000"
export CLOUDINARY_CLOUD_NAME="..."
export CLOUDINARY_API_KEY="..."
export CLOUDINARY_API_SECRET="..."
```



# Contact Management Application - Frontend

A modern React.js frontend application for managing contacts with user authentication, contact CRUD operations, and user profile management. I wasn't able to properly maintain the old commit history, so I created new commits.

## Features

- **Authentication**
  - User registration and login
  - Secure token-based authentication
  - Protected routes
  - Auto-logout on token expiration

- **Contact Management**
  - View all contacts with pagination
  - Search contacts by first name or last name
  - Create new contacts
  - Update existing contacts
  - Delete contacts with confirmation
  - Manage multiple emails and phone numbers per contact
  - Add notes to contacts

- **User Profile**
  - View user information
  - Change password securely
  - Logout functionality


## Tech Stack

- **React 19** - UI library
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Styling
- **React Router** - Client-side routing
- **Axios** - HTTP client
- **Lucide React** - Icon library

(I have used the shadcn library for components)

## Prerequisites

- Node.js 18+ and npm/yarn
- Backend API running on `http://localhost:8080`

## Installation

1. Install dependencies:
```bash
npm install
```

2. Configure environment variables:
```bash

# VITE_API_BASE_URL=http://localhost:8080/api
```

3. Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:5173`

## Development

### Running Development Server
```bash
npm run dev
```

### Building for Production
```bash
npm run build
```

### Preview Production Build
```bash
npm run preview
```

## Project Structure

```
src/
├── components/          # Reusable React components
│   ├── ui/             # Base UI components (Button, Input, etc.)
│   ├── DashboardLayout.tsx
│   ├── ContactFormModal.tsx
│   ├── DeleteConfirmationModal.tsx
├── pages/              # Page components
│   ├── LoginPage.tsx
│   ├── RegisterPage.tsx
│   ├── ContactManagementPage.tsx
│   ├── UserProfilePage.tsx
├── services/           # API services
│   ├── apiClient.ts
│   ├── authService.ts
│   ├── contactService.ts
├── context/            # React context for state management
│   └── AuthContext.tsx
├── types/              # TypeScript type definitions
├── lib/                # Utility functions
├── App.tsx             # Main app component with routing
├── main.tsx            # Application entry point
└── index.css           # Global styles
```

## API Endpoints

The application connects to the following backend endpoints:

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/users/logout` - Logout user

### User
- `POST /api/users/{userId}/change-password` - Change password

### Contacts
- `GET /api/contacts` - Get all contacts (paginated)
- `GET /api/contacts/search` - Search contacts
- `GET /api/contacts/{userId}/{contactId}` - Get single contact
- `POST /api/contacts` - Create contact
- `PUT /api/contacts/{contactId}` - Update contact
- `DELETE /api/contacts/{contactId}` - Delete contact
- `POST /api/contacts/{contactId}/photo` - Upload contact photo

## Authentication

The application uses JWT tokens for authentication:
- Tokens are stored in localStorage
- Tokens are automatically sent in request headers
- On 401 responses, tokens are cleared and user is redirected to login

## Styling

The application uses:
- **Tailwind CSS** for utility-first styling
- **Responsive design** with mobile-first approach
- **shadcn/ui** patterns for consistent component design

## State Management

Uses React Context API for:
- User authentication state
- User information

# Sonar Cloud

## Frontend: https://sonarcloud.io/project/overview?id=10Pearls-Contact-Management-Frontend

## Backend: https://sonarcloud.io/project/overview?id=10Pearls-Contact-Management-Backend

## License

This project is part of the Contact Management Application.

## Support

For issues or questions, please reach out to the development team.
