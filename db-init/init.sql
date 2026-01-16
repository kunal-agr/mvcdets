CREATE TABLE IF NOT EXISTS tbluser (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile NUMERIC(10,0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tblexpense (
    expense_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
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
