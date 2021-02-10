
public class Food extends Edible {

	public Food(String name, double portionSize, String unit, double cal, double fat, double carb, double protein) {
		super(name, portionSize, unit, cal, fat, carb, protein);
	}

	public String toString() {
		return super.toString()+"\nType: food";
	}
}
