package com.epam.bookstore.controller;


import com.epam.bookstore.dto.User;
import com.epam.bookstore.dto.UserRepresentation;
import com.epam.bookstore.jwt.JwtService;
import com.epam.bookstore.service.ServiceException;
import com.epam.bookstore.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api
@RestController
@Slf4j
@RequestMapping("v1/user")
public class UserController {

    private static final String TOKEN_COOKIE_NAME = "token";

    @Value("${server.servlet.context-path}")
    private String applicationContext;

    @Resource(name = "usernameBean")
    private User userBean;


    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @GetMapping
    public UserRepresentation getUser() {
        log.debug("GET getUser(). User login: {}", userBean.getUsername());
        UserRepresentation user = userService.getUserByUsername(userBean.getUsername());
        log.debug("Founded User: {}", user);
        return user;
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) throws ServiceException {
        log.debug("POST login(). User to login: {}", user);

        Authentication authentication = userService.authenticateUser(user);

        String token = jwtService.generateToken(authentication);

        ResponseCookie jwtToken = ResponseCookie.from(TOKEN_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .path(applicationContext)
                .build();
        log.debug("User JWT token: {}", jwtToken);

        UserRepresentation representation = userService.getUserByUsername(user.getUsername());
        log.debug("Founded user {}", representation);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtToken.toString())
                .body(representation);
    }

}
