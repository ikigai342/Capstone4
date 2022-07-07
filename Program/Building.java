/**
 * Is a building class that stores all details regarding a building
 */
public class Building {
    private String buildingType;
    private String projectAddress;
    private int ERFNo;

    // constructor
    public Building(String buildingType, String projectAddress, int ERFNo) {
        this.buildingType = buildingType;
        this.projectAddress = projectAddress;
        this.ERFNo = ERFNo;
    }

    // Getters

    public int getERFNo() {
        return ERFNo;
    }
    public String getProjectAddress() {
        return projectAddress;
    }
    public String getBuildingType() {
        return buildingType;
    }

    /**
     * @return a string with all the building details
     */
    public String toString() {
        return String.format("%o, '%s', '%s'", ERFNo, buildingType, projectAddress);
    }

}
