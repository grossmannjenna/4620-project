DROP SCHEMA IF EXISTS PizzaDB;
CREATE SCHEMA PizzaDB;
USE PizzaDB;

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
CREATE TABLE PICKUP (
    OrderID INT PRIMARY KEY,
    IsPickedUp BOOLEAN NOT NULL,
    CONSTRAINT 'OrderID' FOREIGN KEY('OrderID') REFERENCES 'ORDERTABLE'('OrderID') ON DELETE CASCADE
);
CREATE TABLE DELIVERY (
    OrderID INT PRIMARY KEY,
    HouseNum INT NOT NULL,
    Street VARCHAR(30) NOT NULL,
    City VARCHAR(30) NOT NULL,
    State VARCHAR(2) NOT NULL,
    Zip INT NOT NULL,
    IsDelivered TINY INT NOT NULL
    CONSTRAINT 'OrderID' FOREIGN KEY('OrderID') REFERENCES 'ORDERTABLE'('OrderID') ON DELETE CASCADE
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

CREATE TABLE TOPPING (
    TopID INT NOT NULL,
    TopName VARCHAR(30) NOT NULL,
    SmallAMT DECIMAL(5, 2) NOT NULL,
    MedAMT DECIMAL(5, 2) NOT NULL,
    LgAMT DECIMAL(5, 2) NOT NULL,
    XLAMT DECIMAL(5, 2) NOT NULL,
    CustPrice DECIMAL(5, 2) NOT NULL,
    BusPrice DECIMAL(5, 2) NOT NULL,
    MinINVT INT NOT NULL,
    CurINVT INT NOT NULL
 );

CREATE TABLE PIZZA_TOPPING (
    PizzaID INT NOT NULL,
    TopID INT NOT NULL,
    IsDouble INT NOT NULL,
    CONSTRAINT 'PizzaID' FOREIGN KEY('PizzaID') REFERENCES 'PIZZA'('PizzaID'),
    CONSTRAINT 'TopID' FOREIGN KEY('TopID') REFERENCES 'TOPPING'('TopID')
);

CREATE TABLE DISCOUNT (
    DiscountID INT PRIMARY KEY NOT NULL,
    DiscountName VARCHAR(30) NOT NULL,
    Amount DECIMAL(5,2),
    IsPercent BOOLEAN
);

CREATE TABLE PIZZA_DISCOUNT (
    PizzaID INT NOT NULL,
    DiscountID INT NOT NULL,
    CONSTRAINT 'PizzaID' FOREIGN KEY('PizzaID') REFERENCES 'PIZZA'('PizzaID'),
    CONSTRAINT 'DiscountID' FOREIGN KEY('DiscountID') REFERENCES 'DISCOUNT'('DiscountID')
);

CREATE TABLE ORDER_DISCOUNT (
    OrderID INT NOT NULL,
    DiscountID INT NOT NULL,
    CONSTRAINT 'OrderID' FOREIGN KEY('OrderID') REFERENCES 'ORDERTABLE'('OrderID'),
    CONSTRAINT 'DiscountID' FOREIGN KEY('DiscountID') REFERENCES 'DISCOUNT'('DiscountID')
);

