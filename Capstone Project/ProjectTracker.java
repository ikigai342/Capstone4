import java.io.*;
import java.time.DateTimeException;
import java.util.*;
import java.time.LocalDate;

/**
 *  Program tracks, edits and completes engineering projects
 */

public class ProjectTracker {

	public static void main(String[] args) {
		int userChoice = 99;
		String projectMessage = "Enter project number: ";
		ArrayList<Project> projects = (ArrayList<Project>) loadProjects();
		Scanner input = new Scanner(System.in);

		// User Menu loops until e is entered
		while(true) {
			System.out.println("""
					1. to add project\s
					2. to change due date of project\s
					3. to change project cost\s
					4. to change amount paid on project\s
					5. to update user details to an project\s
					6. to finalise project\s
					7. list projects that needs completion\s
					8. list projects past due dates\s
					0. to exit\s
					""");
			try {
				userChoice = input.nextInt();
			} catch (InputMismatchException e)
			{
				printErrorMessage(e);
			}
			input.nextLine();

			switch (userChoice) {
				// Adds project
				case 1 -> projects.add(addProject(input, projects.size()));

				// Change due date of project
				case 2 -> changeDueDate(projectMessage, projects, input);

				// Change selected project cost
				case 3 -> changeCost(projectMessage, projects, input);

				// Change selected project amount paid
				case 4 -> changedAmountPaid(projectMessage, projects, input);

				// Edit user's details
				case 5 -> editUserDetails(projectMessage, projects, input);

				// Finishes the project
				case 6 -> completeProject(projectMessage, projects, input);

				// List completed projects
				case 7 -> listCompletedProjects(projects);

				// List overdue projects
				case 8 -> listOverdueProjects(projects);

				// Saves data and exits
				case 0 -> {
					saveProjects(projects);
					input.close();
					return;
				}
				// error if invalid option entered
				default -> System.err.println("Invalid input please try again.");
			}
		}
	}

	/**
	 *
	 * Completes the user requested project
	 *
	 * @param projectMessage
	 * @param projects the list of projects
	 * @param input a scanner that takes in all the user inputs
	 */
	private static void completeProject(String projectMessage, ArrayList<Project> projects, Scanner input) {
		try {
			int selectedProject;
			System.out.println(projectMessage);
			selectedProject = input.nextInt();
			input.nextLine();
			System.out.println("Enter the date of completion: ");
			projects.get(selectedProject).setDateComp(getDate(input));
			projects.get(selectedProject).setProjCompleted();
		} catch (IndexOutOfBoundsException | InputMismatchException e)
		{
			printErrorMessage(e);
		}

	}

	/**
	 *
	 * Change the total amount paid for the user requested project
	 *
	 * @param projectMessage
	 * @param projects the list of projects
	 * @param input a scanner that takes in all the user inputs
	 */
	private static void changedAmountPaid(String projectMessage, ArrayList<Project> projects, Scanner input) {
		try {
			int selectedProject;
			System.out.println(projectMessage);
			selectedProject = input.nextInt();
			input.nextLine();
			System.out.println("Enter new project cost: ");
			projects.get(selectedProject).setTotalPaid(input.nextFloat());
		} catch (IndexOutOfBoundsException | InputMismatchException e) {
			printErrorMessage(e);
		}
	}

	/**
	 *
	 * Change the cost for the user requested project
	 *
	 * @param projectMessage
	 * @param projects the list of projects
	 * @param input a scanner that takes in all the user inputs
	 */
	private static void changeCost(String projectMessage, ArrayList<Project> projects, Scanner input) {
		try{
			int selectedProject;
			System.out.println(projectMessage);
			selectedProject = input.nextInt();
			System.out.println("Enter total amount paid: ");
			projects.get(selectedProject).setProjectCost(input.nextFloat());
		}catch (IndexOutOfBoundsException | InputMismatchException e) {
			printErrorMessage(e);
		}
	}

	/**
	 *
	 * Change the due date for the user requested project
	 *
	 * @param projectMessage
	 * @param projects the list of projects
	 * @param input a scanner that takes in all the user inputs
	 */
	private static void changeDueDate(String projectMessage, ArrayList<Project> projects, Scanner input) {
		try{
			int selectedProject;
			System.out.println(projectMessage);
			selectedProject = input.nextInt();
			System.out.println("Enter total new due date (dd mm yyyy): ");
			projects.get(selectedProject).setDeadline(getDate(input));
		} catch (IndexOutOfBoundsException | InputMismatchException e) {
			printErrorMessage(e);
		}
	}

