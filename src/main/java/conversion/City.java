package conversion;

import java.util.List;

public class City {
	private String name;
	private List<String> streets;
	private List<String> zips;
	private List<String> residents;
	private double meanAge;
	
	public City(String name, 
			    List<String> streets, 
			    List<String> zips, 
			    List<String> residents, 
			    double meanAge) {
		this.setName(name);
		this.setStreets(streets);
		this.setZips(zips);
		this.setResidents(residents);
		this.setMeanAge(meanAge);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getStreets() {
		return streets;
	}

	public void setStreets(List<String> streets) {
		this.streets = streets;
	}

	public List<String> getZips() {
		return zips;
	}

	public void setZips(List<String> zips) {
		this.zips = zips;
	}

	public double getMeanAge() {
		return meanAge;
	}

	public void setMeanAge(double meanAge) {
		this.meanAge = meanAge;
	}

	public List<String> getResidents() {
		return residents;
	}

	public void setResidents(List<String> residents) {
		this.residents = residents;
	}
	
	public String toString() {
		return "\nNAME: " + name + "\nStreets: " + streets.toString() + ", Zips: " + zips.toString()
				+ ", Residents: " + residents.toString() + ", Avg age: " + meanAge + "\n" ;
	}
	
}
