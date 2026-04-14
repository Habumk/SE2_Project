package com.klearn.config;

import com.klearn.security.UserDetailsImpl;
import com.klearn.service.AuthService;
import com.klearn.service.StreakService;
import com.klearn.service.XpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Session-based Spring Security configuration.
 * UC-01: Login via formLogin – successHandler updates last_login_at + streak.
 * UC-03: Logout clears JSESSIONID cookie.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                            StreakService streakService,
                                            AuthService authService,
                                            XpService xpService) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/css/**", "/js/**",
                    "/images/**", "/fonts/**", "/assets/**",
                    "/", "/home"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/auth/login")
                .usernameParameter("email")
                // UC-01: After successful login → update last_login_at + streak → redirect /dashboard
                .successHandler((request, response, authentication) -> {
                    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                    Long userId = userDetails.getUserId();

                    // UC-01 post-condition: update last_login_at
                    authService.updateLastLogin(userId);

                    // UC-01 step 6: checkAndUpdate streak
                    streakService.updateStreak(userId);

                    // Recalculate XP and level from completed lessons
                    System.out.println("DEBUG: Login success handler - recalculating stats for user " + userId);
                    xpService.recalculateUserStats(userId);
                    System.out.println("DEBUG: Login success handler - stats recalculated");

                    response.sendRedirect("/dashboard");
                })
                // UC-01 E1: wrong credentials → back to login with error
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                // UC-03: delete session cookie on logout
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}