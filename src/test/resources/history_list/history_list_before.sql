DROP TABLE IF EXISTS history cascade;

CREATE TABLE history (
                         history_id SERIAL PRIMARY KEY,
                         event_type VARCHAR(100) NOT NULL,
                         search_query VARCHAR(100),
                         tag_id INT,
                         book_id INT,
                         event_date TIMESTAMP NOT NULL DEFAULT current_timestamp
);

INSERT INTO history (event_type, search_query, tag_id, book_id) VALUES
('search_book', 'history test', null, null),
('search_book', 'history test 2', null, null);
