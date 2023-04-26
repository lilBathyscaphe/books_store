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
@Table(name = "serials")
public class Serial implements TaggedEntity {

    @Id
    @Column(name = "serial_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "serial_name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "preview_id")
    private PreviewImage previewImage;

    @Column(name = "rate")
    private Double rate;

    @OneToMany
    @JoinColumn(name = "season_id")
    private List<Season> seasons;

    @JoinTable(
            name = "serials_tags",
            joinColumns = @JoinColumn(name = "serial_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Tag> tags;


//    //TODO make ENUM with lambda for every site. Name probably most time will be changing
//    public String getUrlName() {
//        return this.name
//                .replaceAll("[.,']", "")
//                .replaceAll("[^A-Za-z0-9]", " ")
//                .trim()
//                .replaceAll(" +", "-")
//                .toLowerCase(Locale.ROOT);
//    }


}
