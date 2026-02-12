package it.mybooks.mybooks.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import it.mybooks.mybooks.utils.Converters;

@Entity(tableName = "books")
@TypeConverters(Converters.class)
public class Book implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private long uid; // Unique identifier for Room database

    @ColumnInfo(name = "gid")
    private String gid; // Unique identifier from Google Books API
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "subtitle")
    private String subtitle;

    @ColumnInfo(name = "authors")
    private List<String> authors;

    @ColumnInfo(name = "publisher")
    private String publisher;

    @ColumnInfo(name = "published_date")
    private String publishedDate;

    @ColumnInfo(name = "description")
    private String description;

    // ISBN identifiers
    @ColumnInfo(name = "isbn10")
    private String isbn10;

    @ColumnInfo(name = "isbn13")
    private String isbn13;
    @ColumnInfo(name = "page_count")
    private int pageCount;

    @ColumnInfo(name = "categories")
    private List<String> categories;

    @ColumnInfo(name = "language")
    private String language;
    @ColumnInfo(name = "small_thumbnail")
    private String smallThumbnail;

    @ColumnInfo(name = "thumbnail")
    private String thumbnail;
    @ColumnInfo(name = "average_rating")
    private double averageRating;

    @ColumnInfo(name = "ratings_count")
    private int ratingsCount;

    @ColumnInfo(name = "saved_timestamp")
    private long savedTimestamp;

    public Book() {
        // Default constructor required for Room
    }

    public Book(String gid, String title, List<String> authors, String publishedDate) {
        this.gid = gid;
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
    }

    protected Book(Parcel in) {
        uid = in.readLong();
        gid = in.readString();
        title = in.readString();
        subtitle = in.readString();
        authors = in.createStringArrayList();
        publisher = in.readString();
        publishedDate = in.readString();
        description = in.readString();
        isbn10 = in.readString();
        isbn13 = in.readString();
        pageCount = in.readInt();
        categories = in.createStringArrayList();
        language = in.readString();
        smallThumbnail = in.readString();
        thumbnail = in.readString();
        averageRating = in.readDouble();
        ratingsCount = in.readInt();
        savedTimestamp = in.readLong();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    /**
     * Get the primary ISBN (prefer ISBN-13 if available, otherwise ISBN-10)
     */
    public String getPrimaryIsbn() {
        return isbn13 != null ? isbn13 : isbn10;
    }

    /**
     * Get authors as a comma-separated string
     */
    public String getAuthorsAsString() {
        if (authors == null || authors.isEmpty()) {
            return "Unknown Author";
        }
        return String.join(", ", authors);
    }

    /**
     * Get the publication year from the published date
     */
    public String getPublicationYear() {
        if (publishedDate != null && publishedDate.length() >= 4) {
            return publishedDate.substring(0, 4);
        }
        return "Unknown";
    }

    /**
     * Get categories as a comma-separated string
     */
    public String getCategoriesAsString() {
        if (categories == null || categories.isEmpty()) {
            return "Uncategorized";
        }
        return String.join(", ", categories);
    }

    /**
     * Get the primary category/genre
     */
    public String getPrimaryCategory() {
        if (categories != null && !categories.isEmpty()) {
            return categories.get(0);
        }
        return "Uncategorized";
    }

    /**
     * Check if the book has a thumbnail image
     */
    public boolean hasThumbnail() {
        return thumbnail != null && !thumbnail.isEmpty();
    }

    /**
     * Get the best available thumbnail (prefer regular thumbnail over small)
     */
    public String getBestThumbnail() {
        return thumbnail != null ? thumbnail : smallThumbnail;
    }

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "id='" + gid + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", publishedDate='" + publishedDate + '\'' +
                ", isbn13='" + isbn13 + '\'' +
                '}';
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getSavedTimestamp() {
        return savedTimestamp;
    }

    public void setSavedTimestamp(long savedTimestamp) {
        this.savedTimestamp = savedTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(uid);
        dest.writeString(gid);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeStringList(authors);
        dest.writeString(publisher);
        dest.writeString(publishedDate);
        dest.writeString(description);
        dest.writeString(isbn10);
        dest.writeString(isbn13);
        dest.writeInt(pageCount);
        dest.writeStringList(categories);
        dest.writeString(language);
        dest.writeString(smallThumbnail);
        dest.writeString(thumbnail);
        dest.writeDouble(averageRating);
        dest.writeInt(ratingsCount);
        dest.writeLong(savedTimestamp);
    }
}
