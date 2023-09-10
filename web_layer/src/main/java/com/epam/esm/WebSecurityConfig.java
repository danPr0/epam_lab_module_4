package com.epam.esm;

import com.epam.esm.security.exception_handler.AuthEntryPointJwt;
import com.epam.esm.security.exception_handler.CustomAccessDeniedHandler;
import com.epam.esm.security.filter.AuthTokenFilter;
import com.epam.esm.security.oauth2.CustomOAuth2UserService;
import com.epam.esm.security.oauth2.CustomOidcUserService;
import com.epam.esm.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.epam.esm.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

@SpringBootApplication
@PropertySource(value = "classpath:/application-web.yaml", factory = WebSecurityConfig.YamlPropertySourceFactory.class)
@PropertySource(value = "classpath:/application-web-${spring.profiles.active}.yaml", factory =
        WebSecurityConfig.YamlPropertySourceFactory.class)
public class WebSecurityConfig {

    private final UserService             userService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomOidcUserService   oidcUserService;
    private final PasswordEncoder         passwordEncoder;

    private final AuthTokenFilter                    authTokenFilter;
    private final AuthEntryPointJwt                  authEntryPointJwt;
    private final CustomAccessDeniedHandler          accessDeniedHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2FailureHandler;

//    private final AWSXRayServletFilter awsxRayServletFilter = new AWSXRayServletFilter(SegmentNamingStrategy.dynamic("Scorekeep"));

    @Autowired
    public WebSecurityConfig(
            UserService userService, CustomOAuth2UserService oAuth2UserService, CustomOidcUserService oidcUserService,
            PasswordEncoder passwordEncoder, AuthTokenFilter authTokenFilter, AuthEntryPointJwt authEntryPointJwt,
            CustomAccessDeniedHandler accessDeniedHandler, OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler,
            OAuth2AuthenticationFailureHandler oAuth2FailureHandler) {

        this.userService       = userService;
        this.oAuth2UserService = oAuth2UserService;
        this.oidcUserService   = oidcUserService;
        this.passwordEncoder   = passwordEncoder;

        this.authTokenFilter      = authTokenFilter;
        this.authEntryPointJwt    = authEntryPointJwt;
        this.accessDeniedHandler  = accessDeniedHandler;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                    .addFilterAfter(awsxRayServletFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(authEntryPointJwt).and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                    .authorizeHttpRequests()
                    .requestMatchers("/auth/**", "/oauth2/**").anonymous()
                    .requestMatchers(HttpMethod.GET, "/gift-certificates").permitAll()
                    .requestMatchers(HttpMethod.GET, "/gift-certificates/*").permitAll()
                    .requestMatchers("/reset-password", "/confirm-password-reset", "/error").permitAll()
                    .requestMatchers("/change-password", "/logout", "/check-authentication").authenticated()
                    .requestMatchers(HttpMethod.POST, "/orders").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/orders*").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().hasRole("ADMIN").and()
                    .oauth2Login()
                    .authorizationEndpoint().baseUri("/oauth2/sign-in").and()
                    .redirectionEndpoint().baseUri("/oauth2/sign-in/callback/*").and()
                    .userInfoEndpoint()
                    .userService(oAuth2UserService)
                    .oidcUserService(oidcUserService).and()
                    .successHandler(oAuth2SuccessHandler)
                    .failureHandler(oAuth2FailureHandler).and()
                    .httpBasic().disable()
                    .logout().disable()
                    .cors().and()
                    .csrf().disable();

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

/*    @Bean
    public AWSXRayServletFilter tracingFilter() {
        return new AWSXRayServletFilter(SegmentNamingStrategy.dynamic("Scorekeep"));
    }*/

    public static class YamlPropertySourceFactory implements PropertySourceFactory {

        @Override
        public org.springframework.core.env.PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(encodedResource.getResource());

            Properties properties = factory.getObject();

            return new PropertiesPropertySource(Objects.requireNonNull(encodedResource.getResource().getFilename()),
                    Objects.requireNonNull(properties));
        }
    }
}
