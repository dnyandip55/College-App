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


        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%40e771c4bjpg?alt=media&token=9e0d9c28-dff4-423e-aaee-ca0a08c02946", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%40623ebebjpg?alt=media&token=84985360-f6fb-491c-9a36-e83971dab222", "Elephants and tigers may become extinct.",ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/modern-college-admin-af25e.appspot.com/o/gallary%2F%5BB%40bd2ea97jpg?alt=media&token=c06c1913-8698-4b32-b15e-d98473f615ab", "And people do that.",ScaleTypes.CENTER_CROP));





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