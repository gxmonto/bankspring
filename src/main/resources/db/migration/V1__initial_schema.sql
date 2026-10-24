-- Esquema limpio de e-Bank. Reemplaza al dump original (docs/legacy/db_bank_management.sql),
-- que tenia tablas duplicadas (creditcard / credit_card, cardpayment / card_payment)
-- y columnas que no coincidian con las entidades.

CREATE TABLE people (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255),
    surname  VARCHAR(255),
    address  VARCHAR(255),
    contact  VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_people_username UNIQUE (username)
);

CREATE TABLE administrator (
    admin_id BIGINT NOT NULL,
    PRIMARY KEY (admin_id),
    CONSTRAINT fk_administrator_people FOREIGN KEY (admin_id) REFERENCES people (id)
);

CREATE TABLE bank (
    bank_id   BIGINT NOT NULL AUTO_INCREMENT,
    bank_name VARCHAR(255),
    address   VARCHAR(255),
    PRIMARY KEY (bank_id)
);

CREATE TABLE customer (
    customer_id    BIGINT         NOT NULL AUTO_INCREMENT,
    account_number VARCHAR(20)    NOT NULL,
    account_type   VARCHAR(255),
    balance        DECIMAL(15, 2) NOT NULL DEFAULT 0,
    people_id      BIGINT,
    PRIMARY KEY (customer_id),
    CONSTRAINT uk_customer_account_number UNIQUE (account_number),
    CONSTRAINT fk_customer_people FOREIGN KEY (people_id) REFERENCES people (id)
);

CREATE TABLE transaction (
    transaction_id BIGINT         NOT NULL AUTO_INCREMENT,
    type           VARCHAR(20),
    amount         DECIMAL(15, 2),
    balance_after  DECIMAL(15, 2),
    description    VARCHAR(255),
    date           DATETIME(6),
    customer_id    BIGINT,
    PRIMARY KEY (transaction_id),
    CONSTRAINT fk_transaction_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);
CREATE INDEX idx_transaction_customer_date ON transaction (customer_id, date);

CREATE TABLE atm (
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    atm_code     VARCHAR(20) NOT NULL,
    location     VARCHAR(255),
    api_key_hash VARCHAR(64) NOT NULL,
    active       BIT(1)      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_atm_code UNIQUE (atm_code)
);

CREATE TABLE withdrawal_code (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    code_hash   VARCHAR(64) NOT NULL,
    code_hint   VARCHAR(2)  NOT NULL,
    amount      DECIMAL(15, 2),
    status      VARCHAR(20),
    created_at  DATETIME(6),
    expires_at  DATETIME(6),
    redeemed_at DATETIME(6),
    atm_code    VARCHAR(255),
    customer_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT uk_withdrawal_code_hash UNIQUE (code_hash),
    CONSTRAINT fk_withdrawal_code_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
);
CREATE INDEX idx_withdrawal_code_customer ON withdrawal_code (customer_id, status);

CREATE TABLE bankloan (
    loan_id       BIGINT NOT NULL AUTO_INCREMENT,
    amount        DECIMAL(15, 2),
    interest_rate DECIMAL(5, 2),
    duration      BIGINT NOT NULL,
    customer_id   BIGINT,
    bank_id       BIGINT,
    PRIMARY KEY (loan_id),
    CONSTRAINT fk_bankloan_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_bankloan_bank FOREIGN KEY (bank_id) REFERENCES bank (bank_id)
);

CREATE TABLE loanpayment (
    payment_id BIGINT NOT NULL AUTO_INCREMENT,
    amount     DECIMAL(15, 2),
    date       DATE,
    loan_id    BIGINT,
    PRIMARY KEY (payment_id),
    CONSTRAINT fk_loanpayment_loan FOREIGN KEY (loan_id) REFERENCES bankloan (loan_id)
);

CREATE TABLE credit_card (
    card_id      BIGINT NOT NULL AUTO_INCREMENT,
    credit_limit DECIMAL(15, 2),
    annual_fee   DECIMAL(15, 2),
    customer_id  BIGINT,
    bank_id      BIGINT,
    PRIMARY KEY (card_id),
    CONSTRAINT fk_credit_card_customer FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_credit_card_bank FOREIGN KEY (bank_id) REFERENCES bank (bank_id)
);

CREATE TABLE card_payment (
    payment_id BIGINT NOT NULL AUTO_INCREMENT,
    amount     DECIMAL(15, 2),
    date       DATETIME(6),
    card_id    BIGINT,
    PRIMARY KEY (payment_id),
    CONSTRAINT fk_card_payment_card FOREIGN KEY (card_id) REFERENCES credit_card (card_id)
);
