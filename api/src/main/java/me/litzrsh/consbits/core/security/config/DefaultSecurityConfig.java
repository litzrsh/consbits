package me.litzrsh.consbits.core.security.config;

import lombok.RequiredArgsConstructor;
import me.litzrsh.consbits.core.security.RestfulAccessDeniedHandler;
import me.litzrsh.consbits.core.security.RestfulAuthenticationEntryPoint;
import me.litzrsh.consbits.core.security.RestfulAuthenticationFailureHandler;
import me.litzrsh.consbits.core.security.RestfulAuthenticationSuccessHandler;
import me.litzrsh.consbits.core.security.authentication.CustomAuthenticationProvider;
import me.litzrsh.consbits.core.security.authentication.JdbcAuthenticationProcessingFilter;
import me.litzrsh.consbits.core.security.authentication.JdbcLogoutProcessingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DefaultSecurityConfig {

    private final List<CustomAuthenticationProvider> providers;

    private final RestfulAuthenticationSuccessHandler authenticationSuccessHandler;
    private final RestfulAuthenticationFailureHandler authenticationFailureHandler;

    @Bean("securityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = defaultAuthenticationManager(http);
        http.authenticationManager(authenticationManager);
        // Disable unused settings
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);
        // Exception handling
        http.exceptionHandling(e -> e
                .accessDeniedHandler(new RestfulAccessDeniedHandler())
                .authenticationEntryPoint(new RestfulAuthenticationEntryPoint())
        );
        // Authorization request
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1.0/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1.0/.public").permitAll()
                .anyRequest().authenticated()
        );
        // Filters
        http.addFilterBefore(jdbcAuthenticationProcessingFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JdbcLogoutProcessingFilter(), JdbcAuthenticationProcessingFilter.class);
        return http.build();
    }

    @Bean("authenticationManager")
    public AuthenticationManager defaultAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .parentAuthenticationManager(null);
        for (CustomAuthenticationProvider provider : providers) {
            builder.authenticationProvider(provider);
        }
        return builder.build();
    }

    protected JdbcAuthenticationProcessingFilter jdbcAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        JdbcAuthenticationProcessingFilter filter = new JdbcAuthenticationProcessingFilter(authenticationManager);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }
}
