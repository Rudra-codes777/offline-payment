package com.offlinepayment.payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.charset.StandardCharsets;import java.security.*;import java.time.Instant;import java.util.Base64;import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class PaymentApiTest {
 @Autowired MockMvc mvc;
 private KeyPair keys() throws Exception {KeyPairGenerator g=KeyPairGenerator.getInstance("EC");g.initialize(256);return g.generateKeyPair();}
 private String body(String id,String amount,KeyPair pair,Instant now)throws Exception {
  String sender="alice",recipient="bob",created=now.toString(),expires=now.plusSeconds(3600).toString();
  String canonical=id+"\n"+sender+"\n"+recipient+"\n"+amount+"\n"+created+"\n"+expires;
  Signature s=Signature.getInstance("SHA256withECDSA");s.initSign(pair.getPrivate());s.update(canonical.getBytes(StandardCharsets.UTF_8));
  return "{\"id\":\""+id+"\",\"senderId\":\""+sender+"\",\"recipientId\":\""+recipient+"\",\"amount\":\""+amount+"\",\"createdAt\":\""+created+"\",\"expiresAt\":\""+expires+"\",\"signerPublicKey\":\""+Base64.getEncoder().encodeToString(pair.getPublic().getEncoded())+"\",\"signature\":\""+Base64.getEncoder().encodeToString(s.sign())+"\"}";
 }
 @Test void relayAcceptsAndIsIdempotent() throws Exception {
  String id=UUID.randomUUID().toString(),json=body(id,"10.25",keys(),Instant.now());
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ACCEPTED"));
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
 }
 @Test void rejectsSameIdWithDifferentPayload() throws Exception {
  String id=UUID.randomUUID().toString();KeyPair pair=keys();Instant now=Instant.now();
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(body(id,"10.25",pair,now))).andExpect(status().isOk());
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(body(id,"99.00",pair,now))).andExpect(status().isConflict());
 }
 @Test void rejectsTamperedSignature() throws Exception {
  String json=body(UUID.randomUUID().toString(),"10.25",keys(),Instant.now()).replace("\"10.25\"","\"90.00\"");
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isUnauthorized());
 }
}
