package com.epam.bookstore.user.unit;

import com.epam.bookstore.configuration.JwtConfig;
import com.epam.bookstore.dto.User;
import com.epam.bookstore.dto.UserRepresentation;
import com.epam.bookstore.jwt.JwtService;
import com.epam.bookstore.service.ServiceException;
import com.epam.bookstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String GET_USER_BY_TOKEN_ENDPOINT = "/v1/user";
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;
    private static final String LOGIN_ENDPOINT = "/v1/user/login";

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @Test
    public void getUser_whenCallMethodAndHaveTokenCookie_thenReceiveUser() throws Exception {
        final String tokenName = jwtConfig.getTokenName();

        User testUser = new User();
        testUser.setLogin("valid");
        testUser.setPassword("valid");

        UserRepresentation userRepresentation = new UserRepresentation(testUser);

        String token = jwtService.generateToken(new TestingAuthenticationToken(testUser, null));

        Cookie validUserToken = new Cookie(tokenName, token);

        given(userService.getUserByUsername(testUser.getUsername())).willReturn(userRepresentation);

        mockMvc.perform(get(GET_USER_BY_TOKEN_ENDPOINT)
                .cookie(validUserToken))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())));

        verify(userService, times(WANTED_NUMBER_OF_INVOCATIONS)).getUserByUsername(testUser.getUsername());
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void login_whenSendCorrectUserData_thenReceiveCookieAndUser() throws Exception {
        final String VALID_LOGIN = "Test user";
        final String VALID_PASSWORD = "test password";
        final String tokenName = jwtConfig.getTokenName();


        User validUser = new User();
        validUser.setLogin(VALID_LOGIN);
        validUser.setPassword(VALID_PASSWORD);

        UserRepresentation userRepresentation = new UserRepresentation(validUser);

        Authentication testAuthentication = new TestingAuthenticationToken(validUser, null);

        given(userService.authenticateUser(validUser)).willReturn(testAuthentication);
        given(userService.getUserByUsername(validUser.getUsername())).willReturn(userRepresentation);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser))
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andExpect(cookie().exists(tokenName))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(validUser.getUsername())));

        verify(userService, times(WANTED_NUMBER_OF_INVOCATIONS)).authenticateUser(validUser);
        verify(userService, times(WANTED_NUMBER_OF_INVOCATIONS)).getUserByUsername(validUser.getUsername());
        verifyNoMoreInteractions(userService);

    }

    @Test
    //TODO HAVE ALMOST SAME TEST
    public void login_whenSendBadCredentials_thenReceiveUnauthorisedStatusCode() throws Exception {
        final String INVALID_LOGIN = "invalid";
        final String INVALID_PASSWORD = "invalid";

        User invalidUser = new User();
        invalidUser.setLogin(INVALID_LOGIN);
        invalidUser.setPassword(INVALID_PASSWORD);

        given(userService.authenticateUser(invalidUser)).willThrow(new ServiceException("Invalid user", new BadCredentialsException("")));

        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


}
