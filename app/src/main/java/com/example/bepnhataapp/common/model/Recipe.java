package com.example.bepnhataapp.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Recipe implements Parcelable {
    private long id;
    private String name;
    private String category;
    private String imageUrl;
    private boolean isFavorite;
    private String description;
    private String tag;
    private String createdAt;
    private String imageThumb;
    private int commentAmount;
    private int likeAmount;
    private int sectionAmount;

    public Recipe() {}

    public Recipe(long id, String name, String category, String imageUrl, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
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

    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getId() {
        return id;
    }

    // Legacy getters/setters for backward compatibility
    public long getRecipeID(){ return id; }
    public void setRecipeID(long recipeID){ this.id = recipeID; }

    public String getRecipeName(){ return name; }
    public void setRecipeName(String recipeName){ this.name = recipeName; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }

    public String getTag(){ return tag; }
    public void setTag(String tag){ this.tag = tag; }

    public String getCreatedAt(){ return createdAt; }
    public void setCreatedAt(String createdAt){ this.createdAt = createdAt; }

    public String getImageThumb(){ return imageThumb!=null? imageThumb : imageUrl; }
    public void setImageThumb(String imageThumb){ this.imageThumb = imageThumb; this.imageUrl = imageThumb; }

    public int getCommentAmount(){ return commentAmount; }
    public void setCommentAmount(int commentAmount){ this.commentAmount = commentAmount; }

    public int getLikeAmount(){ return likeAmount; }
    public void setLikeAmount(int likeAmount){ this.likeAmount = likeAmount; }

    public int getSectionAmount(){ return sectionAmount; }
    public void setSectionAmount(int sectionAmount){ this.sectionAmount = sectionAmount; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
} 
