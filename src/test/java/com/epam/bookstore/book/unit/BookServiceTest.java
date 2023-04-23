package com.epam.bookstore.book.unit;


import com.epam.bookstore.dto.Book;
import com.epam.bookstore.repostiory.BookRepository;
import com.epam.bookstore.service.BookService;
import com.epam.bookstore.service.ServiceException;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Log4j2
public class BookServiceTest {

    private static final int WANTED_NUMBER_OF_INVOCATIONS = 1;

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @Test
    public void update_whenCallUpdate_thenInvokeUpdateBookRepositoryMethod() {
        Book book = new Book();

        try {
            bookService.updateBook(book);
        } catch (ServiceException e) {
            log.error("Exception during test: ",e);
        }

        verify(bookRepository, times(WANTED_NUMBER_OF_INVOCATIONS)).save(book);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void getBookById_whenCallGetBookById_thenInvokeFindByIdRepositoryMethod() {
        Integer id = 1;

        Book resultBook = new Book();
        resultBook.setId(id);

        Optional<Book> resultOptional = Optional.of(resultBook);

        given(this.bookRepository.findById(id)).willReturn(resultOptional);

        try {
            bookService.getBookById(id);
        } catch (ServiceException e) {
            log.error("Exception during test: ",e);
        }

        verify(bookRepository, times(WANTED_NUMBER_OF_INVOCATIONS)).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void createBook_whenCallCreateBook_thenInvokeCreateBookRepositoryMethod() {
        Book testBook = new Book();
        testBook.setId(1);

        given(bookRepository.save(testBook)).willReturn(testBook);

        try {
            bookService.createBook(testBook);
        } catch (ServiceException e) {
            log.error("Exception during test: ",e);
        }

        verify(bookRepository, times(2)).save(testBook);
        verifyNoMoreInteractions(bookRepository);
    }

}
