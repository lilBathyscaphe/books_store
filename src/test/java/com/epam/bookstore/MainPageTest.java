package com.epam.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
public class MainPageTest {

    private static final String ROOT_ENDPOINT = "/";
    private static final String FORWARD_INDEX_VIEW = "forward:index.html";
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void forwardToIndexPage() throws Exception {
        this.mockMvc.perform(get(ROOT_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(FORWARD_INDEX_VIEW));
    }

}
