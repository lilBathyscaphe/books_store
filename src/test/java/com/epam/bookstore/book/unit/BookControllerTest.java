package com.epam.bookstore.book.unit;


import com.epam.bookstore.TestConfig;
import com.epam.bookstore.configuration.JwtConfig;
import com.epam.bookstore.configuration.PasswordConfig;
import com.epam.bookstore.controller.BookController;
import com.epam.bookstore.dto.Book;
import com.epam.bookstore.finder.CookieFinder;
import com.epam.bookstore.handler.CookieRemoveLogoutHandler;
import com.epam.bookstore.jwt.JwtService;
import com.epam.bookstore.service.BookService;
import com.epam.bookstore.dto.filter.BookFilter;
import com.epam.bookstore.service.FileSystemStorageService;
import com.epam.bookstore.service.ServiceException;
import com.epam.bookstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {BookController.class, ObjectMapper.class})
//@SpringBootTest
@AutoConfigureMockMvc

//@WebMvcTest(BookController.class)   //Annotation used for, configuring Spring container with only needed beans for MVC parts. Contains MockMvc instance.
//@Import(TestConfig.class)

//@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class
//})
@WithMockUser               //Mock authentication to not user DB stored user
//@PropertySource("classpath:endpoints_test.properties")
@TestPropertySource(locations = "classpath:application_test.properties")
public class BookControllerTest {

//    @TestConfiguration
//    static class EmployeeServiceImplTestContextConfiguration {
//
//        @Bean
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder(10);
//        }
//    }
// TODO: 19.10.2021 Try with IMPORT(TestConfig)
    private static final String ALL_BOOKS_VALUE = "";
    private static final String SEARCH_STRING_PARAMETER_NAME = "searchQuery";
    private static final String FILTER_PARAMETER_NAME = "filterType";
    private static final String EXCEPTION_TEST_MESSAGE = "Exception Test Message";

    private static final MockMultipartFile IMAGE_FILE = new MockMultipartFile(
            "imageFile", "image.png", null, "Dummy".getBytes());

    private static final String DIR_FOLDER = "books_previews";

    private static final Book FIRST_TEST_BOOK = new Book();
    private static final Book SECOND_TEST_BOOK = new Book();
    private static final int CORRECT_NUMBER_OF_CALLS = 1;



    @BeforeAll
    public static void init(){
        FIRST_TEST_BOOK.setId(1);
        FIRST_TEST_BOOK.setName("Test book 1");
        FIRST_TEST_BOOK.setAuthor("Test author");
        FIRST_TEST_BOOK.setRate(5);
        FIRST_TEST_BOOK.setPreviewImg(DIR_FOLDER.concat("/test_img_1.png"));

        SECOND_TEST_BOOK.setId(2);
        SECOND_TEST_BOOK.setName("Test book 2");
        SECOND_TEST_BOOK.setAuthor("Test author");
        SECOND_TEST_BOOK.setRate(4);
        SECOND_TEST_BOOK.setPreviewImg(DIR_FOLDER.concat("/test_img_2.png"));
    }



//    @Autowired
//    private WebApplicationContext ctx;


    @MockBean
    private BookService bookService;

    @MockBean
    private FileSystemStorageService storageService;

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    BookController bookController;

//    @BeforeEach
//    public void setUp() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
//    }

    @Autowired
    private ObjectMapper objectMapper;



    @Value("${url.endpoint.books}")
    private String booksEndpoint;
    @Value("${url.endpoint.book}")
    private String SPECIFIC_BOOK_ENDPOINT;


