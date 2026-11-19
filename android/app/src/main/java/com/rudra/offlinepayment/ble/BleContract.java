package com.rudra.offlinepayment.ble;
import java.util.UUID;
public final class BleContract {
 private BleContract(){}
 public static final UUID SERVICE=UUID.fromString("6d8f0001-8a5b-4b7d-9c2e-6e6f666c696e");
 public static final UUID PAYMENT=UUID.fromString("6d8f0002-8a5b-4b7d-9c2e-6e6f666c696e");
 public static final int MAX_PAYLOAD_BYTES=480;
}