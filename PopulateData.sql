-- Elle Hanckel: Topping Table, BasePrice Table, Discount Table, Order 1, Order 3, Order 4, Order 5
-- Jenna Grossmann: Order 2, Order 6, Order 7

INSERT INTO topping VALUES(1, 'Pepperoni', 2, 2.75, 3.5, 4.5, 1.25, 0.2,50, 100);
INSERT INTO topping VALUES(2, 'Sausage', 2.5, 3, 3.5, 4.25, 1.25, 0.15, 50, 100);
INSERT INTO topping VALUES(3,'Ham', 2, 2.5, 3.25, 4, 1.5, 0.15, 25, 78);
INSERT INTO topping VALUES(4, 'Chicken', 1.5, 2, 2.25, 3, 1.75, 0.25, 25, 56);
INSERT INTO topping VALUES(5, 'Green Pepper', 1, 1.5, 2, 2.5, 0.5, 0.02, 25, 79);
INSERT INTO topping VALUES(6, 'Onion', 1, 1.5, 2, 2.75, 0.5, 0.02, 25, 85);
INSERT INTO topping VALUES(7, 'Roma Tomato', 2, 3, 3.5, 4.5, 0.75, 0.03, 10, 86);
INSERT INTO topping VALUES(8, 'Mushrooms', 1.5, 2, 2.5, 3, 0.75, 0.1, 50, 52);
INSERT INTO topping VALUES(9, 'Black Olives', 0.75, 1, 1.5, 2, 0.6, 0.1, 25, 39);
INSERT INTO topping VALUES(10, 'Pineapple', 1, 1.25, 1.75, 2, 1, 0.25, 0, 15);
INSERT INTO topping VALUES(11, 'Jalapenos', 0.5, 0.75, 1.25, 1.75, 0.5, 0.05, 0, 64);
INSERT INTO topping VALUES(12, 'Banana Peppers', 0.6, 1, 1.3, 1.75, 0.5, 0.05, 0, 36);
INSERT INTO topping VALUES(13, 'Regular Cheese', 2, 3.5, 5,7, 0.5, 0.12, 50, 250);
INSERT INTO topping VALUES(14, 'Four Cheese Blend', 2, 3.5, 5, 7, 1, 0.15, 25, 150);
INSERT INTO topping VALUES(15, 'Feta Cheese', 1.75, 3, 4, 5.5, 1.5, 0.18, 0, 75);
INSERT INTO topping VALUES(16, 'Goat Cheese', 1.6, 2.75, 4, 5.5, 1.5, 0.2, 0, 54);
INSERT INTO topping VALUES(17, 'Bacon', 1, 1.5, 2, 3, 1.5, 0.25, 0, 89);


INSERT INTO baseprice VALUES('Small', 'Thin', 3, 0.5);
INSERT INTO baseprice VALUES('Small', 'Original', 3, 0.75);
INSERT INTO baseprice VALUES('Small', 'Pan', 3.5, 1);
INSERT INTO baseprice VALUES('Small', 'Gluten-Free', 4, 2);
INSERT INTO baseprice VALUES('Medium', 'Thin', 5, 1);
INSERT INTO baseprice VALUES('Medium', 'Original', 5, 1.5);
INSERT INTO baseprice VALUES('Medium', 'Pan', 6, 2.25);
INSERT INTO baseprice VALUES('Medium', 'Gluten-Free', 6.25, 3);
INSERT INTO baseprice VALUES('Large', 'Thin', 8, 1.25);
INSERT INTO baseprice VALUES('Large', 'Original', 8, 2);
INSERT INTO baseprice VALUES('Large', 'Pan', 9, 3);
INSERT INTO baseprice VALUES('Large', 'Gluten-Free', 9.5, 4);
INSERT INTO baseprice VALUES('XLarge', 'Thin', 10, 2);
INSERT INTO baseprice VALUES('XLarge', 'Original', 10, 3);
INSERT INTO baseprice VALUES('XLarge', 'Pan', 11.5, 4.5);
INSERT INTO baseprice VALUES('XLarge', 'Gluten-Free', 12.5, 6);

