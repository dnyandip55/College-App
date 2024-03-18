package com.example.collegeapp.ui.faculty;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.collegeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FacultyFragment extends Fragment {
    private RecyclerView animationDepartment,biotechDepartment,botanyDepartment,chemDepartment,csDepartment,electronicDepartment,mathDepartment,microbiologyDepartment,physicsDepartment,statDepartment;

    private LinearLayout animationNoData,biotechNoData,botanyNoData,chemNoData,csNoData,electronicNoData,mathematicsNoData,microbiologyNoData,physicsNoData,statisticsNoData;

    private List<TeacherData>list1,list2,list3,list4,list5,list6,list7,list8,list9,list10;
    private TeacherAdapter adapter;

    private DatabaseReference reference,dbRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_faculty, container, false);

        animationDepartment=view.findViewById(R.id.animationDepartment);
        biotechDepartment=view.findViewById(R.id.biotechDepartment);
        botanyDepartment=view.findViewById(R.id.botonyDepartment);
        chemDepartment=view.findViewById(R.id.chemDepartment);
        csDepartment=view.findViewById(R.id.csDepartment);
        electronicDepartment=view.findViewById(R.id.electronicDepartment);
        mathDepartment=view.findViewById(R.id.mathematicsDepartment);
        microbiologyDepartment=view.findViewById(R.id.microbiologyDepartment);
        physicsDepartment=view.findViewById(R.id.physicsDepartment);
        statDepartment=view.findViewById(R.id.statisticsDepartment);


        animationNoData=view.findViewById(R.id.animationNoData);
        biotechNoData=view.findViewById(R.id.biotechNoData);
        botanyNoData=view.findViewById(R.id.botanyNoData);
        chemNoData=view.findViewById(R.id.chemNoData);
        csNoData=view.findViewById(R.id.csNoData);
        electronicNoData=view.findViewById(R.id.electronicNoData);
        mathematicsNoData=view.findViewById(R.id.mathematicsNoData);
        microbiologyNoData=view.findViewById(R.id.microbiologyNoData);
        physicsNoData=view.findViewById(R.id.physicsNoData);
        statisticsNoData=view.findViewById(R.id.statisticsNoData);


        reference= FirebaseDatabase.getInstance().getReference().child("Teachers");

        animaDepartment();
        bioDepartment();
        botDepartment();
        chemDepartment();
        csDepartment();
        electroDepartment();
        mathematicsDepartment();
        microbioDepartment();
        physicsDepartment();
        statisticsDepartment();

        return view;
    }

    private void animaDepartment() {
        dbRef=reference.child("Animation");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1=new ArrayList<>();
                if(!snapshot.exists()){
                    animationNoData.setVisibility(View.VISIBLE);
                    animationDepartment.setVisibility(View.GONE);
                }else{
                    animationNoData.setVisibility(View.GONE);
                    animationDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    animationDepartment.setHasFixedSize(true);
                    animationDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list1,getContext());
                    animationDepartment.setAdapter(adapter);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void bioDepartment() {
        dbRef=reference.child("Biotechnology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2=new ArrayList<>();
                if(!snapshot.exists()){
                    biotechNoData.setVisibility(View.VISIBLE);
                    biotechDepartment.setVisibility(View.GONE);
                }else{
                    biotechNoData.setVisibility(View.GONE);
                    biotechDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    biotechDepartment.setHasFixedSize(true);
                    biotechDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list2,getContext());
                    biotechDepartment.setAdapter(adapter);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void botDepartment() {
        dbRef=reference.child("Botany");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3=new ArrayList<>();
                if(!snapshot.exists()){
                    botanyNoData.setVisibility(View.VISIBLE);
                    botanyDepartment.setVisibility(View.GONE);
                }else{
                    botanyNoData.setVisibility(View.GONE);
                    botanyDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    botanyDepartment.setHasFixedSize(true);
                    botanyDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list3,getContext());
                    botanyDepartment.setAdapter(adapter);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void chemDepartment() {
        dbRef=reference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4=new ArrayList<>();
                if(!snapshot.exists()){
                    chemNoData.setVisibility(View.VISIBLE);
                    chemDepartment.setVisibility(View.GONE);
                }else{
                    chemNoData.setVisibility(View.GONE);
                    chemDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    chemDepartment.setHasFixedSize(true);
                    chemDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list4,getContext());
                    chemDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void csDepartment() {
        dbRef=reference.child("Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list5=new ArrayList<>();
                if(!snapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else{
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list5.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list5,getContext());
                    csDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void electroDepartment() {
        dbRef=reference.child("Electronic Department");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list6=new ArrayList<>();
                if(!snapshot.exists()){
                    electronicNoData.setVisibility(View.VISIBLE);
                    electronicDepartment.setVisibility(View.GONE);
                }else{
                    electronicNoData.setVisibility(View.GONE);
                    electronicDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list6.add(data);
                    }
                    electronicDepartment.setHasFixedSize(true);
                    electronicDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list6,getContext());
                    electronicDepartment.setAdapter(adapter);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mathematicsDepartment() {
        dbRef=reference.child("Mathematics Department");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list7=new ArrayList<>();
                if(!snapshot.exists()){
                    mathematicsNoData.setVisibility(View.VISIBLE);
                    mathDepartment.setVisibility(View.GONE);
                }else{
                    mathematicsNoData.setVisibility(View.GONE);
                    mathDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list7.add(data);
                    }
                    mathDepartment.setHasFixedSize(true);
                    mathDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list7,getContext());
                    mathDepartment.setAdapter(adapter);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void microbioDepartment() {
        dbRef=reference.child("Microbiology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list8=new ArrayList<>();
                if(!snapshot.exists()){
                    microbiologyNoData.setVisibility(View.VISIBLE);
                    microbiologyDepartment.setVisibility(View.GONE);
                }else{
                    microbiologyNoData.setVisibility(View.GONE);
                    microbiologyDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list8.add(data);
                    }
                    microbiologyDepartment.setHasFixedSize(true);
                    microbiologyDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list8,getContext());
                    microbiologyDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void physicsDepartment() {
        dbRef=reference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list9=new ArrayList<>();
                if(!snapshot.exists()){
                    physicsNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                }else{
                    physicsNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list9.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list9,getContext());
                    physicsDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void  statisticsDepartment() {
        dbRef=reference.child("Statistics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list10=new ArrayList<>();
                if(!snapshot.exists()){
                    statisticsNoData.setVisibility(View.VISIBLE);
                    statDepartment.setVisibility(View.GONE);
                }else{
                    statisticsNoData.setVisibility(View.GONE);
                    statDepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list10.add(data);
                    }
                    statDepartment.setHasFixedSize(true);
                    statDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter=new TeacherAdapter(list10,getContext());
                    statDepartment.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}