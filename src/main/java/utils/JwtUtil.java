package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


public class JwtUtil {

    private static final String SECRET = "tyht-thht-iujyhtg-ytg-yjuhtg-adfpmdn-bgvf-nbv-berzfz-ujhytg";
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 2; 

    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getKey())
                .compact();
    }

    // Retourne le username si le token est valide, null sinon
    public static String validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    // Retourne le username à partir d'une requête, null si invalide
    public static String getUser(HttpServletRequest req) {
        // Using: © 2024 Maxime MORGE <maxime.morge@univ-lyon1.fr>,
        // ref: ExempleCM02, BasicAuthentificationFilter class, function boolean isAuthorized(String authorization)
        String authorization = req.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authorization.substring("BEARER".length()).trim(); // Décoder le token
            return validateToken(token);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

}