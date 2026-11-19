# Security assessment — prototype

## Threats
- Nearby malicious device impersonates a relay or sender.
- BLE packet interception, replay, modification, or relay substitution.
- Malicious relay changes amount or recipient before backend submission.
- Replayed intents or reused IDs.
- Backend endpoint abuse and denial of service.
- Device loss, local queue extraction, and key compromise.

## Controls currently present
- UUID intent identifiers.
- Backend validates amount constraints, sender/recipient difference, creation time, and expiry.
- Backend compares the complete payload for a reused ID and rejects conflicting reuse.
- Backend makes repeated identical accepted intents idempotent.
- BLE GATT service UUID filters discovery; payload size is bounded.

## Missing controls (must not be represented as solved)
- Authenticated device enrollment and authorization.
- Sender digital signature verified against a server-registered public key.
- Relay identity and mutual authentication.
- Application-layer authenticated encryption with nonce/replay protection.
- Android Keystore key generation, rotation, revocation, and recovery.
- Enforced BLE bonding / secure connections and a receipt channel.
- TLS with certificate validation between relay and backend.
- Durable outbox and atomic receipt handling.
- Rate limits, audit trails, monitoring, fraud detection, threat-model review, and penetration testing.

Do not use real financial data or money. The current BLE path is an educational transport proof-of-concept only.
