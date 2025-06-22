package Deliveries.Domain;

/**
 * Site class represents a location where deliveries are made.
 * It contains information about the site ID, address, contact person, phone number, and name.
 */
public class Site {
    //attributes
    private final String siteID;
    private final String address;
    private final String contactPerson;
    private final String phoneNumber;
    private final String name;

    // Constructor
    public Site(String siteID, String address, String contactPerson, String phoneNumber, String name) {
        this.siteID = siteID;
        this.address = address;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    // Getters
    public String getSiteID() {
        return siteID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Override toString method for better readability
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t").append("Site ID: ").append(siteID).append("\n");
        sb.append("\t").append("Name: ").append(name).append("\n");
        sb.append("\t").append("Address: ").append(address).append("\n");
        sb.append("\t").append("Contact Person: ").append(contactPerson).append("\n");
        sb.append("\t").append("Phone Number: ").append(phoneNumber).append("\n");
        return sb.toString();
    }
}
