-- Run this in MySQL Workbench (or mysql CLI) before running the Java app

CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role ENUM('STUDENT', 'LIBRARIAN') NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE books (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    total_copies INT NOT NULL,
    available_copies INT NOT NULL
);

CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_isbn VARCHAR(20) NOT NULL,
    user_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    fine DOUBLE DEFAULT 0,
    FOREIGN KEY (book_isbn) REFERENCES books(isbn),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Sample data to test with
INSERT INTO users (name, role, password) VALUES
('Nuer', 'STUDENT', 'pass123'),
('Admin', 'LIBRARIAN', 'admin123');

INSERT INTO books (isbn, title, author, total_copies, available_copies) VALUES
('978-0134685991', 'Effective Java', 'Joshua Bloch', 3, 3),
('978-0596007126', 'Head First Design Patterns', 'Freeman & Robson', 2, 2),
('978-1491910774', 'Designing Data-Intensive Applications', 'Martin Kleppmann', 1, 1);
commit;
