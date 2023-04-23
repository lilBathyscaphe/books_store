package com.epam.bookstore.book.integrational;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.epam.bookstore.dto.Book;
//import org.junit.Before;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("Test")
@TestPropertySource("/application_test.properties")
@PropertySource("classpath:endpoints_test.properties")
@Sql(value = {"/bookstore_user_before.sql", "/book_list/book_list_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/book_list/book_list_after.sql", "/bookstore_user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerIntegrationTest {

    private static final String GET_ALL_BOOKS = "";
    private static final String SEARCH_STRING_PARAMETER_NAME = "searchQuery";
    private static final String FILTER_PARAMETER_NAME = "filterType";
    private static final String BOOKS_ENDPOINT = "/v1/books";
    private static final String MULTIPART_IMAGE_FILE_PART_NAME = "imageFile";
    private static final String SPECIFIC_BOOK_ENDPOINT = "/v1/books/{bookId}";
    private static final int BOOK_NEW_RATE = 1;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Book FIRST_TEST_BOOK = new Book();
    private static final Book SECOND_TEST_BOOK = new Book();


    @BeforeAll
    public static void init() {
        FIRST_TEST_BOOK.setId(1);
        FIRST_TEST_BOOK.setName("Test book 1");
        FIRST_TEST_BOOK.setAuthor("Test author");
        FIRST_TEST_BOOK.setRate(5);
        FIRST_TEST_BOOK.setPreviewImg("img/test_img_1.png");

        SECOND_TEST_BOOK.setId(2);
        SECOND_TEST_BOOK.setName("Test book 2");
        SECOND_TEST_BOOK.setAuthor("Test author");
        SECOND_TEST_BOOK.setRate(4);
        SECOND_TEST_BOOK.setPreviewImg("img/test_img_2.png");
    }

    @Test
    //https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication
    public void getBooks_whenSendEmptyBookFilter_thenReceiveAllBooks() throws Exception {
        final int RETURNED_BOOKS_SIZE = 2;

        mockMvc.perform(get(BOOKS_ENDPOINT)
                .param(SEARCH_STRING_PARAMETER_NAME, GET_ALL_BOOKS)
                .param(FILTER_PARAMETER_NAME, GET_ALL_BOOKS))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(RETURNED_BOOKS_SIZE)))
                .andExpect(jsonPath("$[0].id", is(FIRST_TEST_BOOK.getId())))
                .andExpect(jsonPath("$[0].name", is(FIRST_TEST_BOOK.getName())))
                .andExpect(jsonPath("$[0].author", is(FIRST_TEST_BOOK.getAuthor())))
                .andExpect(jsonPath("$[0].rate", is(FIRST_TEST_BOOK.getRate())))
                .andExpect(jsonPath("$[0].previewImg", is(FIRST_TEST_BOOK.getPreviewImg())))
                .andExpect(jsonPath("$[1].id", is(SECOND_TEST_BOOK.getId())))
                .andExpect(jsonPath("$[1].name", is(SECOND_TEST_BOOK.getName())))
                .andExpect(jsonPath("$[1].author", is(SECOND_TEST_BOOK.getAuthor())))
                .andExpect(jsonPath("$[1].rate", is(SECOND_TEST_BOOK.getRate())))
                .andExpect(jsonPath("$[1].previewImg", is(SECOND_TEST_BOOK.getPreviewImg())));
    }

    @Test
    //https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication
    public void getBooks_whenSendMostPopularBookFilter_thenReceiveBooksWithRateFive() throws Exception {

        final int EXPECTED_SIZE = 1;

        final String GET_ALL_BOOKS_WITH_RATE_5 = "MOST_POPULAR";

        mockMvc.perform(get(BOOKS_ENDPOINT)
                .param(SEARCH_STRING_PARAMETER_NAME, GET_ALL_BOOKS)
                .param(FILTER_PARAMETER_NAME, GET_ALL_BOOKS_WITH_RATE_5))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].id", is(FIRST_TEST_BOOK.getId())))
                .andExpect(jsonPath("$[0].rate", is(FIRST_TEST_BOOK.getRate())));

    }

    @Test
    //https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication
    public void getBooks_whenSendBookFilterWithBookName_thenReceiveBook() throws Exception {
        final int EXPECTED_SIZE = 1;

        mockMvc.perform(get(BOOKS_ENDPOINT)
                .param(SEARCH_STRING_PARAMETER_NAME, FIRST_TEST_BOOK.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
                .andExpect(jsonPath("$[0].id", is(FIRST_TEST_BOOK.getId())))
                .andExpect(jsonPath("$[0].name", is(FIRST_TEST_BOOK.getName())));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    //https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication
    public void createBook_whenSendValidBook_thenReceiveBookWithId() throws Exception {
        final int NEW_BOOK_ID = 3;

        Book testBook = new Book();
        testBook.setName("New Test book");
        testBook.setAuthor("new test author");
        testBook.setPreviewImg("/previews_images/testImage");

        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "image.png",
                null, "Dummy".getBytes());

        byte[] decodedImageBytes = Base64.getEncoder().encode(imageFile.getBytes());


        String jsonBook = objectMapper.writeValueAsString(testBook);

        MockMultipartFile book = new MockMultipartFile(
                "book", "book", MediaType.APPLICATION_JSON_VALUE, jsonBook.getBytes());

        mockMvc.perform(multipart(BOOKS_ENDPOINT)
                .file(MULTIPART_IMAGE_FILE_PART_NAME,decodedImageBytes)
                .file(book)
                .with(csrf()))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(NEW_BOOK_ID)))
                .andExpect(jsonPath("$.name", is(testBook.getName())));
    }


    @Test
    //https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication
    public void getBookById_whenBookIdIsPresent_thenReceiveBook() throws Exception {

        mockMvc.perform(get(SPECIFIC_BOOK_ENDPOINT, FIRST_TEST_BOOK.getId())
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(FIRST_TEST_BOOK.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_TEST_BOOK.getName())));
    }


    @Test
    //https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication
    public void updateBook_whenBookIsPresent_thenReceiveUpdatedBook() throws Exception {

        Book testBook = new Book();
        testBook.setId(FIRST_TEST_BOOK.getId());
        testBook.setName(FIRST_TEST_BOOK.getName());
        testBook.setAuthor(FIRST_TEST_BOOK.getAuthor());
        testBook.setPreviewImg(FIRST_TEST_BOOK.getPreviewImg());
        testBook.setRate(BOOK_NEW_RATE);

        String jsonBook = objectMapper.writeValueAsString(testBook);

        mockMvc.perform(put(SPECIFIC_BOOK_ENDPOINT, FIRST_TEST_BOOK.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook)
                .with(csrf()))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(FIRST_TEST_BOOK.getId())))
                .andExpect(jsonPath("$.rate", is(testBook.getRate())));

    }


}
