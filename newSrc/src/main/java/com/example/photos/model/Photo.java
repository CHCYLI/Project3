package com.example.photos.model;
//import java.awt.image.*;
import java.io.Serializable;
import java.util.ArrayList;
import android.graphics.Picture;

/*
 * @author Chris Li
 * @author Tony Lu
 */
public class Photo implements Serializable{
	private String nameOfPhoto;
	private Picture containedPictureFile;
	private String location = "";
	private ArrayList<String> people = new ArrayList<String>();
	
	public Photo(String nameOfPhoto, Picture containedPictureFile) {
        this.nameOfPhoto = nameOfPhoto;
        this.containedPictureFile = containedPictureFile;
        //The photo has no initial attributes; location/people tag(s) added by user later
    }
	
	public void setName(String newName) {
		this.nameOfPhoto = newName;
	}
	
	public String getName() {
		return nameOfPhoto;
	}
	
	public void setLocation(String newLocation) {
		this.location = newLocation;
	}
	
	public String getLocation() {
		return location;
	}
	
	public boolean addPerson(String newPerson) {
		for (String i:people) {
			if (i.equals(newPerson)) { //if person with same name is found
				return false; //do not add newPerson
			}
		}
		//else if no repeat found
		this.people.add(newPerson); //add
		return true;
	}
	
	public boolean delPerson(String personToLookFor) {
		for (String i:people) {
			if (i.equals(personToLookFor)) { //if matching name found
				this.people.remove(i); //remove this person
				return true;
			}
		}
		//else if no match
		return false;
	}
	
	public ArrayList<String> getPeople() {
		return people;
	}
}