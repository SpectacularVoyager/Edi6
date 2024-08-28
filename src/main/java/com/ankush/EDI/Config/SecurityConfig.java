package com.ankush.EDI.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .formLogin(Customizer.withDefaults())   //DEFAULT LOGIN FORM
                .csrf(AbstractHttpConfigurer::disable)  //DISABLE CSRF
                .authorizeHttpRequests(x -> x
                        .requestMatchers("/java/api/test/**").permitAll()                                       //TEST API
                        .requestMatchers("/java/api/auth/**").permitAll()                                       //AUTHENTICATION API
                        .requestMatchers("/java/api/user/**").hasAnyAuthority("USER")               //AUTHENTICATION API
                        .requestMatchers("/**").hasAnyAuthority("USER")        //ALLOW LOGGED IN
                        .anyRequest().authenticated()

                ).exceptionHandling(x -> x
                        .accessDeniedPage("/forbidden.html")
                ).logout(x -> {
                    x.logoutUrl("/java/api/auth/logout").permitAll();
                    x.logoutSuccessHandler((a, b, c) -> {
                    });
                }).userDetailsService(userDetailsManager)
                .authenticationManager(authenticationManager)

                .httpBasic(Customizer.withDefaults())
        ;

        return http.build();
    }
}
