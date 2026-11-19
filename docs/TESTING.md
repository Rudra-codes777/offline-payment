# Two-phone test plan

## Backend
1. Run `cd backend && mvn test`.
2. POST a valid intent to `/api/v1/payments/relay`; expect ACCEPTED.
3. Repeat the identical payload; expect the same intent ID and ACCEPTED state.
4. Reuse the same ID with a different amount; expect HTTP 409.
5. Submit an expired intent; expect HTTP 410.
6. GET the accepted intent by ID; expect its stored state.

## BLE integration
1. Use two physical Android phones with BLE enabled and grant runtime permissions.
2. Start backend and configure relay URL to a reachable LAN address.
3. Start relay advertising on phone B.
4. Create an intent on sender phone A and transfer it over BLE.
5. Confirm relay reports backend acceptance.
6. Stop backend or Wi-Fi and verify relay reports failure rather than claiming acceptance.
7. Test out-of-range disconnects, app backgrounding, Bluetooth disabled, permissions denied, duplicate writes, malformed payloads, and intents over 480 bytes.

BLE and real-device tests must be executed manually; repository code alone cannot establish radio interoperability.