INSERT INTO discount VALUES(1, 'Employee',0.15, TRUE);
INSERT INTO discount VALUES(2, 'Lunch Special Medium', 1, FALSE);
INSERT INTO discount VALUES(3, 'Lunch Special Large', 2, FALSE);
INSERT INTO discount VALUES(4, 'Specialty Pizza', 1.5, FALSE);
INSERT INTO discount VALUES(5, 'Happy Hour', 0.1, TRUE);
INSERT INTO discount VALUES(6, 'Gameday Special', 0.2, TRUE);

# Order 1
INSERT INTO customer VALUES(1, NULL, NULL, NULL);
INSERT INTO ordertable VALUES(1, 'dinein', '2025-03-05 12:03', 19.75, 3.68, TRUE, 1);
INSERT INTO dinein VALUES(1, 21);
INSERT INTO pizza VALUES(1, 'Large', 'Thin', 'Complete', '2025-03-05 12:03', 19.75, 3.68, 1);
INSERT INTO pizza_topping VALUES(1, 13, 1);
INSERT INTO pizza_topping VALUES(1, 1, 0);
INSERT INTO pizza_topping VALUES(1, 2, 0);
INSERT INTO pizza_discount VALUES(1, 3);

# Order 2
INSERT INTO customer VALUES(2, NULL, NULL, NULL);
INSERT INTO ordertable VALUES(2, 'dinein','2025-04-03 12:05', 19.73, 4.63, TRUE, 2);
INSERT INTO dinein VALUES(2, 4);
INSERT INTO pizza VALUES(2, 'Medium', 'Pan', 'Complete', '2025-04-03 12:05', 12.85, 3.23, 2);
INSERT INTO pizza_topping VALUES(2, 15, 0);
INSERT INTO pizza_topping VALUES(2, 9, 0);
INSERT INTO pizza_topping VALUES(2, 7, 0);
INSERT INTO pizza_topping VALUES(2, 8, 0);
INSERT INTO pizza_topping VALUES(2, 12, 0);
INSERT INTO pizza_discount VALUES(2,2);
INSERT INTO pizza_discount VALUES(2, 4);

INSERT INTO pizza VALUES(3, 'Small', 'Original', 'Complete', '2025-04-03 12:05', 6.93, 1.40, 2);
INSERT INTO pizza_topping VALUES(3, 13, 0);
INSERT INTO pizza_topping VALUES(3, 4, 0);
INSERT INTO pizza_topping VALUES(3, 12, 0);

# Order 3
INSERT INTO customer VALUES(3, 'Andrew', 'Wilkes-Krier', '864-254-5861');
INSERT INTO ordertable VALUES(3, 'pickup', '2025-03-03 21:30', 89.28, 19.80, TRUE, 3);
INSERT INTO pickup VALUES(3, TRUE);
INSERT INTO pizza VALUES(4, 'Large', 'Original', 'Picked up', '2025-03-03 21:30', 14.88, 3.30, 3);
INSERT INTO pizza_topping VALUES(4, 13, 0);
INSERT INTO pizza_topping VALUES(4, 1, 0);
INSERT INTO pizza VALUES(5, 'Large', 'Original', 'Picked up', '2025-03-03 21:30', 14.88, 3.30, 3);
INSERT INTO pizza_topping VALUES(5, 13, 0);
INSERT INTO pizza_topping VALUES(5, 1, 0);
INSERT INTO pizza VALUES(6, 'Large', 'Original', 'Picked up', '2025-03-03 21:30', 14.88, 3.30, 3);
INSERT INTO pizza_topping VALUES(6, 13, 0);
INSERT INTO pizza_topping VALUES(6, 1, 0);
INSERT INTO pizza VALUES(7, 'Large', 'Original', 'Picked up', '2025-03-03 21:30', 14.88, 3.30, 3);
INSERT INTO pizza_topping VALUES(7, 13, 0);
INSERT INTO pizza_topping VALUES(7, 1, 0);
INSERT INTO pizza VALUES(8, 'Large', 'Original', 'Picked up', '2025-03-03 21:30', 14.88, 3.30, 3);
INSERT INTO pizza_topping VALUES(8, 13, 0);
INSERT INTO pizza_topping VALUES(8, 1, 0);
INSERT INTO pizza VALUES(9, 'Large', 'Original', 'Picked up', '2025-03-03 21:30', 14.88, 3.30, 3);
INSERT INTO pizza_topping VALUES(9, 13, 0);
INSERT INTO pizza_topping VALUES(9, 1, 0);

