DROP TABLE IF EXISTS books cascade;

CREATE TABLE books
(
    book_id     SERIAL PRIMARY KEY,
    book_name   VARCHAR(100) UNIQUE NOT NULL,
    book_author VARCHAR(100)        NOT NULL,
    rate        INT,
    preview_img VARCHAR(100)        NOT NULL
);

INSERT INTO books (book_name, book_author, rate, preview_img) VALUES
('Test book 1', 'Test author', 5, 'img/test_img_1.png'),
('Test book 2', 'Test author', 4, 'img/test_img_2.png');



