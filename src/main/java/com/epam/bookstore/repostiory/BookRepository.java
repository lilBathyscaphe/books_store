package com.epam.bookstore.repostiory;

import com.epam.bookstore.dto.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
