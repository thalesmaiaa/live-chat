![Java](https://img.shields.io/badge/21-red?style=plastic&logoColor=red&label=java&link=https%3A%2F%2Fwww.oracle.com%2Fjava%2Ftechnologies%2Fjavase%2Fjdk21-archive-downloads.html)
![Spring Boot](https://img.shields.io/badge/3.4.x-blue?style=plastic&logoColor=red&label=Spring%20Boot&link=https%3A%2F%2Fspring.io%2Fprojects%2Fspring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-yellow?style=plastic&link=https%3A%2F%2Fwww.postgresql.org%2F)

# Live Chat

A modern, real-time chat backend application designed to support private and group messaging, contact management, and
instant notifications. Built with Spring Boot and Java 21, the project follows a clean, hexagonal architecture (Ports
and Adapters).

## Features

- **Real-Time Messaging**: Supports sending and receiving messages in both private (one-on-one) and group chats.
- **WebSocket Support**: Uses WebSockets for real-time message delivery and interaction between clients and the server.
- **STOMP Broker Integration**: Utilizes STOMP as a message broker over WebSockets to efficiently route messages between
  chat participants.
- **Contact Management**: Users can send, receive, and manage contact requests, and view their contact list.
- **Event-Driven Notifications**: Utilizes Spring’s event system to send notifications for new messages, unread
  messages, and contact requests.
- **User Management**: Register, authenticate, and manage user accounts.
- **Secure API**: OAuth2 JWT-based authentication and stateless session management.

## Hexagonal Architecture (Ports & Adapters)

This project is structured following the **hexagonal (clean) architecture** principles:

- **Core Domain**: Contains business logic, use cases, and domain models, isolated from infrastructure concerns.
- **Ports (Interfaces)**: Define the operations required by the core, such as repositories and notification services.
- **Adapters**: Implement the ports for technologies like databases, REST APIs, WebSockets, security, and event
  publishing.
- **Benefits**: This separation ensures high testability, flexibility to change frameworks or infrastructure, and easy
  maintenance.

<img src="./architecture.png">

## Technologies

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security (OAuth2, JWT)
- PostgreSQL
- WebSockets (with STOMP message broker)
- Event-driven architecture (Spring Events)
- RESTful API

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL database
- (Optional) Docker for containerized deployment

### Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/thalesmaiaa/live-chat.git
   cd live-chat
   ```

2. **Configure the application.yml file:**

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/livechatdb
       username:
       password:
   ```

3. **Generate your private and public keys inside src/main/resources**
   ```
    openssl genrsa > app.key
    openssl rsa -in app.key -pubout -out app.pub
   ```

4. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

### [API Endpoints](./API.md)

### Messaging

- Messages are sent as part of chat creation or via chat endpoints; notifications are automatically generated and
  delivered via the event system.
- Real-time message exchange is handled via WebSockets with STOMP as the message broker.

### WebSockets & Real-Time Messaging

- The application exposes WebSocket endpoints for clients to subscribe and send messages in real time.
- **STOMP Broker**: Acts as the message routing layer, managing subscriptions and message delivery.
- Clients connect to WebSocket endpoints and subscribe to topics or queues for chat updates, ensuring instant message
  delivery and efficient communication.

### Event-Driven Notifications

- Uses Spring's `ApplicationEventPublisher` to emit notification events.
- `NotificationEventListener` handles delivery and processing, decoupling notification logic from core business flows.
- Notifications can be pushed to users via WebSockets, push notification services, or other mechanisms.

## Project Structure

- `adapter/in/` - REST controllers (API endpoints)
- `adapter/out/` - Persistence adapters (repositories, external services)
- `core/domain/` - Core business entities and value objects
- `core/usecase/` - Application business logic (use cases)
- `core/events/` - Event classes and listeners
- `config/` - Security and application configuration

## Simple Web Interface

You can use the [`@thalesmaiaa/live-chat-webapp`](https://github.com/thalesmaiaa/live-chat-webapp) project as a simple web interface to interact with the live-chat backend and its features. This provides a ready-to-use user interface for messaging, contact management, and other chat functionalities.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your proposed changes.

**Author:** [@thalesmaiaa](https://github.com/thalesmaiaa)
