package model;

/**In House extends Part and sets a unique ID for each new part. */
public class InHouse extends Part {
    private int machineId;

    private static int identifier = 1; 

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(InHouse.getIdentifier(), name, price, stock, min, max);
        this.machineId = machineId;
        identifier += 2;

    }



    /** Gets the ID number for the In House Part. */
    public static int getIdentifier() { return identifier;
    }

    /** Getter for Machine ID. */
    public int getMachineId() {
        return machineId;
    }
    /**Setter for Machine ID. */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
