package com.example.contact_sauravbajracharya_c0794691_android.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.contact_sauravbajracharya_c0794691_android.data.ContactRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {


    private final String TAG = ContactViewModel.this.getClass().getSimpleName();

    private ContactRepository contactRepository;
    private final LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        contactRepository = new ContactRepository(application);
        allContacts =  contactRepository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {

        return allContacts;
    }


    public LiveData<Contact> getContact(int id){

        return contactRepository.getContact(id);
    }

    public LiveData<List<Contact>> getSearchContact(String queryData){
        return  contactRepository.getSearchContact(queryData);
    }

    public void insert(Contact contact){

        contactRepository.insert(contact);
    }

    public void update(Contact contact){
        contactRepository.update(contact);
    }
    public void delete(Contact contact){
        contactRepository.delete(contact);
    }







}
