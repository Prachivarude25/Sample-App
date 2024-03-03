package com.example.sampleapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Note implements Parcelable//public class Note //creating a module
{
    String Tittle;
    String Content;
    Timestamp timestamp; // This class from Firebase

    public Note() { // creating constructor

    }

    protected Note(Parcel in) {
        Tittle = in.readString();
        Content = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String tittle) {
        Tittle = tittle;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public com.google.firebase.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(com.google.firebase.Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(Tittle);
        parcel.writeString(Content);
        parcel.writeParcelable(timestamp, i);
    }
}