# Order 4
INSERT INTO ordertable VALUES(4, 'delivery', '2025-04-20 19:11', 68.96, 20.99, TRUE, 3);
INSERT INTO delivery VALUES(4, 115, 'Party Blvd', 'Anderson', 'SC', 29621, TRUE);
INSERT INTO pizza VALUES(10, 'XLarge', 'Original', 'Delivered', '2025-04-20 19:11', 27.94, 9.19, 4);
INSERT INTO pizza_topping VALUES(10, 14, 0);
INSERT INTO pizza_topping VALUES(10, 1, 0);
INSERT INTO pizza_topping VALUES(10, 2, 0);
INSERT INTO pizza VALUES(11, 'XLarge', 'Original', 'Delivered', '2025-04-20 19:11', 31.50, 6.25, 4);
INSERT INTO pizza_topping VALUES(11, 14, 0);
INSERT INTO pizza_topping VALUES(11, 3, 1);
INSERT INTO pizza_topping VALUES(11, 10, 1);
INSERT INTO pizza VALUES(12, 'XLarge', 'Original', 'Delivered', '2025-04-20 19:11', 26.75, 5.55, 4);
INSERT INTO pizza_topping VALUES(12, 14, 0);
INSERT INTO pizza_topping VALUES(12,4, 0);
INSERT INTO pizza_topping VALUES(12, 17, 0);
INSERT INTO order_discount VALUES(4, 6);
INSERT INTO pizza_discount VALUES(12, 4);

# Order 5
INSERT INTO customer VALUES(4, 'Matt', 'Engers', '864-474-9953');
INSERT INTO ordertable VALUES(5, 'pickup', '2025-03-02 17:30', 28.70, 7.84, TRUE, 4);
INSERT INTO pickup VALUES(5, TRUE);
INSERT INTO pizza VALUES(13, 'XLarge', 'Gluten-Free', 'Picked up', '2025-03-02 17:30', 28.70, 7.84, 5);
INSERT INTO pizza_topping VALUES(13, 16, 0);
INSERT INTO pizza_topping VALUES(13, 5, 0);
INSERT INTO pizza_topping VALUES(13, 6, 0);
INSERT INTO pizza_topping VALUES(13, 7, 0);
INSERT INTO pizza_topping VALUES(13, 8, 0);
INSERT INTO pizza_topping VALUES(13, 9, 0);
INSERT INTO pizza_discount VALUES(13, 4);

# Order 6
INSERT INTO customer VALUES(5, 'Frank', 'Turner', '864-232-8944');
INSERT INTO ordertable VALUES(6, 'delivery','2025-03-02 18:17',25.81, 3.64, TRUE, 5);
INSERT INTO delivery VALUES(6, 6745, 'Wessex St', 'Anderson','SC', 29621, TRUE);
INSERT INTO pizza VALUES(14, 'Large', 'Thin', 'Picked Up','2025-03-02 18:17', 25.81, 3.64, 6 );
INSERT INTO pizza_topping VALUES(14, 4, 0);
INSERT INTO pizza_topping VALUES(14, 5, 0);
INSERT INTO pizza_topping VALUES(14, 6, 0);
INSERT INTO pizza_topping VALUES(14, 8, 0);
INSERT INTO pizza_topping VALUES(14, 14, 1);

# Order 7
INSERT INTO customer VALUES(6, 'Milo', 'Auckerman', '864-878-5679');
INSERT INTO ordertable VALUES(7, 'delivery','2025-04-13 20:32',31.67, 6, TRUE, 6);
INSERT INTO delivery VALUES(7, 8879, 'Suburban Home', 'Anderson','SC', 29621, TRUE);
INSERT INTO pizza VALUES(15, 'Large', 'Thin', 'Picked Up','2025-04-13 20:32', 18, 2.75, 7 );
INSERT INTO pizza_topping VALUES(15, 14, 1);
INSERT INTO pizza VALUES(16, 'Large', 'Thin', 'Picked Up','2025-04-13 20:32', 19.25, 3.25, 7 );
INSERT INTO pizza_topping VALUES(16, 13, 0);
INSERT INTO pizza_topping VALUES(16, 1, 1);
INSERT INTO order_discount VALUES(7, 1);