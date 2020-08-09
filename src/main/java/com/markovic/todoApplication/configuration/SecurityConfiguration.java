package com.markovic.todoApplication.configuration;

import com.markovic.todoApplication.constant.SecurityConstant;
import com.markovic.todoApplication.filter.JWTAuthorizationFilter;
import com.markovic.todoApplication.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    // TODO This might be a problem, he uses a constructor on video 35 and specifically says which qualifier the userdetailservice is.
//    @Autowired
//    @Qualifier("userDetailService")
//    private UserDetailsService userDetailsService;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: 7/27/2020 I am not using the two extra classes for custom exception, he defines them here too
        // Disabling csrf
        // We are allowing stateless management -> meaning we dont track who stays logged in, we only care about when they request to log in
        // We permit requests that are from the permitted public urls we defined
        // Any other request will be authenticated
        // Defining to use our filter.
        // TODO: 8/9/2020 FIXXXX THE PERMITALL public urls doesnt work. even though i am accessing /v1/user/register it throws forbidden
        // TODO: 8/9/2020 If i comment the two last lines if this (authentication) then it works.
        http.csrf().disable().cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // We need to define this extra bean
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
}
