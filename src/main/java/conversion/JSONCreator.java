package conversion;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class JSONCreator {

	private List<Profile> profiles;

	/*
	 *  creates a JSON address of a single profile
	 */
	public static JSONObject createJSONAddress(Profile profile) {
		JSONObject jsonAddress = new JSONObject();

		try {
			jsonAddress.put("street", profile.getAddress().getStreet());
			jsonAddress.put("city", profile.getAddress().getCity());
			jsonAddress.put("zip", profile.getAddress().getZip());
		} catch (NullPointerException e) {
			e.getMessage();
		}
		return jsonAddress;
	}

	/*
	 *  creates a JSON contact of a single profile
	 */
	public static JSONArray createJSONContact(Profile profile) {
		JSONArray jsonContact = new JSONArray();
		try {
			for (Contact contact : profile.getContact()) {
				Map<String, String> m = new HashMap<>();
				m.put("type", contact.getType());
				m.put("contact", contact.getContact());
				jsonContact.add(m);
			}
		} catch (NullPointerException e) {
			e.getMessage();
		}
		return jsonContact;
	}

	/*
	 *  creates a JSONArray of all profiles
	 */
	public JSONArray createJSONProfiles() {
		JSONArray jsonProfiles = new JSONArray();

		for (Profile profile : profiles) {
			JSONObject jsonProfile = new JSONObject();
			JSONObject jsonAddress = createJSONAddress(profile);
			JSONArray jsonContact = createJSONContact(profile);

			jsonProfile.put("first name", profile.getFirstName());
			jsonProfile.put("last name", profile.getLastName());
			jsonProfile.put("age", profile.getAge());
			jsonProfile.put("address", jsonAddress);
			jsonProfile.put("contact", jsonContact);
			jsonProfiles.add(jsonProfile);
		}
		return jsonProfiles;
	}

	/*
	 *  uses all of the methods above to create a JSON file
	 */
	public void writeJSON(List<Profile> profiles, String path) {
		this.profiles = profiles;
		try {
			FileWriter fw = new FileWriter(path);
			fw.write(createJSONProfiles().toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