	/**
	 * Iterates through the list and prints all the overdue projects
	 *
	 * @param projects the list of projects
	 */
	private static void listOverdueProjects(ArrayList<Project> projects) {
		Iterator<Project> iterator;
		iterator = projects.iterator();
		while (iterator.hasNext()) {
			Project tempProject = iterator.next();
			if (tempProject.getDueDate().isBefore(LocalDate.now()))
				tempProject.printProject();
		}
	}

	/**
	 * Iterates through the list and prints all the completed projects
	 *
	 * @param projects the list of projects
	 */
	private static void listCompletedProjects(ArrayList<Project> projects) {
		Iterator<Project> iterator;
		iterator = projects.iterator();
		while (iterator.hasNext()) {
			Project tempProject = iterator.next();
			if (Boolean.FALSE.equals(tempProject.getIsCompleted()))
				tempProject.printProject();
		}
	}

	/**
	 * Asks the user what project he wishes to change the contractor/customer/architect details
	 *
	 * @param projectMessage string that asks the user for the project number
	 * @param projects the list of projects
	 * @param input a scanner that takes in all the user inputs
	 */
	private static void editUserDetails(String projectMessage, ArrayList<Project> projects, Scanner input) {
		int selectedProject;
		System.out.println("""
				Whose details do you wish to update:\s
				1. Architect\s
				2. Contractor\s
				3. Customer\s
				0. return to main menu\s
				""");
		int userType;
		userType = input.nextInt();
		// Ask for new credentials if usertype was selected
		if (userType > 0 && userType <= 3) {
			System.out.println(projectMessage);
			selectedProject = input.nextInt();
			System.out.println("Enter user's email: ");
			String userEmail = input.nextLine();
			System.out.println("Enter user's Phone no: ");
			String userPhone = input.nextLine();
			updateUserType(projects, selectedProject, userType, userEmail, userPhone);
		} else if (userType != 0)
			System.err.println("Invalid input input from 0 to 3 ");
	}

	/**
	 * Changes specified user email and number from a project
	 *
	 * @param projects the list of projects
	 * @param selectedProject the project the user selected
	 * @param userType the user that is selected on the project customer/architect/contractor
	 * @param userEmail the selected user new email
	 * @param userPhone the selected user new number
	 */
	private static void updateUserType(ArrayList<Project> projects, int selectedProject, int userType, String userEmail, String userPhone) {
		switch (userType) {
			case 1 -> {
				projects.get(selectedProject).getArchitect().setEmail(userEmail);
				projects.get(selectedProject).getArchitect().setTelNo(userPhone);
			}
			case 2 -> {
				projects.get(selectedProject).getContractor().setEmail(userEmail);
				projects.get(selectedProject).getContractor().setTelNo(userPhone);
			}
			case 3 -> {
				projects.get(selectedProject).getCustomer().setEmail(userEmail);
				projects.get(selectedProject).getCustomer().setTelNo(userPhone);
			}
			default -> System.err.println("Invalid input please try again.");
		}
	}

	/**
	 * Prints an error message based upon the exception that has been thrown
	 *
	 * @param e error message exception
	 */
	private static void printErrorMessage(Exception e) {
		String errorMessage = switch (e.getClass().getSimpleName()) {
			case "NullPointerException", "ArrayIndexOutOfBoundsException" : yield "Project does not exist";
			case "DateTimeException" : yield "Invalid date";
			case "InputMismatchException" : yield "Invalid input please enter a number";
			default : yield e.getMessage();
		};
		System.err.println(errorMessage);
	}

	/**
	 * Takes three integer variables and converts the data to LocalDate
	 *
	 * @param input user input needed for the date
	 * @return a LocalDate variable
	 */
	public static LocalDate getDate(Scanner input) {
		try {
			int day = input.nextInt();
			int month = input.nextInt();
			int year = input.nextInt();
			return LocalDate.of(year, month, day);
		}
		catch (InputMismatchException | DateTimeException e){
			printErrorMessage(e);
		}
		input.nextLine();
		return getDate(input);
	}

