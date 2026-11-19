package com.offlinepayment.payment;
import jakarta.persistence.*;import jakarta.validation.constraints.*;import java.math.BigDecimal;import java.time.Instant;import java.util.UUID;
@Entity @Table(name="payment_intents")
class PaymentEntity {
 @Id UUID id; @Column(nullable=false) String senderId; @Column(nullable=false) String recipientId; @Column(nullable=false,precision=12,scale=2) BigDecimal amount; @Column(nullable=false) Instant createdAt; @Column(nullable=false) Instant expiresAt; @Enumerated(EnumType.STRING) @Column(nullable=false) PaymentStatus status;
 @Lob @Column(nullable=false) String signerPublicKey; @Lob @Column(nullable=false) String signature;
 protected PaymentEntity(){}
 PaymentEntity(PaymentRequest r,PaymentStatus s){id=r.id();senderId=r.senderId();recipientId=r.recipientId();amount=r.amount();createdAt=r.createdAt();expiresAt=r.expiresAt();status=s;signerPublicKey=r.signerPublicKey();signature=r.signature();}
 PaymentResponse response(){return new PaymentResponse(id,senderId,recipientId,amount,createdAt,expiresAt,status);}
}
enum PaymentStatus { PENDING_RELAY, ACCEPTED, EXPIRED }
record PaymentRequest(@NotNull UUID id,@NotBlank @Size(max=80) String senderId,@NotBlank @Size(max=80) String recipientId,@NotNull @DecimalMin("0.01") @Digits(integer=10,fraction=2) BigDecimal amount,@NotNull Instant createdAt,@NotNull Instant expiresAt,@NotBlank String signerPublicKey,@NotBlank String signature){}
record PaymentResponse(UUID id,String senderId,String recipientId,BigDecimal amount,Instant createdAt,Instant expiresAt,PaymentStatus status){}
