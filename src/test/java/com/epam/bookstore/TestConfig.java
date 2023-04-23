package com.epam.bookstore;

import com.epam.bookstore.configuration.JwtConfig;
import com.epam.bookstore.configuration.PasswordConfig;
import com.epam.bookstore.finder.CookieFinder;
import com.epam.bookstore.handler.CookieRemoveLogoutHandler;
import com.epam.bookstore.jwt.JwtService;
import com.epam.bookstore.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(value = {
        PasswordConfig.class,
        UserService.class,
        CookieRemoveLogoutHandler.class,
        CookieFinder.class,
        JwtConfig.class,
        JwtService.class
})
public class TestConfig {

}
