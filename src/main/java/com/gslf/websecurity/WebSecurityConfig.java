package com.gslf.websecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private UserDetailsService userDetailsService;

    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        auth.inMemoryAuthentication()
        .withUser("alice2017").password("12345678Aa").roles("ADMIN")
        .and()
        .withUser("gslf-instructor").password("12345678Aa").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
        http
        		.formLogin().loginPage("/login").permitAll()
        		.and()
        		.logout().permitAll()
        		.and()
                .authorizeRequests()
                    .antMatchers("/resources/**", "/css/**", "/js/**", "/images/**", "/registration", "/", "/forum", "/about-us", "/programs", "/partnership", "/fundraising", "/terms", "/confirm").permitAll()
                    .antMatchers("/forum/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

	@Bean
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
	    return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	}
    
}