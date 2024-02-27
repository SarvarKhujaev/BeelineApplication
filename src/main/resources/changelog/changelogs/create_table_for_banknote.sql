CREATE TABLE IF NOT EXISTS banknote (
    name TEXT NOT NULL,
    amount FLOAT NOT NULL,
    updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ( name, updated_date )
);