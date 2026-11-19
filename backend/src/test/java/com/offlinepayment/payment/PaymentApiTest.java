package com.offlinepayment.payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class PaymentApiTest {
 @Autowired MockMvc mvc;
 @Test void relayAcceptsAndIdempotentlyReturnsSameIntent() throws Exception {
  String id="a54fcd77-6fd5-4b6b-9e7a-2c0f1b0c8b14";
  String body="{\"id\":\""+id+"\",\"senderId\":\"alice\",\"recipientId\":\"bob\",\"amount\":\"10.25\",\"createdAt\":\"2026-11-19T08:00:00Z\",\"expiresAt\":\"2026-11-19T09:00:00Z\"}";
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(body))
    .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ACCEPTED"));
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(body))
    .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
 }
 @Test void rejectsSameIdWithDifferentPayload() throws Exception {
  String id="b54fcd77-6fd5-4b6b-9e7a-2c0f1b0c8b14";
  String body="{\"id\":\""+id+"\",\"senderId\":\"alice\",\"recipientId\":\"bob\",\"amount\":\"10.25\",\"createdAt\":\"2026-11-19T08:00:00Z\",\"expiresAt\":\"2026-11-19T09:00:00Z\"}";
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk());
  String changed=body.replace("10.25","99.00");
  mvc.perform(post("/api/v1/payments/relay").contentType(MediaType.APPLICATION_JSON).content(changed)).andExpect(status().isConflict());
 }
}
