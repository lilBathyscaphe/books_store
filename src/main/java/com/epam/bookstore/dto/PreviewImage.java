package com.epam.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preview_images")
public class PreviewImage {

    @Id
    @GeneratedValue
    @Column(name = "preview_id")
    private Long id;

    @Lob
    @Column(name="image")
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] image;

    @Column(name="preview_type")
    @Enumerated(EnumType.STRING)
    private PreviewImgType type;

}
