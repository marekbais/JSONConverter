package conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

public class JSONConverterTest {

	JSONConverter converter = new JSONConverter();
	
	public JSONArray prepareProfileJSONArray() {

		Profile profile1 = new Profile("fname1", "lname1", 1, new Address("street1", "city1", "zip1"), null);
		Profile profile2 = new Profile("fname2", "lname2", 2, new Address("street2", "city2", "zip2"), null);
		JSONArray jsonArrayProfiles = new JSONArray();
		JSONObject jsonAddress1 = JSONCreator.createJSONAddress(profile1);
		JSONObject jsonAddress2 = JSONCreator.createJSONAddress(profile2);
		JSONObject jsonProfile1 = new JSONObject();
		JSONObject jsonProfile2 = new JSONObject();
		
		jsonProfile1.put("first name", profile1.getFirstName());
		jsonProfile1.put("last name", profile1.getLastName());
		jsonProfile1.put("age", profile1.getAge());
		jsonProfile1.put("address", jsonAddress1);
		jsonProfile1.put("contact", null);
		
		jsonProfile2.put("first name", profile2.getFirstName());
		jsonProfile2.put("last name", profile2.getLastName());
		jsonProfile2.put("age", profile2.getAge());
		jsonProfile2.put("address", jsonAddress2);
		jsonProfile2.put("contact", null);
		
		jsonArrayProfiles.add(jsonProfile1);
		jsonArrayProfiles.add(jsonProfile2);

		return jsonArrayProfiles;
	}
	
	public JSONArray prepareCityJSONArray() {
		List<String> streets1 = new ArrayList<>(Arrays.asList("street1"));
		List<String> zips1 = new ArrayList<>(Arrays.asList("zip1"));
		List<String> residents1 = new ArrayList<String>(Arrays.asList("fname1 lname1"));
		List<String> streets2 = new ArrayList<>(Arrays.asList("street2"));
		List<String> zips2 = new ArrayList<>(Arrays.asList("zip2"));
		List<String> residents2 = new ArrayList<String>(Arrays.asList("fname2 lname2"));
		
		JSONObject jsonCity1 = new JSONObject();
		jsonCity1.put("name", "city1");
		jsonCity1.put("meanAge", 1.0);
		jsonCity1.put("streets", streets1);
		jsonCity1.put("zips", zips1);
		jsonCity1.put("residents", residents1);
		JSONObject jsonCity2 = new JSONObject();
		jsonCity2.put("name", "city2");
		jsonCity2.put("meanAge", 2.0);
		jsonCity2.put("streets", streets2);
		jsonCity2.put("zips", zips2);
		jsonCity2.put("residents", residents2);
		
		JSONArray cityArray = new JSONArray();
		cityArray.add(jsonCity1);
		cityArray.add(jsonCity2);
		return cityArray;
	}
	
	@Test
	public void extractObjectsFromJSONTest() {
		//empty
		converter.setReadPath("");
		List<JSONObject> expected = new ArrayList<>();
		
		assertEquals(expected.toString(), converter.extractObjectsFromJSON().toString());
		
		//single object
		converter.setReadPath("src/test/resources/BasicTestIn1.json");
		JSONArray profileJsonArray = prepareProfileJSONArray();
		expected.add((JSONObject) profileJsonArray.get(0));

		assertEquals(expected.toString(), converter.extractObjectsFromJSON().toString());
		
		//2 objects
		converter.setReadPath("src/test/resources/BasicTestIn2.json");
		expected.add((JSONObject) profileJsonArray.get(1));
		
		assertEquals(expected.toString(), converter.extractObjectsFromJSON().toString());
	}
	
	@Test
	public void calculateMeanAgeTest() {
		List<String> residents1 = new ArrayList<>(Arrays.asList("r1", "r2"));
		List<String> residents2 = new ArrayList<>(Arrays.asList("r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9", "r10"));
		
		long ageSum1 = 2;
		long ageSum2 = 1548486446;
		long ageSum3 = -1;

		double meanAge1 = (double) ageSum1 / residents1.size(); 
		double meanAge2 = (double) ageSum2 / residents2.size();
		double meanAge3 = (double) ageSum3 / residents1.size();
		
		JSONObject cityJsonObject = mock(JSONObject.class);
		when(cityJsonObject.get("meanAge")).thenReturn(ageSum1)
									       .thenReturn(ageSum2)
									       .thenReturn(ageSum3);

		when(cityJsonObject.get("residents")).thenReturn(residents1)
										     .thenReturn(residents2)
										     .thenReturn(residents1);

		
		assertEquals(converter.calculateMeanAge(cityJsonObject), meanAge1, 0.001);
		assertEquals(converter.calculateMeanAge(cityJsonObject), meanAge2, 0.001);
		assertEquals(converter.calculateMeanAge(cityJsonObject), meanAge3, 0.001);
	}
	
