DROP TABLE IF EXISTS tags, books, books_tags, history, bookstore_user_roles, bookstore_role, bookstore_user CASCADE;

CREATE TABLE tags(
                     tag_id SERIAL PRIMARY KEY,
                     tag_name VARCHAR(100) NOT NULL UNIQUE,
                     bullet_color VARCHAR(100) NOT NULL
);

CREATE TABLE books(
                      book_id SERIAL PRIMARY KEY,
                      book_name VARCHAR(100) UNIQUE NOT NULL,
                      book_author VARCHAR(100) NOT NULL,
                      rate INT NOT NULL DEFAULT 0,
                      preview_img VARCHAR(100)
);

CREATE TABLE books_tags(
                           book_id INT REFERENCES books(book_id) ON DELETE CASCADE,
                           tag_id INT REFERENCES tags(tag_id) ON DELETE CASCADE
);


CREATE TABLE history (
                         history_id SERIAL PRIMARY KEY,
                         event_type VARCHAR(100) NOT NULL,
                         search_query VARCHAR(100),
                         tag_id INT,
                         book_id INT,
                         event_date TIMESTAMP NOT NULL DEFAULT current_timestamp,
                         foreign key (book_id) references books,
                         foreign key (tag_id) references tags
);

CREATE TABLE bookstore_user (
                                user_id SERIAL PRIMARY KEY,
                                login VARCHAR(100) UNIQUE NOT NULL,
                                firstname VARCHAR(100) NOT NULL,
                                lastname VARCHAR(100) NOT NULL,
                                pswrd VARCHAR(100) NOT NULL
);

CREATE TABLE bookstore_role (
                                role_id SERIAL PRIMARY KEY,
                                role_name VARCHAR(100) NOT NULL
);

CREATE TABLE bookstore_user_roles (
                                      user_id INT NOT NULL,
                                      role_id INT NOT NULL,
                                      foreign key (role_id) references bookstore_role,
                                      foreign key (user_id) references bookstore_user
);



