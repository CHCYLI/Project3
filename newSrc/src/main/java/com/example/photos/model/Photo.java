package com.example.photos.model;
//import java.awt.image.*;
import java.io.Serializable;
import java.util.ArrayList;
import android.graphics.Picture;
import android.os.Parcelable;

/*
 * @author Chris Li
 * @author Tony Lu
 */
public class Photo implements Serializable{
	private String photoURI; //could change to URI?
	//private Picture containedPictureFile;
	private String location = "";
	private ArrayList<String> people = new ArrayList<String>();
	private String nameOfContainingAlbum = "";
	
	public Photo(String photoURI, String containingAlbum/*, Picture containedPictureFile*/) {
        this.photoURI = photoURI;
		this.nameOfContainingAlbum = containingAlbum;
        //this.containedPictureFile = containedPictureFile;
        //The photo has no initial attributes; location/people tag(s) added by user later
    }
	
	public void setURI(String newURI) {
		this.photoURI = newURI;
	}
	
	public String getURI() {
		return photoURI;
	}
	
	public void setLocation(String newLocation) {
		this.location = newLocation;
	}
	
	public String getLocation() {
		return location;
	}
	
	public boolean addPerson(String newPerson) {
		for (String i:people) {
			if (i.equalsIgnoreCase(newPerson)) { //if person with same name is found
				return false; //do not add newPerson
			}
		}
		//else if no repeat found
		this.people.add(newPerson); //add
		return true;
	}
	
	public boolean delPerson(String personToLookFor) {
		for (String i:people) {
			if (i.equalsIgnoreCase(personToLookFor)) { //if matching name found
				this.people.remove(i); //remove this person
				return true;
			}
		}
		//else if no match
		return false;
	}

	public boolean containsPerson(String newPerson) {
		if (!people.isEmpty()) {
			for (String i:people) {
				if (i.equalsIgnoreCase(newPerson)) { //if person with same name is found
					return true; //do not add newPerson
				}
			}
		}

		//else if people.isEmpty() || no such person found
		return false;
	}
	public ArrayList<String> getPeople() {
		return people;
	}

	public String getNameOfContainingAlbum() {
		return nameOfContainingAlbum;
	}
	public void setNameOfContainingAlbum(String nameOfNewAlbum) {
		nameOfContainingAlbum = nameOfNewAlbum;
	}
}