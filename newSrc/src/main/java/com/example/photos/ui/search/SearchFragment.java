package com.example.photos.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.photos.R;
import com.example.photos.databinding.FragmentSearchBinding;
import com.google.android.material.snackbar.Snackbar;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

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
        // Create an ArrayAdapter using the string array and a default spinner layout.
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

        //SEARCH BUTTON SETUP
        Button searchButton = (Button) root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                //TODO: get content of tag1, tag2, tag1type, tag2type, conjunction
                String tag1;
                //if tag1 empty: error!
                //else (tag1 !empty):
                //if tag2 empty: ignore tag2 & conjunction, search for tag1
                //else: (tag2 !empty)
                //if and: search for tag1 && tag2
                //else (or): search for tag1 || tag2
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