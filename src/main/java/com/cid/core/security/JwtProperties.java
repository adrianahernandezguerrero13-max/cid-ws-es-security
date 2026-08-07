package com.cid.core.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de configuración de JWT para los microservicios CID.
 *
 * <p>Prefijo: {@code cid.security.jwt}. Ejemplo en application.properties:
 * <pre>
 * cid.security.jwt.secret=${JWT_SECRET}
 * cid.security.jwt.issuer=cid-authentication
 * cid.security.jwt.access-ttl-minutes=15
 * cid.security.jwt.refresh-ttl-days=7
 * </pre>
 *
 * <p>Fase 1: firma HS256 con secreto compartido ({@code secret}). El secreto debe
 * tener al menos 32 caracteres (256 bits) para HS256.
 */
@ConfigurationProperties(prefix = "cid.security.jwt")
public class JwtProperties {

    /** Secreto compartido para firmar/verificar (HS256). Mínimo 32 caracteres. */
    private String secret;

    /** Emisor esperado del token (claim {@code iss}). */
    private String issuer = "cid-authentication";

    /** Vigencia del access token en minutos. */
    private long accessTtlMinutes = 15;

    /** Vigencia del refresh token en días. */
    private long refreshTtlDays = 7;

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public long getAccessTtlMinutes() { return accessTtlMinutes; }
    public void setAccessTtlMinutes(long accessTtlMinutes) { this.accessTtlMinutes = accessTtlMinutes; }

    public long getRefreshTtlDays() { return refreshTtlDays; }
    public void setRefreshTtlDays(long refreshTtlDays) { this.refreshTtlDays = refreshTtlDays; }
}
