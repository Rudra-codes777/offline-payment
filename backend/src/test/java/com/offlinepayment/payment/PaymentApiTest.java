package com.offlinepayment.payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class PaymentApiTest {
 @Autowired MockMvc mvc;
 private String body(String id,String amount){
  Instant now=Instant.now();
  return "{\"id\":\""+id+"\",\"senderId\":\"alice\",\"recipientId\":\"bob\",\"amount\":\""+amount+"\",\"createdAt\":\""+now+"\",\"expiresAt\":\""+now.plusSeconds(3600)+"\"}";
 }
 @Test void relayAcceptsAndIsIdempotent() throws Exception {
  String id=UUID.randomUUID().toString(), json=body(id,"10.25");
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ACCEPTED"));
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
 }
 @Test void rejectsSameIdWithDifferentPayload() throws Exception {
  String id=UUID.randomUUID().toString();
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(body(id,"10.25"))).andExpect(status().isOk());
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(body(id,"99.00"))).andExpect(status().isConflict());
 }
}
