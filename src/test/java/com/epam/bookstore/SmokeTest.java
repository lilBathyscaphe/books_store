package com.epam.bookstore;


import com.epam.bookstore.controller.BookController;
import com.epam.bookstore.controller.HistoryController;
import com.epam.bookstore.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class SmokeTest {

    @Autowired
    private UserController userController;
    @Autowired
    private BookController bookController;
    @Autowired
    private HistoryController historyController;

    @Test
    public void contextLoadsUserController() throws Exception {
        assertThat(userController).isNotNull();
    }

    @Test
    public void contextLoadsBookController() throws Exception {
        assertThat(bookController).isNotNull();
    }

    @Test
    public void contextLoadsHistoryController() throws Exception {
        assertThat(historyController).isNotNull();
    }
}