    @Test
    public void getBooks_whenCallGetBooksEndpoint_thenReturnBookList() throws Exception {

        final int EXPECTED_SIZE = 2;

        BookFilter testFilter = new BookFilter();
        testFilter.setSearchQuery(ALL_BOOKS_VALUE);
        testFilter.setFilterType(null);

        given(bookService.getBooks(testFilter)).willReturn(Arrays.asList(FIRST_TEST_BOOK, SECOND_TEST_BOOK));

        mockMvc.perform(get(booksEndpoint)
                .param(SEARCH_STRING_PARAMETER_NAME, ALL_BOOKS_VALUE)
                .param(FILTER_PARAMETER_NAME, ALL_BOOKS_VALUE))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)));

        verify(bookService, times(CORRECT_NUMBER_OF_CALLS)).getBooks(testFilter);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void create_whenCallCreateEndpoint_thenReturnCreatedBook() throws Exception {
        String jsonBook = objectMapper.writeValueAsString(FIRST_TEST_BOOK);
        MockMultipartFile book = new MockMultipartFile(
                "book", "book", MediaType.APPLICATION_JSON_VALUE, jsonBook.getBytes());

        given(bookService.createBook(FIRST_TEST_BOOK)).willReturn(FIRST_TEST_BOOK);

        mockMvc.perform(multipart(booksEndpoint)
                .file(IMAGE_FILE)
                .file(book)
                .with(csrf()))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(FIRST_TEST_BOOK.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_TEST_BOOK.getName())));

        verify(bookService, times(CORRECT_NUMBER_OF_CALLS)).createBook(FIRST_TEST_BOOK);
        verify(storageService, times(CORRECT_NUMBER_OF_CALLS)).store(IMAGE_FILE, FIRST_TEST_BOOK);
        verifyNoMoreInteractions(bookService);
        verifyNoMoreInteractions(storageService);
    }



    @Test
    public void getBookById_whenCallGetBookByIdEndpoint_thenReturnBook() throws Exception {
        given(bookService.getBookById(FIRST_TEST_BOOK.getId())).willReturn(FIRST_TEST_BOOK);

        mockMvc.perform(get(SPECIFIC_BOOK_ENDPOINT, FIRST_TEST_BOOK.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(FIRST_TEST_BOOK.getId())));

        verify(bookService, times(CORRECT_NUMBER_OF_CALLS)).getBookById(FIRST_TEST_BOOK.getId());
        verifyNoMoreInteractions(bookService);
    }




    @Test
    public void update_whenCallUpdateEndpoint_thenReturnUpdatedBook() throws Exception {

        given(bookService.updateBook(FIRST_TEST_BOOK)).willReturn(FIRST_TEST_BOOK);

        mockMvc.perform(put(SPECIFIC_BOOK_ENDPOINT, FIRST_TEST_BOOK.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(FIRST_TEST_BOOK))
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bookService, times(CORRECT_NUMBER_OF_CALLS)).updateBook(FIRST_TEST_BOOK);
        verifyNoMoreInteractions(bookService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void create_whenCallCreateEndpointWithExistingBook_thenReturnConflictStatus() throws Exception {

        given(bookService.createBook(FIRST_TEST_BOOK)).willThrow(
                new ServiceException(EXCEPTION_TEST_MESSAGE, new DataIntegrityViolationException("")));

        String jsonBook = objectMapper.writeValueAsString(FIRST_TEST_BOOK);
        MockMultipartFile book = new MockMultipartFile(
                "book", "book", MediaType.APPLICATION_JSON_VALUE, jsonBook.getBytes());
        mockMvc.perform(multipart(booksEndpoint)
                    .file(IMAGE_FILE)
                    .file(book)
                    .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is(EXCEPTION_TEST_MESSAGE)));
    }

    @Test
    public void update_whenCallUpdateEndpointWithNotExistingBook_thenReturnNotFoundStatus() throws Exception {
        given(bookService.updateBook(new Book())).willThrow(
                new ServiceException(EXCEPTION_TEST_MESSAGE,new NoSuchElementException()));

        mockMvc.perform(put(SPECIFIC_BOOK_ENDPOINT, FIRST_TEST_BOOK.getId())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(new Book()))
                    .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is(EXCEPTION_TEST_MESSAGE)));
    }

    @Test
    public void getBookById_whenCallGetBookByIdEndpointWithNotExistingId_thenReturnNotFoundStatus() throws Exception {
        final int NOT_EXISTING_ID = 100;

        given(bookService.getBookById(NOT_EXISTING_ID)).willThrow(
                new ServiceException(EXCEPTION_TEST_MESSAGE, new NoSuchElementException()));

        mockMvc.perform(get(SPECIFIC_BOOK_ENDPOINT, NOT_EXISTING_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is(EXCEPTION_TEST_MESSAGE)));

    }

}
