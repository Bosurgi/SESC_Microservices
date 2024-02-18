CREATE TABLE module
(
    module_id   BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100)   NOT NULL,
    description TEXT           NOT NULL,
    fee         DECIMAL(10, 2) NOT NULL
);


INSERT INTO module (title, description, fee)
VALUES ('Computer Network Architecture',
        'This module will teach the basics of networking architecture and how to configure a small network.', 350.00),
       ('Database Management Systems',
        'This module covers the fundamental concepts of database management systems and SQL.', 600.00),
       ('Software Engineering',
        'This module focuses on software development methodologies, project management, and quality assurance.',
        800.00),
       ('Data Structures and Algorithms',
        'This module explores data structures such as arrays, linked lists, trees, and algorithms for sorting and searching.',
        450.00),
       ('Artificial Intelligence',
        'This module introduces techniques used in artificial intelligence, including machine learning, neural networks, and natural language processing.',
        900.00),
       ('Cybersecurity',
        'This module covers cybersecurity principles, threats, cryptography, and best practices for securing systems and networks.',
        700.00);