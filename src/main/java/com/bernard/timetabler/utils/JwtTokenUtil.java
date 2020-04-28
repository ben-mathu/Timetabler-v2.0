package com.bernard.timetabler.utils;

import com.bernard.timetabler.dbinit.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * @author com.bernard
 */
public class JwtTokenUtil {

    public String generateToken(String issuer, String subject, Date expiration) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] apiKeyBytes = Constants.SECRET.getBytes(Charset.forName(StandardCharsets.UTF_8.toString()));
        Key encodedKey = new SecretKeySpec(apiKeyBytes, signatureAlgorithm.getJcaName());



        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .signWith(signatureAlgorithm, encodedKey)
                .compact();
    }

    public boolean verifyToken(String issuer, String subject, String token) {
        try {
            Jwts.parser()
                    .requireIssuer(issuer)
                    .requireSubject(subject)
                    .setSigningKey(Constants.SECRET.getBytes())
                    .parseClaimsJws(token);

            return true;
        } catch (MissingClaimException e) {
            System.out.println("Missing Claim: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (InvalidClaimException e) {
            System.out.println("Invalid Claim: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ExpiredJwtException e) {
            System.out.println("Expired Token: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Token: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SignatureException e) {
            System.out.println("Invalid Signature Token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
