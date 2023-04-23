package com.epam.bookstore.service;

import com.epam.bookstore.dto.History;
import com.epam.bookstore.repostiory.HistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class HistoryService {

    private static final String WRONG_FORMAT_MESSAGE = "error.history.wrongFormat";
    private HistoryRepository historyRepository;
    private ResourceBundleMessageSource messageResourceBundle;

    @Autowired
    public HistoryService(HistoryRepository historyRepository, ResourceBundleMessageSource messageResourceBundle) {
        this.historyRepository = historyRepository;
        this.messageResourceBundle = messageResourceBundle;
    }

    public Page<History> getHistory(Pageable page) {
        log.debug("Assembled page request: {}", page);
        Page<History> historyPage = historyRepository.findAll(page);
        log.debug("Response history Page: {}", historyPage);
        return historyPage;
    }

    public History addToHistory(History history) throws ServiceException {
        History addedToHistory = null;
        try {
            addedToHistory = historyRepository.save(history);
            log.debug("Added HistoryBean: {}", addedToHistory);
        } catch (DataIntegrityViolationException e) {
            log.error("Cannot save historyBean: {}", history);
            throw new ServiceException(
                    messageResourceBundle.getMessage(WRONG_FORMAT_MESSAGE, null, Locale.US), e);
        }
        return addedToHistory;
    }

}
