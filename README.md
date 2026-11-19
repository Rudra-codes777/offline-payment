# Offline Payment

An educational prototype exploring how a payment request could be transferred between Android devices without an internet connection, then securely relayed to a backend when connectivity becomes available.

> **Important:** This is a learning project. It does not connect to UPI, banks, payment networks, or move real money.

## Concept

1. A sender creates a payment intent on an Android device while offline.
2. A nearby relay device receives the intent over Bluetooth Low Energy (BLE).
3. The relay forwards it to the backend when it has internet access.
4. The backend validates the request, applies replay and duplicate protections, and records a simulated transaction.
5. A receipt/confirmation is returned through the relay to the sender.

## Planned modules

- `android-sender/` — Android app for creating and tracking offline payment intents.
- `android-relay/` — Android app/service that transfers intents over BLE and syncs them online.
- `backend/` — Spring Boot API for validation, idempotency, simulated ledger entries, and receipts.
- `protocol/` — Message formats and protocol documentation.
- `docs/` — Architecture, threat model, and implementation notes.

## Security goals

- Authenticated and integrity-protected messages.
- Encryption for sensitive message contents.
- Unique transaction identifiers and backend idempotency.
- Expiry and replay protection.
- Explicit handling of pending, accepted, rejected, and confirmed states.

Cryptographic choices and key management will be documented and implemented using established platform/library primitives rather than custom cryptography.

## Intended stack

- Android (Java)
- Bluetooth Low Energy (BLE)
- Spring Boot / Java
- PostgreSQL
- Standard cryptographic libraries and Android Keystore where appropriate

## Development status

Early planning stage. The architecture, protocol, and individual modules will be implemented and tested incrementally.

## Disclaimer

This project is a simulated, educational prototype. Offline transfer of a message is not the same as guaranteed settlement. Do not use it for real payments or store real payment credentials.
