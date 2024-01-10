package com.synrgy.kaboor.backend.security.config;

import com.synrgy.kaboor.backend.AppUser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final AppUserService appUserservice;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(requests -> requests
//                        .requestMatchers("/api/v*/registration/**").authenticated()
//                        .anyRequest().permitAll())
//                .authenticationProvider(daoAuthenticationProvider())
//                .csrf(AbstractHttpConfigurer::disable);
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestMatcher matcher = new AntPathRequestMatcher("/api/v*/auth/register/**", HttpMethod.POST.toString());
        http
                .authenticationProvider(daoAuthenticationProvider())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(matcher).permitAll()
                        .anyRequest().authenticated())
                .formLogin(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserservice);
        return provider;
    }
}