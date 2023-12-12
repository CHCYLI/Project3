package com.example.photos.shared;

import android.text.TextUtils;
import android.util.Pair;
import android.content.Context;
import androidx.lifecycle.ViewModel;
import com.example.photos.model.Photo;
import com.example.photos.ui.home.HomeFragment;
import com.example.photos.ui.photos.PhotosFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

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

    private ArrayList<Pair<String,String>> allTagsList = new ArrayList<Pair<String,String>>(); //stores all tags, for autocomplete
    private ArrayList<Photo> allPhotosList = new ArrayList<Photo>();

    private ArrayList<String> allLocationList = new ArrayList<String>();
    private ArrayList<String> allAlbumsList = new ArrayList<String>();
    private ArrayList<Photo> searchResults = new ArrayList<Photo>();
    private ArrayList<Photo> photosInAlbum = new ArrayList<Photo>(); //temporary arraylist for data passing bet. photos and slideshow
    private String currPhotoURI;
    PhotosFragment tempPhotosFrag = new PhotosFragment();

    // Methods to load data from text files
    public void loadAllPhotosListFromFile() {
        // TODO:Load data from text file and populate the ArrayList
        FileInputStream fis = null;
        ArrayList<String> uriarraylist = new ArrayList<>();
        ArrayList<Character> charArrayList = new ArrayList<Character>();
        ArrayList<String> namearraylist = new ArrayList<>();

        try {
            fis = tempPhotosFrag.getContext().openFileInput(FILE_NAME);
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
                    if (ch == ':') {
                        coloncheck = true;
                        continue;
                    }
                    if (ch == ',') {
                        if (charArrayList.isEmpty()) continue;
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
                    if (ch == ':') {
                        coloncheck = true;
                        continue;
                    }
                    if (ch == ',') {
                        if (charArrayList.isEmpty()) continue;
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
                    if (charArrayList.isEmpty()) continue;
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
        FileInputStream fis = null;
        ArrayList<Character> charArrayList = new ArrayList<Character>();
        String firsttag = "";
        String secondtag = "";

        try {
            //HomeFragment tempHomeFrag = new HomeFragment();
            fis = tempPhotosFrag.getContext().openFileInput(FILE_NAME);
            int ch;
            boolean coloncheck = false;
            boolean firstcomma = false;
            Pair<String, String> tempPair;

            while ((ch = fis.read()) != -1) {
                if (ch == '{') {
                    positioncount = 0;
                    continue;
                }

                if (positioncount == 0) {
                    if (ch == ',') {
                        positioncount = 1;
                    }
                } else if (positioncount == 1) {
                    if (ch == ':') {
                        coloncheck = true;
                        continue;
                    }
                    if (ch == ',') {
                        if (charArrayList.isEmpty()) continue;
                        StringBuilder builder = new StringBuilder(charArrayList.size());
                        for(Character c: charArrayList) {
                            builder.append(c);
                        }
                        allLocationList.add(builder.toString());
                        charArrayList.clear();

                        positioncount = 2;
                        coloncheck = false;
                    } else {
                        if (coloncheck) {
                            charArrayList.add((char)ch);
                        }
                    }
                } else if (positioncount == 2) {
                    if (ch == ':') {
                        coloncheck = true;
                        continue;
                    }
                    if (ch == '[') {
                        continue;
                    }
                    if (ch == ',' && !firstcomma) {
                        if (charArrayList.isEmpty()) continue;
                        StringBuilder builder = new StringBuilder(charArrayList.size());
                        for(Character c: charArrayList) {
                            builder.append(c);
                        }
                        firsttag = builder.toString();
                        charArrayList.clear();
                        firstcomma = true;
                    } else if (ch == ']') {
                        if (charArrayList.isEmpty()) continue;
                        StringBuilder builder = new StringBuilder(charArrayList.size());

                        for(Character c: charArrayList) {
                            builder.append(c);
                        }
                        secondtag = builder.toString();
                        charArrayList.clear();
                        firstcomma = false;
                        coloncheck = false;
                        positioncount = 3;
                    } else {
                        if (coloncheck) {
                            charArrayList.add((char)ch);
                        }
                    }
                } else if (positioncount == 3){
                    continue;
                }
                if (ch == '}') {
                    tempPair = new Pair<String, String>(firsttag, secondtag);
                    allTagsList.add(tempPair);
                }
            }

            fis.close();


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

    public void setAllTagsList(ArrayList<Pair<String, String>> newList) {
        allTagsList = newList;
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

    public void setAllPhotosList(ArrayList<Photo> newList) {
        allPhotosList = newList;
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
    /*public ArrayList<String> getAllAlbumsList() {}
    public boolean addAlbumToAllAlbumsList(String newAlbumName) {}*/

    public void setSearchResults(ArrayList<Photo> photos) {
        this.searchResults = photos;
    }

    public ArrayList<Photo> getSearchResults() {
        return searchResults;
    }

    public void setPhotosInAlbum(ArrayList<Photo> photos) {
        this.photosInAlbum = photos;
    }

    public ArrayList<Photo> getPhotosInAlbum() {
        return photosInAlbum;
    }

    public void setCurrPhotoURI(String name) {
        currPhotoURI = name;
    }
    public String getCurrPhotoURI() {
        return currPhotoURI;
    }


}
