package PasswordManager;

import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@RestController
@RequestMapping("/api/passwords")
public class PasswordManagerController {
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };
    private Map<String, String> passwordStore = new HashMap<>();

    @PostMapping("/add")
    public String addPassword(@RequestParam String site, @RequestParam String password) {
        try {
            String encryptedPassword = encrypt(password);
            passwordStore.put(site, encryptedPassword);
            return "Password added successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding password.";
        }
    }

    @GetMapping("/get")
    public String getPassword(@RequestParam String site) {
        try {
            String encryptedPassword = passwordStore.get(site);
            return encryptedPassword != null ? decrypt(encryptedPassword) : "No password found for this site.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving password.";
        }
    }

    private String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    private String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }
}
