# Offline Payment — Android + Spring Boot MVP

Educational simulation for creating a payment intent offline, storing it on Android, and syncing it later.

> No UPI/bank integration or real-money transfer. Backend acceptance is not settlement.

## Components
- android/: Android Java offline queue and manual HTTP sync.
- backend/: Spring Boot 3.4 / Java 17 REST API, H2 persistence, validation, expiry, idempotent relay.
- protocol/: shared JSON schema.
- docs/: architecture and security limitations.

## Backend
Requires JDK 17+ and Maven:

```bash
cd backend
mvn spring-boot:run
```
API runs at http://localhost:8080. H2 console: /h2-console.

## Android
Open android/ in Android Studio, sync Gradle, and run on an emulator. Default API URL is http://10.0.2.2:8080/api/v1/payments. For a physical phone, use your computer's LAN IP. Create an intent while offline or with backend stopped, then tap Sync queue when reachable.

## API
- POST /api/v1/payments — create pending intent.
- POST /api/v1/payments/relay — accept relayed intent.
- GET /api/v1/payments/{id} — read status.

## Scope
This is an MVP for offline storage + online sync, **not yet a Bluetooth implementation**. BLE GATT and a separate relay app are not included. See docs/ARCHITECTURE.md. Do not use real payment credentials or personal data.
