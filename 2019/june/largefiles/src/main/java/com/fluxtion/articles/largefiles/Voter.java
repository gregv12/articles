package com.fluxtion.articles.largefiles;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Voter {
    
    private String dateString;
    private String firstName;
    private String fullName;
    
    public CharSequence getFirstName() {
        return firstName;
    }

    public void setFirstName(CharSequence firstName) {
        this.firstName = firstName.toString();
    }

    public CharSequence getDateString() {
        return dateString;
    }

    public void setDateString(CharSequence dateString) {
        this.dateString = dateString.toString();
    }

    public CharSequence getFullName() {
        return fullName;
    }

    public void setFullName(CharSequence fullName) {
        this.fullName = fullName.toString();
    }
    
}
