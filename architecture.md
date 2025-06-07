## Architecture & Functionalities Diagram

Below is a high-level diagram illustrating the main components and flow of the Live Chat application, highlighting its
hexagonal structure, REST and WebSocket interfaces, and event-driven notification system.

```mermaid
flowchart TD
    REST_API["REST API (Spring Controllers)"]
    WS_API["WebSocket API (STOMP Endpoints)"]
    USECASE["Use Cases (Chat, Message, Contact)"]
    DOMAIN["Domain Models (User, Chat, Message, Contact)"]
    EVENTP["NotificationEventPublisher"]
    PORTS["Ports (Repository, Notification)"]
    DB["Persistence Adapter (Spring Data JPA, PostgreSQL)"]
    NOTIF["Notification Adapter (WebSocket/STOMP)"]
    EVLIST["NotificationEventListener"]
    WSNOTIF["WebSocket Notification (STOMP Broker)"]

    REST_API -- "Request/Response" --> USECASE
    WS_API -- "Subscribe/Send" --> USECASE
    USECASE -- "Domain Logic" --> DOMAIN
    USECASE -- "Calls" --> PORTS
    PORTS -- "Implements" --> DB
    PORTS -- "Implements" --> NOTIF
    USECASE -- "Publishes NotificationEvent" --> EVENTP
    EVENTP -- "Spring Event" --> EVLIST
    EVLIST -- "Send Notification" --> WSNOTIF
    NOTIF -- "Pushes to" --> WSNOTIF
```

**Legend:**

- **REST API**: Handles HTTP requests (user, chat, contact management)
- **WebSocket API**: Handles real-time message delivery via STOMP
- **Use Cases**: Application business logic (chat, message, contact operations)
- **Domain Models**: Core business entities
- **Ports**: Interfaces for persistence and notification
- **Adapters**: Implementations of ports (e.g., database, WebSocket/STOMP)
- **Notification System**: Event-driven notification processing and delivery

This architecture ensures clear separation of concerns, real-time communication, and a flexible, testable codebase.