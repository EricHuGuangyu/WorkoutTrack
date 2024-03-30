package com.example.workouttrack.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class WorkoutItem {
    private String title;
    private String content;
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public WorkoutItem(String title, String content,String detail) {
        this.title = title;
        this.content = content;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return title + ": " + content;
    }

}
