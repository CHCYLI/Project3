package com.example.photos.ui.slideshow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.photos.R;
import com.example.photos.databinding.FragmentSlideshowBinding;
import com.example.photos.model.Photo;
import com.example.photos.shared.SharedViewModel;
import com.example.photos.ui.results.ResultsViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private ArrayList<Photo> photosInAlbum;
    private String currPhotoURI;

    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ResultsViewModel searchViewModel =
                new ViewModelProvider(this).get(ResultsViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        photosInAlbum = sharedViewModel.getPhotosInAlbum();
        currPhotoURI = sharedViewModel.getCurrPhotoURI();

        Spinner newTypeSpinner = (Spinner) root.findViewById(R.id.newTagSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> newTypeAdapter = ArrayAdapter.createFromResource(
                this.getActivity(),
                R.array.tagChoiceArray,
                android.R.layout.simple_spinner_item
        );
        newTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newTypeSpinner.setAdapter(newTypeAdapter);

        //@tools:sample/backgrounds/scenic[0]

        ImageView displayedImage = (ImageView) root.findViewById(R.id.imageSlideshow);
        displayedImage.setImageResource(R.drawable.ic_menu_gallery); //test image, to be replaced
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.parse(currPhotoURI));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            displayedImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle the error
        } catch (SecurityException e) {
            e.printStackTrace();
            // Handle the security exception
        }

        Button moveButton = (Button) root.findViewById(R.id.moveButton);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAlbumName = ((TextInputEditText) root.findViewById(R.id.newTagTextInput)).getText().toString();
                move(newAlbumName);
            }
        });

        Button prevButton = (Button) root.findViewById(R.id.prevPhotoButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPrevPhoto();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.parse(currPhotoURI));
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    displayedImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // Handle the error
                } catch (SecurityException e) {
                    e.printStackTrace();
                    // Handle the security exception
                }
            }
        });

        Button nextButton = (Button) root.findViewById(R.id.nextPhotoButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNextPhoto();
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.parse(currPhotoURI));
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    displayedImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // Handle the error
                } catch (SecurityException e) {
                    e.printStackTrace();
                    // Handle the security exception
                }
            }
        });

        Button addTagButton = (Button) root.findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText newTagTextInput = (TextInputEditText) root.findViewById(R.id.newTagTextInput);
                Spinner newTagTypeSpinner = (Spinner) root.findViewById(R.id.newTagSpinner);

                String newTagName = newTagTextInput.getText().toString();
                String newTagType = newTagTypeSpinner.getSelectedItem().toString();

                if (newTagType.equals("Location")) addLocation(newTagName);
                else addPerson(newTagName); //Person
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void toNextPhoto() { //updates currPhotoURI
        int currIndex = -1;
        for (int i = 0; i < photosInAlbum.size(); i++) {
            if (photosInAlbum.get(i).getURI().equals(currPhotoURI)) {
                currIndex = i;
                break;
            }
        }
        if (currIndex == -1 || currIndex >= photosInAlbum.size()-1) {
            Toast.makeText(this.getContext(), "Last item in album", Toast.LENGTH_LONG).show();
            return;
        }
        currIndex++;
        currPhotoURI = photosInAlbum.get(currIndex).getURI();
    }

    public void toPrevPhoto() {
        int currIndex = -1;
        for (int i = 0; i < photosInAlbum.size(); i++) {
            if (photosInAlbum.get(i).getURI().equals(currPhotoURI)) {
                currIndex = i;
                break;
            }
        }
        if (currIndex <= 0) {
            Toast.makeText(this.getContext(), "First item in album", Toast.LENGTH_LONG).show();
            return;
        }
        currIndex--;
        currPhotoURI = photosInAlbum.get(currIndex).getURI();
    }

    public void move(String newAlbumName) {
        if (newAlbumName.equals("")) {
            Toast.makeText(this.getContext(), "Album name cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<Photo> temp = sharedViewModel.getAllPhotosList();
        for (Photo i:temp) {
            if (i.getNameOfContainingAlbum().equals(newAlbumName) && i.getURI().equals(currPhotoURI)) { //repeat found
                Toast.makeText(this.getContext(), "Photo of same URI exists in destination", Toast.LENGTH_LONG).show();
                return;
            }
        }
        //loop ends, if no repeat is found
        ArrayList<Photo> tempAllPhotosList = sharedViewModel.getAllPhotosList();
        for (Photo i:tempAllPhotosList) {
            if (i.getURI().equals(currPhotoURI)) {
                i.setNameOfContainingAlbum(newAlbumName);
                break;
            }
        }
        sharedViewModel.setAllPhotosList(tempAllPhotosList);
        Context context = getContext();
        Toast.makeText(context, "Photo moved", Toast.LENGTH_LONG).show();
        //TODO: write to file
    }

    public void addLocation(String newLocation) {
        if (newLocation.equals("")) {
            Context context = getContext();
            Toast.makeText(context, "Tag entry cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<Photo> tempAllPhotosList = sharedViewModel.getAllPhotosList();
        for (Photo i:tempAllPhotosList) {
            if (i.getURI().equals(currPhotoURI)) {
                i.setLocation(newLocation);
                break;
            }
        }
        ArrayList<Pair<String,String>> tempALlTagsList = sharedViewModel.getAllTagsList();
        tempALlTagsList.add(new Pair<>(newLocation, "Location"));
        sharedViewModel.setAllTagsList(tempALlTagsList);
        sharedViewModel.setAllPhotosList(tempAllPhotosList);
        Context context = getContext();
        Toast.makeText(context, "Added tag", Toast.LENGTH_LONG).show();
        //TODO:write to file
    }

    public void addPerson(String newPerson) {
        if (newPerson.equals("")) {
            Context context = getContext();
            Toast.makeText(context, "Tag entry cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<Photo> tempAllPhotosList = sharedViewModel.getAllPhotosList();
        for (Photo i:tempAllPhotosList) {
            if (i.getURI().equals(currPhotoURI)) {
                i.addPerson(newPerson);
                break;
            }
        }

        ArrayList<Pair<String,String>> tempALlTagsList = sharedViewModel.getAllTagsList();
        tempALlTagsList.add(new Pair<>(newPerson, "Person"));
        sharedViewModel.setAllTagsList(tempALlTagsList);
        sharedViewModel.setAllPhotosList(tempAllPhotosList);
        Context context = getContext();
        Toast.makeText(context, "Added tag", Toast.LENGTH_LONG).show();
        //TODO:write to file
    }
}