package it.mybooks.mybooks.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single book item in the Google Books API response
 * Only includes fields needed to populate Room database
 */
public class BookItem {

    @SerializedName("id")
    private String id;

    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}
