package com.moritz.android.locationfinder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "activity_main";
    private static final int LOCATION_REQUEST_CODE = 0;

    private static final int NUM_PAGES = 2;

    private AppViewModel mViewModel;
    private ViewPager2 mPager;

    private PagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting ViewModel
        mViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        //Setting up pager
        mPager = findViewById(R.id.pager);

        //Create new pager adapter connected to this activity, then connect it to the pager
        mPagerAdapter = new PagerAdapter(this);
        mPager.setAdapter(mPagerAdapter);

        //LOCATION MANAGEMENT

        //Requesting location updates from the OS (if we don't already have them)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting location permissions");

            //Requesting permissions if not already granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Not requesting location permissions as the app already had them");
        }

        //Checking if we now have permission to get the location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Starting listening for location updates");
            mViewModel.setIsLocationEnabled(true);

        } else {
            mViewModel.setIsLocationEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        //Make back press move back one page (unless on first page, then just do what you'd normally do)
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "System got permission result");

        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "User granted location permissions");
                    mViewModel.setIsLocationEnabled(true);
                } else {
                    Log.d(TAG, "User denied location permissions");
                    mViewModel.setIsLocationEnabled(false);
                }
        }
    }

    private class PagerAdapter extends FragmentStateAdapter {
        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment returnFragment;

            switch (position) {
                case 0:
                    returnFragment = new StartFragment();
                    break;
                case 1:
                    returnFragment = new LocationFragment();
                    break;
                default:
                    Log.e(TAG, "Attempted to access invalid page");
                    returnFragment = null;
                    break;
            }

            return returnFragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}