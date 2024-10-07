package com.ankush.EDI.Config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration          //MARK AS CONFIGURATION FILE
@EnableWebSecurity      //ENABLE SECURITY
@EnableJdbcHttpSession  //ENABLE JDBC SESSIONS
public class SecurityConfig {
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager manager(DataSource source) {
        return new JdbcUserDetailsManager(source);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder encoder, UserDetailsManager users) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(users)
                .passwordEncoder(encoder);
        return builder.build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http, @Autowired UserDetailsManager userDetailsManager, @Autowired AuthenticationManager authenticationManager, @Autowired PasswordEncoder passwordEncoder) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .formLogin(x -> {
//                    x.loginPage("/login");
//                    //                    x.loginPage("http://localhost:3000/login");
//                    x.loginProcessingUrl("/java/api/auth/login_page");
//                    x.successHandler((a, b, c) -> {
//                    });
//                    x.failureHandler((request, response, exception) -> {
//                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                        response.getOutputStream().println("Username or password incorrect");
//                    });
//                })
                .formLogin(Customizer.withDefaults())
                .logout(x -> {
                    x.logoutUrl("/java/api/user/logout").permitAll();
                    x.logoutSuccessHandler((a, b, c) -> {
                    });
                }).httpBasic(x -> {
                    x.authenticationEntryPoint((request, response, authException) -> {
                        response.setHeader("WWW-Authenticate", "Authenication Error");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                    });
                }).userDetailsService(userDetailsManager)
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(x -> x
                                .requestMatchers("/java/api/test/**").permitAll()                                       //TEST API
                                .requestMatchers("/java/api/auth/**").permitAll()                                       //AUTHENTICATION API
                                .requestMatchers("/java/api/user/**").hasAnyAuthority("USER")               //AUTHENTICATION API
                                .requestMatchers("/java/api/userspace/**").hasAnyAuthority("USER")               //AUTHENTICATION API
                                .requestMatchers("/java/api/auth/login_page").permitAll()
                                .requestMatchers("/login").permitAll()

//                        .requestMatchers("/**").hasAnyAuthority("USER")        //ALLOW LOGGED IN
//                        .anyRequest().authenticated()

                );
        ;

        return http.build();
    }
}
