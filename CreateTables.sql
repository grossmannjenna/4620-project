DROP USER IF EXISTS 'dbtester';
CREATE USER 'dbtester' IDENTIFIED BY 'CPSC4620';
GRANT ALL ON PizzaDB.* TO 'dbtester';

CREATE TABLE CUSTOMER (
    CustID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    FName VARCHAR(30) NOT NULL,
    LName VARCHAR(30) NOT NULL,
    PhoneNum VARCHAR(30) NOT NULL
);

CREATE TABLE ORDERTABLE (
    OrderID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    OrderType VARCHAR(30) NOT NULL,
    OrderDateTime DATETIME NOT NULL,
    CustPrice DECIMAL(5,2) NOT NULL,
    BusPrice DECIMAL(5,2) NOT NULL,
    isComplete TINYINT(1) NOT NULL,
    CustID INT NOT NULL,
    CONSTRAINT 'CustID' FOREIGN KEY('CustID') REFERENCES 'CUSTOMER'('CustID')

);

CREATE TABLE PIZZA (
    PizzaID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Size VARCHAR(30) NOT NULL,
    CrustType VARCHAR(30) NOT NULL,
    PizzaState VARCHAR(30) NOT NULL,
    PizzaDate DATETIME NOT NULL,
    CustPrice DECIMAL(5,2) NOT NULL,
    BusPrice DECIMAL(5,2) NOT NULL,
    OrderID INT NOT NULL,
    CONSTRAINT 'OrderID' FOREIGN KEY('OrderID') REFERENCES 'ORDERTABLE'('OrderID')
);