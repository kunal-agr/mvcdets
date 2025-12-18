CREATE DATABASE mvcdets;

CREATE TABLE tbluser (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tblexpense (
    expense_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    expense_date DATE NOT NULL,
    amount NUMERIC(10,2) NOT NULL,
    category VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES tbluser(user_id)
        ON DELETE CASCADE
);
