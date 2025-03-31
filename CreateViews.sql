-- Elle Hanckel: ToppingPopularity View, ProfitByOrderType View
-- Jenna Grossmann: ProfitByPizza View

# View 1
CREATE OR REPLACE VIEW PizzaDB.ToppingPopularity AS
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
CREATE OR REPLACE VIEW PizzaDB.ProfitByPizza AS
    SELECT B.pizza_Size, B.pizza_CrustType AS Crust,
           SUM(B.pizza_CustPrice-B.pizza_BusPrice) AS Profit, DATE_FORMAT(B.pizza_PizzaDate, '%c/%Y') AS OrderMonth
    FROM pizza B
    LEFT JOIN ordertable OT ON B.ordertable_OrderID = OT.ordertable_OrderID
    GROUP BY B.pizza_Size, B.pizza_CrustType, OrderMonth
    ORDER BY Profit;

# View 3
CREATE OR REPLACE VIEW PizzaDB.ProfitByOrderType AS
    SELECT ordertable_OrderType AS customerType,
           DATE_FORMAT(ordertable_OrderDateTime, '%c/%Y') AS OrderMonth,
           ROUND(SUM(ordertable_CustPrice), 2) AS TotalOrderPrice,
           ROUND(SUM(ordertable_BusPrice), 2) AS TotalOrderCost,
           ROUND(SUM(ordertable_CustPrice - ordertable_BusPrice), 2) AS Profit
    FROM ordertable
    GROUP BY customerType, DATE_FORMAT(ordertable_OrderDateTime, '%c/%Y')
    UNION ALL
    SELECT '',
           'Grand Total',
           ROUND(SUM(ordertable_CustPrice), 2),
           ROUND(SUM(ordertable_BusPrice), 2),
           ROUND(SUM(ordertable_CustPrice - ordertable_BusPrice), 2)
    FROM ordertable;
