## 🏨 Hotel Booking & Management Backend System

A scalable and secure backend solution for hotel booking and management, built using **Spring Boot**, integrated with **Stripe** for payments, and containerized with **Docker** for easy deployment. The application is hosted on **Render** and supports robust admin and user functionalities.

🔗 **Live API:** [https://hotelbooking-service.onrender.com/api/v1/](https://hotelbooking-service.onrender.com/api/v1/)  
📦 **Docker Image:** [`soumyajit2005/hotelbooking-service:v0.0.1`](https://hub.docker.com/r/soumyajit2005/hotelbooking-service)

---

<p align="center">
  <img src="https://img.shields.io/badge/Fueled%20by-Java-007396?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/Tested%20in-Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white" />
  <img src="https://img.shields.io/badge/Crafted%20with-Googling%20Skills-orange?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Tested%20by-Hope%20and%20Luck-yellowgreen?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Code%20Status-Cautiously%20Optimistic-lightgrey?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Breaks%20on-Mondays%20Only-red?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Backend%20Fueled%20by-Keyboard%20Smashing-informational?style=for-the-badge" />
</p>



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


![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-4169E1?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-635BFF?style=for-the-badge&logo=stripe&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)

---

## 📁 Project Structure

```bash
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
```


---

## 🐳 Docker & 🔧 Deployment Instructions
pull the public image from Docker Hub:
```bash
docker pull soumyajit2005/hotelbooking-service:v0.0.1
```
```bash
docker run -p 8000:8000 soumyajit2005/hotelbooking-service:v0.0.1
```

🌐 Deployment
This backend is hosted live on Render using Docker.

📍 API Base URL:
```bash
https://hotelbooking-service.onrender.com/api/v1/
```

🐦 Try Endpoints in Postman:
🔗 with https://hotelbooking-service.onrender.com/api/v1/
> 🔗 View the full API reference in [Postman Collection](https://www.postman.com/newsly-0222/workspace/hotel-booking-backend/collection/39002667-020dd3ae-aa23-4a38-93f2-aba4eebb3e93?action=share&source=copy-link&creator=39002667)

---

## ⚙️ Setup & Run Locally

### 📦 Clone the Repository

```bash
git clone https://github.com/leo-soumyajit/HotelBooking-Backend.git
cd HotelBooking-Backend
```
🛠 Configure Database Connection
Edit the application.properties file:
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/<your_db_name>
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

▶ Run the Application
```bash
./mvnw spring-boot:run
```
