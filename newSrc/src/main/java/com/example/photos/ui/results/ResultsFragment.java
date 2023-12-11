package com.example.photos.ui.results;

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
import androidx.navigation.fragment.NavHostFragment;

import com.example.photos.R;
import com.example.photos.databinding.FragmentResultsBinding;
import com.example.photos.databinding.FragmentSearchBinding;
import com.example.photos.ui.photos.PhotosFragment;

public class ResultsFragment extends Fragment {

    private FragmentResultsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ResultsViewModel searchViewModel =
                new ViewModelProvider(this).get(ResultsViewModel.class);

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle args = getArguments();
        if (args != null && args.containsKey("test")) {
            String myString = args.getString("test");
            System.out.println(myString);
            TextView testTextView = (TextView) root.findViewById(R.id.testTextView);
            testTextView.setText(myString);
        }

        Button temp_toSlideshowButton = root.findViewById(R.id.tempToSlideshow);
        temp_toSlideshowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View root) {
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