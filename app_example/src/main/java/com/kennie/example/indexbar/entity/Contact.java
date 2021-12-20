package com.kennie.example.indexbar.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.kennie.example.indexbar.model.SortModel;

/**
 * 联系人
 */
public class Contact extends SortModel implements Parcelable {

    private String name;

    public Contact(String name) {
        super(name);
        setName(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        super.readFromParcel(source);
        this.name = source.readString();
    }

    protected Contact(Parcel in) {
        super(in);
        this.name = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
