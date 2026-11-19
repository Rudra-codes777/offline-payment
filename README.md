# Offline Payment — Android BLE relay + Spring Boot simulation

Learning prototype: an offline-created payment intent is signed on the sender phone, transferred over BLE to a nearby relay phone, then forwarded to a backend when the relay has internet.

> **Not real UPI. No bank integration, settlement, or real money movement. Do not enter payment credentials or sensitive personal data.**

## Apps
- `android/`: sender app; creates and queues signed intents, scans for relay, sends over BLE.
- `android-relay/`: relay app; advertises BLE GATT service and forwards received intent to backend.
- `backend/`: Spring Boot API; validates, verifies ECDSA signature, persists, expires and deduplicates intents.
- `protocol/`: JSON schema; `docs/`: architecture, security and test notes.

## Backend
Requires JDK 17+ and Maven:
```bash
cd backend
mvn spring-boot:run
```
API at `http://localhost:8080` (H2 in-memory default).

## Two-phone demo
1. Open `android/` in Android Studio and install on sender phone A.
2. Open `android-relay/` and install on relay phone B.
3. Configure relay URL as `http://YOUR_COMPUTER_LAN_IP:8080/api/v1/payments/relay`.
4. Start relay advertising on B.
5. Create signed intent on A, choose send over Bluetooth, and connect to relay.
6. Relay forwards intent to backend. The backend verifies sender signature before acceptance.

## Signature format
Sender signs UTF-8 fields joined by newline: `id, senderId, recipientId, amount, createdAt, expiresAt`. ECDSA P-256 / SHA-256. Private key is stored in Android Keystore. Backend verifies the signature against the public key carried in the request.

**This signature verifies payload integrity, not sender identity**, because this demo has no trusted key enrollment. See `docs/SECURITY.md`.

## Important
BLE currently uses a single GATT write with a 480-byte cap; negotiated MTU and chunking are not implemented. The BLE path is not encrypted at the application layer, backend receipt is not returned to sender, and local backend HTTP is cleartext for development. This is not a production payment product.
