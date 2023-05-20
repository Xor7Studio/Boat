package xor7studio.boat.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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

public class AuthenticationUtils {
    public static final AuthenticationUtils INSTANCE = new AuthenticationUtils();
    private final Path keyPairFile=new File("AUTHENTICATION_KEY_PAIR").toPath();
    protected AuthenticationUtils(){
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
    public BearerTokenData readBearerToken(String token) throws SignatureException,ExpiredJwtException {
        BearerTokenData result=new BearerTokenData();
        PublicKey publicKey = authenticationKeyPair.getPublic();
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .requireIssuer("Boat")
                    .requireAudience("Authentication")
                    .requireExpiration(Date.from(Instant.now()))
                    .setAllowedClockSkewSeconds(5*60)
                    .build()
                    .parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            result.subject=claims.getSubject();
        }catch (JwtException e){
            result.isValid=false;
        }
        return result;
    }
    private @NotNull KeyPair loadKeyPairFromFile(Path keyPairFile) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        String content = new String(Files.readAllBytes(keyPairFile));
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
        Files.write(keyPairFile, (publicKey+":"+privateKey).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private KeyPair generateKeyPair() throws IOException {
        KeyPair result=Keys.keyPairFor(SignatureAlgorithm.RS256);
        saveKeyPairToFile(result);
        return result;
    }
    public static class BearerTokenData{
        public String subject;
        public boolean isValid=true;
    }
}
