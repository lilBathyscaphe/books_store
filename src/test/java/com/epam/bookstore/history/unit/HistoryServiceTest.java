package com.epam.bookstore.history.unit;


import com.epam.bookstore.dto.History;
import com.epam.bookstore.repostiory.HistoryRepository;
import com.epam.bookstore.service.HistoryService;
import com.epam.bookstore.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
public class HistoryServiceTest {

    private static final int CORRECT_NUMBER_OF_CALLS = 1;

    @Autowired
    private HistoryService historyService;

    @MockBean
    private HistoryRepository historyRepository;


    @Test
    public void getHistory_whenCallGetHistory_thenInvokeFindAllRepositoryMethodWithPageRequest() {

        final String SORTING_CRITERIA = "id";
        final int PAGE_VALUE = 0;
        final int PAGE_SIZE_VALUE = 10;

        Pageable pageable = PageRequest.of(PAGE_VALUE,PAGE_SIZE_VALUE,Sort.by(SORTING_CRITERIA).descending());

        historyService.getHistory(pageable);

        verify(historyRepository, times(CORRECT_NUMBER_OF_CALLS)).findAll(pageable);
        verifyNoMoreInteractions(historyRepository);
    }

    @Test
    public void addToHistory_whenCallAddToHistory_thenInvokeSaveRepositoryMethod() {
        History history = new History();

        try {
            historyService.addToHistory(history);
        } catch (ServiceException e) {
            log.error("Exception during test: ", e);
        }

        verify(historyRepository, times(CORRECT_NUMBER_OF_CALLS)).save(history);
        verifyNoMoreInteractions(historyRepository);
    }


}
