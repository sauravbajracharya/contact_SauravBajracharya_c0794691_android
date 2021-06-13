package com.example.contact_sauravbajracharya_c0794691_android.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;
import com.example.contact_sauravbajracharya_c0794691_android.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {

    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacts;

    public ContactRepository(Application application){
       ContactRoomDatabase db =  ContactRoomDatabase.getInstance(application);
        contactDao = db.contactDao();
        allContacts = contactDao.getAllContacts();
    }


    public LiveData<List<Contact>> getAllContacts()
    {
        return allContacts;
    }


    public void insert(Contact employee){
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.insert(employee);
        });
    }

    public void update(Contact employee){
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.update(employee);
        });
    }

    public void delete(Contact employee){
        ContactRoomDatabase.databaseWriteExecutor.execute(() -> {
            contactDao.delete(employee);
        });
    }




}
