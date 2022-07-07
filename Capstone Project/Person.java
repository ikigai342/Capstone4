/**
 * Is a Person class that stores users details for projects
 */

public class Person {
	// Attributes
	private String name;
	private String telNo;
	private String email;
	private String address;
	
	// Constructor
	public Person(String name, String telNo, String email, String address) {
		this.name = name;
		this.telNo = telNo;
		this.email = email;
		this.address = address;
	}	
	
	// Setters
	public void setTelNo(String newTelNo) {
		telNo = newTelNo;
	}
	
	public void setEmail(String newEmail) {
		email = newEmail;
	}

	/**
	 * @return a string with all the person details
	 */
	public String toString() {
		return String.format("%s;%s;%s;%s", name, telNo, email, address);
	}
}
