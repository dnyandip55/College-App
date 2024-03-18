package com.example.collegeapp.ui.about;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.collegeapp.R;

import java.util.ArrayList;
import java.util.List;


public class AboutFragment extends Fragment {


    private ViewPager viewPager;
    private BranchAdapter adapter;

    private List<BranchModel>list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about, container, false);

        list =new ArrayList<>();
        list.add(new BranchModel(R.drawable.ic_computer,"Computer Science",R.string.comp_desc));
        list.add(new BranchModel(R.drawable.ic_chemistry,"Chemistry",R.string.chem_desc));
        list.add(new BranchModel(R.drawable.electronic,"Electronic Science",R.string.electronic_science_desc));
        list.add(new BranchModel(R.drawable.ic_physics,"Physics",R.string.physics_desc));
        list.add(new BranchModel(R.drawable.ic_statistics,"Statistics",R.string.statistics_desc));
        list.add(new BranchModel(R.drawable.ic_math,"Mathematics",R.string.math_desc));
        list.add(new BranchModel(R.drawable.ic_microbiology,"Microbiology",R.string.microbiology_desc));
        list.add(new BranchModel(R.drawable.ic_animation,"Animation",R.string.animation_desc));
        list.add(new BranchModel(R.drawable.ic_biotechnology,"Biotechnology",R.string.biotechnology_desc));
        list.add(new BranchModel(R.drawable.ic_botany,"Botany",R.string.botany_desc));


        adapter=new BranchAdapter(getContext(),list);

        viewPager =view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        ImageView imageView= view.findViewById(R.id.collegeImage);
        Glide.with(getActivity())
                .load("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%40e3d0c20jpg?alt=media&token=8647d628-ad3c-42e2-8a8e-94a1d36c10a1")
                .into(imageView);
        return view;
    }
}