package com.cid.core.annotation;

import com.cid.core.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Activa el soporte de seguridad JWT de CID en el microservicio:
 * <ul>
 *   <li>{@link SecurityAutoConfiguration} — registra {@code JwtService} y {@code JwtProperties}</li>
 * </ul>
 *
 * <p>Es opt-in: solo los servicios que declaren esta anotación obtienen el
 * {@code JwtService}. Requiere configurar {@code cid.security.jwt.secret}.
 *
 * <pre>{@code
 *   @SpringBootApplication
 *   @EnableOracleConnection
 *   @EnableCidWebSupport
 *   @EnableCidSecurity      // emite o valida JWT
 *   public class MyApp { ... }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SecurityAutoConfiguration.class)
public @interface EnableCidSecurity {
}
