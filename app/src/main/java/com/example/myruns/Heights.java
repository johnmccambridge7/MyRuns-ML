package com.example.myruns;

import android.os.Parcel;
import android.os.Parcelable;

public class Heights implements Parcelable {

    private double height;

    protected Heights(Parcel in) {
        height = in.readLong();
    }

    public Heights(double stamp) {
        this.height = stamp;
    }

    public static final Creator<Heights> CREATOR = new Creator<Heights>() {
        @Override
        public Heights createFromParcel(Parcel in) {
            return new Heights(in);
        }

        @Override
        public Heights[] newArray(int size) {
            return new Heights[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public double getHeight() {
        return this.height;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.height);
    }
}
