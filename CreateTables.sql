-- Elle Hanckel: BasePrice Table, PizzaTopping table, PizzaDiscount table, OrderDiscount table, Discount table, topping table
-- Jenna Grossmann: schema setup, Customer Table, Ordertable table, delivery table, dinein table, pickup table, Pizza Table

DROP SCHEMA IF EXISTS PizzaDB;
CREATE SCHEMA PizzaDB;
USE PizzaDB;

DROP USER IF EXISTS 'dbtester';
CREATE USER 'dbtester' IDENTIFIED BY 'CPSC4620';
GRANT ALL ON PizzaDB.* TO 'dbtester';

CREATE TABLE customer (
    CustID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    FName VARCHAR(30),
    LName VARCHAR(30),
    PhoneNum VARCHAR(30)
);

CREATE TABLE orderTable (
    OrderID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    OrderType VARCHAR(30) NOT NULL,
    OrderDateTime DATETIME NOT NULL,
    CustPrice DECIMAL(5,2) NOT NULL,
    BusPrice DECIMAL(5,2) NOT NULL,
    isComplete BOOLEAN NOT NULL,
    CustID INT NOT NULL,
    CONSTRAINT `CustID` FOREIGN KEY(`CustID`) REFERENCES `customer` (`CustID`)
);

CREATE TABLE pickup (
    OrderID INT PRIMARY KEY,
    IsPickedUp BOOLEAN NOT NULL,
    CONSTRAINT `picked_OrderID` FOREIGN KEY(`OrderID`) REFERENCES orderTable (`OrderID`) ON DELETE CASCADE
);

CREATE TABLE delivery (
    OrderID INT PRIMARY KEY,
    HouseNum INT NOT NULL,
    Street VARCHAR(30) NOT NULL,
    City VARCHAR(30) NOT NULL,
    State VARCHAR(2) NOT NULL,
    Zip INT NOT NULL,
    IsDelivered BOOLEAN NOT NULL,
    CONSTRAINT `delivery_OrderID` FOREIGN KEY(`OrderID`) REFERENCES orderTable (`OrderID`) ON DELETE CASCADE
);

CREATE TABLE dinein (
    OrderID INT PRIMARY KEY,
    TableNum INT NOT NULL,
    CONSTRAINT `dineIn_OrderID` FOREIGN KEY(`OrderID`) REFERENCES orderTable (`OrderID`) ON DELETE CASCADE
);

CREATE TABLE pizza (
    PizzaID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    Size VARCHAR(30) NOT NULL,
    CrustType VARCHAR(30) NOT NULL,
    PizzaState VARCHAR(30) NOT NULL,
    PizzaDate DATETIME NOT NULL,
    CustPrice DECIMAL(5,2) NOT NULL,
    BusPrice DECIMAL(5,2) NOT NULL,
    OrderID INT NOT NULL,
    CONSTRAINT `pizza_OrderID` FOREIGN KEY(`OrderID`) REFERENCES orderTable (`OrderID`)
);

CREATE TABLE baseprice (
    Size VARCHAR(30) NOT NULL,
    CrustType VARCHAR(30) NOT NULL,
    CustPrice DECIMAL(5,2) NOT NULL,
    BusPrice DECIMAL(5,2) NOT NULL
);

CREATE TABLE topping (
    TopID INT PRIMARY KEY NOT NULL,
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

CREATE TABLE pizza_topping (
    PizzaID INT NOT NULL,
    TopID INT NOT NULL,
    IsDouble INT NOT NULL,
    CONSTRAINT `fk_PizzaID` FOREIGN KEY(`PizzaID`) REFERENCES pizza (`PizzaID`),
    CONSTRAINT `fk_TopID` FOREIGN KEY(`TopID`) REFERENCES topping (`TopID`)
);

CREATE TABLE discount (
    DiscountID INT PRIMARY KEY NOT NULL,
    DiscountName VARCHAR(30) NOT NULL,
    Amount DECIMAL(5,2),
    IsPercent BOOLEAN
);

CREATE TABLE pizza_discount (
    PizzaID INT NOT NULL,
    DiscountID INT NOT NULL,
    CONSTRAINT `PizzaID` FOREIGN KEY(`PizzaID`) REFERENCES pizza(`PizzaID`),
    CONSTRAINT `DiscountID` FOREIGN KEY(`DiscountID`) REFERENCES discount(`DiscountID`)
);

CREATE TABLE order_discount (
    OrderID INT NOT NULL,
    DiscountID INT NOT NULL,
    CONSTRAINT `fk_OrderID` FOREIGN KEY(`OrderID`) REFERENCES orderTable (`OrderID`),
    CONSTRAINT `fk_DiscountID` FOREIGN KEY(`DiscountID`) REFERENCES discount (`DiscountID`)
);
