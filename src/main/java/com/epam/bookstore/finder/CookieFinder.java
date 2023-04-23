package com.epam.bookstore.finder;

import com.epam.bookstore.configuration.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Slf4j
@Component
public class CookieFinder {

    private final JwtConfig jwtConfig;

    @Autowired
    public CookieFinder(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public Cookie findCookie(Cookie[] cookies, String cookieName) {
        Cookie foundedCookie = null;
        log.debug("Find cookie: {}", cookieName);
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                String receivedCookieName = cookie.getName();
                log.debug("Cookie name: {}", receivedCookieName);
                if (receivedCookieName.equals(cookieName)) {
                    foundedCookie = cookie;
                    break;
                }

            }
        }
        return foundedCookie;
    }

    public Cookie findTokenCookie(Cookie[] cookies) {
        return findCookie(cookies, jwtConfig.getTokenName());
    }

}
