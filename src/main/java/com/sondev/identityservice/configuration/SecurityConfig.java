package com.sondev.identityservice.configuration;
import com.sondev.identityservice.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity //Optional ==> because Auto Enable
public class SecurityConfig {
// referent --> https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html

    private final String[] PUBLIC_ENDPOINTS = {
            "/users","/auth/log-in","/auth/token","/auth/introspect"
    };

    @Value("${jwt.signerKey}")
    private String  signerKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(Customizer.withDefaults())
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults());\
        httpSecurity.authorizeHttpRequests(request ->
                // Chỉ định endpoint không cần authorized
                request
                        // Allow POST requests to public endpoints without authentication
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()

                        // Chỉ có Admin mới có thể truy cập được
                        .requestMatchers(HttpMethod.GET, "/users")
                        .hasRole(Role.ADMIN.name()) //.hasAuthority("ROLE_ADMIN")
                        // All other requests must be authenticated
                        .anyRequest().authenticated());

        // Đăng ký 1 provide manager: để support cho JWT Token
        // Configure OAuth2 Resource Server to use JWT
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        // Disable CSRF protection
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return  jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA512");
// lưu ý lúc encode: JWSAlgorithm.HS512
// decode "HmacSHA512"
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    // Khai bao và đánh dấu là 1 Bean để có thể sử dụng @Autowired
    @Bean // Could not autowire. No beans of 'PasswordEncoder' type found.
    PasswordEncoder passwordEncoder (){
        return  new BCryptPasswordEncoder(10);
    }
}
