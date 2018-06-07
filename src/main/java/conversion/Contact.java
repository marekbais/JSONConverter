package conversion;

public class Contact {

	private String type;
	private String contact;
	
	public Contact(String type, String contact) {
		this.type = type;
		this.contact = contact;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
}
