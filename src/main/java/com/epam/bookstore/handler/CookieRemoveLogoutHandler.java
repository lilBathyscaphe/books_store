package com.epam.bookstore.handler;

import com.epam.bookstore.finder.CookieFinder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//https://stackoverflow.com/questions/15946132/springsecurity-fail-to-delete-jsessionid
@Log4j2
@Component
public class CookieRemoveLogoutHandler implements LogoutHandler {

    private static final int COOKIE_REMOVE_AGE = 0;

    @Value("${server.servlet.context-path}")
    private String applicationContextPath;

    private final CookieFinder cookieFinder;

    @Autowired
    public CookieRemoveLogoutHandler(CookieFinder cookieFinder) {
        this.cookieFinder = cookieFinder;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        Cookie[] cookies = httpServletRequest.getCookies();
        log.debug("Cookies size: {}", cookies.length);

        Cookie jwtCookie = cookieFinder.findTokenCookie(cookies);

        jwtCookie.setPath(applicationContextPath);
        jwtCookie.setMaxAge(COOKIE_REMOVE_AGE);
        httpServletResponse.addCookie(jwtCookie);
    }
}
