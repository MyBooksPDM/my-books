package it.mybooks.mybooks.model;

import com.google.gson.annotations.SerializedName;

/**
 * Industry identifier (ISBN) from Google Books API
 */
public class IndustryIdentifier {

    @SerializedName("type")
    private String type; // "ISBN_10" or "ISBN_13"

    @SerializedName("identifier")
    private String identifier;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
