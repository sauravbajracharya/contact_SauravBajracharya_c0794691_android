package com.example.contact_sauravbajracharya_c0794691_android.ui.main.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_sauravbajracharya_c0794691_android.R;
import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable{


    public List<Contact> contactList;
    private List<Contact> contactListFiltered;

    public RecyclerViewAdapter(List<Contact> contacts, Context context, OnContactClickListener onContactClickListener) {
        this.contactList = contacts;
        this.contactListFiltered = contacts;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
    }

    private Context context;
    private OnContactClickListener onContactClickListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Contact contact = contactListFiltered.get(position);
        holder.first_name.setText(contact.getFirstName());
        holder.last_name.setText(contact.getLastName());
        holder.email.setText(contact.getEmail());
        holder.number.setText(contact.getPhoneNumber());
        holder.address.setText(contact.getAddress());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirstName().toLowerCase().contains(charString.toLowerCase()) || row.getLastName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (List<Contact>) filterResults.values;

                // refresh the list with filtered data
                Log.e( "publishResults: ","called" );
                Log.e( "publishResults: ",new GsonBuilder().create().toJson(contactListFiltered));
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{


        private TextView first_name;
        private TextView last_name;
        private TextView email;
        private TextView number;
        private TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            first_name = itemView.findViewById(R.id.first_name_row);
            last_name = itemView.findViewById(R.id.last_name_row);
            email = itemView.findViewById(R.id.email_row);
            number = itemView.findViewById(R.id.number_row);
            address = itemView.findViewById(R.id.address_row);


            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            onContactClickListener.onContactClick(getAdapterPosition());
            return false;
        }
    }


    public interface OnContactClickListener {
        void onContactClick(int position);
    }









}



