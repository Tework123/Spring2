package springApp2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import springApp2.models.enums.Role;
import springApp2.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
// заменяет globalSecurity, для работы защищающих аннотаций, в это проекте без них
//@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers(HttpMethod.GET,
                                        "/post",
                                        "/post/{id}",
                                        "/profile/{id}",
                                        "/error/**",
                                        "/registration",
                                        "/photos/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/post/createPost",
                                        "/post/{id}/editPost",
                                        "/post/followPosts",
                                        "/profile/edit",
                                        "/profile/likedPosts",
                                        "/profile/{id}/author",
                                        "/profile/{id}/follower"
                                ).authenticated()
                                .requestMatchers(
                                        "/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,
                                        "/post",
                                        "/follow/{id}",
                                        "/post/{id}/status"
                                ).authenticated()
                                .requestMatchers(HttpMethod.POST,
                                        "/registration").permitAll()

                                .requestMatchers(HttpMethod.PATCH,
                                        "/post/{id}",
                                        "profile/edit").authenticated()
                                .requestMatchers(HttpMethod.DELETE,
                                        "/post/{id}",
                                        "/profile/delete"
                                ).authenticated()

                                .anyRequest().authenticated()
//                        формочки все равно возвращает, даже без авторизации

                ).rememberMe((remember) -> remember
//              куки устанавливаются, после дропа сервера не слетают, key обязателен
                                .alwaysRemember(true)
                                .tokenValiditySeconds(60 * 60 * 24 * 365)
                                .key("mySecret")
                ).formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/post", true)
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

}
