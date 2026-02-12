package it.mybooks.mybooks.data.model;
import java.util.List;
public class Book {

    // Unique identifier of the book (from Google Books API)
    private String id;

    private String gid;
    // Basic book information
    private String title;
    private String subtitle;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;

    // ISBN identifiers (extracted and normalized)
    private String isbn10;
    private String isbn13;

    // Additional details
    private int pageCount;
    private List<String> categories;
    private String language;
    private String maturityRating;

    // Image URLs
    private String smallThumbnail;
    private String thumbnail;

    // External links
    private String previewLink;
    private String infoLink;

    // Ratings
    private double averageRating;
    private int ratingsCount;

    // Empty constructor (required by serializers and databases)
    public Book() {
    }

    // Constructor with essential fields
    public Book(String id, String title, List<String> authors, String publishedDate) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
    }

    /* -------------------- GETTERS & SETTERS -------------------- */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
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

    public String getMaturityRating() {
        return maturityRating;
    }

    public void setMaturityRating(String maturityRating) {
        this.maturityRating = maturityRating;
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

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
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

    /* -------------------- UTILITY METHODS -------------------- */

    /**
     * Returns the preferred ISBN (ISBN-13 if available, otherwise ISBN-10)
     */
    public String getPrimaryIsbn() {
        return isbn13 != null ? isbn13 : isbn10;
    }

    /**
     * Returns authors in a readable format
     */
    public String getAuthorsAsString() {
        if (authors == null || authors.isEmpty()) {
            return "Unknown Author";
        }
        return String.join(", ", authors);
    }

    /**
     * Extracts the publication year from the published date
     */
    public String getPublicationYear() {
        if (publishedDate != null && publishedDate.length() >= 4) {
            return publishedDate.substring(0, 4);
        }
        return "Unknown";
    }

    /**
     * Returns the main category of the book
     */
    public String getPrimaryCategory() {
        if (categories != null && !categories.isEmpty()) {
            return categories.get(0);
        }
        return "Uncategorized";
    }

    /**
     * Checks whether a thumbnail image is available
     */
    public boolean hasThumbnail() {
        return thumbnail != null && !thumbnail.isEmpty();
    }

    /**
     * Returns the best available thumbnail URL
     */
    public String getBestThumbnail() {
        return thumbnail != null ? thumbnail : smallThumbnail;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                '}';
    }
}

