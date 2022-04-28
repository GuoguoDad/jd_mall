package com.aries.rn;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Page implements Parcelable {

    private String name;

    private String url;

    private Bundle extras;

    public String url() {
        return url;
    }


    public Page() {
    }

    public String filePath() {
        int start = url.lastIndexOf("://") + 3;
        return url.substring(start);
    }

    public Page url(String url) {
        this.url = url;
        return this;
    }

    public String name() {
        return name;
    }

    public Page name(String name) {
        this.name = name;
        return this;
    }

    public Bundle extras() {
        return extras;
    }

    public Page extras(Bundle extras) {
        this.extras = extras;
        return this;
    }


    public Page extras(String key, String value) {
        if (null == this.extras) {
            this.extras = new Bundle();
        }
        this.extras.putString(key, value);
        return this;
    }

    public Page extras(String key, Integer value) {
        if (null == this.extras) {
            this.extras = new Bundle();
        }
        this.extras.putInt(key, value);
        return this;
    }

    public Page extras(String key, byte value) {
        if (null == this.extras) {
            this.extras = new Bundle();
        }
        this.extras.putByte(key, value);
        return this;
    }

    public Page extras(String key, long value) {
        if (null == this.extras) {
            this.extras = new Bundle();
        }
        this.extras.putLong(key, value);
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeBundle(this.extras);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.url = source.readString();
        this.extras = source.readBundle(getClass().getClassLoader());
    }

    protected Page(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.extras = in.readBundle(getClass().getClassLoader());
    }

    public static final Creator<Page> CREATOR = new Creator<Page>() {
        @Override
        public Page createFromParcel(Parcel source) {
            return new Page(source);
        }

        @Override
        public Page[] newArray(int size) {
            return new Page[size];
        }
    };
}
