package com.home_manager.config;

import com.home_manager.model.enums.RoleEnum;
import com.home_manager.repository.UserRepository;
import com.home_manager.service.HomeManagerUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

        http.
                sessionManagement().
                sessionCreationPolicy( SessionCreationPolicy.ALWAYS).
        and().
                authorizeHttpRequests().
                requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().
                antMatchers("/favicon.ico", "/", "/register", "/forgot-password", "/reset-password").permitAll().
                antMatchers("/admin").hasRole(RoleEnum.ADMIN.name()).
                antMatchers("/manager").hasRole(RoleEnum.MANAGER.name()).
                antMatchers("/cashier").hasRole(RoleEnum.CASHIER.name()).
                anyRequest().authenticated().
        and().
                formLogin().loginPage("/").
                usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                defaultSuccessUrl("/profile").
                failureForwardUrl("/login-fail").
        and().
                rememberMe().
                rememberMeParameter("remember-me").
                tokenValiditySeconds(7 * 24 * 60 * 60).
                key("home-manager_remember_me").
        and().
                logout().
                logoutUrl("/logout").
                logoutSuccessUrl("/").
                invalidateHttpSession(true).
                deleteCookies("JSESSIONID");
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new HomeManagerUserDetailsService(userRepository);
    }
}
