CREATE OR REPLACE VIEW ToppingPopularity AS
    SELECT T.TopName,
           SUM(CASE WHEN PT.IsDouble = 1 THEN 2
                    WHEN PT.IsDouble = 0 THEN 1
                    ELSE 0
           END) AS ToppingCount
    FROM TOPPING T
    LEFT JOIN PIZZA_TOPPING PT on T.TopID = PT.TopID
    GROUP BY T.TopName
    ORDER BY ToppingCount DESC, TopName ASC;

SELECT *
FROM ToppingPopularity;