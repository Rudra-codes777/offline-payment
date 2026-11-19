package com.offlinepayment.payment;
import java.math.BigDecimal;import java.nio.charset.StandardCharsets;import java.security.*;import java.security.spec.X509EncodedKeySpec;import java.util.Base64;
final class IntentSignatureVerifier {
 private IntentSignatureVerifier(){}
 static String canonical(PaymentRequest r){return r.id()+"\n"+r.senderId()+"\n"+r.recipientId()+"\n"+r.amount().toPlainString()+"\n"+r.createdAt()+"\n"+r.expiresAt();}
 static boolean verify(PaymentRequest r){
  try{
   byte[] keyBytes=Base64.getDecoder().decode(r.signerPublicKey());
   byte[] signature=Base64.getDecoder().decode(r.signature());
   if(keyBytes.length>256||signature.length>128)return false;
   PublicKey key=KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(keyBytes));
   Signature verifier=Signature.getInstance("SHA256withECDSA");verifier.initVerify(key);verifier.update(canonical(r).getBytes(StandardCharsets.UTF_8));return verifier.verify(signature);
  }catch(Exception e){return false;}
 }
}
