package finalexam;

public class Order {

	private double burgers;
	private double fries;
	private double shakes;
	private double orderNumber;
	public static final double salesTax = 1.06;

	public Order(double orderNumber, double burgers, double fries, double shakes) {
		this.orderNumber = orderNumber;
		this.burgers = burgers;
		this.fries = fries;
		this.shakes = shakes;

	}

	public Order(double burgers, double fries, double shakes) {
		this(1000, burgers, fries, shakes);
	}

	public double getTotal() {

		double total = ((5 * burgers) + (fries * 2) + (shakes * 3.5)) * salesTax;
		return Math.round(total * 100) / 100.0;

	}

	public double getBurgers() {
		return burgers;
	}

	public double getFries() {
		return fries;
	}

	public double getShakes() {
		return shakes;
	}

	public double getOrderNumber() {
		return orderNumber;
	}

}
