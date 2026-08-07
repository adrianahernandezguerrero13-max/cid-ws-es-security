package com.cid.core.security;

/**
 * Par de tokens emitido tras una autenticación exitosa.
 *
 * @param accessToken  token de acceso (vida corta) para autorizar peticiones
 * @param refreshToken token de refresco (vida larga) para renovar el access token
 */
public record TokenPair(String accessToken, String refreshToken) {
}
