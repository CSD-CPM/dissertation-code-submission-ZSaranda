# GOSTI! - Online Food Ordering System

## Overview

GOSTI! is an online food ordering system developed as part of the MSc Web and Mobile Development dissertation project.

The system enables customers to browse restaurant menus, add items to a cart, place food orders, and track their order history through a mobile application. Restaurant administrators can manage incoming orders and update order statuses through a separate Android application.

The project was developed using Android (Kotlin), Spring Boot, PostgreSQL, Docker, and RESTful APIs.

---

## Project Information

**Project Title:** GOSTI! – Online Food Ordering System

**Student:** Saranda Zekolli

**Degree:** MSc Web and Mobile Development

**Institution:** CITY College, University of York Europe Campus



---

## Technologies Used

### Frontend (Android Applications)

- Kotlin
- Android Studio
- XML Layouts
- RecyclerView
- Retrofit
- Coil Image Library
- ConstraintLayout
- CardView

### Backend

- Spring Boot
- Spring Data JPA
- Hibernate
- RESTful APIs
- Jakarta Validation

### Database

- PostgreSQL
- Docker
- Docker Compose

---

## System Architecture

The system follows a client-server architecture.

```
Customer Android App
         |
         | REST API
         v
   Spring Boot Backend
         |
         v
 PostgreSQL Database
      (Docker)
         ^
         |
         | REST API
         |
Restaurant Android App
```

---

## Main Features

### Customer Application

- Browse restaurant menu
- View food details
- Add items to cart
- Remove items from cart
- Update item quantities
- Place food orders
- View recent orders
- View order status

### Restaurant Application

- View incoming orders
- Accept orders
- Update order status
- View order details
- Manage menu items

### Backend System

- Menu management
- Order processing
- Cart management
- Data validation
- REST API communication
- Database integration

---

## Repository Structure

```
.
├── customer-app/
├── restaurant-app/
├── backend/
├── database/
├── docker-compose.yml
└── README.md
```


## Installation and Setup

### 1. Clone Repository

```bash
git clone https://github.com/CSD-CPM/dissertation-code-submission-ZSaranda.git
cd dissertation-code-submission-ZSaranda
```

### 2. Start PostgreSQL Database

The PostgreSQL database is deployed using Docker.

```bash
docker compose up -d
```

or

```bash
docker-compose up -d
```

Verify that the container is running:

```bash
docker ps
```

Connect to PostgreSQL:

```bash
docker exec -it gosti-postgres psql -U gosti_user -d gosti_db
```
---


### 3. Run Backend Server

Navigate to the backend project:

```bash
cd gosti-backend
```

Run Spring Boot:

```bash
./gradlew bootRun
```


The backend server will start on:

```text
http://localhost:8080
```

---

### 4. Run Android Applications

Open the Android projects in Android Studio.

For both customer and restaurant applications:

1. Open Android Studio
2. Select the project folder
3. Sync Gradle dependencies
4. Connect an Android device or start an emulator
5. Run the application

---



## Academic Use

This repository was developed and submitted as part of an MSc dissertation project.

The project is intended for academic assessment and demonstration purposes.

---
