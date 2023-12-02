/*
 * @author Chris Li
 * @author Tony Lu
 */
package com.example.photos.model;
import android.graphics.Picture;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
	private String albumName;
	private ArrayList<Photo> photosInAlbum;

	public Album(String name, String user) {
		this.albumName = name;
		this.photosInAlbum = new ArrayList<Photo>();
	}

	public void setAlbumName(String name) {
		this.albumName = name;
	}

	public String getAlbumName() {
		return albumName;
	}

	public boolean addPhoto(Photo newPhoto) {
		for (Photo i:photosInAlbum) {
			String iName = i.getName();
			if (iName.equals(newPhoto.getName())) { //if photo with same name is found
				return false; //do not add newPhoto
			}
		}
		//else if no repeat
		photosInAlbum.add(newPhoto);
		return true;
	}

	public boolean delPhoto(String photoName) {
		for (Photo i:photosInAlbum) {
			String iName = i.getName();
			if (iName.equals(photoName)) { //if matching name found
				this.photosInAlbum.remove(i); //remove photo
				return true;
			}
		}
		//else if no match
		return false;
	}

	//--------------------------LEGACY---------------------------------
	public int getAlbumSize() {
		return photosInAlbum.size();
	}

	public Photo getPhoto(String photoName) {
		for (Photo i:photosInAlbum) {
			String iName = i.getName();
			if (iName.equals(photoName)) { //if matching name found
				return i;
			}
		}
		//else
		return null;
	}

	public Photo getPhoto(int index) { //get photos by index
		return photosInAlbum.get(index);
	}

	public int getPhotoIndex(String name) {
		for (int i = 0; i < photosInAlbum.size(); i++) {
			if (photosInAlbum.get(i).getName().equals(name)) return i;
		}
		return -1;
	}
}