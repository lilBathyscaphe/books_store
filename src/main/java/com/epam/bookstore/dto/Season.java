package com.epam.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seasons")
public class Season {

    @Id
    @Column(name = "season_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "season_number")
    private int number;

    @Column(name = "season_name")
    private String name;

    @Lazy
    @OneToMany
    @JoinColumn(name="episode_id")
    private List<Episode> episodes;

}
