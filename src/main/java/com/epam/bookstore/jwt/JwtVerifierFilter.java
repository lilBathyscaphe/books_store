package com.epam.bookstore.jwt;

import org.springframework.util.StringUtils;
import com.epam.bookstore.dto.User;
import com.epam.bookstore.finder.CookieFinder;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtVerifierFilter extends OncePerRequestFilter {

    private static final String LOGIN_LOGGER_PLACEHOLDER = "login";

    private final User userBean;
    private final CookieFinder cookieFinder;

    private final JwtService tokenService;

    public JwtVerifierFilter(User userBean, CookieFinder cookieFinder, JwtService tokenService) {
        this.userBean = userBean;
        this.cookieFinder = cookieFinder;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] userCookies = httpServletRequest.getCookies();
        Cookie tokenCookie = cookieFinder.findTokenCookie(userCookies);
        if (tokenCookie != null && StringUtils.hasLength(tokenCookie.getValue())) {
            Claims body = tokenService.getClaims(tokenCookie.getValue());

            String login = body.getSubject();
            log.debug("Username from token: {}", login);

            Authentication authentication = tokenService.getAuthentication(tokenCookie.getValue());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            putLoginInLoggerContext(login);
            fillUserBean(login);
        }


        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private void fillUserBean(String login) {
        userBean.setLogin(login);
        log.debug("User bean {}", userBean);
    }

    private void putLoginInLoggerContext(String login) {
        ThreadContext.put(LOGIN_LOGGER_PLACEHOLDER, login);
    }


}
