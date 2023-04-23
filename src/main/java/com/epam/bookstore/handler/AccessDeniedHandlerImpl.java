package com.epam.bookstore.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private static final String AUTHORITIES_ABSENTEE_MESSAGE_KEY = "error.user.authoritiesAbsentee";
    private final ResourceBundleMessageSource messageResourceBundle;

    @Autowired
    public AccessDeniedHandlerImpl(ResourceBundleMessageSource messageResourceBundle) {
        this.messageResourceBundle = messageResourceBundle;
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException {
        log.error("Authentication error for user: {}", e.getMessage());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
                messageResourceBundle.getMessage(AUTHORITIES_ABSENTEE_MESSAGE_KEY, null, Locale.US)
        );
    }
}
