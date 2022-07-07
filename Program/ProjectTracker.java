import java.sql.*;
import java.time.DateTimeException;
import java.util.*;
import java.time.LocalDate;

/**
 *  Program tracks, edits and completes engineering projects
 */

public class ProjectTracker {
	// Constants
	private static final String PROJECT_MESSAGE = "Enter project number: ";
	private static final String SQL_LIST_UNCOMPLETED = "SELECT * FROM project WHERE isCompleted =" + false + ";";
	private static final String SQL_LIST_OVERDUE = "SELECT * FROM project WHERE isCompleted = " + false + " AND deadline < '" + LocalDate.now() + "';";
	private static final String SQL_SEARCH_PROJECT = "SELECT * FROM project WHERE projectNumber =";
	private static final String PROJECT_NOT_EXIST = "Project does not exist\n";


	public static void main(String[] args) {
		int userChoice;
		Scanner input = new Scanner(System.in);

		// Database connection
		try {
			Connection connection = connectToDatabase();
			Statement statement = connection.createStatement();

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
					9. search for project\s
					0. to exit\s
					""");

			userChoice = getUserChoice(input);

			switch (userChoice) {
					// Adds project
					case 1 -> addProject(input, statement);

					// Change due date of project
					case 2 -> changeDueDate(input, statement);

					// Change selected project cost
					case 3 -> changeCost(input, statement);

					// Change selected project amount paid
					case 4 -> changedAmountPaid(input, statement);

					// Edit user's details
					case 5 -> editUserDetails(input, statement);

					// Finishes the project
					case 6 -> completeProject(input, statement);

					// List completed projects
					case 7 -> listProjects(statement, SQL_LIST_UNCOMPLETED);

					// List overdue projects
					case 8 -> listProjects(statement, SQL_LIST_OVERDUE);

					// List user requested project
					case 9 -> searchProject(input, statement);

					// exits
					case 0 -> {
						input.close();
						statement.close();
						connection.close();
						return;
					}
					// error if invalid option entered
					default -> System.err.println("Invalid input please try again.");
				}
			}
		} catch (SQLException e) {
			printErrorMessage(e);
			System.exit(0);
		}
	}

	/**
	 * Asks the user for their menu selection and throws an exception if the value is not an integer
	 *
	 * @param input
	 * @return the user's choice
	 */
	public static int getUserChoice(Scanner input) {
		int userChoice;
		try {
			userChoice = input.nextInt();
		} catch (InputMismatchException e)
		{
			printErrorMessage(e);
			userChoice = 99;
		}
		input.nextLine();
		return userChoice;
	}

	/**
	 * Program connects to the database
	 *
	 * @return the connection to the database
	 * @throws SQLException
	 */
	public static Connection connectToDatabase() throws SQLException {
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/path",
				"username",
				"password"
		);


		return connection;
	}


	/**
	 * Search for the user-selected project from the database
	 *
	 * @param input user input needed for adding a project
	 * @param statement
	 * @throws SQLException
	 */
	public static void searchProject(Scanner input, Statement statement) throws SQLException {
		System.out.println(PROJECT_MESSAGE);
		try {
			int projectNumber = input.nextInt();
			Project selectedProject = getSelectedProject(projectNumber, statement);
			if (selectedProject != null)
			{
				selectedProject.printProject();
			} else {
				System.err.println(PROJECT_NOT_EXIST);
			}
		} catch (InputMismatchException e) {
			printErrorMessage(e);
		}
	}

	/**
	 * List projects from the database with the selected criteria
	 *
	 * @param statement
	 * @param sql criteria of selection
	 * @throws SQLException
	 */
	public static void listProjects(Statement statement, String sql) throws SQLException {
		ResultSet resultSet = statement.executeQuery(
				sql
		);
		Queue<Integer> projectSelected = new PriorityQueue<>();
		while (resultSet.next())
		{
			projectSelected.add(resultSet.getInt("projectNumber"));
		}
		while (!projectSelected.isEmpty())
		{
			Project completedProject = getSelectedProject(projectSelected.poll(), statement);
			completedProject.printProject();
		}
		resultSet.close();
	}

	/**
	 * Asks user for details to add a new project to the database
	 *
	 * @param input user input needed for adding a project
	 * @param statement
	 * @throws SQLException
	 */
	public static void addProject(Scanner input, Statement statement) throws SQLException {
		try {
			Project newProject;
			// Get the details for the new project
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

			// Store details relating to the building in the new building object
			Building building = new Building(buildingType, projAddress, ERFNo);

			// Get and store the associates' details in their respective objects
			Person tempCustomer = addPerson("customer", input);
			Person tempArchitect = addPerson("architect", input);
			Person tempProjectManager = addPerson("manager", input);
			Person tempStructuralEngineer = addPerson("engineer", input);

			// Add associates details to their respective tables in the database
			insertIntoTable(statement, tempArchitect.getPersonType(), tempArchitect.tableValues(), tempArchitect.toString());
			insertIntoTable(statement, tempCustomer.getPersonType(), tempCustomer.tableValues(), tempCustomer.toString());
			insertIntoTable(statement, "project_manager", tempProjectManager.tableValues(), tempProjectManager.toString());
			insertIntoTable(statement, "structural_engineer", tempStructuralEngineer.tableValues(), tempStructuralEngineer.toString());

			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM customer ORDER BY customerID DESC LIMIT 1;"
			);
			int tempID = 0;
			if (resultSet.next()){
				tempID = resultSet.getInt("customerID");
			}

			// Add project details to database
			newProject = new Project(tempID, projectName, building, projectCost, totalPaid, deadline, false,
					tempCustomer, tempArchitect, tempProjectManager, tempStructuralEngineer, null);

			String tempString = newProject.toString() + ", " + tempID + ", " + tempID + ", " + tempID + ", " + tempID;
			insertIntoTable(statement, "project", newProject.tableValues(), tempString);
			resultSet.close();
		// Recursively loop through method wrong input is entered
		} catch (InputMismatchException e) {
			printErrorMessage(e);
			input.nextLine();
			addProject(input, statement);
		}
	}

	/**
	 * Change the due date for the user requested project
	 * @param input a scanner that takes in all the user inputs
	 * @param statement
	 * @throws SQLException
	 */
	public static void changeDueDate(Scanner input, Statement statement) throws SQLException {
		System.out.println(PROJECT_MESSAGE);
		try {
			int selectedProject = input.nextInt();
			System.out.println("Enter total new due date (dd mm yyyy): ");
			int changed = statement.executeUpdate(
					"UPDATE project SET deadline = '" + getDate(input) + "' WHERE projectNumber =" + selectedProject
			);
			if (changed > 0) {
				System.out.println("Due date changed successfully\n");
			} else {
				System.err.println(PROJECT_NOT_EXIST);
			}

		} catch (InputMismatchException e) {
			printErrorMessage(e);
		}
	}

	/**
	 * Change the cost for the user requested project
	 *
	 * @param input a scanner that takes in all the user inputs
	 * @param statement
	 * @throws SQLException
	 */
	public static void changeCost(Scanner input, Statement statement) throws SQLException {
		try {
			System.out.println(PROJECT_MESSAGE);
			int selectedProject = input.nextInt();
			System.out.println("Enter new total cost for the project: ");
			float newProjectCost = input.nextFloat();
			int changed = statement.executeUpdate(
					"UPDATE project SET projectCost = '" + newProjectCost +"' WHERE projectNumber =" + selectedProject
			);
			if (changed > 0) {
				System.out.println("Due date changed successfully\n");
			} else {
				System.err.println(PROJECT_NOT_EXIST);
			}
		} catch (InputMismatchException e) {
			printErrorMessage(e);
		}
	}

	/**
	 * Change the amount paid for the user requested project
	 *
	 * @param input a scanner that takes in all the user inputs
	 * @param statement
	 * @throws SQLException
	 */
	public static void changedAmountPaid(Scanner input, Statement statement) throws SQLException {
		try {
			System.out.println(PROJECT_MESSAGE);
			int selectedProject = input.nextInt();
			System.out.println("Enter amount paid to the project: ");
			float newAmountPaid = input.nextFloat();
			int changed = statement.executeUpdate(
					"UPDATE project SET projectCost = '" + newAmountPaid +"' WHERE projectNumber =" + selectedProject
			);
			if (changed > 0) {
				System.out.println("Due date changed successfully\n");
			} else {
				System.err.println(PROJECT_NOT_EXIST);
			}
		} catch (InputMismatchException e) {
			printErrorMessage(e);
		}
	}

	/**
	 * Asks the user what project he wishes to change the associate's details
	 *
	 * @param input a scanner that takes in all the user inputs
	 * @param statement
	 * @throws SQLException
	 */
	public static void editUserDetails(Scanner input, Statement statement) throws SQLException {
		try {
			int selectedProject;
			System.out.println("""
				Whose details do you wish to update:\s
				1. Architect\s
				2. Customer\s
				3. Project Manager\s
				4. Structural Engineer\s
				0. return to main menu\s
				""");
			int userType;
			userType = input.nextInt();
			input.nextLine();
			// Ask for new credentials if usertype was selected
			if (userType > 0 && userType <= 4) {
				System.out.println(PROJECT_MESSAGE);
				selectedProject = input.nextInt();
				input.nextLine();
				System.out.println("Enter user's email: ");
				String userEmail = input.nextLine();
				System.out.println("Enter user's Phone no: ");
				String userPhone = input.nextLine();
				updateUserType(selectedProject, userType, userEmail, userPhone, statement);
			} else if (userType != 0)
				System.err.println("Invalid input input from 0 to 4");

		} catch (InputMismatchException e) {
			printErrorMessage(e);
		}
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
			input.nextLine();
			return LocalDate.of(year, month, day);
		}
		catch (InputMismatchException | DateTimeException e){
			printErrorMessage(e);
		}
		return getDate(input);
	}

	/**
	 * Prints an error message based upon the error type
	 *
	 * @param e error type
	 */
	public static void printErrorMessage(Exception e){
		String errorMessage = switch (e.getClass().getSimpleName()) {
			case "NullPointerException", "ArrayIndexOutOfBoundsException":
				yield "Project does not exist";
			case "DateTimeException":
				yield "Invalid date";
			case "InputMismatchException":
				yield "Invalid input please enter a number";
			default:
				yield e.getMessage();
		};
		System.err.println(errorMessage);
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

			return new Person(name, telNo, email, address, personType);
		}
		catch (Exception e)
		{
			printErrorMessage(e);
		}
		return addPerson(personType, input);
	}

	/**
	 *	Changes specified user email and number from the user selected project
	 *	in the database
	 *
	 * @param selectedProject
	 * @param userType the selected associate
	 * @param userEmail the selected user new email
	 * @param userPhone the selected user new number
	 * @param statement
	 * @throws SQLException
	 */
	public static void updateUserType(int selectedProject, int userType, String userEmail, String userPhone, Statement statement) throws SQLException {
		switch (userType) {
			case 1 -> statement.executeUpdate("UPDATE architect SET architectEmail = '" + userEmail
					+"', architectNumber = '" + userPhone +"' WHERE architectID =" + selectedProject);

			case 2 -> statement.executeUpdate("UPDATE customer SET customerEmail = '" + userEmail
						+"', customerNumber = '" + userPhone +"' WHERE customerID =" + selectedProject);

			case 3 -> statement.executeUpdate("UPDATE project_manager SET managerEmail = '" + userEmail
						+ "', managerNumber = '" + userPhone +"' WHERE managerID =" + selectedProject);

			case 4 -> statement.executeUpdate("UPDATE structural_engineer SET engineerEmail = '" + userEmail
						+"', engineerNumber = '" + userPhone +"' WHERE engineerID =" + selectedProject);

			default -> System.err.println("Invalid input please try again.");

		}
	}

	/**
	 * Inserts data to the database
	 *
	 * @param statement
	 * @param tableName
	 * @param columnNames
	 * @param tableValues
	 * @throws SQLException
	 */
	public static void insertIntoTable(Statement statement, String tableName, String columnNames, String tableValues) throws SQLException {
		statement.executeUpdate(
				"INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + tableValues + ");"
		);
	}

	/**
	 * Completes the user selected project and prints invoice if customer has
	 * not fully paid for the project
	 *
	 * @param input user input needed for adding a person
	 * @param statement
	 * @throws SQLException
	 */
	public static void completeProject(Scanner input, Statement statement) throws SQLException  {
		// Get project number
		int selectedProject;
		System.out.println(PROJECT_MESSAGE);
		selectedProject = input.nextInt();
		input.nextLine();

		System.out.println("Enter Date of completion (dd mm yyyy):");
		ResultSet resultSet = statement.executeQuery(
				SQL_SEARCH_PROJECT + selectedProject + ";"
		);

		boolean tempCompleted = false;
		float tempPaid = 0;
		float tempCost = 0;
		if (resultSet.next()) {
			tempCompleted = resultSet.getBoolean("isCompleted");
			tempPaid = resultSet.getFloat("amountPaid");
			tempCost = resultSet.getFloat("projectCost");
		}
		// Checks if project is all ready completed
		if (!tempCompleted){
			// Add date of completion to the project
			statement.executeUpdate(
					"UPDATE project SET dateOfCompletion = '" + getDate(input) +"', isCompleted = true WHERE projectNumber =" + selectedProject + ";"
			);
			// Checks if project is fully paid if not prints invoice
			if (tempPaid < tempCost)
			{
				Project completedProject = getSelectedProject(selectedProject, statement);
				completedProject.generateInvoice();
			}
		} else
		{
			System.err.println("Project is completed\n");
		}
		resultSet.close();
	}

	/**
	 * Gets the selected building details from database and stores it in a Building class
	 *
	 * @param statement
	 * @param selectedProject
	 * @return selected Building class
	 * @throws SQLException
	 */
	public static Building getSelectedBuilding(Statement statement, int selectedProject) throws SQLException {
		ResultSet resultSet = statement.executeQuery(
				SQL_SEARCH_PROJECT + selectedProject + ";"
		);
		if (resultSet.next()) {
			int tempErf = resultSet.getInt("erfNum");
			String tempBuildingType = resultSet.getString("buildingType");
			String tempAddress = resultSet.getString("projectAddress");
			return new Building(tempBuildingType, tempAddress, tempErf);
		}
		resultSet.close();
		return null;
	}

	/**
	 * Gets the selected user details from database and stores it in a User class
	 *
	 * @param statement
	 * @param selectedProject
	 * @return selected User class
	 * @throws SQLException
	 */
	public static Person getSelectedUser(Statement statement, int selectedProject, String userType, String databaseName) throws SQLException {
		ResultSet resultSet = statement.executeQuery(
				"SELECT * FROM "+ databaseName + " WHERE " + userType + "ID =" + selectedProject + ";"
		);
		if (resultSet.next()) {
			String tempName = resultSet.getString(userType + "Name");
			String tempEmail = resultSet.getString(userType + "Email");
			String tempAddress = resultSet.getString(userType + "Address");
			String tempPhone = resultSet.getString(userType + "Number");
			return new Person(tempName, tempPhone, tempEmail, tempAddress, userType);
		}
		resultSet.close();
		return null;
	}

	/**
	 * Gets the selected project details from database and stores it in a Project class
	 *
	 * @param statement
	 * @param selectedProject
	 * @return selected Project class
	 * @throws SQLException
	 */
	public static Project getSelectedProject(int selectedProject, Statement statement) throws SQLException {
		// Store details relating to the building in the new building object
		Building tempBuilding = getSelectedBuilding(statement, selectedProject);

		// Get and store the associates' details in their respective objects
		Person tempArchitect = getSelectedUser(statement, selectedProject, "architect", "architect");
		Person tempCustomer = getSelectedUser(statement, selectedProject, "customer", "customer");
		Person tempManager = getSelectedUser(statement, selectedProject, "manager", "project_manager");
		Person tempEngineer = getSelectedUser(statement, selectedProject, "engineer", "structural_engineer");

		ResultSet resultSet = statement.executeQuery(
				SQL_SEARCH_PROJECT + selectedProject + ";"
		);
		// Get and store the remainder details for the project
		if (resultSet.next()){
			int tempID = resultSet.getInt("projectNumber");
			String tempName = resultSet.getString("projectName");
			float tempCost = resultSet.getFloat("projectCost");
			float tempPaid = resultSet.getFloat("amountPaid");
			LocalDate tempDeadline = LocalDate.parse(resultSet.getString("deadline"));
			LocalDate tempCompletion;
			String tempString = resultSet.getString("dateOfCompletion");
			if (tempString == null)
				tempCompletion = null;
			else
				tempCompletion = LocalDate.parse(tempString);
			Boolean tempCompleted = resultSet.getBoolean("isCompleted");

			return new Project(tempID, tempName, tempBuilding, tempCost, tempPaid, tempDeadline, tempCompleted,
					tempCustomer, tempArchitect, tempManager, tempEngineer, tempCompletion);
		}
		resultSet.close();
		return null;
	}
}