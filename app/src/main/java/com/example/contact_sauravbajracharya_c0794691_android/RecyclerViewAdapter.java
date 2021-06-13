package com.example.contact_sauravbajracharya_c0794691_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_sauravbajracharya_c0794691_android.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    private List<Contact> contactList;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener onContactClickListener) {
        this.contactList = contactList;
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

        Contact contact = contactList.get(position);
        holder.first_name.setText(contact.getFirstName());
        holder.last_name.setText(contact.getLastName());
        holder.email.setText(contact.getEmail());
        holder.number.setText(contact.getPhoneNumber());
        holder.address.setText(contact.getAddress());



    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


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


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onContactClickListener.onContactClick(contactList.get(getAdapterPosition()).getId());
        }
    }


    public interface OnContactClickListener {
        void onContactClick(int contactId);
    }


}
