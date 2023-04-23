INSERT INTO tags (tag_name, bullet_color) VALUES ('Must Read Titles', '#210786');
INSERT INTO tags (tag_name, bullet_color) VALUES ('Best Of List', '#278b3f');
INSERT INTO tags (tag_name, bullet_color) VALUES ('Classic Novels', '#6882e2');

INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Jewel on Nizam', 'Greta Devil', 5, 'books_previews/1_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Jamies Kitchen', 'Jamie Oliver', 4, 'books_previews/2_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Inexpensive Family Meals', 'Simon Holst', 4, 'books_previews/3_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Paleo Slow Cooking', 'Chrissy Gower', 5, 'books_previews/4_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Cook Like an Italian', 'Tobie Puttock', 3, 'books_previews/5_book_preview.png');

INSERT INTO history (event_type, search_query, tag_id, book_id) VALUES
                                                                    ('search_book', 'history check', null, null),
                                                                    ('search_book', 'history check2', null, null),
                                                                    ('search_book', 'history check3', null, null),
                                                                    ('search_book', 'history check4', null, null),
                                                                    ('search_book', 'history check5', null, null);

INSERT INTO bookstore_user (login,firstname, lastname, pswrd) VALUES ('zxc', 'Admin', 'Adminov', '$2a$10$SK/s6WfjxSM619PSz45KiObWbUHncYP3idqqyAUgEG7I0TBYcaohS');
INSERT INTO bookstore_user (login,firstname, lastname, pswrd) VALUES ('zzz', 'Slepy', 'Head', '$2a$10$SK/s6WfjxSM619PSz45KiObWbUHncYP3idqqyAUgEG7I0TBYcaohS');

INSERT INTO bookstore_role (role_name) values ('ROLE_USER');
INSERT INTO bookstore_role (role_name) values ('ROLE_ADMIN');

INSERT INTO bookstore_user_roles (user_id, role_id) VALUES
                                                        (1,1),
                                                        (1,2),
                                                        (2,1);





