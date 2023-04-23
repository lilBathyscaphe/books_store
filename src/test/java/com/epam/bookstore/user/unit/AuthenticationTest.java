package com.epam.bookstore.user.unit;

import com.epam.bookstore.configuration.JwtConfig;
import com.epam.bookstore.controller.BookController;
import com.epam.bookstore.controller.UserController;
import com.epam.bookstore.dto.User;
import com.epam.bookstore.dto.UserRepresentation;
import com.epam.bookstore.service.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {


    private static final String LOGIN_ENDPOINT = "/v1/user/login";
    private static final String LOGOUT_ENDPOINT = "/v1/user/logout";
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookController bookController;
    @MockBean
    private UserController userController;


    @Test
    public void addressResources_whenNotAuthorized_thenReceiveUnauthorisedStatus() throws Exception {
        final String BOOK_RESOURCE_URL = "/v1/books";

        mockMvc.perform(get(BOOK_RESOURCE_URL))
                .andDo(print())
                .andExpect(status().is4xxClientError());


        verify(bookController, never()).getBooks(null);
    }

    @Test
    public void login_whenSendCorrectUserData_thenReceiveAuthorisedStatusAndCookie() throws Exception {
        final String VALID_USERNAME = "Test user";
        final String VALID_PASSWORD = "test password";
        final String TOKEN_COOKIE_DUMMY = "token=test";

        User validUser = new User();
        validUser.setLogin(VALID_USERNAME);
        validUser.setPassword(VALID_PASSWORD);

        UserRepresentation userRepresentation = new UserRepresentation(validUser);

        ResponseEntity<UserRepresentation> response = ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, TOKEN_COOKIE_DUMMY)
                .body(userRepresentation);

        given(userController.login(validUser)).willReturn(response);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validUser))
                    .with(csrf())
                 )
                .andExpect(status().isOk())
                .andExpect(cookie().exists(jwtConfig.getTokenName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username",is(validUser.getUsername())));

        verify(userController, times(WANTED_NUMBER_OF_INVOCATIONS)).login(validUser);
        verifyNoMoreInteractions(userController);
    }

    @Test
    //TODO HAVE ALMOST SAME TEST
    public void login_whenSendBadCredentials_thenReceiveUnauthorisedStatusCode() throws Exception {
        final String INVALID_LOGIN = "invalid";
        final String INVALID_PASSWORD = "invalid";

        User invalidUser = new User();
        invalidUser.setLogin(INVALID_LOGIN);
        invalidUser.setPassword(INVALID_PASSWORD);

        given(userController.login(invalidUser)).willThrow(new ServiceException("Invalid user", new BadCredentialsException("")));

        mockMvc.perform(post(LOGIN_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void logout_whenSendLogoutRequest_thenRemoveTokenCookie() throws Exception {
        final int COOKIE_REMOVE_AGE = 0;
        String tokenName = jwtConfig.getTokenName();
        Cookie tokenCookie = new Cookie(tokenName,"testValue");

        mockMvc.perform(post(LOGOUT_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(tokenCookie)
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge(tokenName, COOKIE_REMOVE_AGE));
    }



}
