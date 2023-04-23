package com.epam.bookstore.history.unit;


import com.epam.bookstore.TestConfig;
import com.epam.bookstore.configuration.JwtConfig;
import com.epam.bookstore.configuration.PasswordConfig;
import com.epam.bookstore.controller.BookController;
import com.epam.bookstore.controller.HistoryController;
import com.epam.bookstore.dto.History;
import com.epam.bookstore.finder.CookieFinder;
import com.epam.bookstore.handler.CookieRemoveLogoutHandler;
import com.epam.bookstore.jwt.JwtService;
import com.epam.bookstore.service.HistoryService;
import com.epam.bookstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoryController.class)
@Import(TestConfig.class)
//@PropertySource("classpath:endpoints_test.properties")
@TestPropertySource(locations = "classpath:application_test.properties")

@WithMockUser               //Mock authentication to not use DB stored user
public class HistoryControllerTest {


    private static final History FIRST_HISTORY_MESSAGE = new History();
    private static final History SECOND_HISTORY_MESSAGE = new History();
    private static final String HISTORY_ENDPOINT = "/v1/history";
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;

    @BeforeAll
    public static void init(){
        FIRST_HISTORY_MESSAGE.setId(1);
        FIRST_HISTORY_MESSAGE.setEventDate(LocalDateTime.now());
        FIRST_HISTORY_MESSAGE.setEventType("test_event");

        SECOND_HISTORY_MESSAGE.setId(2);
        SECOND_HISTORY_MESSAGE.setEventDate(LocalDateTime.now());
        SECOND_HISTORY_MESSAGE.setEventType("test_event");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HistoryService historyService;

    @Test
    public void getHistory_whenCallGetHistory_thenReturnHistoryPage() throws Exception {
        final String HISTORY_FILTER_PAGE_PARAM = "number";
        final String HISTORY_FILTER_PAGE_SIZE_PARAM = "size";
        final String PAGE_VALUE = "0";
        final String PAGE_SIZE_VALUE = "2";

        final int EXPECTED_SIZE = 2;

        Page<History> page = new PageImpl(Arrays.asList(FIRST_HISTORY_MESSAGE, SECOND_HISTORY_MESSAGE));

        Pageable pageable = PageRequest.of(0,2);

        given(historyService.getHistory(pageable)).willReturn(page);

        mockMvc.perform(get(HISTORY_ENDPOINT)
                    .param(HISTORY_FILTER_PAGE_SIZE_PARAM, PAGE_SIZE_VALUE)
                    .param(HISTORY_FILTER_PAGE_PARAM, PAGE_VALUE)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(EXPECTED_SIZE)));

        verify(historyService, times(WANTED_NUMBER_OF_INVOCATIONS)).getHistory(pageable);
        verifyNoMoreInteractions(historyService);

    }


    @Test
    public void addToHistory_whenCallAddToHistoryEndpoint_thenReturnCreatedHistory() throws Exception {

        given(historyService.addToHistory(FIRST_HISTORY_MESSAGE)).willReturn(FIRST_HISTORY_MESSAGE);

        mockMvc.perform(post(HISTORY_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(FIRST_HISTORY_MESSAGE))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(FIRST_HISTORY_MESSAGE.getId())));

        verify(historyService, times(WANTED_NUMBER_OF_INVOCATIONS)).addToHistory(FIRST_HISTORY_MESSAGE);
        verifyNoMoreInteractions(historyService);

    }

}
