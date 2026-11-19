# Architecture and limitations

This MVP implements an Android offline intent queue and direct HTTP synchronization to a Spring Boot API. The API validates intent fields and expiry, persists them, and accepts repeated identical deliveries idempotently. H2 is the default; PostgreSQL can be configured through DB_URL, DB_USER, DB_PASSWORD, and DB_DRIVER.

**BLE is not implemented in this version.** There is no GATT discovery, pairing, relay-device application, encrypted BLE transport, authentication, device identity, cryptographic signature verification, or real bank integration. The /relay endpoint is the future relay handoff boundary. Do not represent backend ACCEPTED as settlement.

Before real sensitive use: add HTTPS, authenticated devices, Android Keystore-backed keys, a reviewed message-signing/encryption protocol, authorization, fraud controls, audit logs, rate limiting, and independent security review. This is a learning simulation only.
