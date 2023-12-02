package com.example.photos.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.photos.model.Home;

import com.example.photos.R;

public class albumList extends AppCompatActivity {

    ListView albumlist;
    String listofalbum[] = Home.albums.toArray(new String[Home.albums.size()]);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
    }
}