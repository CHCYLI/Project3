package com.example.photos.shared;

import android.text.TextUtils;
import android.util.Pair;
import android.content.Context;
import androidx.lifecycle.ViewModel;

import com.example.photos.model.Home;
import com.example.photos.model.Photo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import com.example.photos.ui.home.HomeFragment;

public class SharedViewModel extends ViewModel {

    /*
    arraylist containing all tags
    -used for autocomplete (in Search)
    -stored in the form of pair [tagName, tagType], where tagType can be either Location/Person
    -when a new tag is added, attempt to save the new tag in the arraylist
            -look for repeats: don't add if this tag already exists (use equalsIgnoreCase())
    -format: [tag1,type1][tag2,type2]

    arraylist containing all photos
    -used for Results
    -includes their URI, location, people, and nameOfContainingAlbum
    -when a new photo is added, store its URI and nameOfContainingAlbum in the arrayList (location and people are empty upon creation)
        -location/people data are filled in using the addTag functionality (in Slideshow)
    -when displaying photos in an album (in Photos), loop through all photos in the arraylist & find photos with matching String nameOfContainingAlbum (use equalsIgnoreCase())
        -create a temporary arraylist to store matching photos
        -show in listView once search is complete
    -when displaying search results (in Search), loop through all photos w/ matching tags
        -create a temporary arraylist to store matching photos
        -show in listView once search is complete
    -(photo repeats are permitted if in different albums)
    -format: {URI:pathToImageFile,Location:place,People:[person1,person2],Album:albumName}

    arraylist of albums
    -used for Home
    -includes names of albums
    -format: album1,album2

    temp arraylist for search
     */

    private String selectedAlbumName;
    String FILE_NAME = "allPhotos.txt";
    int positioncount=0;

    public HomeFragment tempHomeFrag = new HomeFragment();

    private ArrayList<Pair<String,String>> allTagsList = new ArrayList<>(); //stores all tags, for autocomplete
    private ArrayList<Photo> allPhotosList = new ArrayList<>();
    private ArrayList<String> allAlbumsList = new ArrayList<>();
    private ArrayList<Photo> searchResults = new ArrayList<>();

    // Methods to load data from text files
    public void loadAllPhotosListFromFile() {
        // TODO:Load data from text file and populate the ArrayList
        FileInputStream fis = null;
        //FileOutputStream fos = null;
        //ArrayList<Photo> tempitems = new ArrayList<>();
        ArrayList<String> uriarraylist = new ArrayList<>();
        ArrayList<Character> charArrayList = new ArrayList<Character>();
        ArrayList<String> namearraylist = new ArrayList<>();

        try {
            fis = tempHomeFrag.getContext().openFileInput(FILE_NAME);
            int ch;
            //boolean firstcomma = false;
            int commacount = 0;
            boolean coloncheck = false;

            while ((ch = fis.read()) != -1) {


                if (ch == '{') {
                    positioncount = 0;
                    continue;
                }

                if (positioncount == 0) {
                    if (ch == ':') coloncheck = true;
                    if (ch == ',') {
                        StringBuilder builder = new StringBuilder(charArrayList.size());
                        for(Character c: charArrayList) {
                            builder.append(c);
                        }
                        uriarraylist.add(builder.toString());
                        charArrayList.clear();

                        positioncount = 1;
                        coloncheck = false;
                    } else {
                        if (coloncheck) {
                            charArrayList.add((char)ch);
                        }
                    }
                } else if (positioncount == 1) {
                    if (ch == ',') {
                        positioncount = 2;
                    }
                } else if (positioncount == 2) {
                    if (ch == ',') {
                        if (commacount == 0) {
                            commacount = 1;
                            continue;
                        } else {
                            positioncount = 3;
                            commacount = 0;
                        }
                    }
                } else if (positioncount == 3){
                    if (ch == ':') coloncheck = true;
                    if (ch == ',') {
                        StringBuilder builder = new StringBuilder(charArrayList.size());
                        for(Character c: charArrayList) {
                            builder.append(c);
                        }
                        namearraylist.add(builder.toString());
                        charArrayList.clear();

                        positioncount = 1;
                        coloncheck = false;
                    } else {
                        if (coloncheck) {
                            charArrayList.add((char)ch);
                        }
                    }

                }
                if (ch =='}') {
                    StringBuilder builder = new StringBuilder(charArrayList.size());
                    for(Character c: charArrayList) {
                        builder.append(c);
                    }
                    if (!builder.toString().equals(""))
                        namearraylist.add(builder.toString());

                    charArrayList.clear();

                }
            }

            fis.close();

            //*****use for loop here to finish the rest*************//
            for (int i = 0; i < uriarraylist.size(); i++) {
                Photo temp = new Photo(uriarraylist.get(i), namearraylist.get(i));
                allPhotosList.add(temp);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //return tempitems;
    }
    public void saveAllPhotosListToFile() {
        // TODO:Save data from arraylist to text file
    }

    public void loadAllTagsListFromFile() {
        // TODO:Load data from text file and populate the ArrayList
    }
    public void saveAllTagsListToFile() {
        // TODO:Save data from arraylist to text file
    }

    public void setSelectedAlbumName(String name) {
        selectedAlbumName = name;
    }
    public String getSelectedAlbumName() {
        return selectedAlbumName;
    }

    public ArrayList<Pair<String,String>> getAllTagsList() {
        return allTagsList;
    }

    public boolean addTagToAllTagsList(String tagName, String tagType) {
        for (Pair<String,String> i:allTagsList) {
            if (i.first.equals(tagName) && i.second.equals(tagType)) {
                return false;
            }
        }
        //tag does not exist, add tag to list
        allTagsList.add(new Pair<>(tagName,tagType));
        return true;
    }

    public ArrayList<Photo> getAllPhotosList() {
        return allPhotosList;
    }

    public boolean addPhotoToAllPhotosList(String newURI, String newAlbumName) {
        for (Photo i:allPhotosList) {
            if (i.getURI().equals(newURI) && i.getNameOfContainingAlbum().equals(newAlbumName)) { //duplicate photo exists
                return false;
            }
        }
        //photo does not exist, add photo to list
            //NOTE: photo with duplicate URI but in different albums are allowed
        allPhotosList.add(new Photo(newURI,newAlbumName));
        return true;
    }

    //Made redundant
    /*public ArrayList<String> getAllAlbumsList() {
        return allAlbumsList;
    }

    public boolean addAlbumToAllAlbumsList(String newAlbumName) {
        for (String i:allAlbumsList) {
            if (i.equals(newAlbumName)) { //duplicate album exists
                return false;
            }
        }
        //album does not exist, add album to list
        allAlbumsList.add(newAlbumName);
        return true;
    }*/

    public void setSearchResults(ArrayList<Photo> photos) {
        this.searchResults = photos;
    }

    public ArrayList<Photo> getSearchResults() {
        return searchResults;
    }
}
