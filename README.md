 🏨 Hotel Booking & Management Backend System

A scalable and secure backend solution for hotel booking and management, built using **Spring Boot**, integrated with **Stripe** for payments, and containerized with **Docker** for easy deployment. The application is hosted on **Render** and supports robust admin and user functionalities.

🔗 **Live API:** [https://hotelbooking-service.onrender.com/api/v1/](https://hotelbooking-service.onrender.com/api/v1/)  
📦 **Docker Image:** [`soumyajit2005/hotelbooking-service:v0.0.1`](https://hub.docker.com/r/soumyajit2005/hotelbooking-service)

---

## 🚀 Features

- 🔐 **User Authentication & OTP Email Verification**
- 🛏️ **Hotel Room & Inventory Management**
- 📅 **Booking System**: Create, update, cancel bookings
- 📈 **Dynamic Pricing Strategy**
- 💰 **Minimum Price Display Per Hotel**
- 👥 **Guest Management** for group travel
- 💳 **Stripe Payment Integration**
- 👤 **Role-Based Access Control (RBAC)**
- 🖥️ **Admin Panel Functions**
- 📊 **Report Generation**

---

## 🛠️ Tech Stack

| Layer              | Technology                               |
|-------------------|-------------------------------------------|
| **Backend**        | Spring Boot, Spring MVC, Spring Security |
| **Database**       | PostgreSQL                               |
| **ORM**            | Hibernate (JPA)                          |
| **Authentication** | JWT, Email OTP                           |
| **Payment**        | Stripe API                               |
| **Deployment**     | Docker, Render                           |
| **API Docs**       | Postman / Swagger                        |

---

## 📁 Project Structure

src/
├── main/
│ ├── java/com/soumyajit/HotelBooking/
│ │ ├── Advices/
│ │ ├── config/
│ │ ├── controller/
│ │ ├── dtos/
│ │ ├── EmailService/
│ │ ├── entities/
│ │ ├── Exception/
│ │ ├── repository/
│ │ ├── Security/
│ │ ├── service/
│ │ ├── Strategy/
│ │ └── util/
│ │ └── HotelBookingApplication.java
│ └── resources/
├── test/
├── Dockerfile
└── target/




## 🐳 Docker
### 🔧 Build the Docker Image
```bash
pull the public image from Docker Hub:
docker pull soumyajit2005/hotelbooking-service:v0.0.1

docker run -p 8000:8000 soumyajit2005/hotelbooking-service:v0.0.1
🌐 Deployment
This backend is hosted live on Render using Docker.

📍 API Base URL:
https://hotelbooking-service.onrender.com/api/v1/

Try Endpoints in Postman:
with https://hotelbooking-service.onrender.com/api/v1/
https://newsly-0222.postman.co/workspace/My-Workspace~8a29e63c-8e4a-451c-88bf-3d8a9eee5e76/collection/39002667-020dd3ae-aa23-4a38-93f2-aba4eebb3e93?action=share&source=copy-link&creator=39002667

🧪 Run Locally
Clone the repository:

git clone https://github.com/yourusername/hotel-booking-backend.git
cd hotel-booking-backend
