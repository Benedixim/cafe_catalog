package com.example.traineejava.configs;

import com.example.traineejava.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, NoOpPasswordEncoder noOpPasswordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(noOpPasswordEncoder);

        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/profile", "/dishes/add", "/categories/add", "/recipes/add").hasAnyRole( "ADMIN", "USER")
                        .requestMatchers("/cafes/add", "/cafes/{id}/edit").hasAnyRole( "ADMIN")
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/favicon.png", "/svg/**").permitAll()
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")//ошибка пост запроса
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                //.csrf().disable();
                .csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }

//    @Bean
//
//    public SecurityFilterChain securityFilterChain3(HttpSecurity http) throws Exception {
//        return http
//                .authorizeHttpRequests(auth -> {
//                            auth.requestMatchers("/").permitAll();
//                            auth.requestMatchers("/error").permitAll();
//                            auth.anyRequest().authenticated();
//
//                        }
//                )
//                .formLogin(withDefaults())
//                .build();
//
//
//    }


    @SuppressWarnings("deprecation")
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
