package it.mybooks.mybooks.data.remote;

import it.mybooks.mybooks.data.model.BookItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit service interface for Google Books API
 * Base URL: <a href="https://www.googleapis.com/books/v1/">...</a>
 * API Documentation: <a href="https://developers.google.com/books/docs/v1/using">...</a>
 */
public interface BookApiService {

    /**
     * Search for books with a query string
     * Example: searchBooks("tolkien", 10, 0)
     *
     * @param query Search query (e.g., "tolkien", "isbn:9780547928227", "intitle:hobbit")
     * @param maxResults Maximum number of results to return (1-40, default 10)
     * @param startIndex Index of the first result to return (for pagination)
     * @return BookAPIResponse containing list of books
     */
    @GET("volumes")
    Call<BookApiResponse> searchBooks(
            @Query("q") String query,
            @Query("maxResults") Integer maxResults,
            @Query("startIndex") Integer startIndex
    );

    /**
     * Search for books with a query string (simplified version with default pagination)
     *
     * @param query Search query
     * @return BookAPIResponse containing list of books
     */
    @GET("volumes")
    Call<BookApiResponse> searchBooks(@Query("q") String query);

    /**
     * Get detailed information about a specific book by its ID
     * Example: getBookById("Wfan6L9RGgYC")
     *
     * @param volumeId The Google Books volume ID
     * @return BookItem containing detailed book information
     */
    @GET("volumes/{volumeId}")
    Call<BookItem> getBookById(@Path("volumeId") String volumeId);

    /**
     * Search books by title
     *
     * @param title Book title to search for
     * @param maxResults Maximum number of results
     * @return BookAPIResponse containing list of books
     */
    @GET("volumes")
    Call<BookApiResponse> searchByTitle(
            @Query("intitle") String title,
            @Query("maxResults") Integer maxResults
    );

    /**
     * Search books by author
     *
     * @param author Author name to search for
     * @param maxResults Maximum number of results
     * @return BookAPIResponse containing list of books
     */
    @GET("volumes")
    Call<BookApiResponse> searchByAuthor(
            @Query("inauthor") String author,
            @Query("maxResults") Integer maxResults
    );

    /**
     * Search books by ISBN
     *
     * @param isbn ISBN-10 or ISBN-13 to search for
     * @return BookAPIResponse containing the book (should be single result)
     */
    @GET("volumes")
    Call<BookApiResponse> searchByISBN(@Query("isbn") String isbn);

    /**
     * Search books with advanced filters
     *
     * @param query Search query
     * @param filter Filter results (e.g., "ebooks", "free-ebooks", "paid-ebooks", "full", "partial")
     * @param orderBy Sort order ("relevance" or "newest")
     * @param maxResults Maximum number of results
     * @param startIndex Starting index for pagination
     * @return BookAPIResponse containing list of books
     */
    @GET("volumes")
    Call<BookApiResponse> searchBooksAdvanced(
            @Query("q") String query,
            @Query("filter") String filter,
            @Query("orderBy") String orderBy,
            @Query("maxResults") Integer maxResults,
            @Query("startIndex") Integer startIndex
    );
}
