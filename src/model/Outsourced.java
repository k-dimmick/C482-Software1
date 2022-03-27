package model;

/**Outsourced extends Part and sets a unique ID for each new part. */
public class Outsourced extends Part{
    private String companyName;

    private static int identifier = 2;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(Outsourced.getIdentifier(), name, price, stock, min, max);
        this.companyName = companyName;
        identifier += 2;
    }

    /** Gets the ID number for the Outsourced Part. */
    public static int getIdentifier() {
        return identifier;
    }
    /** Gets the Company Name for a given Part.*/
    public String getCompanyName() {
        return companyName;
    }
    /** Sets the company name fora given part. */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

