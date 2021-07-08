package com.adidas.test.email.webcontroller.config;

import com.adidas.test.pubservice.common.security.JwtAuthenticationFilter;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(final HttpSecurity http) throws Exception {

    http.cors().configurationSource(corsConfigurationSource());
    http.csrf().disable();
    http.headers().frameOptions().disable();

    http.authorizeRequests()
        //.expressionHandler(new CustomWebSecurityExpressionHandler())
        .antMatchers(
            "/v3/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        // .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(this.authenticationTokenFilterBean(), BasicAuthenticationFilter.class);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    //configuration.setAllowedMethods(ImmutableList.of("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowSemicolon(true);
    return firewall;
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
    web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
  }

  @Bean
  public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
    return new JwtAuthenticationFilter();
  }

}
