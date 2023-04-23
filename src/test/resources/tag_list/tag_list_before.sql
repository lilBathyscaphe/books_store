DROP TABLE IF EXISTS tags cascade;

CREATE TABLE tags(
                     tag_id SERIAL PRIMARY KEY,
                     tag_name VARCHAR(100) NOT NULL UNIQUE,
                     bullet_color VARCHAR(100) NOT NULL
);
INSERT INTO tags (tag_name, bullet_color) VALUES
('Test tag 1', 'test color'),
('Test tag 2', 'test color');
