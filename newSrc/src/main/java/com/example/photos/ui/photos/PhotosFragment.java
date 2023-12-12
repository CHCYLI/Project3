package com.example.photos.ui.photos;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.photos.R;
import com.example.photos.databinding.FragmentPhotosBinding;
import com.example.photos.model.Photo;
import com.example.photos.shared.SharedViewModel;
import com.example.photos.ui.home.HomeFragment;
import com.example.photos.ui.results.ResultsViewModel;

import java.util.ArrayList;

public class PhotosFragment extends Fragment {
    public String albumName;
    private ArrayList<Photo> listOfPhotos;
    private FragmentPhotosBinding binding;
    private SharedViewModel sharedViewModel;
    private Uri selectedImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ResultsViewModel searchViewModel =
                new ViewModelProvider(this).get(ResultsViewModel.class);

        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        albumName = sharedViewModel.getSelectedAlbumName();
        TextView photosTestText = root.findViewById(R.id.photosTestText);
        photosTestText.setText(albumName);

        Button addPhotoButton = root.findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View root) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        Button temp_toSlideshowButton = root.findViewById(R.id.toSlideshowButton);
        temp_toSlideshowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View root) {
                NavHostFragment.findNavController(PhotosFragment.this).navigate(R.id.action_nav_photos_to_nav_slideshow);
            }
        });

        //final TextView textView = binding.textSearch;
        //searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            sharedViewModel.addPhotoToAllPhotosList("", ""); //TODO:REPLACE ""
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}