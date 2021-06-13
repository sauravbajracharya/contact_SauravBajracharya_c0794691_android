package com.example.contact_sauravbajracharya_c0794691_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;
import com.example.contact_sauravbajracharya_c0794691_android.model.ContactViewModel;

import java.util.Arrays;

public class AddContactActivity extends AppCompatActivity {

    public static final String FIRST_NAME_REPLY = "first_name_reply";
    public static final String LAST_NAME_REPLY = "last_name_reply";
    public static final String EMAIL_REPLY = "email_reply";
    public static final String NUMBER_REPLY = "number_reply";
    public static final String ADDRESS_REPLY = "address_reply";

    private Contact contactTobeUpdated;


    private EditText etFirstName, etLastName, etEmail, etNumber, etAddress;


    private ContactViewModel contactViewModel;

    private boolean isEditing = false;

    private int contactId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etNumber = findViewById(R.id.et_number);
        etAddress = findViewById(R.id.et_address);

        Button addUpdateButton = findViewById(R.id.btn_add_contact);

        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpdateContact();

            }
        });


        if (getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            Log.d("TAG", "onCreate: " + contactId);

            contactViewModel.getContact(contactId).observe(this, contact -> {
                if (contact != null) {
                    etFirstName.setText(contact.getFirstName());
                    etLastName.setText(contact.getLastName());
                    etEmail.setText(contact.getEmail());
                    etNumber.setText(String.valueOf(contact.getPhoneNumber()));
                    etAddress.setText(contact.getAddress());
                    contactTobeUpdated = contact;
                }
            });
            TextView label = findViewById(R.id.label);
            isEditing = true;
            label.setText(R.string.update_label);
            addUpdateButton.setText(R.string.update_contact_btn_text);
        }


    }

    private void addUpdateContact() {

        String first_name = etFirstName.getText().toString().trim();
        String last_name = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (first_name.isEmpty()) {
            etFirstName.setError("name field cannot be empty");
            etFirstName.requestFocus();
            return;
        }

        if (last_name.isEmpty()) {
            etLastName.setError("name field cannot be empty");
            etLastName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("salary field cannot be empty");
            etEmail.requestFocus();
            return;
        }

        if (number.isEmpty()) {
            etNumber.setError("salary field cannot be empty");
            etNumber.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            etAddress.setError("salary field cannot be empty");
            etAddress.requestFocus();
            return;
        }


        if (isEditing) {
            Contact contact = new Contact();
            contact.setId(contactId);
            contact.setFirstName(first_name);
            contact.setLastName(last_name);
            contact.setEmail(email);
            contact.setPhoneNumber(Integer.parseInt(number));
            contact.setAddress(address);
            contactViewModel.update(contact);
        } else {
            Intent replyIntent = new Intent();
            replyIntent.putExtra(FIRST_NAME_REPLY, first_name);
            replyIntent.putExtra(LAST_NAME_REPLY, last_name);
            replyIntent.putExtra(EMAIL_REPLY, email);
            replyIntent.putExtra(NUMBER_REPLY, number);
            replyIntent.putExtra(ADDRESS_REPLY, address);
            setResult(RESULT_OK, replyIntent);

            Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
        }

        finish();

    }
}
