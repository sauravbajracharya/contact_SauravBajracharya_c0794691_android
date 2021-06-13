package com.example.contact_sauravbajracharya_c0794691_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;
import com.example.contact_sauravbajracharya_c0794691_android.model.ContactViewModel;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener{


    private ContactViewModel contactViewModel;

    private RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;

    public static final String CONTACT_ID = "contact_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactViewModel.getAllContacts().observe(this, contacts -> {
            //set adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });


    }

    @Override
    public void onContactClick(int position) {
        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }
}