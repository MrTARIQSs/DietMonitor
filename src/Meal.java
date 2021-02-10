import java.util.*;

public class Meal {

	private int day;
	private HashMap<Integer,ArrayList<Edible>> map = new HashMap<>();

	public Meal() {
	}
	
	public void makeMeal(Edible edible,int day) {
		this.day = day;
		if(edible!=null) {
		if (!map.containsKey(day))
			map.put(day, new ArrayList<Edible>());
		map.get(day).add(edible);
		Collections.sort(map.get(day), new Comparator<Edible>() {
		    public int compare(Edible lhs, Edible rhs) {
		        return lhs.getName().charAt(0) < rhs.getName().charAt(0) ? -1 : (lhs.getName().charAt(0) > rhs.getName().charAt(0)) ? 1 : 0;
		    }
		});
		}
	}
	
	public void Clear() {
		if(map.get(day)!=null)
		map.get(day).clear();
	}
	
	public void check() throws tooFatException {
		double sum1 = 0, sum2=0;
		for (int i = 0; i < map.get(day).size(); i++) {
			sum1 += map.get(day).get(i).getCarb() + map.get(day).get(i).getProtein() + map.get(day).get(i).getFat();
			sum2 += map.get(day).get(i).getPortionSize();
			if (sum1 > sum2) {
				throw new tooFatException(); // in case it exceeds the specified limit
			}
			
		}
		
	}
	
	public HashMap<Integer,ArrayList<Edible>> getMeals(){
		return map;
	}

}
