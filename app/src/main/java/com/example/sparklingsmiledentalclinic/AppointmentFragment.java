package com.example.sparklingsmiledentalclinic;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {

    public AppointmentFragment() {
        // Required empty public constructor
    }

    TextView pickDateTxtView;
    Button pickDateBtn;
    Button appointmentBtn;
    String nameOfUser;

    Calendar c;
    DatePickerDialog dpd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appointment, container, false);

        pickDateTxtView = (TextView) v.findViewById(R.id.pickDateTxtView);
        pickDateBtn = (Button) v.findViewById(R.id.pickDateBtn);
        appointmentBtn = v.findViewById(R.id.makeAnAppointment);


        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        pickDateTxtView.setText(mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickDateTxtView.equals("")) {
                    //Toast.makeText(getActivity().getApplicationContext(),"You need to pick a date for an appointment", Toast.LENGTH_SHORT).show();
                } else {
                    makeAnAppointment();
                }
            }
        });

        return v;
    }

    public void makeAnAppointment(){

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    nameOfUser = user.getName().toString();
                    Log.i("JOOOJ", pickDateTxtView + " : " + nameOfUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //FirebaseDatabase.getInstance().getReference().child("Appointments").updateChildren(appointment);

    }
}
