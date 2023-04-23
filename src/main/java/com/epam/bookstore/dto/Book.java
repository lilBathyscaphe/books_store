package com.epam.bookstore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer id;

    @Column(name = "book_name")
    private String name;

    @Column(name = "book_author")
    private String author;

    @Column(name = "rate")
    private int rate;

    @Column(name = "preview_img")
    private String previewImg;


    //https://www.baeldung.com/jpa-many-to-many
    //https://stackoverflow.com/questions/5478328/in-which-case-do-you-use-the-jpa-jointable-annotation
    @JoinTable(
            name = "books_tags",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;

}
