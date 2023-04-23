package com.epam.bookstore.user.unit;

import com.epam.bookstore.dto.User;
import com.epam.bookstore.repostiory.UserRepository;
import com.epam.bookstore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
public class UserServiceTest {

    private static final String TEST_USERNAME = "Test user";
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser               //Mock authentication to not user DB stored user
    public void loadUserByUsername_whenCallMethod_thenInvokeFindByUsernameRepositoryMethod() {
        final Optional<User> expectedResult = Optional.of(new User());

        given(userRepository.findByLogin(TEST_USERNAME)).willReturn(expectedResult);

        userService.loadUserByUsername(TEST_USERNAME);

        verify(userRepository, times(WANTED_NUMBER_OF_INVOCATIONS)).findByLogin(TEST_USERNAME);
        verifyNoMoreInteractions(userRepository);
    }

}
