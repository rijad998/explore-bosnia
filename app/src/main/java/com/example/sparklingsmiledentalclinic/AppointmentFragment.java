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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

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
    Appointment appointment;
    User user;
    Boolean alreadyExist;

    Calendar c;
    DatePickerDialog dpd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appointment, container, false);

        pickDateTxtView = (TextView) v.findViewById(R.id.pickDateTxtView);
        pickDateBtn = (Button) v.findViewById(R.id.pickDateBtn);
        appointmentBtn = v.findViewById(R.id.makeAnAppointmentBtn);


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
                        pickDateTxtView.setText(mYear + "/" + (mMonth + 1) + "/" + mDayOfMonth);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickDateTxtView.getText().toString().equals("")) {
                    Toast.makeText(getActivity(),"You need to pick a date for an appointment", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        getAnAppointment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return v;
    }

    public void getAnAppointment(){
        final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().equals(currentUserID)){
                         user = snapshot.getValue(User.class);
                         nameOfUser = user.getName();
                         appointment = new Appointment(nameOfUser, pickDateTxtView.getText().toString());
                         checkTheDatabase();
                         break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkTheDatabase() {
        alreadyExist = false;
        final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Appointments");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   if (snapshot.equals(pickDateTxtView.getText())){
                        alreadyExist = true;
                        userReference.child(pickDateTxtView.getText().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long numberOfAppointments = dataSnapshot.getChildrenCount();
                                if (numberOfAppointments < 11) {
                                    addAnAppointment();
                                } else {
                                    Toast.makeText(getActivity(), "Sorry but there are no available appointments on " + pickDateTxtView.getText(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                if(!alreadyExist){
                    addAnAppointment();
                }
            };

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addAnAppointment() {
        Toast.makeText(getActivity(), appointment.getName(), Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference().child("Appointments").child(pickDateTxtView.getText().toString()).setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Appointment successfully addedd", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Hmmm, something went wrong... Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
