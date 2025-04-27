package cpsc4620;

public class DeliveryOrder extends Order
{
	private String Address;

	private boolean isDelivered;

	public DeliveryOrder(int orderID, int custID, String date, double custPrice, double busPrice, boolean isComplete, String address)
	{
		super(orderID, custID, DBNinja.delivery, date, custPrice, busPrice, isComplete);
		this.Address = address;
		this.isDelivered = false;
	}
	public DeliveryOrder(int orderID, int custID, String date, double custPrice, double busPrice, boolean isComplete, boolean isDelivered, String address)
	{
		super(orderID, custID, DBNinja.delivery, date, custPrice, busPrice, isComplete);
		this.Address = address;
		this.isDelivered = isDelivered;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	@Override
	public String toString() {
		return super.toString() + " | Delivered to: " + Address + " | Order Delivered: " + ((isDelivered)?"Yes":"No");
	}

	// HELPER METHODS TO GET ADDRESS COMPONENTS
	public int getHouseNum(String address) {
		if (address == null || address.isEmpty()) return 0;
		try {
			String[] parts = address.split("\t");
			return Integer.parseInt(parts[0].trim());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;

		}
	}
	public String getStreet(String address) {
			if (address == null || address.isEmpty()) return "";
			try {
				String[] parts = address.split("\t");
				return parts[1].trim();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
	}

	public String getCity(String address) {
			if (address == null || address.isEmpty()) return "";
			try {
				String[] parts = address.split("\t");
				return parts[2].trim();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
	}

	public String getState(String address) {
			if (address == null || address.isEmpty()) return "";
			try {
				String[] parts = address.split("\t");
				return parts[3].trim();
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
	}

	public int getZip(String address) {
			if (address == null || address.isEmpty()) return 0;
			try {
				String[] parts = address.split("\t");
				return Integer.parseInt(parts[4].trim());
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
	}


	}
