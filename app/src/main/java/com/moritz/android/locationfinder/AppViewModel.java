package com.moritz.android.locationfinder;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppViewModel extends ViewModel {
    //LOCATION ACCESS
    private MutableLiveData<Boolean> isLocationEnabled;

    private static final String TAG = "app_view_model";

    public LiveData<Boolean> getLocationEnabled() {
        if (isLocationEnabled == null) {
            //Creating MutableLiveData if this is the first time it is called (since this object will
            //  effectively be a singleton
            isLocationEnabled = new MutableLiveData<>();
            setIsLocationEnabled(false);
        }

        //Return MutableLiveData as a LiveData (i.e. not mutable)
        return isLocationEnabled;
    }

    public void setIsLocationEnabled(boolean locationEnabled) {
        if (isLocationEnabled == null) {
            isLocationEnabled = new MutableLiveData<>();
        }

        isLocationEnabled.postValue(locationEnabled);
    }

    //ACTUAL LOCATION DATA
    private MutableLiveData<Location> locationData;

    /**
     * Gets the LiveData for the location. The main activity must add the location observer to this
     *  to be effective
     * @return The LiveData object for the current location
     */
    public LiveData<Location> getLocationData() {
        if (locationData == null) {
            locationData = new MutableLiveData<>();
        }

        return locationData;
    }

    public void initialiseLocationData(Location location) {
        locationData = new MutableLiveData<>();
        locationData.postValue(location);
    }

    /**
     * Makes the listener that will update the location value held by this ViewModel (must be
     *  externally set to listen to location services)
     * @return the listener to listen to for location updates
     */
    public LocationListener createLocationListener() {
        if (locationData == null) {
            locationData = new MutableLiveData<>();
        }

        return new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.v(TAG, "Location listener received location update");

                locationData.postValue(location);
            }
        };
    }

    //SAVED LOCATION DATA (FOR MEASURING DISTANCES)
    private MutableLiveData<Location> savedLocation;

    public LiveData<Location> getSavedLocation() {
        if (savedLocation == null) {
            savedLocation = new MutableLiveData<>();
        }

        return savedLocation;
    }

    public void setSavedLocation(Location location) {
        if (savedLocation == null) {
            savedLocation = new MutableLiveData<>();
        }

        //Updating location value
        savedLocation.postValue(location);
    }

    //DISTANCE DATA FIXME I might be overusing LiveData here
    private MutableLiveData<Float> distanceToSaved;

    public LiveData<Float> getDistanceToSaved() {
        if (distanceToSaved == null) {
            distanceToSaved = new MutableLiveData<>();
        }

        return distanceToSaved;
    }

    public void setDistanceToSaved(float distance) {
        if (distanceToSaved == null) {
            distanceToSaved = new MutableLiveData<>();
        }

        distanceToSaved.postValue(distance);
    }
}
