package com.se518.teamproject;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT email, password, active FROM useracct WHERE email=?")
                .authoritiesByUsernameQuery("select email, role FROM authority WHERE email=?");
    }

      @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> {
            try {
                authz
                .requestMatchers("/", "/login", "/user/register/**", "/logout", "/upload/**", "/file/").permitAll()
                .anyRequest().authenticated();
                        // Uncomment this line to protect URLs after login
                        //.requestMatchers("/user/list/**").authenticated()
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/upload/**", "/file/**") // Allow file upload without CSRF protection
        );
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/403"));

        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/landing", true)  // Ensure this redirects to a valid page after successful login
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        );

        http.securityContext(securityContext -> securityContext.requireExplicitSave(true));

        return http.build();
    }


    // needed for auto login
    // @Bean
    // public SecurityContextRepository securityContextRepository() {
    //     return new DelegatingSecurityContextRepository(
    //             new RequestAttributeSecurityContextRepository(),
    //             new HttpSessionSecurityContextRepository()
    //     );
    // }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
