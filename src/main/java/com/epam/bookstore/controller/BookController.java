package com.epam.bookstore.controller;

import com.epam.bookstore.dto.Book;
import com.epam.bookstore.dto.filter.BookFilter;
import com.epam.bookstore.service.BookService;
import com.epam.bookstore.service.FileSystemStorageService;
import com.epam.bookstore.service.ServiceException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("v1/books")
@Slf4j
public class BookController {

    private final BookService bookService;
    private final FileSystemStorageService storageService;

    @Autowired
    public BookController(BookService bookService,
                          FileSystemStorageService storageService) {
        this.bookService = bookService;
        this.storageService = storageService;
    }

    @GetMapping
    public List<Book> getBooks(BookFilter bookFilter) {
        log.debug("GET getBooks(). Received bookFilter {}", bookFilter);
        return bookService.getBooks(bookFilter);
    }


    //https://www.baeldung.com/sprint-boot-multipart-requests
    //https://stackoverflow.com/questions/16648549/converting-file-to-multipartfile
    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping
    public Book createBook(@RequestPart("imageFile") MultipartFile imageFile,
                           @RequestPart("book") Book book) throws IOException {
        log.debug("Parsed book {}", book);
        log.debug("Parsed file {}", imageFile);
        log.debug(imageFile.getContentType());
        log.debug(imageFile.getName());
        Book createdBook = bookService.createBook(book);

        storageService.store(imageFile, createdBook);
        log.debug("Created book: {}", createdBook);
        return createdBook;
    }

    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable Integer bookId) throws ServiceException {
        log.debug("GET getBookById(). Received ID {}", bookId);
        return bookService.getBookById(bookId);
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@RequestBody Book book) throws ServiceException {
        log.debug("PUT updateBook(). Received book {}", book);
        return bookService.updateBook(book);
    }

}
