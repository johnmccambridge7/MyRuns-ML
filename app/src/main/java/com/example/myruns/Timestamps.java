package com.example.myruns;

import android.os.Parcel;
import android.os.Parcelable;

public class Timestamps implements Parcelable {

    private long stamp;

    protected Timestamps(Parcel in) {
        stamp = in.readLong();
    }

    public Timestamps(long stamp) {
        this.stamp = stamp;
    }

    public static final Creator<Timestamps> CREATOR = new Creator<Timestamps>() {
        @Override
        public Timestamps createFromParcel(Parcel in) {
            return new Timestamps(in);
        }

        @Override
        public Timestamps[] newArray(int size) {
            return new Timestamps[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public long getStamp() {
        return this.stamp;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.stamp);
    }
}
