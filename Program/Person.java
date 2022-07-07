/**
 * Is a Person class that stores users details for projects
 */

public class Person {
	// Attributes
	private String name;
	private String telNo;
	private String email;
	private String address;
	private String personType;
	
	// Constructor
	public Person(String name, String telNo, String email, String address, String personType) {
		this.name = name;
		this.telNo = telNo;
		this.email = email;
		this.address = address;
		this.personType = personType;
	}

	// Getters
	public String getName() {
		return name;
	}
	public String getPersonType() {
		return personType;
	}
	public String getEmail() {
		return email;
	}
	public String getAddress() {
		return address;
	}
	public String getTelNo() {
		return telNo;
	}

	/**
	 * @return a string with all the person details
	 */
	public String toString() {
		return String.format("'%s', '%s', '%s', '%s'", name, email, address, telNo);
	}

	/**
	 * Gets the required table value names to insert a project in the database
	 *
	 * @return table value names
	 */
	public String tableValues() {
		return String.format("%sName, %sEmail, %sAddress, %sNumber", personType, personType, personType, personType);
	}

}
