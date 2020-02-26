package com.example.realtimelocationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class OnBoardingSlider extends PagerAdapter  {

    Context context;
    LayoutInflater layoutInflater;

    public OnBoardingSlider(Context context) {
        this.context = context;
    }

    //Arrays
    public int[] slide_images = {

        R.drawable.map_icon,
        R.drawable.search,
        R.drawable.child_care
    };


    public String[] slide_headings = {

            "Real Time Tracking",
            "Make Friends",
            "Locate your Child"
    };

    public String[] slide_desc = {

            "You can track in real time. Just make friends and track your child in real time",
            "Makes new friend to track your child",
            "Do not worry about your child. You can locate your child sitting in your home"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slideImageId);
        TextView slideHeading = view.findViewById(R.id.slideHeading);
        TextView slideText = view.findViewById(R.id.slideText);

        slideImageView.setImageResource(slide_images[position]);
        //slideImageView.setBackground(slide_images_bg[position]);
        slideHeading.setText(slide_headings[position]);
        slideText.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        //super.destroyItem(container, position, object);
        container.removeView((LinearLayout) object);
    }
}
