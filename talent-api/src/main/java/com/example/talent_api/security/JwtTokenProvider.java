//package com.example.talent_api.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    @Value("${app.jwt-secret}")
//    private String jwtSecret;
//
//    @Value("${app.jwt-expiration-milliseconds}")
//    private long jwtExpirationDate;
//
//    // Generate JWT token
//    public String generateToken(Authentication authentication){
//        String username = authentication.getName();
//
//        Date currentDate = new Date();
//
//        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
//
//        String token = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(expireDate)
//                .signWith(key())
//                .compact();
//
//        return token;
//    }
//
//    private Key key(){
//        return Keys.hmacShaKeyFor(
//                Decoders.BASE64.decode(jwtSecret)
//        );
//    }
//
//    // Get username from JWT token
//    public String getUsername(String token){
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        String username = claims.getSubject();
//
//        return username;
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            System.out.println("Validating token: " + token);
//            Jwts.parserBuilder()
//                    .setSigningKey(key())
//                    .build()
//                    .parseClaimsJws(token);
//            System.out.println("Token validated successfully");
//            return true;
//        } catch (JwtException ex) {
//            System.out.println("Token validation error: " + ex.getMessage());
//        }
//        return false;
//    }
//
//
//
//}

package com.example.talent_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    private final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        logger.info("Generated JWT Token: " + token);
        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        logger.info("Extracted username from token: " + username);
        return username;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            logger.info("Token validation successful.");
            return true;
        } catch (MalformedJwtException ex) {
            logger.severe("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.severe("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.severe("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.severe("JWT claims string is empty: " + ex.getMessage());
        }
        return false;
    }
}
