# Offline Payment — Android BLE relay + Spring Boot simulation

A learning prototype demonstrating an offline-created payment intent transferred over Bluetooth Low Energy (BLE) to a nearby relay phone, then forwarded to a backend when the relay has internet.

> **Not real UPI. No bank integration, settlement, or real money movement. Do not enter payment credentials or sensitive personal data.**

## Repository apps
- `android/`: sender Android app. Creates an intent, stores it locally, scans for a BLE relay, and writes the intent to the relay GATT characteristic.
- `android-relay/`: relay Android app. Advertises a BLE GATT service, receives a payment-intent payload, and forwards it to the backend over HTTP.
- `backend/`: Spring Boot REST API with validation, persistence, expiry, and duplicate-intent handling.
- `protocol/`: shared intent schema.
- `docs/`: protocol, test plan, security analysis.

## Run backend
Requires JDK 17+ and Maven:
```bash
cd backend
mvn spring-boot:run
```
Backend: `http://localhost:8080`. H2 in-memory database is the default.

## Run on two Android phones
1. Open `android/` in Android Studio and install it on phone A (sender).
2. Open `android-relay/` in a second Android Studio window and install it on phone B (relay).
3. Ensure the backend is reachable from phone B. In the relay app, set the API URL to `http://YOUR_COMPUTER_LAN_IP:8080/api/v1/payments/relay`.
4. Open the relay app on phone B and tap **Start relay**. Grant nearby-device Bluetooth permissions.
5. On phone A, create an intent and tap **Send first queued intent over Bluetooth**. Grant scan/connect permissions and tap **Find relay and transfer**.
6. Keep phone B connected to the internet. It forwards received intents to the backend.

For emulator-to-host networking, use `10.0.2.2`; for physical devices, use the computer's LAN IP and a shared network.

## BLE protocol
Both apps use the same service and characteristic UUIDs in `android/.../BleContract.java` and `android-relay/.../MainActivity.java`. One JSON intent is sent in a single GATT write, limited to 480 bytes in this prototype.

## API
- `POST /api/v1/payments` — create pending intent.
- `POST /api/v1/payments/relay` — accept a relayed intent.
- `GET /api/v1/payments/{id}` — retrieve status.

## Important limitations
This is a BLE transport demonstration, **not a secure payment system**. BLE pairing/bonding is not enforced; payload signatures and device enrollment are not implemented; relay-to-backend uses cleartext HTTP in the sample; there is no user authentication, authorization, key management, anti-fraud engine, or bank integration. A GATT write acknowledgement only means the BLE stack received the write; the sender app currently does not receive an authenticated backend receipt. These gaps must be fixed and tested before any sensitive or production use. See `docs/SECURITY.md` and `docs/TESTING.md`.
