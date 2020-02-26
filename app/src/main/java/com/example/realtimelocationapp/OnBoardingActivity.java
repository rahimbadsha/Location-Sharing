package com.example.realtimelocationapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;
    private ImageView[] mImg;

    private OnBoardingSlider onBoardingSlider;

    private Button mNextbtn;
    private Button mBackbtn;

    private int mCurrentPage, counter = 2;
    String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(OnBoardingActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        mNextbtn = findViewById(R.id.nextButton);
        mBackbtn = findViewById(R.id.prevButton);

        onBoardingSlider = new OnBoardingSlider(this);
        mSlideViewPager.setAdapter(onBoardingSlider);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        // onClickListeners


        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSlideViewPager.setCurrentItem(mCurrentPage + 1);


                if (value.equals("Finish"))
                {
                    value = "";
                    Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                value = mNextbtn.getText().toString();
            }
        });

        mBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
                counter--;
            }
        });

    }


    public void addDotsIndicator(int position)
    {
        mDots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if(position == 0)
        {
            mNextbtn.setEnabled(true);
            mBackbtn.setEnabled(false);
            mBackbtn.setVisibility(View.INVISIBLE);

            mNextbtn.setText("Next");
            mBackbtn.setText("");
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            mCurrentPage = position;

            if(position == 0)
            {
                mNextbtn.setEnabled(true);
                mBackbtn.setEnabled(false);
                mBackbtn.setVisibility(View.INVISIBLE);

                mNextbtn.setText("Next");
                mBackbtn.setText("");
            }
            else if(position == mDots.length-1)
            {
                mNextbtn.setEnabled(true);
                mBackbtn.setEnabled(true);
                mBackbtn.setVisibility(View.VISIBLE);

                mNextbtn.setText("Finish");
                mBackbtn.setText("Back");


            }
            else
            {
                mNextbtn.setEnabled(true);
                mBackbtn.setEnabled(true);
                mBackbtn.setVisibility(View.VISIBLE);

                mNextbtn.setText("Next");
                mBackbtn.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

}
