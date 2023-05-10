package xor7studio.boat.session;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Date;

public class AuthenticationManager {
    public static final AuthenticationManager INSTANCE = new AuthenticationManager();
    private final Path keyPairFile=Path.of("AUTHENTICATION_KEY_PAIR");
    //移动到Settings
    @Getter
    @Setter
    private static String ISSUER = "Boat";
    protected AuthenticationManager(){
        try{
            authenticationKeyPair = Files.exists(keyPairFile)?
                    loadKeyPairFromFile(keyPairFile):generateKeyPair();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    private final KeyPair authenticationKeyPair;
    public PublicKey getAuthenticationPublicKey(){
        return authenticationKeyPair.getPublic();
    }
    public String generateBearerToken(String data,long expirationTime, TemporalUnit unit){
        Instant now = Instant.now();
        Instant expiration = now.plus(expirationTime, unit);
        return Jwts.builder()
                .setSubject(data)
                .setIssuer("Boat")
                .setAudience("Authentication")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(authenticationKeyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }
    public String readBearerToken(String token) throws SignatureException,ExpiredJwtException {
        PublicKey publicKey = authenticationKeyPair.getPublic();
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        if(claims.getExpiration().before(Date.from(Instant.now())))
            throw new ExpiredJwtException(claimsJws.getHeader(), claims,"");
        if(claims.getIssuer().equals(""));
        return claims.getSubject();
    }
    private @NotNull KeyPair loadKeyPairFromFile(Path keyPairFile) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        String content = Files.readString(keyPairFile);
        String[] parts = content.split(":");
        if(parts.length!=2) return generateKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Decoders.BASE64.decode(parts[0]));
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(parts[1]));
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        return new KeyPair(publicKey, privateKey);
    }

    private void saveKeyPairToFile(@NotNull KeyPair keyPair) throws IOException {
        String publicKey = Encoders.BASE64.encode(keyPair.getPublic().getEncoded());
        String privateKey = Encoders.BASE64.encode(keyPair.getPrivate().getEncoded());
        Files.writeString(keyPairFile, publicKey+":"+privateKey, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, IOException {
        KeyPair result=Keys.keyPairFor(SignatureAlgorithm.RS256);
        saveKeyPairToFile(result);
        return result;
    }
}
