package com.shashank.quakewatch.customAdapterAndLoader;

public class earthquake {
    private final double mMagnitude;
    private final String mLocation;
    private final long mTimeInMilliseconds;
    private final String mURL;
    private final String mTitle;
    private final String mFeltNo;
    private final String mCoordinates;

    public earthquake(double magnitude, String location, long TimeInMilliseconds, String url, String title,String feltNo, String coordinates) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = TimeInMilliseconds;
        mURL = url;
        mTitle = title;
        mFeltNo = feltNo;
        mCoordinates = coordinates;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getURL() {
        return mURL;
    }

    public String getTitle(){return mTitle;}

    public String getFeltNo(){return mFeltNo;}

    public String getCoordinates(){return mCoordinates;}

}