	/**
	 * Asks user for details to add a new project to add to the list of projects
	 *
	 * @param input user input needed for adding a project
	 * @param projectNumber project number designated to project
	 * @return returns a new project
	 */
	public static Project addProject(Scanner input, int projectNumber) {
		try {
			// Get project details
			System.out.println("Enter project name: ");
			String projectName = input.nextLine();
			System.out.println("Enter building type: ");
			String buildingType = input.nextLine();
			System.out.println("Enter project address: ");
			String projAddress = input.nextLine();
			System.out.println("Enter ERF number: ");
			int ERFNo = input.nextInt();
			System.out.println("Enter project cost: ");
			float projectCost = input.nextFloat();
			System.out.println("Enter total amount paid for project thus far: ");
			float totalPaid = input.nextFloat();
			System.out.println("Enter project expected completion date (dd mm yyyy): ");
			LocalDate deadline = ProjectTracker.getDate(input);

			// Building details
			Building building = new Building(buildingType, projAddress, ERFNo);

			// Get the associates details
			Person architect = addPerson("architect", input);
			Person contractor = addPerson("contractor", input);
			Person customer = addPerson("customer", input);

			return new Project(projectNumber, projectName, building, projectCost,
					totalPaid, deadline,  false, architect, contractor, customer, LocalDate.of(1,1,1));
		}
		catch (Exception e)
		{
			printErrorMessage(e);
		}
		// If invalid recurse until a valid project is given
		input.nextLine();
		return addProject(input, projectNumber);
	}

	/**
	 * Asks user for details to add a new person to a project
	 *
	 * @param personType string that stores if they are a customer/contractor/architect
	 * @param input user input needed for adding a person
	 * @return returns a new person
	 */
	public static Person addPerson(String personType, Scanner input) {
		try {
			// Get person details
			System.out.println("Enter " + personType + " Details");
			System.out.println("Enter name: ");
			String name = input.nextLine();
			System.out.println("Enter phone number: ");
			String telNo = input.nextLine();
			System.out.println("Enter email: ");
			String email = input.nextLine();
			System.out.println("Enter address: ");
			String address = input.nextLine();

			return new Person(name, telNo, email, address);
		}
		catch (Exception e)
		{
			printErrorMessage(e);
		}
		return addPerson(personType, input);
	}

	/**
	 * Loads all the projects from projects.txt file and stores them in a
	 * list of projects
	 *
	 * @return a list of all the projects
	 */
	public static List<Project> loadProjects() {
		try {
			File projectFile = new File("projects.txt");
			Scanner projectScanner = new Scanner(projectFile);
			ArrayList<Project> projects = new ArrayList<>();

			while (projectScanner.hasNextLine()) {
				// Splits and stores data in temp array until formatted to work with project class
				String[] tempProject = projectScanner.nextLine().split(";");
				LocalDate tempDeadline = LocalDate.of(Integer.parseInt(tempProject[6]), Integer.parseInt(tempProject[7]),
										Integer.parseInt(tempProject[8]));
				LocalDate tempDateComp = LocalDate.of(Integer.parseInt(tempProject[22]), Integer.parseInt(tempProject[23]),
						Integer.parseInt(tempProject[24]));

				// Stores building details
				Building tempBuilding = new Building(tempProject[1], tempProject[2], Integer.parseInt(tempProject[3]));
				// Add associates to person class
				Person tempArchitect = new Person(tempProject[10], tempProject[11], tempProject[12], tempProject[13]);
				Person tempContractor = new Person(tempProject[14], tempProject[15], tempProject[16], tempProject[17]);
				Person tempCustomer = new Person(tempProject[18], tempProject[19], tempProject[20], tempProject[21]);

				// Adds all to a project class and adds it to a list
				projects.add(new Project(projects.size(), tempProject[0], tempBuilding,
						Float.parseFloat(tempProject[4]), Float.parseFloat(tempProject[5]), tempDeadline,
						Boolean.parseBoolean(tempProject[9]), tempArchitect, tempContractor, tempCustomer, tempDateComp));
			}

			return projects;
		// Checks if file exists
		} catch (FileNotFoundException e) {
			System.err.println("No file found");
			return new ArrayList<>();
		}

	}

	/**
	 * Saves all the projects to projects.txt file
	 *
	 * @param projects list of all the projects
	 */
	public static void saveProjects(List<Project> projects) {
		try {
			File file = new File("projects.txt");
			FileWriter fileWriter = new FileWriter(file);
			for (Project project : projects) {
				fileWriter.write(project.toString() + "\n");
			}
			fileWriter.close();
		// Checks if file can be accessed/created
		} catch (IOException e) {
			printErrorMessage(e);
		}
	}

}
