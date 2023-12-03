package com.example.photos.ui.checkalbums;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos.R;

import com.example.photos.databinding.FragmentAlbumBinding;
import com.example.photos.databinding.FragmentHomeBinding;
import com.example.photos.ui.home.HomeViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class AlbumFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;

    private FragmentAlbumBinding binding;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view.findViewById(R.id.listView).setOnClickListener(this);
        String[] temp = {"example", "testing"};
        listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, temp);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView)getView().findViewById(R.id.listView);
        button = (Button)getView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();
    }*/

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getParentFragment().getContext();
                Toast.makeText(context, "Album Removed", Toast.LENGTH_LONG).show();
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void addItem(View view) {
        EditText inputName = getView().findViewById(R.id.editTextText);
        String itemText = inputName.getText().toString();

        if (!(itemText.equals(""))) {
            itemsAdapter.add(itemText);
            inputName.setText("");
        } else {
            Toast.makeText(getParentFragment().getActivity(), "Enter the name please", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlbumViewModel AlbumViewModel =
                new ViewModelProvider(this).get(AlbumViewModel.class);
        //View view = inflater.inflate(R.layout.fragment_album, container, false);

        binding = FragmentAlbumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*listView = (ListView)getView().findViewById(R.id.listView);
        button = (Button)getView().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();*/

        //final TextView textView = binding.editTextText;
        //AlbumViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Toast.makeText(getActivity(), "example", Toast.LENGTH_SHORT).show();
        }

        if (position == 1) {
            Toast.makeText(getActivity(), "testing", Toast.LENGTH_SHORT).show();
        }
    }
}