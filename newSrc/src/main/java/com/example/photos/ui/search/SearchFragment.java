package com.example.photos.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.photos.R;
import com.example.photos.databinding.FragmentSearchBinding;
import com.example.photos.model.Photo;
import com.example.photos.shared.SharedViewModel;
import com.example.photos.ui.photos.PhotosFragment;
import com.example.photos.ui.results.ResultsFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private static final String[] autoCompleteTestArr = new String[] {
            "Alabama", "Alaska", "Arizona", "Arkansas", //US states starting with A
            "Alderney", "Liberty", "Leonida", "San Andreas", "North Yankton" //Fictional states in the GTA series
    };

    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //SPINNER SETUP
        Spinner typeSpinner1 = (Spinner) root.findViewById(R.id.typeSpinner1);
        Spinner typeSpinner2 = (Spinner) root.findViewById(R.id.typeSpinner2);
        Spinner conjSpinner = (Spinner) root.findViewById(R.id.conjSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(
                this.getActivity(),
                R.array.tagChoiceArray,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        typeSpinner1.setAdapter(tagAdapter);
        typeSpinner2.setAdapter(tagAdapter);

        ArrayAdapter<CharSequence> conjAdapter = ArrayAdapter.createFromResource(
                this.getActivity(),
                R.array.conjunctionChoiceArray,
                android.R.layout.simple_spinner_item
        );
        conjAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conjSpinner.setAdapter(conjAdapter);

        //TEXT INPUT SETUP
        AutoCompleteTextView tag1input = (AutoCompleteTextView) root.findViewById(R.id.tagInputEntry1);
        AutoCompleteTextView tag2input = (AutoCompleteTextView) root.findViewById(R.id.tagInputEntry2);
        ArrayAdapter<String> autoInputAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, autoCompleteTestArr);
        tag1input.setAdapter(autoInputAdapter); tag2input.setAdapter(autoInputAdapter);

        //SEARCH BUTTON SETUP
        Button searchButton = (Button) root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner tag1typeChoice = (Spinner) root.findViewById(R.id.typeSpinner1);
                Spinner tag2typeChoice = (Spinner) root.findViewById(R.id.typeSpinner2);
                Spinner conjunctionChoice = (Spinner) root.findViewById(R.id.conjSpinner);

                String tag1 = tag1input.getText().toString();
                String tag2 = tag2input.getText().toString();
                String tag1type = tag1typeChoice.getSelectedItem().toString();
                String tag2type = tag2typeChoice.getSelectedItem().toString();
                String conjunction = conjunctionChoice.getSelectedItem().toString();

                ArrayList<Photo> searchResults = performSearch(tag1,tag2,tag1type,tag2type,conjunction);

                if (searchResults != null) {
                    SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                    sharedViewModel.setSearchResults(searchResults);

                    NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.action_nav_search_to_nav_results);
                }
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

    //may change type from void to ArrayList<Photo>
    public ArrayList<Photo> performSearch(String tag1, String tag2, String tag1type, String tag2type, String conjunction) {
        ArrayList<Photo> searchArrayList = new ArrayList<Photo>();
        if (tag1.equals("")) { //if tag1 is empty
            Context context = getContext();
            Toast.makeText(context, "Tag 1 Entry cannot be empty", Toast.LENGTH_LONG).show();
            return null;
        }

        if (tag2.equals("")) { //if tag2 is empty, then only do search on tag1
            for (Photo i:sharedViewModel.getAllPhotosList()) {
                if (tag1type.equals("Location") && i.getLocation().equalsIgnoreCase(tag1)) {
                        searchArrayList.add(i);
                    }
                    else if (tag1type.equals("Person") && i.containsPerson(tag1)) {
                        searchArrayList.add(i);
                    }
            }
        }

        else { //tag2 is not empty
            for (Photo i:sharedViewModel.getAllPhotosList()) {
                boolean tag1match = false, tag2match = false;
                if (tag1type.equals("Location") && i.getLocation().equalsIgnoreCase(tag1)) { //tag1 location match
                    tag1match = true;
                    if (tag2type.equals("Location") && i.getLocation().equalsIgnoreCase(tag2)) {
                        tag2match = true;
                    }
                    else if (tag2type.equals("Person") && i.containsPerson(tag2)) {
                        tag2match = true;
                    }
                }
                else if (tag1type.equals("Person") && i.containsPerson(tag1)) { //tag1 person match
                    if (i.containsPerson(tag1)) {
                        tag1match = true;
                        if (tag2type.equals("Location") && i.getLocation().equalsIgnoreCase(tag2)) {
                            tag2match = true;
                        }
                        else if (tag2type.equals("Person") && i.containsPerson(tag2)) {
                            tag2match = true;
                        }
                    }
                }
                if (conjunction.equals("And") && (tag1match && tag2match)) {
                    searchArrayList.add(i);
                }
                else if (conjunction.equals("Or") && (tag1match || tag2match)) { //"Or"
                    searchArrayList.add(i);
                }
            }
        }

        return searchArrayList;
    }
}