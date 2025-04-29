-- Elle Hanckel: ToppingPopularity View, ProfitByOrderType View
-- Jenna Grossmann: ProfitByPizza View

# View 1
CREATE VIEW PizzaDB.ToppingPopularity AS
    SELECT T.topping_TopName as Topping,
           SUM(CASE WHEN PT.pizza_topping_IsDouble = 1 THEN 2
                    WHEN PT.pizza_topping_IsDouble = 0 THEN 1
                    ELSE 0
           END) AS ToppingCount
    FROM pizza_topping PT
    RIGHt JOIN PizzaDB.topping T on PT.topping_TopID = T.topping_TopID
    GROUP BY T.Topping_TopName
    ORDER BY ToppingCount DESC, Topping_TopName ASC;

# View 2
CREATE VIEW PizzaDB.ProfitByPizza AS
    SELECT B.pizza_Size AS Size,
           B.pizza_CrustType AS Crust,
           ROUND(SUM(B.pizza_CustPrice-B.pizza_BusPrice), 2) AS Profit,
           DATE_FORMAT(B.pizza_PizzaDate, '%c/%Y') AS OrderMonth
    FROM pizza B
    LEFT JOIN ordertable OT ON B.ordertable_OrderID = OT.ordertable_OrderID
    WHERE B.pizza_PizzaState = 'completed'
    GROUP BY B.pizza_Size, B.pizza_CrustType, OrderMonth
    ORDER BY B.pizza_Size, B.pizza_CrustType;

# View 3
CREATE VIEW PizzaDB.ProfitByOrderType AS
    SELECT o.ordertable_OrderType AS customerType,
           DATE_FORMAT(ordertable_OrderDateTime, '%c/%Y') AS OrderMonth,
           ROUND(SUM(p.pizza_CustPrice), 2) AS TotalOrderPrice,
           ROUND(SUM(p.pizza_BusPrice), 2) AS TotalOrderCost,
           ROUND(SUM(p.pizza_CustPrice - p.pizza_BusPrice), 2) AS Profit
    FROM pizza p
    JOIN ordertable o ON p.ordertable_OrderID = o.ordertable_OrderID
    WHERE p.pizza_PizzaState = 'completed'
    GROUP BY customerType, OrderMonth
    UNION ALL
    SELECT '',
           'Grand Total',
           ROUND(SUM(p.pizza_CustPrice), 2),
           ROUND(SUM(p.pizza_BusPrice), 2),
           ROUND(SUM(p.pizza_CustPrice - p.pizza_BusPrice), 2)
    FROM pizza p
    JOIN ordertable o ON p.ordertable_OrderID = o.ordertable_OrderID
    WHERE p.pizza_PizzaState = 'completed';