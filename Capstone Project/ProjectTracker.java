/* Program tracks, edits and completes engineering projects */
import java.util.Scanner;
import java.time.LocalDate;

public class ProjectTracker {

	public static void main(String[] args) {
		Project[] arrProject = new Project[100];
		Scanner input = new Scanner(System.in);
		int projNo = 0;
		int projSel;
		String userChoice = "";
				
		// User Menu loops until e is entered
		while(userChoice != "e") {
			System.out.println("Enter (ap) to add project \n"
					+ "(cd) to change date of project \n"
					+ "(p) amount paid on project \n"
					+ "(uc) update contractor details \n"
					+ "(f) finalise project \n"
					+ "(e) exit");
			userChoice = input.next();
			switch(userChoice) {
				// adds project
				case "ap":
					arrProject[projNo] = addProject(input, projNo);				
					break;
				// change due date of project
				case "cd": 
					System.out.println("Enter project number: ");
					projSel = input.nextInt();
					System.out.println("Enter total new due date (dd mm yyyy): ");
					arrProject[projSel].setDeadline(getDate(input));		
					break;
				// change selected project amount paid
				case "p":
					System.out.println("Enter project number: ");
					projSel = input.nextInt();
					System.out.println("Enter total amount paid: ");
					arrProject[projSel].setTotalPaid(input.nextFloat());			
					break;
				// edit user contractor details
				case "uc":
					System.out.println("Enter project number: ");
					projSel = input.nextInt();
					System.out.println("Enter contractor email: ");
					arrProject[projSel].getContractor().setEmail(input.next());
					System.out.println("Enter contractor Phone no: ");
					arrProject[projSel].getContractor().setTelNo(input.next());
					break;
				// Finishes the project
				case "f":
					System.out.println("Enter project number: ");
					projSel = input.nextInt();
					System.out.println("Enter the date of completion: ");
					arrProject[projSel].setDateComp(getDate(input));
					arrProject[projSel].setProjCompleted();
					break;	
				// Exits the program if i don't assign userChoice to e it does 
				// not exit
				case "e":
					userChoice = "e";
					break;		
				// error if invalid option entered
				default:
					System.out.println("Invalid input please try again.");
					break;
			}
		}
	}
	
	// Function gets localdate
	public static LocalDate getDate(Scanner input) {
		int day = input.nextInt();
		int month = input.nextInt();
		int year = input.nextInt();
		LocalDate tempDate = LocalDate.of(year, month, day);
		return tempDate;
	}

	// Function adds project
	public static Project addProject(Scanner input, int projNo) {	
		projNo++;	
		// Get project details
		System.out.println("Enter project name: ");
		String projName = input.next();
		System.out.println("Enter building type: ");
		String buildingType = input.next();
		System.out.println("Enter project address: ");
		String projAddress = input.next();
		System.out.println("Enter ERF number: ");
		int ERFNo = input.nextInt();
		System.out.println("Enter project cost: ");
		float projCost = input.nextFloat();
		System.out.println("Enter total amount paid for project thus far: ");
		float totalPaid = input.nextFloat();
		System.out.println("Enter project expected completion date (dd mm yyyy): ");
		LocalDate deadline = ProjectTracker.getDate(input);
			
		Person architect = addPerson("architect", input);
		Person contractor = addPerson("contractor", input);
		Person customer = addPerson("customer", input);

		return new Project(projNo, projName, buildingType, projAddress, ERFNo, projCost, 
						totalPaid, deadline, false, architect, contractor, customer);					
	}	

	// Function adds person
	public static Person addPerson(String personType, Scanner input) {
		// Get person details
		System.out.println("Enter " + personType + " Details");
		System.out.println("Enter name: ");
		String name = input.next();
		System.out.println("Enter phone number: ");
		String telNo = input.next();
		System.out.println("Enter email: ");
		String email = input.next();
		System.out.println("Enter address: ");
		String address = input.next();

		return new Person(name, telNo, email, address);
	}
}
