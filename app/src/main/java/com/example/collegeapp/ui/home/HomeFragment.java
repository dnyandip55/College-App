package com.example.collegeapp.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.collegeapp.R;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private ImageView map;

    ArrayList<SlideModel> imageList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_home, container, false);


        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%40eb1f8b8jpg?alt=media&token=419ba810-9dbe-41d9-b51e-c6152fe3d01e", ".....", ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%40a5d84abjpg?alt=media&token=9d8f8f45-d30a-4ff5-b230-b9fb6075a5ee", "....",ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%40d8c6cdbjpg?alt=media&token=acc9e8d7-1138-4785-98bd-39896a6a130a", "And people do that.",ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%4080c6f87jpg?alt=media&token=eae4c77e-cd40-45c8-87d5-8799059d9438", "...",ScaleTypes.CENTER_INSIDE));
        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%4070dba14jpg?alt=media&token=6e32c93a-7c6e-483f-ad68-808c07b1fd7d", "...",ScaleTypes.CENTER_INSIDE));





        ImageSlider imageSlider =view.findViewById(R.id.image_slider);

        imageSlider.setImageList(imageList);

        map=view.findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });

       return view;
    }

    private void openMap() {
        Uri uri=Uri.parse("geo:0,0?q=Modern College Of Arts Science & Commerce, Shivajinagar");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}