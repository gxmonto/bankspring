create database bank_management;
use bank_management;

-- Tabla People
CREATE TABLE People (
    id INT PRIMARY KEY auto_increment,
    name VARCHAR(255),
    surname VARCHAR(255),
    address varchar(255),
    contact VARCHAR(255)
);

-- Tabla Customer
CREATE TABLE Customer (
    customerId INT PRIMARY KEY auto_increment,
    customerType VARCHAR(255),
    balance DECIMAL(10, 2), -- Agregado para llevar el saldo del cliente
    FOREIGN KEY (customerId) REFERENCES People(id)
);

-- Tabla Administrator
CREATE TABLE Administrator (
    adminId INT PRIMARY KEY,
    FOREIGN KEY (adminId) REFERENCES People(id)
);

-- Tabla Bank
CREATE TABLE Bank (
    bankId INT PRIMARY KEY auto_increment,
    bankName VARCHAR(255),
    address VARCHAR(255)
);

-- Tabla BankLoan
CREATE TABLE BankLoan (
    loanId INT PRIMARY KEY auto_increment,
    amount DECIMAL(10, 2),
    interestRate DECIMAL(5, 2),
    duration INT,
    customerId INT, -- Agregado para vincular el préstamo con un cliente
    bankId INT,
    FOREIGN KEY (customerId) REFERENCES Customer(customerId),
    FOREIGN KEY (bankId) REFERENCES Bank(bankId)
);

-- Tabla CreditCard
CREATE TABLE CreditCard (
    cardId INT PRIMARY KEY auto_increment,
    creditLimit DECIMAL(10, 2),
    annualFee DECIMAL(10, 2),
    customerId INT, -- Agregado para vincular la tarjeta de crédito con un cliente
    bankId INT,
    FOREIGN KEY (customerId) REFERENCES Customer(customerId),
    FOREIGN KEY (bankId) REFERENCES Bank(bankId)
);

-- Tabla Transaction
CREATE TABLE Transaction (
    transactionId INT PRIMARY KEY auto_increment,
    type ENUM('deposit', 'withdrawal', 'payment'),
    amount DECIMAL(10, 2),
    date DATE,
    customerId INT,
    FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);

-- Tabla LoanPayment
CREATE TABLE LoanPayment (
    paymentId INT PRIMARY KEY auto_increment,
    amount DECIMAL(10, 2),
    date DATE,
    loanId INT,
    FOREIGN KEY (loanId) REFERENCES BankLoan(loanId)
);

-- Tabla CardPayment
CREATE TABLE CardPayment (
    paymentId INT PRIMARY KEY auto_increment,
    amount DECIMAL(10, 2),
    date DATE,
    cardId INT,
    FOREIGN KEY (cardId) REFERENCES CreditCard(cardId)
);