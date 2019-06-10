package com.fluxtion.articles.largefiles;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Voter {
    
    private CharSequence dateString;
    private CharSequence firstName;
    private CharSequence fullName;
    
    public CharSequence getFirstName() {
        return firstName;
    }

    public void setFirstName(CharSequence firstName) {
        this.firstName = firstName;
    }

    public CharSequence getDateString() {
        return dateString;
    }

    public void setDateString(CharSequence dateString) {
        this.dateString = dateString;
    }

    public CharSequence getFullName() {
        return fullName;
    }

    public void setFullName(CharSequence fullName) {
        this.fullName = fullName;
    }
    
}
