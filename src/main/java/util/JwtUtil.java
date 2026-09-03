package org.example.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // Static secret string for consistent key generation across JVM restarts
    private static final String SECRET_STRING = "MySuperSecretKeyForSunriseDentalClinicApp12345";

    // Token එක Sign කරන්න භාවිතා කරන ඉතාමත් ආරක්ෂිත රහස් යතුර (Secret Key)
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    // Token එකක වලංගු කාලය (පැය 1ක් ලෙස සකසා ඇත: 1000ms * 60s * 60m)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // 1. JWT Token එකක් අලුතින් ජනනය කිරීම
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username) // ප්‍රධාන දත්තය (Username)
                .claim("role", role) // අමතර දත්ත (Role)
                .issuedAt(new Date()) // නිකුත් කළ වෙලාව
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // කල් ඉකුත් වන වෙලාව
                .signWith(SECRET_KEY) // අපේ යතුරෙන් අත්සන් කිරීම
                .compact(); // String එකක් ලෙස සැකසීම
    }

    // 2. Token එකෙන් Username එක කියවා ගැනීම
    public static String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // යතුර හරහා token එක verify කිරීම
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject(); // අර කලින් දාපු subject එක (username) ගැනීම
    }

    // 3. Token එකෙන් Role එක කියවා ගැනීම
    public static String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class); // අර කලින් දාපු claim එක (role) ගැනීම
    }
}