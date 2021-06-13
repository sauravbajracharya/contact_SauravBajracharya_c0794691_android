package com.example.contact_sauravbajracharya_c0794691_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;
import com.example.contact_sauravbajracharya_c0794691_android.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener{


    private ContactViewModel contactViewModel;

    private RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;

    public static final String CONTACT_ID = "contact_id";

    private Contact deletedContact;
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            launcher.launch(intent);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Contact contact = contactViewModel.getAllContacts().getValue().get(position);
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Are you sure?");
                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            deletedContact = contact;
                            contactViewModel.delete(contact);
                            Snackbar.make(recyclerView, deletedContact.getFirstName() + " is deleted!", Snackbar.LENGTH_LONG)
                                    .setAction("Undo", v -> contactViewModel.insert(deletedContact)).show();
                        });
                        builder.setNegativeButton("No", (dialog, which) -> recyclerViewAdapter.notifyDataSetChanged());
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
//                    case ItemTouchHelper.RIGHT:
//                        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
//                        intent.putExtra(CONTACT_ID, contact.getId());
//                        startActivity(intent);
//                        break;

                }
            }

//            @Override
//            public void onChildDraw(@NonNull  Canvas c, @NonNull  RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        .setIconHorizontalMargin(1,1)
//                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
//                        .addSwipeRightActionIcon(R.drawable.ic_update)
//                        .create()
//                        .decorate();
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
        };


    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String first_name = data.getStringExtra(AddContactActivity.FIRST_NAME_REPLY);
                    String last_name = data.getStringExtra(AddContactActivity.LAST_NAME_REPLY);
                    String email = data.getStringExtra(AddContactActivity.EMAIL_REPLY);
                    String number = data.getStringExtra(AddContactActivity.NUMBER_REPLY);
                    String address = data.getStringExtra(AddContactActivity.ADDRESS_REPLY);
//                    // getting the current date
//                    Calendar cal = Calendar.getInstance();
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
//                    String joiningDate = sdf.format(cal.getTime());

                    Contact contact = new Contact(first_name, last_name, email, number, address);
                    contactViewModel.insert(contact);
                }
            });

    @Override
    public void onContactClick(int position) {
        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }
}