package com.epam.bookstore.dto.filter;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BookFilter {

    private String searchQuery;

    private FilterType filterType;
}
