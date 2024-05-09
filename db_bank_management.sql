create database bank_management;
use bank_management;

-- Tabla People
CREATE TABLE People (
    id BIGINT PRIMARY KEY auto_increment not null,
    name VARCHAR(255),
    surname VARCHAR(255),
    address varchar(255),
    contact VARCHAR(255)
);

-- Tabla Customer
CREATE TABLE Customer (
    customer_Id BIGINT PRIMARY KEY auto_increment not null,
    account_Type VARCHAR(255),
    balance DECIMAL(10, 2), -- Agregado para llevar el saldo del cliente
    FOREIGN KEY (customer_Id) REFERENCES People(id)
);

-- Tabla Administrator
CREATE TABLE Administrator (
    admin_Id BIGINT PRIMARY KEY,
    FOREIGN KEY (admin_Id) REFERENCES People(id)
);

-- Tabla Bank
CREATE TABLE Bank (
    bank_Id BIGINT PRIMARY KEY auto_increment not null,
    bankName VARCHAR(255),
    address VARCHAR(255)
);

-- Tabla BankLoan
CREATE TABLE BankLoan (
    loan_Id BIGINT PRIMARY KEY auto_increment not null,
    amount DECIMAL(10, 2),
    interest_rate DECIMAL(5, 2),
    duration bigint,
    customer_Id BIGINT, -- Agregado para vincular el préstamo con un cliente
    bank_Id BIGINT,
    FOREIGN KEY (customer_Id) REFERENCES Customer(customer_Id),
    FOREIGN KEY (bank_Id) REFERENCES Bank(bank_Id)
);

-- Tabla CreditCard
CREATE TABLE CreditCard (
    card_Id BIGINT PRIMARY KEY auto_increment not null,
    creditLimit DECIMAL(10, 2),
    annualFee DECIMAL(10, 2),
    customer_Id BIGINT, -- Agregado para vincular la tarjeta de crédito con un cliente
    bank_Id BIGINT,
    FOREIGN KEY (customer_Id) REFERENCES Customer(customer_Id),
    FOREIGN KEY (bank_Id) REFERENCES Bank(bank_Id)
);

-- Tabla Transaction
CREATE TABLE Transaction (
    transaction_Id BIGINT PRIMARY KEY auto_increment not null NOT NULL,
    type ENUM('deposit', 'withdrawal', 'payment'),
    amount DECIMAL(10, 2),
    date DATE,
    customer_Id BIGINT,
    FOREIGN KEY (customer_Id) REFERENCES Customer(customer_Id)
);

-- Tabla LoanPayment
CREATE TABLE LoanPayment (
    payment_Id BIGINT PRIMARY KEY auto_increment not null,
    amount DECIMAL(10, 2),
    date DATE,
    loan_Id BIGINT,
    FOREIGN KEY (loan_Id) REFERENCES BankLoan(loan_Id)
);

-- Tabla CardPayment
CREATE TABLE CardPayment (
    payment_Id BIGINT PRIMARY KEY auto_increment not null,
    amount DECIMAL(10, 2),
    date DATE,
    card_Id BIGINT,
    FOREIGN KEY (card_Id) REFERENCES CreditCard(card_Id)
);