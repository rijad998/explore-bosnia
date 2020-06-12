package com.example.sparklingsmiledentalclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity {

    Button adminLogoutBtn;
    TextView adminDateTxtView;
    Button adminDateChooseBtn;
    Calendar c;
    DatePickerDialog dpd;
    DateC datec;
    ListView adminAppointmentsLW;
    Button findAppointmentBtn;
    PersonListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        adminLogoutBtn = findViewById(R.id.adminLogoutBtn);
        adminDateTxtView = (TextView) findViewById(R.id.adminDateTxtView);
        adminDateChooseBtn = (Button) findViewById(R.id.adminDateChooseBtn);
        adminAppointmentsLW = findViewById(R.id.adminAppointmentsLW);
        findAppointmentBtn = findViewById(R.id.findAppointmentsBtn);

        adminLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                finish();
            }
        });

        adminDateChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(AdminActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        adminDateTxtView.setText(mYear + "/" + (mMonth + 1) + "/" + mDayOfMonth);
                        datec = new DateC(mDayOfMonth, (mMonth + 1), mYear);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        findAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnTheAppointments();
            }
        });

    }

    public void returnTheAppointments(){

        final ArrayList<Appointment> appointmentsList = new ArrayList<>();
        adapter = new PersonListAdapter(AdminActivity.this, R.layout.adapter_view_layout, appointmentsList);
        adminAppointmentsLW.setAdapter(adapter);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Appointments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(datec.getYear().toString())){
                        FirebaseDatabase.getInstance().getReference().child("Appointments").child(datec.getYear().toString()).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshotM : dataSnapshot.getChildren()){
                                    if(snapshotM.getKey().equals(datec.getMonth().toString())){
                                        FirebaseDatabase.getInstance().getReference().child("Appointments").child(datec.getYear().toString()).child(datec.getMonth().toString()).addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot snapshotD : dataSnapshot.getChildren()){
                                                    if(snapshotD.getKey().equals(datec.getDay().toString())){
                                                        FirebaseDatabase.getInstance().getReference().child("Appointments").child(datec.getYear().toString()).child(datec.getMonth().toString()).child(datec.getDay().toString()).addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                appointmentsList.clear();
                                                                for(DataSnapshot snapshotP : dataSnapshot.getChildren()){
                                                                    Appointment appoint = snapshotP.getValue(Appointment.class);
                                                                    appointmentsList.add(appoint);
                                                                }
                                                                adapter.notifyDataSetChanged();
                                                                return;
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


