package xor7studio.boat.session;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AuthenticationManager {
    public static final AuthenticationManager INSTANCE = new AuthenticationManager();
    protected AuthenticationManager(){
        try{
            Path keyPairFile=Path.of("AUTHENTICATION_KEY_PAIR");
            if (Files.exists(keyPairFile)) {
                authenticationKeyPair = loadKeyPairFromFile(keyPairFile);
            } else {
                authenticationKeyPair = generateKeyPair();
                saveKeyPairToFile(keyPairFile, authenticationKeyPair);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Getter
    private final KeyPair authenticationKeyPair;
    private static @NotNull KeyPair loadKeyPairFromFile(Path keyPairFile) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        byte[] bytes = Files.readAllBytes(keyPairFile);
        String content = new String(bytes, StandardCharsets.UTF_8);
        String[] parts = content.split(":");

        byte[] publicKeyBytes = Base64.getDecoder().decode(parts[0]);
        byte[] privateKeyBytes = Base64.getDecoder().decode(parts[1]);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    private static void saveKeyPairToFile(Path keyPairFile, @NotNull KeyPair keyPair) throws IOException {
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        String content = Base64.getEncoder().encodeToString(publicKeyBytes) + ":" + Base64.getEncoder().encodeToString(privateKeyBytes);
        Files.writeString(keyPairFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
