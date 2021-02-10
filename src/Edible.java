
public class Edible {
	private double cal;
	private double protein;
	private double carb;
	private double portionSize;
	private double fat;
	String name;
	String unit;

	public Edible(String name, double portionSize, String unit, double cal, double fat, double carb, double protein) {
		this.name = name;
		this.portionSize = portionSize;
		this.unit = unit;
		this.cal = cal;
		this.fat = fat;
		this.carb = carb;
		this.protein = protein;
	}


	public double getCal() {
		return cal;
	}

	public void setCal(double cal) {
		this.cal = cal;
	}

	public double getProtein() {
		return protein;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public double getCarb() {
		return carb;
	}

	public void setCarb(double carb) {
		this.carb = carb;
	}

	public double getPortionSize() {
		return portionSize;
	}

	public void setPortionSize(double portionSize) {
		this.portionSize = portionSize;
	}

	public double getFat() {
		return fat;
	}

	public void setFat(double fat) {
		this.fat = fat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String toString() {
		
			return "Name: " + name + "\n" + "Portion Size: " + portionSize + unit + "\n" +"Calories: "+ cal + "\n" +"Fats: "+ fat + "\n" +"Carbohydrates: "+ carb + "\n" +"Proteins: "+ protein;
	}

}
