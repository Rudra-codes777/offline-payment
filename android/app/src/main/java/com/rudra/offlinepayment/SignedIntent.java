package com.rudra.offlinepayment;
import android.security.keystore.KeyGenParameterSpec;import android.security.keystore.KeyProperties;import java.nio.charset.StandardCharsets;import java.security.*;import java.security.spec.ECGenParameterSpec;import java.util.Base64;import org.json.JSONObject;
final class SignedIntent {
 private static final String STORE="AndroidKeyStore",ALIAS="offline_payment_sender_signing_v1";
 private SignedIntent(){}
 static JSONObject create(String id,String sender,String recipient,String amount,String created,String expires)throws Exception{
  KeyStore ks=KeyStore.getInstance(STORE);ks.load(null);
  if(!ks.containsAlias(ALIAS)){KeyPairGenerator gen=KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC,STORE);gen.initialize(new KeyGenParameterSpec.Builder(ALIAS,KeyProperties.PURPOSE_SIGN|KeyProperties.PURPOSE_VERIFY).setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1")).setDigests(KeyProperties.DIGEST_SHA256).setUserAuthenticationRequired(false).build());gen.generateKeyPair();}
  PublicKey pub=ks.getCertificate(ALIAS).getPublicKey();PrivateKey priv=(PrivateKey)ks.getKey(ALIAS,null);
  String canonical=id+"\n"+sender+"\n"+recipient+"\n"+amount+"\n"+created+"\n"+expires;
  Signature signer=Signature.getInstance("SHA256withECDSA");signer.initSign(priv);signer.update(canonical.getBytes(StandardCharsets.UTF_8));byte[] sig=signer.sign();
  JSONObject j=new JSONObject();j.put("id",id);j.put("senderId",sender);j.put("recipientId",recipient);j.put("amount",amount);j.put("createdAt",created);j.put("expiresAt",expires);j.put("signerPublicKey",Base64.getEncoder().encodeToString(pub.getEncoded()));j.put("signature",Base64.getEncoder().encodeToString(sig));return j;
 }
}
