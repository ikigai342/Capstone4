/* Is a project class that stores all details regarding a project*/
import java.time.LocalDate;

public class Project {
	// Attributes
	private int projNo;
	private String projName;
	private String buildingType;
	private String projAddress;
	private int ERFNo;
	private float projCost;
	private float totalPaid;
	private LocalDate deadline;
	private LocalDate dateComp;
	private Boolean isCompleted;
	private Person architect;
	private Person contractor;
	private Person customer;
	
	// Constructor
	public Project(int projNo, String projName, String buildingType, String projAddress, int ERFNo, 
					float projCost, float totalPaid, LocalDate deadline, Boolean isCompleted,
					Person architect, Person contractor, Person customer) {
		this.projNo = projNo;
		this.projName = projName;
		this.buildingType = buildingType;
		this.projAddress = projAddress;
		this.ERFNo = ERFNo;
		this.projCost = projCost;
		this.totalPaid = totalPaid;
		this.deadline = deadline;
		this.dateComp = null;
		this.isCompleted = isCompleted;
		this.architect = architect;
		this.contractor = contractor;
		this.customer = customer;
	}	
	
	// Getters
	public Person getContractor() {
		return contractor;
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
	
	public void setProjCompleted() {
		isCompleted = true;
	}
	
}
