package com.epam.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "episodes")
public class Episode {

    @Id
    @Column(name = "episode_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "episode_name")
    private String name;

    @Column(name = "episode_number")
    private int episodeNumber;

    @Column(name = "description")
    private String descriptions;

    @Column(name = "episode_text", columnDefinition = "TEXT")
    private String episodeText;

    @Transient
    private List<String> words;

}
