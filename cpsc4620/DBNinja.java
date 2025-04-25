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


	private static boolean connect_to_db() throws SQLException, IOException 
	{

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	public static void addOrder(Order o) throws SQLException, IOException 
	{
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
	}
	
	public static int addPizza(java.util.Date d, int orderID, Pizza p) throws SQLException, IOException
	{
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

		return -1;
	}

	// IN PROGRESS- NEED GET METHODS FOR FIELDS
	public static int addCustomer(Customer c) throws SQLException, IOException
	 {
		/*
		 * This method adds a new customer to the database.
		 * 
		 */
		
		 connect_to_db();

		 try {
			 PreparedStatement os;
			 ResultSet rset;
			 String query = "INSERT INTO customer (customer_Fname, customer_Lname, customer_PhoneNum)" +
					 "VALUES (?, ?, ?)";
			 os = conn.prepareStatement(query);

			 os.setString(1, c.getFName());
			 os.setString(2, c.getLName());
			 os.setString(3, c.getPhoneNum());
			 os.executeUpdate();
		 } catch (SQLException e) {
			 e.printStackTrace();
			 // process the error or re-raise the exception to a higher level
		 }

		 conn.close();


		 //return new customer's ID
		 return -1;
	}

	//IN PROGRESS- JENNA
	public static void completeOrder(int OrderID, order_state newState ) throws SQLException, IOException
	{
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

			String query = ""

	}


	public static ArrayList<Order> getOrders(int status) throws SQLException, IOException
	 {
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

		return null;
	}
	
	// COMPLETE- JENNA
	public static Order getLastOrder() throws SQLException, IOException
	{
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
			query = "SELECT * From ordertable ORDER BY OrderDateTime DESC LIMIT 1;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while (rset.next())
			{
				int orderid = rset.getInt("orderTable_OrderID");
				string type = rset.getString("ordertable_OrderType");
				DATETIME datetime = rset.getDatetime("ordertable_OrderDateTime");
				decimal custprice = rset.getDecimal("ordertable_CustPrice");
				decimal busprice = rset.getDecimal("ordertable_BusPrice");
				boolean complete = rset.getBoolean("ordertable_isComplete");
				int custid = rset.getInt("customer_CustID");
				order = new Order(orderid, type, datetime, custprice, busprice, complete, custid);

			}
		} catch(SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		 return order;
	}

	// COMPLETE - Jenna
	public static ArrayList<Order> getOrdersByDate(String date) throws SQLException, IOException
	 {
		/*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *  
		 */
		 return null;

		 connect_to_db();

		 ArrayList<ordertable> orderList = new ArrayList<>();

		 try{
			 PreparedStatement ps;
			 ResultSet rset;
			 String query;
			 query = "Select ordertable_OrderID, ordertable_OrderType,"
					 + "ordertable_CustPrice, ordertable_BusPrice, ordertable_isComplete, customer_CustID" +
					 "From ordertable Where ordertable_OrderDateTime = ?;";
			 ps.conn.prepareStatement(query);
			 ps.setString(1, date);
			 rset = ps.executeQuery();
			 while(rset.next())
			 {
				 int id = rset.getInt("ordertable_OrderID");
				 String ordertype = rset.getString("ordertable_OrderType");
				 decimal custprice = rset.getdecimal("ordertable_CustPrice");
				 decimal busprice = rset.getdecimal("ordertable_BusPrice");
				 boolean complete = rset.getBoolean("ordertable_isComplete");
				 int cusid = rset.getInt("customer_CustID");
				 ordertable order = new ordertable(id, ordertype, date, cusprice, busprice, complete, cusid);
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
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException 
	{
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
			query = "Select discount_DiscountID, discount_DiscountName, discount_Amount, discount_IsPercent From discount" +
					"ORDER BY discount_DiscountName;";
			ps = conn.prepareStatement(query);
			rset = os.executeQuery();
			while(rset.next())
			{
				int id = rset.getInt("dicount_DiscountID");
				String name = rset.getString("discount_DiscountName");
				Decimal amount = rset.getDecimal("discount_Amount");
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
	public static Discount findDiscountByName(String name) throws SQLException, IOException 
	{
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
			query = "Select discount_DiscountID, discount_DiscountName, discount_Amount, discount_IsPercent" +
					"from Discount Where discount_DiscountName=?;";
			ps = conn.prepareStatement(query);
			ps.setString(1, discountname);
			rset = os.executeQuery();
			while(rset.next())
			{
				int id = rset.getInt("discount_DiscountID");
				String name = rset.getString("discount_DiscountName");
				Decimal amount = rset.getDecimal("discount_Amount");
				Boolean ispercent = rset.getBoolean("discount_IsPercent");
				discount = new Discount(id, name, amount, ispercent);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}


		conn.close();
		return discount;
	}


	// COMPLETE - ELLE
	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException 
	{
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
			query = "Select customer_CustID, customer_FName, customer_LName, customer_Phone From customer ORDER BY customer_LName, customer_FName;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while(rset.next())
			{
				int id = rset.getInt("customer_CustID");
				String fname = rset.getString("customer_FName");
				String lname = rset.getString("customer_LName");
				String phone = rset.getString("customer_Phone"); // note the use of field names in the getSting methods
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
	public static Customer findCustomerByPhone(String phoneNumber)  throws SQLException, IOException 
	{
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
			query = "Select customer_CustID, customer_FName, customer_LName From customer WHERE customer_Phone=?;";
			os = conn.prepareStatement(query);
			os.setString(1, phoneNumber);
			rset = os.executeQuery();
			while(rset.next())
			{
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
	public static String getCustomerName(int CustID) throws SQLException, IOException 
	{
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
		
		while(rset.next())
		{
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
			while(rset2.next())
			{
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
	public static ArrayList<Topping> getToppingList() throws SQLException, IOException 
	{
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
			query = "Select * From Topping WHERE topping_CurINVT > 0 ORDER BY topping_TopName;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while(rset.next())
			{
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
	public static Topping findToppingByName(String name) throws SQLException, IOException 
	{
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
			query = "Select * From Topping Where Topping_CurINVT > 0 ORDER BY Topdping_TopName;";
			os = conn.prepareStatement(query);
			os.setString(1, name);
			rset = os.executeQuery();
			while(rset.next())
			{
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

	// COMPLETE? - ELLE
	public static ArrayList<Topping> getToppingsOnPizza(Pizza p) throws SQLException, IOException 
	{
		/* 
		 * This method builds an ArrayList of the toppings ON a pizza.
		 * The list can then be added to the Pizza object elsewhere in the
		 */

		connect_to_db();
		ArrayList<Topping> pizzaToppings = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select t.TopID, t.TopName, t.SmallAMT, t.MedAMT, t.LgAMT, t.XLAMT, t.CustPrice, t.BusPrice, t.MinINVT, t.CurINVT, t.IsDouble " +
					"From topping t" +
					"JOIN pizza_topping pt ON t.TopID = pt.TopID" +
					"WHERE Pizza ID =?;";
			os = conn.prepareStatement(query);
			os.setInt(1, p.getPizzaID());
			rset = os.executeQuery();
			while(rset.next())
			{
				Topping top = new Topping (
						rset.getInt("TopID"),
						rset.getString("TopName"),
						rset.getDouble("SmallAMT"),
						rset.getDouble("MedAMT"),
						rset.getDouble("LgAMT"),
						rset.getDouble("XLAMT"),
						rset.getDouble("CustPrice"),
						rset.getDouble("BusPrice"),
						rset.getInt("MinINVT"),
						rset.getInt("CurINVT")
				);
				top.setDoubled(rset.getBoolean("IsDouble"));
				pizzaToppings.add(top);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return pizzaToppings;
	}

	// COMPLETE - ELLE
	public static void addToInventory(int toppingID, double quantity) throws SQLException, IOException 
	{
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 * 
		 * */
		connect_to_db();

		try {
			PreparedStatement os;
			String query;
			query = "UPDATE Topping SET CurINVT = CurINVT + ? WHERE TopID = ?;";
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
	
	
	public static ArrayList<Pizza> getPizzas(Order o) throws SQLException, IOException 
	{
		/*
		 * Build an ArrayList of all the Pizzas associated with the Order.
		 * 
		 */
		return null;
	}

	//COMPLETE ? - ELLE
	public static ArrayList<Discount> getDiscounts(Order o) throws SQLException, IOException 
	{
		/* 
		 * Build an array list of all the Discounts associted with the Order.
		 * 
		 */

		connect_to_db();
		ArrayList<Discount> orderDiscounts = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select D.DiscountID, D.DiscountName, D.Amount, D.isPercent" +
					"From OrderDiscount OD" +
					"JOIN Discount D ON OD.DiscountID = D.DiscountID" +
					"WHERE OD.OrderID =?;";
			os = conn.prepareStatement(query);
			os.setInt(1, o.getOrderID());
			rset = os.executeQuery();
			while(rset.next())
			{
				Discount dis = new Discount (
						rset.getInt("DiscountID"),
						rset.getString("DiscountName"),
						rset.getDouble("Amount"),
						rset.getBoolean("IsPercent")
				);
				orderDiscounts.add(dis);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();
		return  orderDiscounts;
	}

	// COMPLETE - ELLE
	public static ArrayList<Discount> getDiscounts(Pizza p) throws SQLException, IOException 
	{
		/* 
		 * Build an array list of all the Discounts associted with the Pizza.
		 * 
		 */

		connect_to_db();
		ArrayList<Discount> pizzaDiscounts = new ArrayList<>();

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "Select D.DiscountID, D.DiscountName, D.Amount, D.isPercent" +
					"From PizzaDiscount PD" +
					"JOIN Discount D ON PD.DiscountID = D.DiscountID" +
					"WHERE PD.PizzaID =?;";
			os = conn.prepareStatement(query);
			os.setInt(1, p.getPizzaID());
			rset = os.executeQuery();
			while(rset.next())
			{
				Discount dis = new Discount (
						rset.getInt("DiscountID"),
						rset.getString("DiscountName"),
						rset.getDouble("Amount"),
						rset.getBoolean("IsPercent")
				);
				pizzaDiscounts.add(dis);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();
		return  pizzaDiscounts;

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
			query = "Select pizza_CustPrice From Pizza WHERE pizza_Size=? AND pizza_CrustType=?;";
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
			query = "Select pizza_BusPrice From Pizza WHERE pizza_Size=? AND pizza_CrustType=?;";
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
			query = "Select * From ToppingPopularity ORDER BY ToppingCount DESC TopName ASC;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();

			System.out.printf("%-20s %-20s\n", "Topping", "Topping Count");
			System.out.printf("%-20s %-20s\n", "-------", "-------------");

			while(rset.next()) {

				String topName = rset.getString("TopName");
				int count = rset.getInt("ToppingCount");

				System.out.printf("%-20s %-20s\n", topName, count);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}

	// COMPLETED - ELLE
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

				String size = rset.getString("PizzaSize");
				String crust = rset.getString("PizzaCrust");
				double profit = rset.getDouble("Profit");
				String date = rset.getString("LastOrderDate");

				System.out.printf("%-15s %-15s %-9.2f %-20s\n", size, crust, profit, date);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}

	// COMPLETED - ELLE
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
			query = "Select * From ProfitByOrderType ORDER BY OrderType, OrderMonth;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();

			System.out.printf("%-15s %-15s %-20s %-20s %-10s\n", "Customer Type", "Order Month", "Total Order Price", "Total Order Cost", "Profit");
			System.out.printf("%-15s %-15s %-20s %-20s %-10s\n", "-------------", "-----------", "-----------------", "----------------", "------");

			double totalPrice = 0.0;
			double totalCost = 0.0;
			double totalProfit = 0.0;

			while(rset.next()) {

				String type = rset.getString("OrderType");
				String date = rset.getString("OrderMonth");
				double price = rset.getDouble("TotalCustPrice");
				double cost = rset.getDouble("TotalBusPrice");
				double profit = rset.getDouble("Profit");

				System.out.printf("%-15s %-15s $%-19.2f $%-19.2f %-10.2f\n", type, date, price, cost, profit);

				totalPrice += price;
				totalCost += cost;
				totalProfit += profit;
			}

			System.out.printf("%-31s $%-19.2f $%-19.2 %-10.2f\n", "Grand Total", totalPrice, totalCost, totalProfit);

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