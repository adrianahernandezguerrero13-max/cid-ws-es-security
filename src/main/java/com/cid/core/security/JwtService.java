package com.cid.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * Servicio compartido para emitir y validar JWT en los microservicios CID.
 *
 * <p>Fase 1: firma HS256 con secreto compartido ({@link JwtProperties#getSecret()}).
 * El emisor ({@code authentication}) genera los tokens y el resto de servicios los
 * verifican con el mismo secreto.
 *
 * <p>Claims del access token: {@code sub} (userId), {@code name}, {@code rol},
 * {@code iss}, {@code iat}, {@code exp}. El refresh token añade {@code type=refresh}.
 */
public class JwtService {

    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_REFRESH = "refresh";

    private final JwtProperties properties;
    private final SecretKey key;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        String secret = properties.getSecret();
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new JwtException(
                    "cid.security.jwt.secret ausente o menor a 32 caracteres (requerido para HS256)");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Emite el par de tokens (access + refresh) para un usuario autenticado.
     *
     * @param userId      identificador del usuario (claim {@code sub})
     * @param extraClaims claims adicionales para el access token (ej. name, rol)
     */
    public TokenPair generateTokens(String userId, Map<String, Object> extraClaims) {
        Instant now = Instant.now();

        String access = Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(userId)
                .claims(extraClaims == null ? Map.of() : extraClaims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(properties.getAccessTtlMinutes() * 60)))
                .signWith(key)
                .compact();

        String refresh = Jwts.builder()
                .issuer(properties.getIssuer())
                .subject(userId)
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(properties.getRefreshTtlDays() * 24 * 60 * 60)))
                .signWith(key)
                .compact();

        return new TokenPair(access, refresh);
    }

    /**
     * Valida y parsea un token, verificando firma, expiración y emisor.
     *
     * @return los claims del token
     * @throws JwtException si el token es inválido, expiró o el emisor no coincide
     */
    public Claims parse(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(properties.getIssuer())
                    .build()
                    .parseSignedClaims(token);
            return jws.getPayload();
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            throw new JwtException("Token JWT inválido o expirado", e);
        }
    }

    /** Extrae el userId ({@code sub}) de un token válido. */
    public String extractUserId(String token) {
        return parse(token).getSubject();
    }

    /** Indica si el token es un refresh token (claim {@code type=refresh}). */
    public boolean isRefreshToken(String token) {
        return TYPE_REFRESH.equals(parse(token).get(CLAIM_TYPE, String.class));
    }
}
