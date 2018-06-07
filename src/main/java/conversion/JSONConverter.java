package conversion;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONConverter {

	private String readPath;

	public void setReadPath(String readPath) {
		this.readPath = readPath;
	}

	public String getReadPath() {
		return readPath;
	}

	/*
	 *  extracts JSONObjects from file
	 */
	public List<JSONObject> extractObjectsFromJSON() {
		JSONParser parser = new JSONParser();
		List<JSONObject> profiles = new ArrayList<>();

		try {
			JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(readPath));
			Iterator<Object> profilesItr = jsonArray.iterator();
			while (profilesItr.hasNext()) {
				profiles.add((JSONObject) profilesItr.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return profiles;
	}

	/*
	 * converts JSONs between 2 formats
	 */
	public JSONArray convertJSONs() throws Exception {
		List<JSONObject> listOfProfileObjects = extractObjectsFromJSON();
		List<JSONObject> cities = new ArrayList<>();

		for (JSONObject profileObj : listOfProfileObjects) {
			String cityName;
			JSONObject address;
			String street;
			String zip;
			String resident;

			try {
				address = (JSONObject) profileObj.get("address");
				cityName = (String) address.get("city");
				street = (String) address.get("street");
				zip = (String) address.get("zip");
				resident = (String) (profileObj.get("first name") + " " + profileObj.get("last name"));
			} catch (NullPointerException e) {
				throw new Exception("Object invalid", e);
			}
			if (cityName == null) {
				cityName = "not stated";
			}
			
			boolean cityAlreadyInTheList = false;
			for (JSONObject cityObject : cities) {
				// if city already exists, add the data from profileObj
				if (cityObject.get("name").equals(cityName)) {
					if (!((Collection<String>) cityObject.get("streets")).contains(street)) {
						((Collection<String>) cityObject.get("streets")).add(street);
					}
					if (!((Collection<String>) cityObject.get("zips")).contains(zip)) {
						((Collection<String>) cityObject.get("zips")).add(zip);
					}
					((Collection<String>) cityObject.get("residents")).add(resident);
					cityObject.replace("meanAge", (long) cityObject.get("meanAge") + (long) profileObj.get("age"));
					cityAlreadyInTheList = true;
					break;
				}
			}
			// if city's not in the list yet, add newCity to the list
			if (!cityAlreadyInTheList) {
				JSONObject newCity = new JSONObject();
				List<String> streets = new ArrayList<>(Arrays.asList(street));
				List<String> zips = new ArrayList<>(Arrays.asList(zip));
				List<String> residents = new ArrayList<>(Arrays.asList(resident));
				newCity.put("name", cityName);
				newCity.put("streets", streets);
				newCity.put("zips", zips);
				newCity.put("residents", residents);
				newCity.put("meanAge", profileObj.get("age"));
				cities.add(newCity);
			}
		}
		// divide ages to get mean, then add updated JSONObjects to JSONArray
		JSONArray cityJsonArray = new JSONArray();
		for (JSONObject cityJsonObject : cities) {
			cityJsonObject.replace("meanAge", calculateMeanAge(cityJsonObject));
			cityJsonArray.add(cityJsonObject);
			deleteNulls(cityJsonObject);
		}
		return cityJsonArray;
	}

	/*
	 *  deletes nulls from lists
	 */
	public void deleteNulls(JSONObject cityJsonObject) {
		List<String> nullValues = new ArrayList<>(Arrays.asList(null, "null null"));
		((Collection<String>) cityJsonObject.get("streets")).removeAll(nullValues);
		((Collection<String>) cityJsonObject.get("zips")).removeAll(nullValues);
		((Collection<String>) cityJsonObject.get("residents")).removeAll(nullValues);
	}

	/*
	 *  calculates correct meanAge
	 */
	public double calculateMeanAge(JSONObject cityJsonObject) {
		String strSumOfAges = cityJsonObject.get("meanAge").toString();
		double sumOfAges = Double.parseDouble(strSumOfAges);
		int population = ((List<Object>) cityJsonObject.get("residents")).size();

		DecimalFormat twoDecPlaces = new DecimalFormat("##.##");
		return Double.parseDouble(twoDecPlaces.format((sumOfAges / population)).replaceAll(",", "."));
	}

	/*
	 * uses all of the methods above to convert JSON files
	 */
	public void convertFromTo(String readPath, String writePath) throws Exception {
		setReadPath(readPath);
		try {
			FileWriter fw = new FileWriter(writePath);
			fw.write(convertJSONs().toJSONString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
