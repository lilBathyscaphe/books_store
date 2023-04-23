package com.epam.bookstore.controller;

import com.epam.bookstore.dto.History;
import com.epam.bookstore.service.HistoryService;
import com.epam.bookstore.service.ServiceException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("v1/history")
@Slf4j
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }


    @GetMapping
    public Page<History> getHistory(Pageable page) {
        log.debug("GET getHistory(). Received filter: {}", page);
        return historyService.getHistory(page);
    }


    //Сделана облегченная реализация. В идеале, для поддержания транзакционнасти, сообщения истории должен генерировать back-end.
    //В данном проекте, сообщения истории создает клиент.
    @PostMapping
    public History addToHistory(@RequestBody History history) throws ServiceException {
        log.debug("POT addToHistory(). Received history bean: {}", history);
        return historyService.addToHistory(history);
    }

}
