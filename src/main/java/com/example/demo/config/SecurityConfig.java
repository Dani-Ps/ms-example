package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withUsername("admin")
                .password(passwordEncoder().encode("test"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

        // Define un Bean de Spring que construye la cadena de filtros de Spring Security
// SecurityFilterChain es el conjunto de filtros que se ejecutan en cada request HTTP
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        // HttpSecurity permite configurar cómo se protege la aplicación
//        return http
//
//                // Define las reglas de AUTORIZACIÓN por endpoint
//                .authorizeHttpRequests(auth -> auth
//
//                        // Permite el acceso libre al endpoint /public
//                        // No requiere autenticación ni token
//                        .requestMatchers("/public").permitAll()
//
//                        // Permite el acceso al endpoint /admin solo a usuarios con rol ADMIN
//                        // Spring internamente espera el rol como "ROLE_ADMIN"
//                        .requestMatchers("/admin").hasRole("ADMIN")
//
//                        // Cualquier otro endpoint:
//                        // - requiere que el usuario esté autenticado
//                        // - no importa el rol, solo que esté logueado
//                        .anyRequest().authenticated()
//                )
//
//                // Construye y devuelve la cadena de filtros de seguridad
//                .build();
//    }


// Define el Bean que configura la seguridad global del microservicio
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            return http

                    // Habilita CORS en Spring Security
                    // Permite que el backend acepte peticiones desde otros dominios
                    // (por ejemplo un frontend en React o Angular)
                    // Usa la configuración definida en CorsConfigurationSource
                    .cors(Customizer.withDefaults())

                    // Deshabilita la protección CSRF
                    // Correcto para APIs REST que:
                    // - no usan sesiones
                    // - no usan cookies
                    // - usan JWT en el header Authorization
                    .csrf(csrf -> csrf.disable())

                    // Configura las reglas de autorización
                    .authorizeHttpRequests(auth -> auth

                            // Exige que TODAS las peticiones estén autenticadas
                            // No distingue rutas ni roles
                            // Ideal cuando:
                            // - la autorización fina se maneja en un API Gateway
                            // - o se valida por claims del JWT
                            .anyRequest().authenticated()
                    )

                    // Construye y devuelve la configuración de seguridad
                    .build();
        }
}

