package com.epam.bookstore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@Data
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer id;
    @Column(name = "event_type")
    private String eventType;
    @Column(name = "search_query")
    private String searchQuery;
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    //https://www.baeldung.com/jpa-join-column
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    //https://www.baeldung.com/jpa-join-column
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
