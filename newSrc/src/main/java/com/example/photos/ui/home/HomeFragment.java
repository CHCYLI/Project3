package com.example.photos.ui.home;

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

import com.example.photos.databinding.FragmentHomeBinding;
import com.example.photos.ui.home.HomeViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class HomeFragment extends Fragment{
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;
    private Button button_rename;

    private FragmentHomeBinding binding;

    private static final String FILE_NAME = "albumslist.txt";
    private static final String TEMP_NAME = "tempalbums.txt";
    String albumName;
    EditText mEditText;
    //String[] temp = {"example", "testing"};
    EditText newAlbumName;

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

                FileOutputStream fos = null;
                FileInputStream fis = null;
                try {
                    //Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
                    fis = getContext().openFileInput(FILE_NAME);
                    //Toast.makeText(context, "World", Toast.LENGTH_LONG).show();
                    fos = getContext().openFileOutput(TEMP_NAME, Context.MODE_PRIVATE);

                    int ch;
                    int commaCount = 0;
                    while ((ch = fis.read()) != -1) {
                        if (ch == ',') commaCount++;
                        if (commaCount-1 != position)
                            fos.write(ch);
                    }

                    fos.close();
                    fis.close();

                    FileInputStream tempAlbumFile = getContext().openFileInput(TEMP_NAME);
                    FileOutputStream newFile = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                    while ((ch = tempAlbumFile.read()) != -1) {
                        newFile.write(ch);
                    }

                    tempAlbumFile.close();
                    newFile.close();

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
                return true;
            }
        });
    }

    private void setButton_rename(View view){
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String text = mEditText.getText().toString();
        String newName = newAlbumName.getText().toString();
        ArrayList<String> tempitems = new ArrayList<>();
        ArrayList<Character> charArrayList = new ArrayList<Character>();

        if (text.equals("") || newName.equals("")) {
            Toast.makeText(getActivity(), "Name entry cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            fis = getContext().openFileInput(FILE_NAME);
            fos = getContext().openFileOutput(TEMP_NAME, Context.MODE_PRIVATE);
            boolean firstcomma = false;

            int ch;
            while ((ch = fis.read()) != -1) {
                if (ch == ',' && !firstcomma) {
                    firstcomma = true;
                    //fos.write(ch);
                } else if (ch == ',') {
                    StringBuilder builder = new StringBuilder(charArrayList.size());
                    for(Character c: charArrayList) {
                        builder.append(c);
                    }
                    if (text.equals(builder.toString())) {
                        charArrayList.clear();
                        Toast.makeText(view.getContext(), "Found it!", Toast.LENGTH_LONG).show();
                        continue;
                    }
                    tempitems.add(builder.toString());
                    char[] temp = builder.toString().toCharArray();
                    fos.write(',');
                    for (int i = 0; i < temp.length; i++) {
                        fos.write(temp[i]);
                    }
                    charArrayList.clear();
                } else {
                    charArrayList.add((char)ch);
                }
            }

            if (!charArrayList.isEmpty()) {
                StringBuilder builder = new StringBuilder(charArrayList.size());
                for(Character c: charArrayList) {
                    builder.append(c);
                }
                char[] temp = builder.toString().toCharArray();
                fos.write(',');
                for (int i = 0; i < temp.length; i++) {
                    fos.write(temp[i]);
                }
                tempitems.add(builder.toString());
                charArrayList.clear();
            }

            char[] tempArray = newName.toCharArray();
            tempitems.add(newName);
            fos.write(',');
            for (int i = 0; i < tempArray.length; i++) {
                fos.write(tempArray[i]);
            }


            fis.close();
            fos.close();

            FileInputStream tempAlbumFile = getContext().openFileInput(TEMP_NAME);
            FileOutputStream newFile = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            while ((ch = tempAlbumFile.read()) != -1) {
                newFile.write(ch);
            }

            tempAlbumFile.close();
            newFile.close();

            mEditText.getText().clear();
            newAlbumName.getText().clear();

            items = load(view);
            itemsAdapter =  new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, items);
            listView.setAdapter(itemsAdapter);

            Toast.makeText(view.getContext(), "Rename successfully!", Toast.LENGTH_LONG).show();
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

    private void addItem(View view) {
        EditText inputName = view.findViewById(R.id.editTextText);
        String itemText = inputName.getText().toString();

        if (!(itemText.equals(""))) {
            //mEditText = view.findViewById(R.id.editTextText);
            itemsAdapter.add(itemText);
            save(view);
            inputName.setText("");
        } else {
            Toast.makeText(getActivity(), "Name entry cannot be empty", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        button = view.findViewById(R.id.button);
        button_rename = view.findViewById(R.id.button_rename);
        mEditText = view.findViewById(R.id.editTextText);
        newAlbumName = view.findViewById(R.id.editTextText2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View viewone) {
                addItem(view);
            }
        });


        button_rename.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View viewone) {

                setButton_rename(view);
            }
        });

        items = load(view);
        itemsAdapter =  new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, items);

        listView.setAdapter(itemsAdapter);

        setUpListViewListener();
        return view;
    }

    public void save(View view) {
        String text = mEditText.getText().toString();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = getContext().openFileInput(FILE_NAME);
            fos = getContext().openFileOutput(TEMP_NAME, Context.MODE_PRIVATE);

            int ch;
            while ((ch = fis.read()) != -1) {
                fos.write(ch);
            }

            char[] tempArray = text.toCharArray();
            fos.write(',');
            for (int i = 0; i < tempArray.length; i++) {
                fos.write(tempArray[i]);
            }

            fis.close();
            fos.close();

            FileInputStream tempAlbumFile = getContext().openFileInput(TEMP_NAME);
            FileOutputStream newFile = getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            while ((ch = tempAlbumFile.read()) != -1) {
                newFile.write(ch);
            }

            tempAlbumFile.close();
            newFile.close();


            mEditText.getText().clear();
            Toast.makeText(view.getContext(), "Saved To" + getActivity().getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
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

    public ArrayList<String> load(View view) {
        FileInputStream fis = null;
        //FileOutputStream fos = null;
        ArrayList<String> tempitems = new ArrayList<>();
        //ArrayAdapter<String> tempitemsAdapter;
        ArrayList<Character> charArrayList = new ArrayList<Character>();

        try {
            fis = getContext().openFileInput(FILE_NAME);
            int ch;
            boolean firstcomma = false;

            while ((ch = fis.read()) != -1) {
                if (ch == ',' && !firstcomma) {
                    firstcomma = true;
                    continue;
                } else if (ch == ',') {
                    StringBuilder builder = new StringBuilder(charArrayList.size());
                    for(Character c: charArrayList) {
                        builder.append(c);
                    }
                    tempitems.add(builder.toString());
                    charArrayList.clear();
                } else {
                    charArrayList.add((char)ch);
                }
            }

            StringBuilder builder = new StringBuilder(charArrayList.size());
            for(Character c: charArrayList) {
                builder.append(c);
            }
            if (!builder.toString().equals(""))
                tempitems.add(builder.toString());
            charArrayList.clear();

            fis.close();

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

        return tempitems;
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