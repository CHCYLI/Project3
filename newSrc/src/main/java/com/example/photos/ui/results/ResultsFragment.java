package com.example.photos.ui.results;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.photos.R;
import com.example.photos.databinding.FragmentResultsBinding;
import com.example.photos.databinding.FragmentSearchBinding;
import com.example.photos.model.Photo;
import com.example.photos.shared.SharedViewModel;
import com.example.photos.ui.home.HomeFragment;
import com.example.photos.ui.photos.PhotosFragment;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ResultsFragment extends Fragment {

    private FragmentResultsBinding binding;

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

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        ArrayList<Photo> searchResults = sharedViewModel.getSearchResults();
        ArrayList<String> searchResultsURIs = searchResults.stream().map(Photo::getURI).collect(Collectors.toCollection(ArrayList::new));
        ListView resultsListView = (ListView) root.findViewById(R.id.searchResultsListView);
        ArrayAdapter<String> resultsAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1,searchResultsURIs);
        resultsListView.setAdapter(resultsAdapter);

        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getContext();

                String imageURI = searchResultsURIs.get(position);
                sharedViewModel.setCurrPhotoURI(imageURI);

                NavHostFragment.findNavController(ResultsFragment.this).navigate(R.id.action_nav_results_to_nav_slideshow);
            }
        });

        //final TextView textView = binding.textSearch;
        //searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}