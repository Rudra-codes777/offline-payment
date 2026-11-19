# Security assessment — prototype

## Implemented integrity control
The sender creates a P-256 ECDSA key pair in Android Keystore. Each intent is signed with SHA256withECDSA over this canonical UTF-8 message:
`id\nsenderId\nrecipientId\namount\ncreatedAt\nexpiresAt`.
The public key and signature travel with the intent. The backend verifies the signature before accepting or storing the intent and rejects altered content. Private signing key material is non-exportable from Android Keystore on supported devices.

**Important trust limitation:** the public key is currently self-presented inside the intent. This proves that the payload was signed by the corresponding private key and detects tampering, but it does NOT prove that the key belongs to the claimed sender ID. A malicious client can create its own key and claim another sender ID. Real identity requires authenticated enrollment, verified account binding, key attestation where appropriate, and server-side key registration/revocation.

## Remaining security work
- Authenticated enrollment and server-side sender-key registry.
- Mutual TLS or authenticated device tokens for relay-to-backend.
- HTTPS in deployment; sample Android cleartext networking is for local development only.
- Authenticated encryption and replay protection for BLE; signature does not encrypt payload.
- Enforce bonded BLE connections with appropriate secure characteristics and test OEM behavior.
- Signed backend receipt with a pinned server verification key, delivered back to sender.
- Durable relay outbox, atomic receipt processing, rate limits, audit logs, monitoring, fraud detection, key rotation/revocation, and independent review.

Do not use real financial data or money. This remains an educational simulation.
