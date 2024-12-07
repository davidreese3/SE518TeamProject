package com.se518.teamproject;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private RateLimitingFilter rateLimitingFilter;


    /*@Autowired
    public WebSecurityConfig(CustomAuthenticationFailureHandler failureHandler,
                             MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.failureHandler = failureHandler;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }*/


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider customAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> {
            try {
                authz
                        .requestMatchers("/", "/login", "/user/register/**", "/logout").permitAll()
                        // Protected URLs
                        .requestMatchers("/user/list/**").authenticated()
                        // Any other request must be authenticated
                        .anyRequest().authenticated();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //http.addFilterBefore(accountLockFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);



        // when access denied, show "/403" page
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .accessDeniedPage("/403"));
        // "/login" allow custom login page
        // "/login?error" displays login.html with error message
        // "/userHome" default page when /login was the path.
        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureHandler(failureHandler)
                //.failureUrl("/login?error")
                .defaultSuccessUrl("/landing")
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")                   // The URL to handle logout
                .logoutSuccessUrl("/login?logout")      // Redirect URL after successful logout
                .invalidateHttpSession(true)            // Invalidate session on logout
                .deleteCookies("JSESSIONID")            // Remove session cookie
                .permitAll()
        );
        // needed for auto-login
        http.securityContext((securityContext) -> securityContext.requireExplicitSave(true));



        return http.build();
    }

    /*@Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery("SELECT email, password, active FROM useracct WHERE email=?");
        manager.setAuthoritiesByUsernameQuery("SELECT email, role FROM authority WHERE email=?");
        return manager;
    }*/

    @Bean
    public AccountLockFilter accountLockFilter() {
        return new AccountLockFilter(loginAttemptService);
    }

    // needed for auto login
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

}
