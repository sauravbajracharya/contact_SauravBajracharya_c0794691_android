package com.example.contact_sauravbajracharya_c0794691_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;
import com.example.contact_sauravbajracharya_c0794691_android.model.ContactViewModel;

public class MainActivity extends AppCompatActivity {


    private ContactViewModel contactViewModel;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);


    }
}