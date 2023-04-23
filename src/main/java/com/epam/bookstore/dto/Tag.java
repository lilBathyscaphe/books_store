package com.epam.bookstore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer id;
    @Column(name = "tag_name")
    private String name;
    @Column(name = "bullet_color")
    private String bulletColor;

    //cannot use @RequiredArgsConstructor, because in some cases id can be null
    // and Tag(Integer id) constructor will still need
    public Tag(Integer id) {
        this.id = id;
    }
}
