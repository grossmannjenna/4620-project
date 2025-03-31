CREATE OR REPLACE VIEW ToppingPopularity AS
    SELECT T.TopName,
           SUM(CASE WHEN PT.IsDouble = 1 THEN 2
                    WHEN PT.IsDouble = 0 THEN 1
                    ELSE 0
           END) AS ToppingCount
    FROM PIZZA_TOPPING PT
    RIGHT JOIN Topping T on PT.TopID = T.TopID
    GROUP BY T.TopName
    ORDER BY ToppingCount DESC, TopName ASC;

SELECT *
FROM ToppingPopularity;

CREATE OR REPLACE VIEW ProfitByPizza AS
    SELECT B.Size, B.CrustType AS Crust,
           SUM(B.CustPrice-B.BusPrice) AS Profit, MONTH(OT.OrderDateTime) AS OrderMonth
    FROM PIZZA B
    LEFT JOIN ORDERTABLE OT ON B.BusPrice = OT.BusPrice
    GROUP BY Size, CrustType,OrderMonth
    ORDER BY Profit;

SELECT *
FROM ProfitByPizza;