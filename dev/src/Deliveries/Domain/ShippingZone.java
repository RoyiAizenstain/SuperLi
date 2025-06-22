package Deliveries.Domain;

import java.util.List;

/**
 * Represents a shipping zone that contains a list of sites.
 */
public class ShippingZone {
    // ShippingZone class represents a shipping zone in the transportation system.
    private String shippingZoneID;
    private String shippingZoneName;
    private List<Site> sites;

    // Constructor
    public ShippingZone(String shippingZoneID, String shippingZoneName, List<Site> sites) {
        this.shippingZoneID = shippingZoneID;
        this.shippingZoneName = shippingZoneName;
        this.sites = sites;
    }

    // Getters and Setters
    public String getShippingZoneID() {
        return shippingZoneID;
    }

    public void setShippingZoneID(String shippingZoneID) {
        this.shippingZoneID = shippingZoneID;
    }

    public String getShippingZoneName() {
        return shippingZoneName;
    }

    public void setShippingZoneName(String shippingZoneName) {
        this.shippingZoneName = shippingZoneName;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    // Method to add a site to the shipping zone
    public void addSite(Site site) {
        this.sites.add(site);
    }

    // Method to remove a site from the shipping zone
    public void removeSite(Site site) {
        this.sites.remove(site);
    }

    // Override toString method for better readability
    public String toString() {
        StringBuilder siteDetails = new StringBuilder();
        siteDetails.append("Shipping Zone ID: ").append(shippingZoneID).append("\n");
        siteDetails.append("Sites in Shipping Zone:\n");
        if (sites.isEmpty()) {
            siteDetails.append("No sites available in this shipping zone.\n");
            return siteDetails.toString();
        }
        //append tab for each site
        //add counter for each site
        int counter = 1;
        for (Site site : sites) {
            siteDetails.append(counter).append(". ").append("\n");
            siteDetails.append(site.toString());
            counter++;
        }
        return siteDetails.toString();
    }

}
