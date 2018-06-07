package conversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

	public static void main(String[] args) throws Exception {
		List<Contact> contact1 = new ArrayList<>();
		contact1.add(new Contact("type1", "contact1"));
		contact1.add(new Contact("type2", "contact2"));

		Profile profile1 = new Profile("fname1", "lname1", -1, new Address("street1", "city1", "zip1"), null);
		Profile profile2 = new Profile("fname2", "lname2", 2, new Address("street2", "city1", "zip2"), contact1);
		Profile profile3 = new Profile("fname3", "lname3", 3, new Address("street3", "city1", "zip3"), contact1);
		Profile profile4 = new Profile("fname4", "lname4", 4, new Address("street4", "city1", "zip4"), null);
		Profile profile5 = new Profile("fname5", "lname5", 5, new Address("street5", "city1", "zip5"), null);
		Profile profile6 = new Profile("fname6", "lname6", 45243, new Address("street5", "city2", "zip5"), null);
		Profile profile7 = new Profile("fname7", "lname7", 453453, new Address("street5", "city2", "zip5"), null);
		Profile profile8 = new Profile("fname8", "lname8", 85776, new Address("street5", "city2", "zip5"), null);
		Profile profile9 = new Profile("fname9", "lname9", 435, new Address("street5", "city2", "zip5"), null);
		Profile profile10 = new Profile("fname10", "lname10", 55555, new Address("street5", "city2", "zip5"), null);
		
		List<Profile> profiles = new ArrayList<>(Arrays.asList(profile1, profile2, profile3, profile4, profile5, 
												 profile6, profile7, profile8, profile9, profile10));
		
		JSONCreator jsonCreator = new JSONCreator();
		jsonCreator.writeJSON(profiles, "src/main/resources/SampleJSONIn.json");
		
		JSONConverter jsonConverter = new JSONConverter();
		jsonConverter.convertFromTo("src/main/resources/SampleJSONIn.json","src/main/resources/SampleJSONOut.json");
		
		JSONReader jsonReader = new JSONReader();
		System.out.println(jsonReader.extractCities("src/main/resources/SampleJSONOut.json").toString());
	}

}
