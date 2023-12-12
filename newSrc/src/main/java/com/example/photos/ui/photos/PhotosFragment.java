package com.example.photos.ui.photos;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.util.stream.Collectors;

public class PhotosFragment extends Fragment {
    public String albumName;
    private ArrayList<Photo> photosInAlbum;
    private FragmentPhotosBinding binding;
    private SharedViewModel sharedViewModel;
    private Uri selectedImage;
    private ActivityResultLauncher<String> getContentLauncher;
    private ArrayList<String> photosURIs;

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

        //for loop, creating arraylist of all photos in album
        photosInAlbum = new ArrayList<Photo>();
        for (Photo i:sharedViewModel.getAllPhotosList()) {
            if (i.getNameOfContainingAlbum().equals(albumName)) {
                photosInAlbum.add(i);
            }
        }

        photosURIs = new ArrayList<String>();

        if (photosInAlbum != null) {
            sharedViewModel.setPhotosInAlbum(photosInAlbum);
            photosURIs = photosInAlbum.stream().map(Photo::getURI).collect(Collectors.toCollection(ArrayList::new));
        }

        ListView photosListView = (ListView) root.findViewById(R.id.photosListView);
        ArrayAdapter<String> photosAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1,photosURIs);
        photosListView.setAdapter(photosAdapter);
        photosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getContext();
                String imageURI = photosURIs.get(position);
                sharedViewModel.setSelectedAlbumName(albumName);
                sharedViewModel.setCurrPhotoURI(imageURI);

                NavHostFragment.findNavController(PhotosFragment.this).navigate(R.id.action_nav_photos_to_nav_slideshow);
            }
        });

        getContentLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            ImageView testImageView = root.findViewById(R.id.testImageView);
                            testImageView.setImageURI(result);
                            photosTestText.setText(result.toString());

                            //move to after loop
                            photosURIs.add(result.toString());
                            photosAdapter.notifyDataSetChanged();
                            photosInAlbum.add(new Photo(result.toString(),albumName));
                            sharedViewModel.setPhotosInAlbum(photosInAlbum);
                            sharedViewModel.addPhotoToAllPhotosList(result.toString(),albumName);

                            /*for (Photo i:sharedViewModel.getAllPhotosList()) {
                                if (i.getURI().equals(result.toString())) {
                                    Toast.makeText(getContext(), "Duplicate exists", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }*/
                            //No repeat found:

                            //TODO: save to file
                        }
                    }
                });

        // Set up a button to open the gallery
        Button addPhotoButton = root.findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentLauncher.launch("image/*");
            }
        });

        /*Button temp_toSlideshowButton = root.findViewById(R.id.toSlideshowButton);
        temp_toSlideshowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View root) {
                NavHostFragment.findNavController(PhotosFragment.this).navigate(R.id.action_nav_photos_to_nav_slideshow);
            }
        });*/

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}