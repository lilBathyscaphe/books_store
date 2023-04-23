DROP TABLE IF EXISTS bookstore_user, bookstore_role, bookstore_user_roles CASCADE;

CREATE TABLE bookstore_user (
                                user_id SERIAL PRIMARY KEY,
                                login VARCHAR(100) UNIQUE NOT NULL,
                                firstname VARCHAR(100) NOT NULL,
                                lastname VARCHAR(100) NOT NULL,
                                pswrd VARCHAR(100) NOT NULL
);


INSERT INTO bookstore_user (login,firstname, lastname, pswrd) VALUES
('Test Admin', 'Test Admin', 'Adminovich', '$2a$10$SK/s6WfjxSM619PSz45KiObWbUHncYP3idqqyAUgEG7I0TBYcaohS'),
('Test', 'Test', 'Testovich', '$2a$10$SK/s6WfjxSM619PSz45KiObWbUHncYP3idqqyAUgEG7I0TBYcaohS');


CREATE TABLE bookstore_role (
            role_id SERIAL PRIMARY KEY,
            role_name VARCHAR(100) NOT NULL
);

INSERT INTO bookstore_role (role_name) values
('ROLE_USER'),
('ROLE_ADMIN');


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