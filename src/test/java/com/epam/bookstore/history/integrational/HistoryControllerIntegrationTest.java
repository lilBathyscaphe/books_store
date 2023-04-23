package com.epam.bookstore.history.integrational;


import com.epam.bookstore.dto.History;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("Test")
@TestPropertySource("/application_test.properties")
@Sql(value = {"/bookstore_user_before.sql", "/history_list/history_list_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/history_list/history_list_after.sql", "/bookstore_user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class HistoryControllerIntegrationTest {


    private static final String CONTROLLER_URL = "/v1/history";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private static final History FIRST_HISTORY_MESSAGE = new History();
    private static final History SECOND_HISTORY_MESSAGE = new History();

    @BeforeAll
    public static void init() {
        FIRST_HISTORY_MESSAGE.setId(1);
        FIRST_HISTORY_MESSAGE.setEventDate(LocalDateTime.now());
        FIRST_HISTORY_MESSAGE.setEventType("search_book");
        FIRST_HISTORY_MESSAGE.setSearchQuery("history test");

        SECOND_HISTORY_MESSAGE.setId(2);
        SECOND_HISTORY_MESSAGE.setEventDate(LocalDateTime.now());
        SECOND_HISTORY_MESSAGE.setEventType("search_book");
        SECOND_HISTORY_MESSAGE.setSearchQuery("history test 2");
    }

    @Test
    public void getHistory_whenSendHistoryFilter_thenReceiveHistoryPage() throws Exception {
        final String HISTORY_FILTER_PAGE_PARAM = "page";
        final String HISTORY_FILTER_PAGE_SIZE_PARAM = "pageSize";
        final String HISTORY_FILTER_SORTING_PARAM = "sort";
        final String PAGE_VALUE = "0";
        final String PAGE_SIZE_VALUE = "2";
        final String PAGE_SORTING_VALUE = "id,DESC";

        final int RETURNED_HISTORY_SIZE = 2;


        mockMvc.perform(get(CONTROLLER_URL)
                .param(HISTORY_FILTER_PAGE_PARAM, PAGE_VALUE)
                .param(HISTORY_FILTER_PAGE_SIZE_PARAM, PAGE_SIZE_VALUE)
                .param(HISTORY_FILTER_SORTING_PARAM, PAGE_SORTING_VALUE))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(RETURNED_HISTORY_SIZE)))
                .andExpect(jsonPath("$.content[0].id", is(SECOND_HISTORY_MESSAGE.getId())))
                .andExpect(jsonPath("$.content[0].eventType", is(SECOND_HISTORY_MESSAGE.getEventType())))
                .andExpect(jsonPath("$.content[0].searchQuery", is(SECOND_HISTORY_MESSAGE.getSearchQuery())))
                .andExpect(jsonPath("$.content[1].id", is(FIRST_HISTORY_MESSAGE.getId())))
                .andExpect(jsonPath("$.content[1].eventType", is(FIRST_HISTORY_MESSAGE.getEventType())))
                .andExpect(jsonPath("$.content[1].searchQuery", is(FIRST_HISTORY_MESSAGE.getSearchQuery())));
    }

    @Test
    public void addToHistory_whenSendHistory_thenReceiveCreatedHistory() throws Exception {
        final int EXPECTED_ID = 3;

        History newHistory = new History();
        newHistory.setEventType("search_book");
        newHistory.setSearchQuery("Add to history");
        newHistory.setEventDate(LocalDateTime.now());

        mockMvc.perform(post(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newHistory))
                .with(csrf()))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID)))
                .andExpect(jsonPath("$.eventType", is(newHistory.getEventType())))
                .andExpect(jsonPath("$.searchQuery", is(newHistory.getSearchQuery())));
    }


}
