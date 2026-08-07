package com.cid.core.security;

/**
 * Excepción para fallos relacionados con JWT (firma inválida, token expirado,
 * emisor incorrecto, tipo de token inesperado o configuración ausente).
 *
 * <p>No expone el valor del token ni el secreto en su mensaje.
 */
public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
