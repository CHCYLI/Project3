package com.example.photos.model;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * @author Chris Li
 * @author Tony Lu
 */
public class Home implements Serializable{
	public ArrayList<Album> listOfAlbums = new ArrayList<Album>();
	private Photo copiedPhoto = null;
	private String username;
	public static ArrayList<Album> albums;
	
	public Home (String name) {
		username = name;
		albums = new ArrayList<Album>();
	}
	
}