/**
 * Is a project class that stores all details regarding a project
 */
import java.time.LocalDate;

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
	private Person architect;
	private Person contractor;
	private Person customer;
	
	// Constructor
	public Project(int projectNumber, String projectName, Building building, float projectCost, float totalPaid,
				   LocalDate deadline, Boolean isCompleted, Person architect, Person contractor, Person customer,
				   LocalDate dateComp) {
		this.projectNumber = projectNumber;
		this.projectName = projectName;
		this.building = building;
		this.projectCost = projectCost;
		this.totalPaid = totalPaid;
		this.deadline = deadline;
		this.dateComp = dateComp;
		this.isCompleted = isCompleted;
		this.architect = architect;
		this.contractor = contractor;
		this.customer = customer;
	}	
	
	// Getters
	public Person getContractor() {
		return contractor;
	}

	public Person getArchitect() {
		return architect;
	}

	public Person getCustomer() {
		return customer;
	}

	public LocalDate getDueDate() {
		return deadline;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}
	
	// Setters
	public void setDeadline(LocalDate newDeadline) {
		deadline = newDeadline;
	}
	
	public void setDateComp(LocalDate completionDay) {
		dateComp = completionDay;
	}
	
	public void setTotalPaid(float newTotal) {
		totalPaid = newTotal;
	}

	public void setProjectCost(float newTotal) {
		projectCost = newTotal;
	}

	public void setProjCompleted() {
		isCompleted = true;
	}

	/**
	 * @return a string with all project details
	 */
	public String toString() {
		return  projectName + ';' + building.toString() + ';' + projectCost + ';' + totalPaid + ';' + deadline.getYear()
				+ ';' + deadline.getMonthValue() + ';' + deadline.getDayOfMonth() + ';' + isCompleted + ';'
				+ architect.toString() + ';' + contractor.toString() + ';' + customer.toString() + ';'
				+ dateComp.getYear() + ';' + deadline.getMonthValue() + ';' + deadline.getDayOfMonth();
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

}
