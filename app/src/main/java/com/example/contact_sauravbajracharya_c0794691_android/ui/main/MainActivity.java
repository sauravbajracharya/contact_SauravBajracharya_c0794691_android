package com.example.contact_sauravbajracharya_c0794691_android.ui.main;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.contact_sauravbajracharya_c0794691_android.ui.add.AddContactActivity;
import com.example.contact_sauravbajracharya_c0794691_android.R;
import com.example.contact_sauravbajracharya_c0794691_android.ui.main.adapter.RecyclerViewAdapter;
import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;
import com.example.contact_sauravbajracharya_c0794691_android.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private TextView contactSize;

    private EditText searchText;

    private Button searchButton;

    private ContactViewModel contactViewModel;

    private TelephonyManager mTelephonyManager;

    private RecyclerView recyclerView;

    private List<Contact> contactArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;
    public static final String CONTACT_ID = "contact_id";

    private Contact deletedContact;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        setUpViews();
        initListeners();
        prepareRecyclerView();
        doEditTextObserveWork();

        mTelephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);

        contactViewModel.getAllContacts().observe(this, contacts -> {
            //set adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this);
            recyclerView.setAdapter(recyclerViewAdapter);

            contactSize.setText("Total Contacts  "+ String.valueOf(contacts.size()));

        });

    }


    private String getTotalCount(){

        if (recyclerViewAdapter != null){

            return String.valueOf(recyclerViewAdapter.contactList.size());

        }
        return "Loading...";
    }

    private void initListeners() {

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            launcher.launch(intent);
        });


    }

    private void doEditTextObserveWork() {

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                recyclerViewAdapter.getFilter().filter(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setUpViews() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactSize = findViewById(R.id.total_count);
        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.button);

    }

    private void prepareRecyclerView() {



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
                    case ItemTouchHelper.RIGHT:
                        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                        intent.putExtra(CONTACT_ID, contact.getId());
                        startActivity(intent);
                        recyclerViewAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildDraw(@NonNull  Canvas c, @NonNull  RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

//                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                        .setIconHorizontalMargin(1,1)
//                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
//                        .addSwipeRightActionIcon(R.drawable.ic_update)
//                        .create()
//                        .decorate();
            }
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


                    Contact contact = new Contact(first_name, last_name, email, number, address);
                    contactViewModel.insert(contact);
                }
            });

    @Override
    public void onContactClick(int position) {

        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        String toEmail = contact.getEmail();
        String toNumber = contact.getPhoneNumber();

        String[] options = {"Send Email", "Send SMS", "Call"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick an option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                if ("Send Email".equals(options[which])) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ toEmail});
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                } else if ("Send SMS".equals(options[which])) {
                    Intent smsintent = new Intent(Intent.ACTION_VIEW);
                    smsintent.setData(Uri.parse("smsto:" + toNumber));
                    smsintent.putExtra("address", toNumber);
//                    smsintent.putExtra("sms_body", "Hello");
                    smsintent.putExtra("exit_on_sent", true);
                    startActivity(smsintent);
                } else if ("Call".equals(options[which])) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse(("tel:" + toNumber)));
                    startActivity(callIntent);
                }
            }
        });
        builder.show();

    }

}