	@Test
	public void convertJSONsTest() throws Exception {
		List<JSONObject> objectsExtracted = new ArrayList<>();
		for(Object profileObject : prepareProfileJSONArray()) {
			objectsExtracted.add((JSONObject) profileObject);
		}
		JSONConverter spyExtractionConverter = spy(converter);
		spyExtractionConverter.setReadPath("");
		when(spyExtractionConverter.extractObjectsFromJSON()).thenReturn(objectsExtracted);

		assertEquals(prepareCityJSONArray(), spyExtractionConverter.convertJSONs());
		verify(spyExtractionConverter, times(prepareCityJSONArray().size())).calculateMeanAge((JSONObject) any());
	}
	
	@Test
	public void convertJSONsFileWithWrongObjectsTest() {
		try {
			converter.convertFromTo("src/test/resources/WrongFileIn.json", "src/test/resources/WrongFileOut.json");
			fail("Expected an Exception");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "Object invalid");
		}
	}
	
	@Test
	public void completeConversionTest() throws Exception {
		converter.convertFromTo("src/test/resources/CompleteConversionTestIn1.json", "src/test/resources/CompleteConversionTestOut1.json");
		converter.convertFromTo("src/test/resources/CompleteConversionTestIn2.json", "src/test/resources/CompleteConversionTestOut2.json");
		converter.convertFromTo("src/test/resources/CompleteConversionTestIn3.json", "src/test/resources/CompleteConversionTestOut3.json");
		converter.convertFromTo("src/test/resources/CompleteConversionTestIn4.json", "src/test/resources/CompleteConversionTestOut4.json");
		
		JSONParser parser = new JSONParser();
		String converted1 = parser.parse(new FileReader("src/test/resources/CompleteConversionTestOut1.json")).toString();
		String expected1 = "[]";
		
		String converted2 = parser.parse(new FileReader("src/test/resources/CompleteConversionTestOut2.json")).toString();
		String expected2 = "[{\"meanAge\":0.0,\"streets\":[],\"name\":\"not stated\",\"residents\":[],\"zips\":[]}]";
		
		String converted3 = parser.parse(new FileReader("src/test/resources/CompleteConversionTestOut3.json")).toString();
		String expected3 = "[{\"meanAge\":1.0,\"streets\":[\"street1\"],\"name\":\"city1\",\"residents\":[\"fname1 lname1\"],\"zips\":[\"zip1\"]},"
						  + "{\"meanAge\":2.0,\"streets\":[\"street2\"],\"name\":\"city2\",\"residents\":[\"fname2 lname2\"],\"zips\":[\"zip2\"]},"
						  + "{\"meanAge\":3.0,\"streets\":[\"street3\"],\"name\":\"city3\",\"residents\":[\"fname3 lname3\"],\"zips\":[\"zip3\"]},"
						  + "{\"meanAge\":4.5,\"streets\":[],\"name\":\"not stated\",\"residents\":[\"fname4 lname4\",\"fname5 lname5\"],\"zips\":[]}]";
		
		String converted4 = parser.parse(new FileReader("src/test/resources/CompleteConversionTestOut4.json")).toString();
		String expected4 = "[{\"meanAge\":64047.5,\"streets\":[\"street1\",\"street2\",\"street3\",\"street4\",\"street5\"],\"name\":\"city1\"," 
						   + "\"residents\":[\"fname1 lname1\",\"fname2 lname2\",\"fname3 lname3\",\"fname4 lname4\",\"fname5 lname5\",\"fname6 lname6\",\"fname7 lname7\",\"fname8 lname8\",\"fname9 lname9\",\"fname10 lname10\"],"
						   + "\"zips\":[\"zip1\",\"zip2\",\"zip3\",\"zip4\",\"zip5\"]}]";
		
		assertEquals(expected1, converted1);
		assertEquals(expected2, converted2);
		assertEquals(expected3, converted3);
		assertEquals(expected4, converted4);
	}
	
}
