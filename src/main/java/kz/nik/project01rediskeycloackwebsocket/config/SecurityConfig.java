package kz.nik.project01rediskeycloackwebsocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html","/users.html","/chat.html").permitAll() // Доступ к index.html и статическим ресурсам
                        .requestMatchers("/user").permitAll()
                        .requestMatchers("/user/*").permitAll()
                        .requestMatchers("/user/*/*").permitAll()
                        .requestMatchers("/chat/*").permitAll()
                        .requestMatchers("/chat/*/*").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /*.oauth2ResourceServer(o -> o.jwt(
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(keyCloakRoleConverter())
                ))*/;

        return http.build();
    }

   /* @Bean
    public KeycloakRoleConverter keyCloakRoleConverter(){
        return new KeycloakRoleConverter();
    }*/
}
