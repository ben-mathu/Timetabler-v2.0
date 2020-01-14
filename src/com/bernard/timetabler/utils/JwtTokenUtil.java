package com.bernard.timetabler.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * @author bernard
 */
public class JwtTokenUtil {
    public String generateToken(String issuer, String subject, String username, Date expiration) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(username)
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .signWith(key)
                .compact();
    }

    public boolean verifyToken(String issuer, String subject, String username, String token) {

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        try {
            Jwts.parser()
                    .requireIssuer(issuer)
                    .requireSubject(subject)
                    .requireAudience(username)
                    .setSigningKey(key)
                    .parseClaimsJws(token);

            return true;
        } catch (MissingClaimException e) {
            return false;
        } catch (InvalidClaimException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }
}
