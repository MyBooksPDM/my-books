package it.mybooks.mybooks.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.data.model.BookItem;

/**
 * Response model for Google Books API
 * Maps to the JSON response structure from <a href="https://www.googleapis.com/books/v1/volumes">...</a>
 */
public class BookApiResponse {

    @SerializedName("kind")
    private String kind;

    @SerializedName("totalItems")
    private Integer totalItems;

    @SerializedName("items")
    private List<BookItem> items;

    private transient List<Book> books;

    public BookApiResponse() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public List<BookItem> getItems() {
        return items;
    }

    public void setItems(List<BookItem> items) {
        this.items = items;
    }

    /**
     * Get the converted list of Book domain models
     * Converts from BookItem (API response) to Book (domain model)
     *
     * @return List of Book objects, never null
     */
    public List<Book> getBooks() {
        if (books == null) {
            // Convert items to books on first access
            if (items != null && !items.isEmpty()) {
                books = BookApiMapper.toDomainList(items);
            } else {
                books = new ArrayList<>();
            }
        }
        return books;
    }
}
