package com.epam.bookstore.controller;

import com.epam.bookstore.dto.Serial;
import com.epam.bookstore.dto.filter.UserFilter;
import com.epam.bookstore.service.SerialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/serials")
public class SerialController {

    private SerialService serialService;

    @Autowired
    public SerialController(SerialService serialService) {
        this.serialService = serialService;
    }

    @GetMapping
    public List<Serial> getSerials(UserFilter userFilter) {
        log.debug("GET getSerials(). Received userFilter {}", userFilter);
        return serialService.getSerials(userFilter);
    }

}
