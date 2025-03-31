-- Elle Hanckel: BasePrice Table, PizzaTopping table, PizzaDiscount table, OrderDiscount table, Discount table, topping table
-- Jenna Grossmann: schema setup, Customer Table, Ordertable table, delivery table, dinein table, pickup table, Pizza Table

DROP SCHEMA IF EXISTS PizzaDB;
CREATE SCHEMA PizzaDB;
USE PizzaDB;

DROP USER IF EXISTS 'dbtester';
CREATE USER 'dbtester' IDENTIFIED BY 'CPSC4620';
GRANT ALL ON PizzaDB.* TO 'dbtester';

CREATE TABLE customer (
    customer_CustID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    customer_FName VARCHAR(30) NOT NULL,
    customer_LName VARCHAR(30) NOT NULL,
    customer_PhoneNum VARCHAR(30) NOT NULL
);

CREATE TABLE ordertable (
    ordertable_OrderID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    ordertable_OrderType VARCHAR(30) NOT NULL,
    ordertable_OrderDateTime DATETIME NOT NULL,
    ordertable_CustPrice DECIMAL(5,2) NOT NULL,
    ordertable_BusPrice DECIMAL(5,2) NOT NULL,
    ordertable_isComplete BOOLEAN DEFAULT 0,
    customer_CustID INT,
    CONSTRAINT `customer_CustID` FOREIGN KEY(`customer_CustID`) REFERENCES `customer` (`customer_CustID`)
);

CREATE TABLE pickup (
    ordertable_OrderID INT PRIMARY KEY,
    pickup_IsPickedUp BOOLEAN NOT NULL DEFAULT 0,
    CONSTRAINT `picked_OrderID` FOREIGN KEY(`ordertable_OrderID`) REFERENCES ordertable (`ordertable_OrderID`) ON DELETE CASCADE
);

CREATE TABLE delivery (
    ordertable_OrderID INT PRIMARY KEY,
    delivery_HouseNum INT NOT NULL,
    delivery_Street VARCHAR(30) NOT NULL,
    delivery_City VARCHAR(30) NOT NULL,
    delivery_State VARCHAR(2) NOT NULL,
    delivery_Zip INT NOT NULL,
    delivery_IsDelivered BOOLEAN NOT NULL DEFAULT 0,
    CONSTRAINT `delivery_OrderID` FOREIGN KEY(`ordertable_OrderID`) REFERENCES ordertable (`ordertable_OrderID`) ON DELETE CASCADE
);

CREATE TABLE dinein (
    ordertable_OrderID INT PRIMARY KEY,
    dinein_TableNum INT NOT NULL,
    CONSTRAINT `dineIn_OrderID` FOREIGN KEY(`ordertable_OrderID`) REFERENCES ordertable (`ordertable_OrderID`) ON DELETE CASCADE
);

CREATE TABLE baseprice (
    baseprice_Size VARCHAR(30) NOT NULL,
    baseprice_CrustType VARCHAR(30) NOT NULL,
    baseprice_CustPrice DECIMAL(5,2) NOT NULL,
    baseprice_BusPrice DECIMAL(5,2) NOT NULL
);

CREATE TABLE pizza (
    pizza_PizzaID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    baseprice_Size VARCHAR(30) NOT NULL,
    baseprice_CrustType VARCHAR(30) NOT NULL,
    pizza_PizzaState VARCHAR(30) NOT NULL,
    pizza_PizzaDate DATETIME NOT NULL,
    pizza_CustPrice DECIMAL(5,2) NOT NULL,
    pizza_BusPrice DECIMAL(5,2) NOT NULL,
    ordertable_OrderID INT NOT NULL,
    CONSTRAINT `pizza_OrderID` FOREIGN KEY(`ordertable_OrderID`) REFERENCES ordertable (`ordertable_OrderID`),
    CONSTRAINT `baseprice_Size` FOREIGN KEY(`baseprice_Size`) REFERENCES baseprice (`baseprice_Size`),
    CONSTRAINT `baseprice_CrustType` FOREIGN KEY(`baseprice_CrustType`) REFERENCES baseprice (`baseprice_CrustType`)
);

CREATE TABLE topping (
    topping_TopID INT PRIMARY KEY NOT NULL,
    topping_TopName VARCHAR(30) NOT NULL,
    topping_SmallAMT DECIMAL(5, 2) NOT NULL,
    topping_MedAMT DECIMAL(5, 2) NOT NULL,
    topping_LgAMT DECIMAL(5, 2) NOT NULL,
    topping_XLAMT DECIMAL(5, 2) NOT NULL,
    topping_CustPrice DECIMAL(5, 2) NOT NULL,
    topping_BusPrice DECIMAL(5, 2) NOT NULL,
    topping_MinINVT INT NOT NULL,
    topping_CurINVT INT NOT NULL
 );

CREATE TABLE pizza_topping (
    pizza_PizzaID INT NOT NULL,
    topping_TopID INT NOT NULL,
    pizza_topping_IsDouble INT NOT NULL,
    CONSTRAINT `fk_PizzaID` FOREIGN KEY(`pizza_PizzaID`) REFERENCES pizza (`pizza_PizzaID`),
    CONSTRAINT `fk_TopID` FOREIGN KEY(`topping_TopID`) REFERENCES topping (`topping_TopID`)
);

CREATE TABLE discount (
    discount_DiscountID INT PRIMARY KEY NOT NULL,
    discount_DiscountName VARCHAR(30) NOT NULL,
    discount_Amount DECIMAL(5,2),
    discount_IsPercent BOOLEAN
);

CREATE TABLE pizza_discount (
    pizza_PizzaID INT NOT NULL,
    discount_DiscountID INT NOT NULL,
    CONSTRAINT `PizzaID` FOREIGN KEY(`pizza_PizzaID`) REFERENCES pizza(`pizza_PizzaID`),
    CONSTRAINT `DiscountID` FOREIGN KEY(`discount_DiscountID`) REFERENCES discount(`discount_DiscountID`)
);

CREATE TABLE order_discount (
    ordertable_OrderID INT NOT NULL,
    discount_DiscountID INT NOT NULL,
    CONSTRAINT `fk_OrderID` FOREIGN KEY(`ordertable_OrderID`) REFERENCES ordertable (`ordertable_OrderID`),
    CONSTRAINT `fk_DiscountID` FOREIGN KEY(`discount_DiscountID`) REFERENCES discount (`discount_DiscountID`)
);
