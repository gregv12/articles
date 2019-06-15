package com.fluxtion.articles.largefiles;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Voter {
    
    private String dateString;
    private String firstName;
    private String fullName;
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.toString();
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString.toString();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName.toString();
    }
    
}
