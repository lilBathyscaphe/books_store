package com.epam.bookstore;

import com.epam.bookstore.controller.BookController;
import com.epam.bookstore.dto.Book;
import com.epam.bookstore.dto.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser               //Mock authentication to not user DB stored user
public class CSRFTest {
    private static final String BOOKS_ENDPOINT = "/v1/books";
    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookController bookController;

    @Autowired
    private ObjectMapper objectMapper;

    private static final MockMultipartFile IMAGE_FILE = new MockMultipartFile(
            "imageFile", "image.png", null, "Dummy".getBytes());

    @Test
    public void givenNoCsrf_whenAddTag_thenGetForbidden() throws Exception {
        mockMvc.perform(
                post(BOOKS_ENDPOINT))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenCsrf_whenAddFoo_thenCreated() throws Exception {
        Book book = new Book();

        String jsonBook = objectMapper.writeValueAsString(book);
        MockMultipartFile bookFile = new MockMultipartFile(
                "book", "book", MediaType.APPLICATION_JSON_VALUE, jsonBook.getBytes());

        given(bookController.createBook(IMAGE_FILE, book)).willReturn(book);


        mockMvc.perform(multipart(BOOKS_ENDPOINT)
                    .file(IMAGE_FILE)
                    .file(bookFile)
                    .with(csrf()))
                .andExpect(status().isOk());

        verify(bookController, times(WANTED_NUMBER_OF_INVOCATIONS)).createBook(IMAGE_FILE, book);

    }
}
