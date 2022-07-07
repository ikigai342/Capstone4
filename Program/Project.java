import java.io.*;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Is a project class that stores all details regarding a project
 */
public class Project {
	// Attributes
	private int projectNumber;
	private String projectName;
	private Building building;
	private float projectCost;
	private float totalPaid;
	private LocalDate deadline;
	private LocalDate dateComp;
	private Boolean isCompleted;
	private Person customer;
	private Person architect;
	private Person projectManager;
	private Person structuralEngineer;
	
	// Constructor
	public Project(int projectNumber, String projectName, Building building, float projectCost, float totalPaid,
				   LocalDate deadline, Boolean isCompleted, Person customer, Person architect, Person projectManager,
				   Person structuralEngineer, LocalDate dateComp) {
		this.projectNumber = projectNumber;
		this.projectName = projectName;
		this.building = building;
		this.projectCost = projectCost;
		this.totalPaid = totalPaid;
		this.deadline = deadline;
		this.dateComp = dateComp;
		this.isCompleted = isCompleted;
		this.customer = customer;
		this.architect = architect;
		this.projectManager = projectManager;
		this.structuralEngineer = structuralEngineer;
	}

	/**
	 * @return a string with all project details
	 */
	public String toString() {
		Statement statement;
		return String.format("'%s', '%s', '%s', '%s', null, false, %o, '%s', '%s'",
				projectName, projectCost, totalPaid, deadline, building.getERFNo(), building.getBuildingType(),
				building.getProjectAddress());
	}

	/**
	 * Gets the required table value names to insert a project in the database
	 *
	 * @return table value names
	 */
	public String tableValues() {
		return "projectName, projectCost, amountPaid, deadline, dateOfCompletion, isCompleted, erfNum, buildingType," +
				" projectAddress, customerID, architectID, managerID, engineerID";
	}

	/**
	 * Prints projects details in a user-friendly manner
	 */
	public void printProject() {
		System.out.printf("""
				Project Number: %s
				Project Name: %s
				Project Cost: R%.2f
				Total Amount Paid: R%.2f
				Due Date: %s
				%n""", projectNumber, projectName, projectCost, totalPaid, deadline);
	}

	/**
	 * Generates a text invoice for the customer
	 */
	public void generateInvoice()
	{
		try
		{
			FileWriter fileWriter = new FileWriter( projectNumber + "-" + projectName + ".txt");
			fileWriter.write(String.format("""
    			%s
    			%s
    			%s
    			%s
    			%s
    			=======================================
    			Project Cost: R%.2f
    			Total Amount Paid: R%.2f
    			=======================================
    			Amount Due: R%.2f
				""",projectName, customer.getName(), customer.getEmail(), customer.getTelNo(), customer.getAddress(),
					projectCost, totalPaid, (projectCost-totalPaid)));

			fileWriter.close();
		} catch (IOException e)
		{
			System.err.println("Could not create invoice.\n");
		}

	}


}
