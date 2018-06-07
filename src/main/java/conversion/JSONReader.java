package conversion;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {

	/*
	 *  returns JSONArray from JSON file
	 */
	public JSONArray extractJSONArrayFromFile(String path) {
		JSONParser parser = new JSONParser();
		JSONArray citiesJSONArray = new JSONArray();

		try {
			citiesJSONArray = (JSONArray) parser.parse(new FileReader(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return citiesJSONArray;
	}

	/*
	 *  builds a City instance from JSONObject data
	 */
	public City extractCityFromJSONObject(JSONObject jsonObject) {
		String name = jsonObject.get("name").toString();
		double meanAge = Double.parseDouble(jsonObject.get("meanAge").toString());
		List<String> streets = (List<String>) jsonObject.get("streets");
		List<String> zips = (List<String>) jsonObject.get("zips");
		List<String> residents = (List<String>) jsonObject.get("residents");

		return new City(name, streets, zips, residents, meanAge);
	}

	/*
	 *  returns all cities in JSONArray
	 */
	public List<City> extractCitiesFromJSONArray(JSONArray jsonArray) {
		List<City> cities = new ArrayList<>();
		for (Object jsonObject : jsonArray) {
			City newCity = extractCityFromJSONObject((JSONObject) jsonObject);
			cities.add(newCity);
		}
		return cities;
	}

	/*
	 *  uses all of the methods above to give final output
	 */
	public List<City> readJSON(String path) {
		return extractCitiesFromJSONArray(extractJSONArrayFromFile(path));
	}
}
