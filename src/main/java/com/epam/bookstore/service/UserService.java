package com.epam.bookstore.service;

import com.epam.bookstore.dto.User;
import com.epam.bookstore.dto.UserRepresentation;
import com.epam.bookstore.repostiory.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private static final String WRONG_AUTHORIZATION_DATA = "error.user.wrongAuthorizationData";
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private ResourceBundleMessageSource messageResourceBundle;


    @Autowired
    @Lazy   //https://blog.actorsfit.com/a?ID=00800-0c91ddd9-ed32-4cb1-a400-8b2fe173805b
    public UserService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       ResourceBundleMessageSource messageResourceBundle) {
        this.userRepository = userRepository;
        this.messageResourceBundle = messageResourceBundle;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageResourceBundle.getMessage(WRONG_AUTHORIZATION_DATA, null, Locale.US)));
    }

    public UserRepresentation getUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) loadUserByUsername(username);
        log.debug("Founded User: {}", user.getUsername());
        return new UserRepresentation(user);
    }


    public Authentication authenticateUser(User user) throws ServiceException {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            log.debug("Authentication: {}", authentication.isAuthenticated());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("Exception during authorization", e.getMessage());
            throw new ServiceException(
                    messageResourceBundle.getMessage(WRONG_AUTHORIZATION_DATA, null, Locale.US), e);
        }
        return authentication;
    }


}
