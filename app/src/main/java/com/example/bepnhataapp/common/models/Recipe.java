package com.example.bepnhataapp.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Recipe implements Parcelable {
    private String name;
    private String category;
    private String imageUrl;
    private boolean isFavorite;

    public Recipe(String name, String category, String imageUrl, boolean isFavorite) {
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        category = in.readString();
        imageUrl = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
} 