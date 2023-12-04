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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class AlbumFragment extends Fragment{
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;

    private FragmentAlbumBinding binding;

    private static final String FILE_NAME = "albumslist.txt";
    String albumName;
    //String[] temp = {"example", "testing"};

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //view.findViewById(R.id.listView).setOnClickListener(this);
        String[] temp = {"example", "testing"};
        listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, temp);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

    }*/

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
                Context context = getContext();
                Toast.makeText(context, "Album Removed", Toast.LENGTH_LONG).show();
                albumName = items.get(position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    private void addItem(View view) {
        EditText inputName = view.findViewById(R.id.editTextText);
        String itemText = inputName.getText().toString();

        if (!(itemText.equals(""))) {
            itemsAdapter.add(itemText);
            save(view);
            inputName.setText("");
        } else {
            Toast.makeText(getActivity(), "Enter the name please", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        button =view.findViewById(R.id.button);
        load(view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View viewone) {
                addItem(view);
            }
        });

        items = new ArrayList<>();
        itemsAdapter =  new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, items);

        listView.setAdapter(itemsAdapter);

        setUpListViewListener();
        return view;
    }

    public void save(View view) {
        albumName = view.findViewById(R.id.editTextText).toString();
        FileOutputStream fos = null;
        try {
            fos = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(albumName.getBytes());
            albumName = "";
            Toast.makeText(view.getContext(), "Saved To" + getContext().getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(View view) {
        FileInputStream fis = null;

        try {
            fis = getContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                builder.append(text).append("\n");
            }

            albumName = builder.toString();

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
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Toast.makeText(getActivity(), "example", Toast.LENGTH_SHORT).show();
        }

        if (position == 1) {
            Toast.makeText(getActivity(), "testing", Toast.LENGTH_SHORT).show();
        }
    }*/
}