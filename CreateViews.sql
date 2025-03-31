-- Elle Hanckel: ToppingPopularity View, ProfitByOrderType View
-- Jenna Grossmann: ProfitByPizza View

# View 1
CREATE OR REPLACE VIEW ToppingPopularity AS
    SELECT T.TopName as Topping,
           SUM(CASE WHEN PT.IsDouble = 1 THEN 2
                    WHEN PT.IsDouble = 0 THEN 1
                    ELSE 0
           END) AS ToppingCount
    FROM pizza_topping PT
    RIGHT JOIN topping T on PT.TopID = T.TopID
    GROUP BY T.TopName
    ORDER BY ToppingCount DESC, TopName ASC;

# View 2
CREATE OR REPLACE VIEW ProfitByPizza AS
    SELECT B.Size, B.CrustType AS Crust,
           SUM(B.CustPrice-B.BusPrice) AS Profit, DATE_FORMAT(B.PizzaDate, '%c/%Y') AS OrderMonth
    FROM pizza B
    LEFT JOIN ordertable OT ON B.OrderID = OT.OrderID
    GROUP BY B.Size, B.CrustType, OrderMonth
    ORDER BY Profit;

# View 3
CREATE OR REPLACE VIEW ProfitByOrderType AS
    SELECT OrderType AS customerType,
           DATE_FORMAT(OrderDateTime, '%c/%Y') AS OrderMonth,
           ROUND(SUM(CustPrice), 2) AS TotalOrderPrice,
           ROUND(SUM(BusPrice), 2) AS TotalOrderCost,
           ROUND(SUM(CustPrice - BusPrice), 2) AS Profit
    FROM ordertable
    GROUP BY customerType, DATE_FORMAT(OrderDateTime, '%c/%Y')
    UNION ALL
    SELECT '',
           'Grand Total',
           ROUND(SUM(CustPrice), 2),
           ROUND(SUM(BusPrice), 2),
           ROUND(SUM(CustPrice - BusPrice), 2)
    FROM ordertable;
