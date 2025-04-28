package cpsc4620;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/*
 * This file is where you will implement the methods needed to support this application.
 * You will write the code to retrieve and save information to the database and use that
 * information to build the various objects required by the applicaiton.
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string for pickup, delivery and dine-in.
 * DO NOT change these constant values.
 * 
 * You can add any helper methods you need, but you must implement all the methods
 * in this class and use them to complete the project.  The autograder will rely on
 * these methods being implemented, so do not delete them or alter their method
 * signatures.
 * 
 * Make sure you properly open and close your DB connections in any method that
 * requires access to the DB.
 * Use the connect_to_db below to open your connection in DBConnector.
 * What is opened must be closed!
 */

/*
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// DO NOT change these variables!
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";

	public enum order_state {
		PREPARED,
		DELIVERED,
		PICKEDUP
	}


	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	// COMPLETED - Jenna
	public static void addOrder(Order o) throws SQLException, IOException {
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, pickup, pizzas, toppings
		 * on pizzas, order discounts and pizza discounts.
		 *
		 * This is a KEY method as it must store all the data in the Order object
		 * in the database and make sure all the tables are correctly linked.
		 *
		 * Remember, if the order is for Dine In, there is no customer...
		 * so the cusomter id coming from the Order object will be -1.
		 *
		 */
		connect_to_db();


		try {
			// UPDATE Order DB
			PreparedStatement os;

			String ordertablequery = "INSERT INTO ordertable (ordertable_OrderType, ordertable_OrderDateTime, " +
					"ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete, customer_CustID) " +
					"VALUES (?,?,?,?,?,?);";
			os = conn.prepareStatement(ordertablequery, Statement.RETURN_GENERATED_KEYS);

			os.setString(1, o.getOrderType());
			os.setTimestamp(2, Timestamp.valueOf(o.getDate()));
			os.setDouble(3, o.getCustPrice());
			os.setDouble(4, o.getBusPrice());
			os.setBoolean(5, o.getIsComplete());
			if(o.getCustID() != -1) {  //check if the order is dine in
				os.setInt(6, o.getCustID());
			} else {
				os.setNull(6, java.sql.Types.INTEGER);
			}
			os.executeUpdate();

			// get the new orderID
			int orderID = -1;
			ResultSet keys = os.getGeneratedKeys();
			if(keys.next()) {
				orderID = keys.getInt(1);
				o.setOrderID(orderID);
			}
			os.close();

			PreparedStatement ot;
			// specific order type tables
			switch (o.getOrderType()) {
				case "pickup":
					String pickupQuery = "INSERT INTO pickup (ordertable_OrderID, pickup_IsPickedUp)" +
							"VALUES (?,?);";
					ot = conn.prepareStatement(pickupQuery);
					ot.setInt(1, orderID);
					ot.setBoolean(2, false);
					ot.executeUpdate();
					ot.close();
					break;

				case "delivery":
					DeliveryOrder delivery = (DeliveryOrder) o;
					String deliveryQuery = "INSERT INTO delivery(ordertable_OrderID, delivery_HouseNum, " +
							"delivery_Street, delivery_City, delivery_State, delivery_Zip, delivery_IsDelivered)" +
							"VALUES (?, ?, ?, ?, ?, ?,?);";
					ot = conn.prepareStatement(deliveryQuery);

					String[] addressParts = delivery.getAddress().split("\t");

					ot.setInt(1, orderID);
					ot.setString(2, addressParts[0]);
					ot.setString(3, addressParts[1]);
					ot.setString(4, addressParts[2]);
					ot.setString(5, addressParts[3]);
					ot.setString(6, addressParts[4]);
					ot.setBoolean(7, false);

					ot.executeUpdate();
					ot.close();
					break;

				case "dine_in":
					DineinOrder dinein = (DineinOrder) o;
					String dineInQuery = "INSERT INTO dinein (ordertable_OrderID, dinein_TableNum) " +
							"VALUES (?, ?);";
					ot = conn.prepareStatement(dineInQuery);
					ot.setInt(1, orderID);
					ot.setInt(2, dinein.getTableNum());
					ot.executeUpdate();
					ot.close();
					break;
			}

			// insert pizzas
			for(Pizza pizza : o.getPizzaList()) {
				String pizzaQuery = "INSERT INTO pizza (pizza_Size ,pizza_CrustType, pizza_PizzaState, " +
						"pizza_PizzaDate,pizza_CustPrice, pizza_BusPrice,ordertable_OrderID)" +
						"VALUES (?, ?, ?, ?, ?, ?, ?);";
				PreparedStatement pzs = conn.prepareStatement(pizzaQuery, PreparedStatement.RETURN_GENERATED_KEYS);

				pzs.setString(1, pizza.getSize());
				pzs.setString(2, pizza.getCrustType());
				pzs.setString(3, pizza.getPizzaState());
				pzs.setTimestamp(4, Timestamp.valueOf(o.getDate())); // assuming date comes from order
				pzs.setDouble(5, pizza.getCustPrice());
				pzs.setDouble(6, pizza.getBusPrice());
				pzs.setInt(7, orderID);

				pzs.executeUpdate();

				int pizzaID = -1;
				ResultSet pizzaKeys = pzs.getGeneratedKeys();

				if (pizzaKeys.next()) {
					pizzaID = pizzaKeys.getInt(1);
					pizza.setPizzaID(pizzaID);
				}
				pzs.close();

				//insert toppings
				for (Topping t : pizza.getToppings()) {
					PreparedStatement topOS;
					String topQuery;

					topQuery = "INSERT INTO pizza_topping(pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)\n" +
							"VALUES (?, ?, ?);";
					topOS = conn.prepareStatement(topQuery);
					topOS.setInt(1, pizzaID);
					topOS.setInt(2, t.getTopID());
					topOS.setBoolean(3, t.getDoubled());
					topOS.executeUpdate();
					topOS.close();
				}

				//insert pizza discounts
				for (Discount dis : pizza.getDiscounts()) {
					PreparedStatement disOS;
					String disQuery;

					disQuery = "INSERT INTO pizza_discount (pizza_PizzaID, discount_DiscountID) " +
							"VALUES (?, ?);";
					disOS = conn.prepareStatement(disQuery);

					disOS.setInt(1, pizzaID);
					disOS.setInt(2, dis.getDiscountID());
					disOS.executeUpdate();
					disOS.close();
				}
			}
			for (Discount disc : o.getDiscountList()) {
				String queryOD = "INSERT INTO order_discount (ordertable_OrderID, discount_DiscountID) VALUES (?, ?);";
				PreparedStatement odps = conn.prepareStatement(queryOD);

				odps.setInt(1, orderID);
				odps.setInt(2, disc.getDiscountID());

				odps.executeUpdate();
				odps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}
		conn.close();
	}

	// COMPLETED - ELLE
	public static int addPizza(java.util.Date d, int orderID, Pizza p) throws SQLException, IOException {
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind you must also add the pizza discounts and toppings
		 * associated with the pizza.
		 *
		 * NOTE: there is a Date object passed into this method so that the Order
		 * and ALL its Pizzas can be assigned the same DTS.
		 *
		 * This method returns the id of the pizza just added.
		 *
		 */
		connect_to_db();
		int pizzaID = -1;

		try {
			PreparedStatement os;
			String query;
			query = "INSERT INTO pizza (pizza_Size, pizza_CrustType, pizza_PizzaState, " +
					"pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice, ordertable_OrderID) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?);";
			os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			os.setInt(1, orderID);
			os.setString(2, p.getSize());
			os.setString(3, p.getCrustType());
			os.setString(4, p.getPizzaState());
			os.setTimestamp(5, new java.sql.Timestamp(d.getTime()));
			os.setDouble(6, p.getCustPrice());
			os.setDouble(7, p.getBusPrice());
			os.executeUpdate();

			ResultSet keys = os.getGeneratedKeys();

			if (keys.next()) {
				pizzaID = keys.getInt(1);
				p.setPizzaID(pizzaID);
			}

			for (Topping t : p.getToppings()) {
				PreparedStatement topOS;
				String topQuery;

				topQuery = "INSERT INTO pizza_topping(pizza_PizzaID, topping_TopID, pizza_topping_IsDouble)\n" +
						"VALUES (?, ?, ?);";
				topOS = conn.prepareStatement(topQuery);
				topOS.setInt(1, pizzaID);
				topOS.setInt(2, t.getTopID());
				topOS.setBoolean(3, t.getDoubled());
				topOS.executeUpdate();
				topOS.close();
			}

			for (Discount dis : p.getDiscounts()) {
				PreparedStatement disOS;
				String disQuery;

				disQuery = "INSERT INTO pizza_discount (pizza_PizzaID, discount_DiscountID)\n" +
						"VALUES (?, ?);";
				disOS = conn.prepareStatement(disQuery);

				disOS.setInt(1, pizzaID);
				disOS.setInt(2, dis.getDiscountID());

				disOS.executeUpdate();
				disOS.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}
		conn.close();
		
		return pizzaID;
	}

	// COMPLETE -Jenna
	public static int addCustomer(Customer c) throws SQLException, IOException {
		/*
		 * This method adds a new customer to the database.
		 *
		 */

		connect_to_db();
		int newCustID = c.getCustID();

		try {
			PreparedStatement os;
			String query = "INSERT INTO customer (customer_Fname, customer_Lname, customer_PhoneNum) " +
					"VALUES (?, ?, ?);";
			os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			os.setString(1, c.getFName());
			os.setString(2, c.getLName());
			os.setString(3, c.getPhone());
			os.executeUpdate();

			ResultSet keys = os.getGeneratedKeys();
			if (keys.next()) {
				int custID = keys.getInt(1);
				c.setCustID(custID);
			}
			os.close();

		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		//return new customer's ID
		return newCustID;
	}

	//COMPLETE- JENNA
	public static void completeOrder(int OrderID, order_state newState) throws SQLException, IOException {
		/*
		 * Mark that order as complete in the database.
		 * Note: if an order is complete, this means all the pizzas are complete as well.
		 * However, it does not mean that the order has been delivered or picked up!
		 *
		 * For newState = PREPARED: mark the order and all associated pizza's as completed
		 * For newState = DELIVERED: mark the delivery status
		 * FOR newState = PICKEDUP: mark the pickup status
		 *
		 */
		connect_to_db();

		try {
			PreparedStatement os;

			// change pizza statuses
			if (newState == order_state.PREPARED) {

				// mark order as complete
				String Orderquery = "UPDATE ordertable SET ordertable_isComplete = 1 " +
						"WHERE ordertable_OrderID =?;";
				os = conn.prepareStatement(Orderquery);
				os.setInt(1, OrderID);
				os.executeUpdate();
				os.close();

				// mark pizzas as complete
				String Pizzaquery = "UPDATE pizza SET pizza_PizzaState = 'completed' " +
						"WHERE ordertable_OrderID = ?;";
				os = conn.prepareStatement(Pizzaquery);
				os.setInt(1, OrderID);
				os.executeUpdate();
				os.close();

			} else if (newState == order_state.PICKEDUP) {
				// mark pizza as picked up
				String Pizzaquery = "UPDATE pizza SET pizza_PizzaState = 'pickedup' " +
						"WHERE ordertable_OrderID =?;";
				os = conn.prepareStatement(Pizzaquery);
				os.setInt(1, OrderID);
				os.executeUpdate();
				os.close();

				// mark pickup table as picked up
				String pickupQuery = "UPDATE pickup SET pickup_IsPickedUp = 1 " +
						"WHERE ordertable_OrderID = ?;";
				os = conn.prepareStatement(pickupQuery);
				os.setInt(1, OrderID);
				os.executeUpdate();
				os.close();

			} else if (newState == order_state.DELIVERED) {
				// mark pizza as delivered
				String Pizzaquery = "UPDATE pizza SET pizza_PizzaState = 'delivered' " +
						"WHERE ordertable_OrderID =?;";
				os = conn.prepareStatement(Pizzaquery);
				os.setInt(1, OrderID);
				os.executeUpdate();
				os.close();

				// mark pickup table as picked up
				String deliveryQuery = "UPDATE delivery SET delivery_IsDelivered = 1 " +
						"WHERE ordertable_OrderID = ?;";
				os = conn.prepareStatement(deliveryQuery);
				os.setInt(1, OrderID);
				os.executeUpdate();
				os.close();

			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();
	}

	//COMPLETE - ELLE
	public static ArrayList<Order> getOrders(int status) throws SQLException, IOException {
		/*
		 * Return an ArrayList of orders.
		 * 	status   == 1 => return a list of open (ie oder is not completed)
		 *           == 2 => return a list of completed orders (ie order is complete)
		 *           == 3 => return a list of all the orders
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 *
		 * You must fully populate the Order object, this includes order discounts,
		 * and pizzas along with the toppings and discounts associated with them.
		 *
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */
		connect_to_db();
		ArrayList<Order> orders = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select * From ordertable ";
			if (status == 1) {
				query += "WHERE ordertable_isComplete = 0 ";
			} else if (status == 2) {
				query += "WHERE ordertable_isComplete = 1 ";
			}
			query += "ORDER BY ordertable_OrderID ASC;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while (rset.next()) {
				int orderID = rset.getInt("ordertable_OrderID");
				int custID = rset.getInt("customer_CustID");
				String orderType = rset.getString("ordertable_OrderType");
				String date = rset.getString("ordertable_OrderDateTime");
				double custPrice = rset.getDouble("ordertable_CustPrice");
				double busPrice = rset.getDouble("ordertable_BusPrice");
				boolean complete = rset.getBoolean("ordertable_isComplete");

				Order order = null;

				if (orderType.equalsIgnoreCase("dinein")) {
					String query1 = "SELECT dinein_TableNum FROM dinein WHERE ordertable_OrderID=?;";
					PreparedStatement dineinStatement = conn.prepareStatement(query1);
					dineinStatement.setInt(1, orderID);
					ResultSet dineinResult = dineinStatement.executeQuery();

					int tableNum = -1;
					if (dineinResult.next()) {
						tableNum = dineinResult.getInt("dinein_tableNum");
					}
					dineinStatement.close();
					dineinResult.close();

					order = new DineinOrder(orderID, custID, date, custPrice, busPrice, complete, tableNum);
				} else if (orderType.equalsIgnoreCase("pickup")) {
					String query2 = "SELECT pickup_IsPickedUp FROM pickup WHERE ordertable_OrderID=?;";
					PreparedStatement pickupStatement = conn.prepareStatement(query2);
					pickupStatement.setInt(1, orderID);
					ResultSet pickupResult = pickupStatement.executeQuery();

					boolean pickup = false;
					if (pickupResult.next()) {
						pickup = pickupResult.getBoolean("pickup_IsPickedUp");
					}
					pickupStatement.close();
					pickupResult.close();

					order = new PickupOrder(orderID, custID, date, custPrice, busPrice, pickup, complete);
				} else if (orderType.equalsIgnoreCase("delivery")) {
					String query3 = "SELECT delivery_HouseNum, delivery_Street, delivery_City, delivery_State, delivery_Zip, delivery_IsDelivered FROM delivery WHERE ordertable_OrderID=?;";
					PreparedStatement deliveryStatement = conn.prepareStatement(query3);
					deliveryStatement.setInt(1, orderID);
					ResultSet deliveryResult = deliveryStatement.executeQuery();

					boolean isDelivered = false;
					String address = "";

					if (deliveryResult.next()) {
						String num = deliveryResult.getString("delivery_HouseNum");
						String street = deliveryResult.getString("delivery_Street");
						String city = deliveryResult.getString("delivery_City");
						String state = deliveryResult.getString("delivery_State");
						String zip = deliveryResult.getString("delivery_Zip");
						isDelivered = deliveryResult.getBoolean("delivery_IsDelivered");

						address = num + "\t" + street + "\t" + city + "\t" + state + "\t" + zip;
					}
					deliveryStatement.close();
					deliveryResult.close();

					order = new DeliveryOrder(orderID, custID, date, custPrice, busPrice, complete, isDelivered, address);
				}

				if (order != null) {
					order.setPizzaList(getPizzas(order));
					order.setDiscountList(getDiscounts(order));

					orders.add(order);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return orders;

	}

	// COMPLETE- JENNA
	public static Order getLastOrder() throws SQLException, IOException {
		/*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there will ALWAYS be a "last order"!
		 */
		connect_to_db();
		Order order = null;

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT * From ordertable ORDER BY ordertable_OrderDateTime DESC LIMIT 1;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while (rset.next()) {
				int orderid = rset.getInt("ordertable_OrderID");
				int custid = rset.getInt("customer_CustID");
				String type = rset.getString("ordertable_OrderType");
				String datetime = rset.getString("ordertable_OrderDateTime");
				Double custprice = rset.getDouble("ordertable_CustPrice");
				Double busprice = rset.getDouble("ordertable_BusPrice");
				boolean complete = rset.getBoolean("ordertable_isComplete");

				order = new Order(orderid, custid, type, datetime, custprice, busprice, complete);

				order.setPizzaList(getPizzas(order));
				order.setDiscountList(getDiscounts(order));
			}
			os.close();
			rset.close();

		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return order;
	}

	// COMPLETE - Jenna
	public static ArrayList<Order> getOrdersByDate(String date) throws SQLException, IOException {
		/*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *
		 */

		connect_to_db();

		ArrayList<Order> orderList = new ArrayList<>();

		try {
			PreparedStatement ps;
			ResultSet rset;
			String query;
			query = "SELECT ordertable_OrderID, ordertable_OrderType, " +
					"ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete, customer_CustID " +
					"FROM ordertable " +
					"WHERE DATE(ordertable_OrderDateTime) = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, date);
			rset = ps.executeQuery();
			while (rset.next()) {
				int Orderid = rset.getInt("ordertable_OrderID");
				int cusid = rset.getInt("customer_CustID");
				String ordertype = rset.getString("ordertable_OrderType");
				double custprice = rset.getDouble("ordertable_CustPrice");
				double busprice = rset.getDouble("ordertable_BusPrice");
				boolean complete = rset.getBoolean("ordertable_isComplete");
				Order order = new Order(Orderid, cusid, ordertype, date, custprice, busprice, complete);

				order.setPizzaList(getPizzas(order));
				order.setDiscountList(getDiscounts(order));

				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();
		return orderList;
	}

	//COMPLETE -Jenna
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		/*
		 * Query the database for all the available discounts and
		 * return them in an arrayList of discounts ordered by discount name.
		 *
		 */
		connect_to_db();
		ArrayList<Discount> discountList = new ArrayList<>();

		try {
			PreparedStatement ps;
			ResultSet rset;
			String query;
			query = "Select discount_DiscountID, discount_DiscountName, discount_Amount, discount_IsPercent From discount " +
					"ORDER BY discount_DiscountName;";
			ps = conn.prepareStatement(query);
			rset = ps.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("discount_DiscountID");
				String name = rset.getString("discount_DiscountName");
				Double amount = rset.getDouble("discount_Amount");
				Boolean ispercent = rset.getBoolean("discount_IsPercent");
				Discount discount = new Discount(id, name, amount, ispercent);

				discountList.add(discount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return discountList;
	}

	//COMPLETE - Jenna
	public static Discount findDiscountByName(String name) throws SQLException, IOException {
		/*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *
		 */
		connect_to_db();
		Discount discount = null;

		try {
			PreparedStatement ps;
			ResultSet rset;
			String query;
			query = "Select discount_DiscountID, discount_DiscountName, discount_Amount, discount_IsPercent " +
					"from discount Where discount_DiscountName=?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, name);
			rset = ps.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("discount_DiscountID");
				String Dname = rset.getString("discount_DiscountName");
				Double amount = rset.getDouble("discount_Amount");
				Boolean ispercent = rset.getBoolean("discount_IsPercent");
				discount = new Discount(id, Dname, amount, ispercent);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}


		conn.close();
		return discount;
	}

	// COMPLETE - ELLE
	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		/*
		 * Query the data for all the customers and return an arrayList of all the customers.
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */
		connect_to_db();
		ArrayList<Customer> customerList = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select customer_CustID, customer_FName, customer_LName, customer_PhoneNum From customer ORDER BY customer_LName, customer_FName;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("customer_CustID");
				String fname = rset.getString("customer_FName");
				String lname = rset.getString("customer_LName");
				String phone = rset.getString("customer_PhoneNum"); // note the use of field names in the getSting methods
				Customer customer = new Customer(id, fname, lname, phone);

				customerList.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return customerList;

	}

	// COMPLETE - ELLE
	public static Customer findCustomerByPhone(String phoneNumber) throws SQLException, IOException {
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *
		 */
		connect_to_db();
		Customer customer = null;

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select customer_CustID, customer_FName, customer_LName From customer WHERE customer_PhoneNum=? ORDER BY customer_CustID;";
			os = conn.prepareStatement(query);
			os.setString(1, phoneNumber);
			rset = os.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("customer_CustID");
				String fname = rset.getString("customer_FName");
				String lname = rset.getString("customer_LName"); // note the use of field names in the getSting methods
				customer = new Customer(id, fname, lname, phoneNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return customer;

	}

	// COMPLETE - GIVEN EXAMPLE
	public static String getCustomerName(int CustID) throws SQLException, IOException {
		/*
		 * COMPLETED...WORKING Example!
		 *
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with
		 * your database from Java.
		 *
		 * Notice how the connection to the DB made at the start of the
		 *
		 */

		connect_to_db();

		/*
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 *
		 */
		String cname1 = "";
		String cname2 = "";
		String query = "Select customer_FName, customer_LName From customer WHERE customer_CustID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);

		while (rset.next()) {
			cname1 = rset.getString(1) + " " + rset.getString(2);
		}

		/*
		 * an BETTER example of the same query using a prepared statement...
		 * with exception handling
		 *
		 */
		try {
			PreparedStatement os;
			ResultSet rset2;
			String query2;
			query2 = "Select customer_FName, customer_LName From customer WHERE customer_CustID=?;";
			os = conn.prepareStatement(query2);
			os.setInt(1, CustID);
			rset2 = os.executeQuery();
			while (rset2.next()) {
				cname2 = rset2.getString("customer_FName") + " " + rset2.getString("customer_LName"); // note the use of field names in the getSting methods
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		//return cname1;
		// OR
		return cname2;

	}


	// COMPLETE - ELLE
	public static ArrayList<Topping> getToppingList() throws SQLException, IOException {
		/*
		 * Query the database for the aviable toppings and
		 * return an arrayList of all the available toppings.
		 * Don't forget to order the data coming from the database appropriately.
		 *
		 */

		connect_to_db();
		ArrayList<Topping> toppingList = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select * From topping WHERE topping_CurINVT > 0 ORDER BY topping_TopName;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("topping_TopID");
				String name = rset.getString("topping_TopName");
				double small = rset.getDouble("topping_SmallAMT");
				double med = rset.getDouble("topping_MedAMT");
				double large = rset.getDouble("topping_LgAMT");
				double xl = rset.getDouble("topping_XLAMT");
				double custP = rset.getDouble("topping_CustPrice");
				double busP = rset.getDouble("topping_BusPrice");
				int min = rset.getInt("topping_MinINVT");
				int cur = rset.getInt("topping_CurINVT");
				Topping topping = new Topping(id, name, small, med, large, xl, custP, busP, min, cur);

				toppingList.add(topping);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return toppingList;
	}

	// COMPLETE - ELLE
	public static Topping findToppingByName(String name) throws SQLException, IOException {
		/*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *
		 */
		connect_to_db();
		Topping topping = null;

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select * From topping Where topping_TopName=?;";
			os = conn.prepareStatement(query);
			os.setString(1, name);
			rset = os.executeQuery();
			while (rset.next()) {
				int id = rset.getInt("topping_TopID");
				String toppingName = rset.getString("topping_TopName");
				double small = rset.getDouble("topping_SmallAMT");
				double med = rset.getDouble("topping_MedAMT");
				double large = rset.getDouble("topping_LgAMT");
				double xl = rset.getDouble("topping_XLAMT");
				double custP = rset.getDouble("topping_CustPrice");
				double busP = rset.getDouble("topping_BusPrice");
				int min = rset.getInt("topping_MinINVT");
				int cur = rset.getInt("topping_CurINVT");
				topping = new Topping(id, name, small, med, large, xl, custP, busP, min, cur);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		if (topping != null) {
			return topping;
		} else {
			return null;
		}
	}

	// COMPLETE - ELLE
	public static ArrayList<Topping> getToppingsOnPizza(Pizza p) throws SQLException, IOException {
		/*
		 * This method builds an ArrayList of the toppings ON a pizza.
		 * The list can then be added to the Pizza object elsewhere in the
		 */

		//connect_to_db();
		ArrayList<Topping> pizzaToppings = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select t.topping_TopID, t.topping_TopName, t.topping_SmallAMT, t.topping_MedAMT, t.topping_LgAMT, t.topping_XLAMT, " +
					"t.topping_CustPrice, t.topping_BusPrice, t.topping_MinINVT, t.topping_CurINVT, pizza_topping_IsDouble " +
					"From pizza_topping pt " +
					"JOIN topping t ON pt.topping_TopID = t.topping_TopID " +
					"WHERE pt.pizza_PizzaID =?;";
			os = conn.prepareStatement(query);
			os.setInt(1, p.getPizzaID());
			rset = os.executeQuery();
			while (rset.next()) {
				Topping top = new Topping(
						rset.getInt("topping_TopID"),
						rset.getString("topping_TopName"),
						rset.getDouble("topping_SmallAMT"),
						rset.getDouble("topping_MedAMT"),
						rset.getDouble("topping_LgAMT"),
						rset.getDouble("topping_XLAMT"),
						rset.getDouble("topping_CustPrice"),
						rset.getDouble("topping_BusPrice"),
						rset.getInt("topping_MinINVT"),
						rset.getInt("topping_CurINVT")
				);
				top.setDoubled(rset.getBoolean("topping_IsDouble"));
				pizzaToppings.add(top);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		//conn.close();

		return pizzaToppings;
	}

	// COMPLETE - Jenna
	public static void addToInventory(int toppingID, double quantity) throws SQLException, IOException {
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 *
		 * */
		connect_to_db();

		try {
			PreparedStatement os;
			String query;
			query = "UPDATE topping SET topping_CurINVT = topping_CurINVT + ? WHERE topping_TopID = ?;";
			os = conn.prepareStatement(query);
			os.setDouble(1, quantity);
			os.setInt(2, toppingID);
			os.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}
		conn.close();
	}

	// COMPLETE -Jenna
	public static ArrayList<Pizza> getPizzas(Order o) throws SQLException, IOException {
		/*
		 * Build an ArrayList of all the Pizzas associated with the Order.
		 *
		 */
		//connect_to_db();
		ArrayList<Pizza> pizzas = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT p.pizza_PizzaID, p.pizza_Size, p.pizza_CrustType, p.pizza_PizzaState, " +
					"p.pizza_PizzaDate, p.pizza_CustPrice, p.pizza_BusPrice, o.ordertable_OrderID " +
					"from pizza p " +
					"JOIN ordertable o ON p.ordertable_OrderID = o.ordertable_OrderID " +
					"WHERE p.ordertable_OrderID =?;";
			os = conn.prepareStatement(query);
			os.setInt(1, o.getOrderID());
			rset = os.executeQuery();
			while (rset.next()) {
				Pizza pizza = new Pizza(
						rset.getInt("pizza_PizzaID"),
						rset.getString("pizza_Size"),
						rset.getString("pizza_CrustType"),
						rset.getInt("ordertable_OrderID"),
						rset.getString("pizza_PizzaState"),
						rset.getString("pizza_PizzaDate"),
						rset.getDouble("pizza_CustPrice"),
						rset.getDouble("pizza_BusPrice")
				);
				pizzas.add(pizza);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		//conn.close();

		return pizzas;
	}

	//COMPLETE -Jenna
	public static ArrayList<Discount> getDiscounts(Order o) throws SQLException, IOException {
		/*
		 * Build an array list of all the Discounts associted with the Order.
		 *
		 */
		// connect_to_db();
		ArrayList<Discount> discounts = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT d.discount_DiscountID, d.discount_DiscountName, d.discount_Amount, d.discount_IsPercent " +
					"from discount d " +
					"JOIN order_discount od ON d.discount_DiscountID = od.discount_DiscountID " +
					"WHERE od.ordertable_OrderID = ?;";
			os = conn.prepareStatement(query);
			os.setInt(1, o.getOrderID());
			rset = os.executeQuery();
			while (rset.next()) {
				Discount disc = new Discount(
						rset.getInt("discount_DiscountID"),
						rset.getString("discount_DiscountName"),
						rset.getDouble("discount_Amount"),
						rset.getBoolean("discount_IsPercent")
				);
				discounts.add(disc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		//conn.close();

		return discounts;
	}

	//COMPLETE -Jenna
	public static ArrayList<Discount> getDiscounts(Pizza p) throws SQLException, IOException {
		/*
		 * Build an array list of all the Discounts associted with the Pizza.
		 *
		 */
		//connect_to_db();
		ArrayList<Discount> discounts = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT d.discount_DiscountID, d.discount_DiscountName, d.discount_Amount, d.discount_IsPercent " +
					"from discount d " +
					"JOIN pizza_discount pd ON d.discount_DiscountID = pd.discount_DiscountID " +
					"WHERE pd.pizza_pizzaID = ?;";
			os = conn.prepareStatement(query);
			os.setInt(1, p.getPizzaID());
			rset = os.executeQuery();
			while (rset.next()) {
				Discount disc = new Discount(
						rset.getInt("discount_DiscountID"),
						rset.getString("discount_DiscountName"),
						rset.getDouble("discount_Amount"),
						rset.getBoolean("discount_IsPercent")
				);
				discounts.add(disc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}
		//conn.close();

		return discounts;

	}

	// COMPLETE - ELLE
	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException
	{
		/* 
		 * Query the database fro the base customer price for that size and crust pizza.
		 * 
		*/
		connect_to_db();
		double price = 0.0;

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select pizza_CustPrice From pizza WHERE pizza_Size=? AND pizza_CrustType=?;";
			os = conn.prepareStatement(query);
			os.setString(1, size);
			os.setString(2, crust);
			rset = os.executeQuery();
			while(rset.next())
			{
				double custPrice = rset.getDouble("pizza_custPrice");
				price = custPrice;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return price;
	}

	// COMPLETE - ELLE
	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException 
	{
		/* 
		 * Query the database fro the base business price for that size and crust pizza.
		 * 
		*/
		connect_to_db();
		double price = 0.0;

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select pizza_BusPrice From pizza WHERE pizza_Size=? AND pizza_CrustType=?;";
			os = conn.prepareStatement(query);
			os.setString(1, size);
			os.setString(2, crust);
			rset = os.executeQuery();
			while(rset.next())
			{
				double busPrice = rset.getDouble("pizza_busPrice");
				price = busPrice;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return price;
	}

	// COMPLETE - ELLE
	public static void printToppingReport() throws SQLException, IOException
	{
		/*
		 * Prints the ToppingPopularity view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 * HINT: You need to match the expected output EXACTLY....I would suggest
		 * you look at the printf method (rather that the simple print of println).
		 * It operates the same in Java as it does in C and will make your code
		 * better.
		 * 
		 */
		connect_to_db();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select * From ToppingPopularity;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();

			System.out.printf("%-25s %-15s\n", "Topping", "Topping Count");
			System.out.printf("%-25s %-15s\n", "-------", "-------------");

			while(rset.next()) {

				String topName = rset.getString("Topping");
				int count = rset.getInt("ToppingCount");

				System.out.printf("%-25s %-15d\n", topName, count);

			}

			os.close();
			rset.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}

	// COMPLETE - ELLE
	public static void printProfitByPizzaReport() throws SQLException, IOException 
	{
		/*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 * HINT: You need to match the expected output EXACTLY....I would suggest
		 * you look at the printf method (rather that the simple print of println).
		 * It operates the same in Java as it does in C and will make your code
		 * better.
		 * 
		 */
		connect_to_db();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select * From ProfitByPizza ORDER BY Profit;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();

			System.out.printf("%-15s %-15s %-10s %-20s\n", "Pizza Size", "Pizza Crust", "Profit", "Last Order Date");
			System.out.printf("%-15s %-15s %-10s %-20s\n", "----------", "-----------", "------", "---------------");

			while(rset.next()) {

				String size = rset.getString("Size");
				String crust = rset.getString("Crust");
				double profit = rset.getDouble("Profit");
				String date = rset.getString("OrderMonth");

				System.out.printf("%-15s %-15s %-9.2f %-20s\n", size, crust, profit, date);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}

	// COMPLETE - ELLE
	public static void printProfitByOrderTypeReport() throws SQLException, IOException
	{
		/*
		 * Prints the ProfitByOrderType view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 *
		 * HINT: You need to match the expected output EXACTLY....I would suggest
		 * you look at the printf method (rather that the simple print of println).
		 * It operates the same in Java as it does in C and will make your code
		 * better.
		 * 
		 */
		connect_to_db();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select * From ProfitByOrderType;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();

			System.out.printf("%-15s %-15s %-20s %-20s %-10s\n", "Customer Type", "Order Month", "Total Order Price", "Total Order Cost", "Profit");
			System.out.printf("%-15s %-15s %-20s %-20s %-10s\n", "-------------", "-----------", "-----------------", "----------------", "------");

			while(rset.next()) {

				String type = rset.getString("customerType");
				String date = rset.getString("OrderMonth");
				double price = rset.getDouble("TotalOrderPrice");
				double cost = rset.getDouble("TotalOrderCost");
				double profit = rset.getDouble("Profit");

				if (date.equals("Grand Total")) {
					System.out.printf("%-31s $%-19.2f $%-19.2f %-10.2f\n", "Grand Total", price, cost, profit);
				} else {
					System.out.printf("%-15s %-15s $%-19.2f $%-19.2f %-10.2f\n", type, date, price, cost, profit);
				}

			}
			os.close();
			rset.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}

	/*
	 * These private methods help get the individual components of an SQL datetime object. 
	 * You're welcome to keep them or remove them....but they are usefull!
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}