package com.epam.bookstore.dto;

/**
 * For Spring JPA PreviewImage.class was set @Enumerated(EnumType.STRING).
 * It is mean that ENUM values must not be renamed or else all data structure in DB will be broken
 */
public enum PreviewImgType {

    BOOK,
    SERIAL

}
