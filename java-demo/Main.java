import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Signature;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws Exception {
        byte[] message = "hello cbom".getBytes(StandardCharsets.UTF_8);

        // 1) Hash (SHA-256)
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha256.digest(message);

        // 2) Encrypt (AES/GCM/NoPadding)
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        SecretKey aesKey = kg.generateKey();

        byte[] nonce = new byte[12];
        new java.security.SecureRandom().nextBytes(nonce);

        Cipher aesgcm = Cipher.getInstance("AES/GCM/NoPadding");
        aesgcm.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(128, nonce));
        byte[] ciphertext = aesgcm.doFinal(message);

        // 3) Sign (SHA256withRSA)
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(kp.getPrivate());
        sig.update(message);
        byte[] signature = sig.sign();

        System.out.println("sha256_b64=" + b64(digest));
        System.out.println("aesgcm_nonce_b64=" + b64(nonce));
        System.out.println("aesgcm_ciphertext_b64=" + b64(ciphertext));
        System.out.println("rsa_signature_b64=" + b64(signature));
    }

    private static String b64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
