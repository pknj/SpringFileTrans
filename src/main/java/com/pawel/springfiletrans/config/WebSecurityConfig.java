package com.pawel.springfiletrans.config;

import com.pawel.springfiletrans.security.JwtAuthenticationEntryPoint;
import com.pawel.springfiletrans.security.JwtAuthorizationTokenFilter;
import com.pawel.springfiletrans.security.JwtTokenUtil;
import com.pawel.springfiletrans.security.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationEntryPoint unauthorizedHandler;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private JwtUserDetailsService jwtUserDetailsService;

  @Value("${jwt.header}")
  private String tokenHeader;

  @Value("${jwt.route.authentication.path}")
  private String authenticationPath;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(jwtUserDetailsService)
        .passwordEncoder(passwordEncoderBean());
  }

  @Bean
  public PasswordEncoder passwordEncoderBean() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    // Custom JWT based security filter
    JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(), jwtTokenUtil, tokenHeader);

    httpSecurity
        .cors().configurationSource(corsConfigurationSource()).and()

        // we don't need CSRF because our token is invulnerable
        .csrf().disable()

        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

        .authorizeRequests()

        .antMatchers("/admin/**").hasAnyRole("ADMIN")

        // Un-secure H2 Database
        .antMatchers("/h2-console/**/**").permitAll()

        .antMatchers("/auth/**").permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .anyRequest().denyAll()

        .and()


        // don't create session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
    ;


    // disable page caching
//    httpSecurity
//        .headers()
//        .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
//        .cacheControl()
//    ;
  }

//  @Override
//  public void configure(WebSecurity web) {
//    // AuthenticationTokenFilter will ignore the below paths
//    web
//        // To allow Pre-flight [OPTIONS] request from browser
//        .ignoring()
//        .antMatchers(HttpMethod.OPTIONS,"/**")
//
//        // allow anonymous resource requests
//        .and()
//        .ignoring()
//        .antMatchers(
//            HttpMethod.GET,
//            "/",
//            "/*.html",
//            "/favicon.ico",
//            "/**/*.html",
//            "/**/*.css",
//            "/**/*.js"
//        )
//
//        // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
//        //.and()
//        //.ignoring()
//        //.antMatchers("/h2-console/**/**")
//     ;
//  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.addAllowedOrigin("http://localhost:4200");
    configuration.addAllowedHeader("*");
    configuration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }


}