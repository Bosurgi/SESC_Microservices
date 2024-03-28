package com.sesc.libraryservice.security;

import com.sesc.libraryservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class LibrarySecurityConfig {

    private final LibraryUserDetailsService userDetailsService;

    private final StudentRepository studentRepository;

    @Autowired
    public LibrarySecurityConfig(
            LibraryUserDetailsService userDetailsService,
            StudentRepository studentRepository
    ) {
        this.userDetailsService = userDetailsService;
        this.studentRepository = studentRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    // Setting the Customised User Detail Service and the Password Encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    // Setting the Filter chain allowing the user to access the login page and the registration page
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/v1/students/register/").permitAll()
                        .requestMatchers("/changepassword").authenticated()
                        // TODO: Adding forbidden access to certain pages unless admin
                        .requestMatchers("/api/v1/students/").hasAuthority("ADMIN")
                        .anyRequest().hasAnyAuthority("REGISTERED", "ADMIN"))

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("studentId")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll())

                .csrf(AbstractHttpConfigurer::disable)

                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LibraryUrlAuthenticationSuccessHandler(studentRepository);
    }
}
