package com.cid.core.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuración de seguridad CID. Registra el {@link JwtService} y habilita
 * {@link JwtProperties}.
 *
 * <p>Se activa al usar {@code @EnableCidSecurity} en la clase principal del
 * microservicio. Es opt-in: los servicios que no la declaran no se ven afectados.
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityAutoConfiguration {

    @Bean
    public JwtService jwtService(JwtProperties properties) {
        return new JwtService(properties);
    }
}
