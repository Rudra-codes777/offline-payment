package com.offlinepayment.payment;
import jakarta.validation.Valid;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.http.HttpStatus;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.springframework.web.bind.annotation.*;import org.springframework.web.server.ResponseStatusException;import java.time.Instant;import java.util.UUID;
interface PaymentRepository extends JpaRepository<PaymentEntity,UUID>{}
@Service class PaymentService {
 private final PaymentRepository repo; PaymentService(PaymentRepository repo){this.repo=repo;}
 @Transactional PaymentResponse create(PaymentRequest r,boolean relay){validate(r);PaymentEntity p=repo.findById(r.id()).orElse(null);if(p!=null){same(p,r);if(p.status==PaymentStatus.ACCEPTED)return p.response();}else p=new PaymentEntity(r,PaymentStatus.PENDING_RELAY);
 if(relay){if(!p.expiresAt.isAfter(Instant.now())){p.status=PaymentStatus.EXPIRED;repo.save(p);throw new ResponseStatusException(HttpStatus.GONE,"Intent expired");}p.status=PaymentStatus.ACCEPTED;}return repo.save(p).response();}
 @Transactional(readOnly=true) PaymentResponse get(UUID id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)).response();}
 void validate(PaymentRequest r){if(r.senderId().equals(r.recipientId())||r.createdAt().isAfter(Instant.now().plusSeconds(60))||!r.expiresAt().isAfter(r.createdAt())||r.expiresAt().isAfter(r.createdAt().plusSeconds(86400)))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid intent");}
 void same(PaymentEntity p,PaymentRequest r){if(!p.senderId.equals(r.senderId())||!p.recipientId.equals(r.recipientId())||p.amount.compareTo(r.amount())!=0||!p.createdAt.equals(r.createdAt())||!p.expiresAt.equals(r.expiresAt()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Intent ID reused with different payload");}
}
@RestController @RequestMapping("/api/v1/payments") class PaymentController {
 private final PaymentService service; PaymentController(PaymentService s){service=s;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) PaymentResponse create(@Valid @RequestBody PaymentRequest r){return service.create(r,false);}
 @PostMapping("/relay") PaymentResponse relay(@Valid @RequestBody PaymentRequest r){return service.create(r,true);}
 @GetMapping("/{id}") PaymentResponse get(@PathVariable UUID id){return service.get(id);}
}
