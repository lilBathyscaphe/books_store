package com.epam.bookstore.service;

import com.epam.bookstore.dto.Book;
import com.epam.bookstore.dto.filter.BookFilter;
import com.epam.bookstore.dto.filter.FilterType;
import com.epam.bookstore.repostiory.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class BookService {

    private static final String NOT_FOUND_MESSAGE = "error.book.notFoundWithParam";
    private static final String NOT_FOUND_BY_ID_MESSAGE = "error.book.notFoundById";
    private static final String NOT_UNIQUE_NAME_MESSAGE = "error.book.notUniqueName";

    private static final String NAME_ATTRIBUTE = "name";
    private static final String AUTHOR_ATTRIBUTE = "author";
    private static final String RATE_ATTRIBUTE = "rate";
    private static final String ATTRIBUTE_ID = "id";
    private static final String LIKE_WILD_CARD = "%";
    private static final int MAX_RATE = 5;
    private static final String PREVIEW_IMAGE_PATH_FORMAT = "%s/%s";
    private static final String PREVIEW_IMAGE_FORMAT = "%d_book_preview.png";

    @Value("${application.storage.baseDir}")
    private String previewDir;

    private EntityManager manager;
    private BookRepository bookRepository;
    private ResourceBundleMessageSource messageResourceBundle;

    @Autowired
    public BookService(BookRepository bookRepository, EntityManager manager, ResourceBundleMessageSource messageResourceBundle) {
        this.manager = manager;
        this.bookRepository = bookRepository;
        this.messageResourceBundle = messageResourceBundle;
    }

    //https://www.baeldung.com/jpa-and-or-criteria-predicates
    public List<Book> getBooks(BookFilter bookFilter) {
        CriteriaQuery<Book> query = generateQuery(bookFilter);

        TypedQuery<Book> queryResult = manager.createQuery(query);
        log.debug("Assembled query {}", queryResult.unwrap(Query.class).getQueryString());
        List<Book> foundedBooks = queryResult.getResultList();
        log.debug("Founded books: {}", foundedBooks);
        return foundedBooks;
    }

    private CriteriaQuery<Book> generateQuery(BookFilter bookFilter) {
        CriteriaBuilder criteria = manager.getCriteriaBuilder();
        CriteriaQuery<Book> query = criteria.createQuery(Book.class);
        Root<Book> book = query.from(Book.class);
        String searchQuery = bookFilter.getSearchQuery();
        if (searchQuery == null) {
            searchQuery = LIKE_WILD_CARD;
        } else {
            searchQuery = LIKE_WILD_CARD
                    .concat(searchQuery.toUpperCase())
                    .concat(LIKE_WILD_CARD);
        }

        Predicate namePredicate = criteria.like(
                criteria.upper(book.get(NAME_ATTRIBUTE)), searchQuery);
        Predicate authorPredicate = criteria.like(
                criteria.upper(book.get(AUTHOR_ATTRIBUTE)), searchQuery);

        Predicate searchPredicate = criteria.or(namePredicate, authorPredicate);
        FilterType filter = bookFilter.getFilterType();
        if (FilterType.MOST_POPULAR.equals(filter)) {
            Predicate searchWithRate = criteria.and(searchPredicate, criteria.equal(book.get(RATE_ATTRIBUTE), MAX_RATE));
            query.where(searchWithRate);
        } else {
            query.where(searchPredicate);
        }
        query.orderBy(criteria.asc(book.get(ATTRIBUTE_ID)));
        return query;
    }


    //    @Transactional //https://www.baeldung.com/jpa-transaction-required-exception
    //https://thorben-janssen.com/criteria-updatedelete-easy-way-to/
    public Book updateBook(Book book) throws ServiceException {
        Book updatedBook = null;
        try {
            updatedBook = bookRepository.save(book);
            log.debug("Updated book: {}", updatedBook);
        } catch (NoSuchElementException e) {
            log.error("Book not found: {}", book);
            throw new ServiceException(
                    messageResourceBundle.getMessage(NOT_FOUND_MESSAGE, null, Locale.US), e);
        }
        return updatedBook;
    }


    public Book getBookById(Integer bookId) throws ServiceException {
        Book foundedBook = null;
        try {
            foundedBook = bookRepository.findById(bookId).orElseThrow(NoSuchElementException::new);
            log.debug("Founded book by id: {}", foundedBook);
        } catch (NoSuchElementException e) {
            log.error("Book not found by ID: {}", bookId);
            throw new ServiceException(
                    messageResourceBundle.getMessage(NOT_FOUND_BY_ID_MESSAGE, null, Locale.US), e);
        }
        return foundedBook;
    }

    public Book createBook(Book book) {
        Book savedBook = null;
        try {
            savedBook = bookRepository.save(book);
            log.debug("Saved book: {}", savedBook);
            savedBook.setPreviewImg(String.format(PREVIEW_IMAGE_PATH_FORMAT, previewDir, generatePreviewImageName(savedBook)));

            savedBook = updateBook(savedBook);
            log.debug("Result book: {}", savedBook);
        } catch (DataIntegrityViolationException e) {
            log.error("Book cannot be created: {}", book);
            throw new ServiceException(
                    messageResourceBundle.getMessage(NOT_UNIQUE_NAME_MESSAGE, null, Locale.US), e);
        }
        return savedBook;
    }

    private String generatePreviewImageName(Book book) {
        return String.format(PREVIEW_IMAGE_FORMAT, book.getId());
    }
}
