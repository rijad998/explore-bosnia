package com.example.sparklingsmiledentalclinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PersonListAdapter extends ArrayAdapter<Appointment> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    int mResource;

    public PersonListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Appointment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String phone = getItem(position).getPhone();

        Appointment appointment = new Appointment(name, phone);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView pNameTV = (TextView)convertView.findViewById(R.id.personNameAdminTextView);
        TextView pPhoneTV = (TextView)convertView.findViewById(R.id.phoneNumberAdminTextView);

        pNameTV.setText(name);
        pPhoneTV.setText(phone);

        return convertView;
    }
}
