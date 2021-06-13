package com.example.contact_sauravbajracharya_c0794691_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Query("DELETE FROM contact")
    void deleteAll();

    @Query("SELECT * FROM contact ORDER BY first_name ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contact WHERE id == :id")
    LiveData<Contact> getContact(int id);

    @Query("SELECT * FROM contact WHERE first_name == :queryData")
    LiveData<List<Contact>> getSearchContact(String queryData);


    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);
}
