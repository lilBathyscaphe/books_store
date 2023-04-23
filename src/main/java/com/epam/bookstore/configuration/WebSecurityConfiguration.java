package com.epam.bookstore.configuration;

import com.epam.bookstore.dto.User;
import com.epam.bookstore.finder.CookieFinder;
import com.epam.bookstore.jwt.JwtService;
import com.epam.bookstore.jwt.JwtVerifierFilter;
import com.epam.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.context.annotation.RequestScope;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
//https://stackoverflow.com/questions/31186826/spring-security-method-security-annotation-secured-is-not-working-java-con
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final PasswordEncoder passwordEncoder;
    private final UserService userDetailsService;
//    private final AccessDeniedHandler accessDeniedHandler;
    private final LogoutHandler cookieRemoveLogoutHandler;

    private final CookieFinder cookieFinder;

    private final JwtService tokenService;

    private static final String ROOT_URI = "/";
    private static final String LOGIN_URI = "/v1/user/login";
    private static final String LOGOUT_URI = "/v1/user/logout";

    private static final String PREVIEWS_FOLDER = "/books_previews/**";     //To prevent filter triggering on static

    private final String[] whitelist = {
            ROOT_URI,
            "index",
            LOGIN_URI,          //This url has to be in WebSecurity configuration to refer URL without CSRF.
                                //Otherwise client need to send multiple request (first to get CSRF, then actually login)
            PREVIEWS_FOLDER,
            "/css/**",
            "/js/**",
            "/fonts/**",
            "/img/**"
    };

    @Autowired
    public WebSecurityConfiguration(PasswordEncoder passwordEncoder,
                                    UserService userDetailsService,
//                                    AccessDeniedHandler accessDeniedHandler,
                                    LogoutHandler cookieRemoveLogoutHandler,
                                    CookieFinder cookieFinder,
                                    JwtService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
//        this.accessDeniedHandler = accessDeniedHandler;
        this.cookieRemoveLogoutHandler = cookieRemoveLogoutHandler;
        this.cookieFinder = cookieFinder;
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(whitelist)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(
                        new JwtVerifierFilter(usernameBean(), cookieFinder, tokenService),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .and()
                .logout()
                .logoutUrl(LOGOUT_URI)
                .addLogoutHandler(cookieRemoveLogoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .clearAuthentication(true);

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    //Filter ignore URI. Used to not filter static files and make available main page.
    //https://stackoverflow.com/questions/22767205/spring-security-exclude-url-patterns-in-security-annotation-configurartion
    //web security PURPOSE
    //https://stackoverflow.com/questions/56388865/spring-security-configuration-httpsecurity-vs-websecurity
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(whitelist);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @RequestScope
    public User usernameBean() {
        return new User();
    }




}
