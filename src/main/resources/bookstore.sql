DROP TABLE IF EXISTS tags, books, books_tags, history, bookstore_user_roles, bookstore_role, bookstore_user CASCADE;

CREATE TABLE tags(
	tag_id SERIAL PRIMARY KEY, 
	tag_name VARCHAR(100) NOT NULL UNIQUE,
	bullet_color VARCHAR(100) NOT NULL
);

INSERT INTO tags (tag_name, bullet_color) VALUES ('Must Read Titles', '#210786');
INSERT INTO tags (tag_name, bullet_color) VALUES ('Best Of List', '#278b3f');
INSERT INTO tags (tag_name, bullet_color) VALUES ('Classic Novels', '#6882e2');


CREATE TABLE books(
	book_id SERIAL PRIMARY KEY,
	book_name VARCHAR(100) UNIQUE NOT NULL,
	book_author VARCHAR(100) NOT NULL,
	rate INT NOT NULL DEFAULT 0,
	preview_img VARCHAR(100) 
);

INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Jewel on Nizam', 'Greta Devil', 5, 'books_previews/1_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Jamies Kitchen', 'Jamie Oliver', 4, 'books_previews/2_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Inexpensive Family Meals', 'Simon Holst', 4, 'books_previews/3_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Paleo Slow Cooking', 'Chrissy Gower', 5, 'books_previews/4_book_preview.png');
INSERT INTO books (book_name, book_author, rate, preview_img) VALUES ('Cook Like an Italian', 'Tobie Puttock', 3, 'books_previews/5_book_preview.png');


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

INSERT INTO history (event_type, search_query, tag_id, book_id) VALUES 
('search_book', 'history check', null, null),
('search_book', 'history check2', null, null),
('search_book', 'history check3', null, null),
('search_book', 'history check4', null, null),
('search_book', 'history check5', null, null);


CREATE TABLE bookstore_user (
	user_id SERIAL PRIMARY KEY,
	login VARCHAR(100) UNIQUE NOT NULL,
	firstname VARCHAR(100) NOT NULL,
	lastname VARCHAR(100) NOT NULL,
	pswrd VARCHAR(100) NOT NULL
);


INSERT INTO bookstore_user (login,firstname, lastname, pswrd) VALUES ('zxc', 'Admin', 'Adminov', '$2a$10$SK/s6WfjxSM619PSz45KiObWbUHncYP3idqqyAUgEG7I0TBYcaohS');
INSERT INTO bookstore_user (login,firstname, lastname, pswrd) VALUES ('zzz', 'Slepy', 'Head', '$2a$10$SK/s6WfjxSM619PSz45KiObWbUHncYP3idqqyAUgEG7I0TBYcaohS');

CREATE TABLE bookstore_role (
	role_id SERIAL PRIMARY KEY,
	role_name VARCHAR(100) NOT NULL 
);

INSERT INTO bookstore_role (role_name) values ('ROLE_USER');
INSERT INTO bookstore_role (role_name) values ('ROLE_ADMIN');


CREATE TABLE bookstore_user_roles (
	user_id INT NOT NULL,
	role_id INT NOT NULL,
	foreign key (role_id) references bookstore_role,
	foreign key (user_id) references bookstore_user
);

INSERT INTO bookstore_user_roles (user_id, role_id) VALUES 
(1,1),
(1,2),
(2,1);





