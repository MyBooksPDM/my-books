package it.mybooks.mybooks.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import it.mybooks.mybooks.data.local.AppDatabase;
import it.mybooks.mybooks.data.local.BookDao;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.utils.AppExecutors;
import it.mybooks.mybooks.data.remote.BookApiResponse;
import it.mybooks.mybooks.data.remote.BookApiService;
import it.mybooks.mybooks.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static final String TAG = BookRepository.class.getName();

    private final BookDao bookDao;
    private final BookApiService apiService;

    private final MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> searchError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);


    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.bookDao = db.bookDao();
        this.apiService = RetrofitClient.getInstance().getGoogleBooksService();
    }

    public LiveData<List<Book>> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getSearchError() {
        return searchError;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void searchBooks(String query) {
        Log.d(TAG, "Searching for: " + query);
        isLoading.setValue(true);

        apiService.searchBooks(query).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<BookApiResponse> call, @NonNull Response<BookApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookApiResponse apiResponse = response.body();
                    List<Book> books = apiResponse.getBooks();

                    Log.d(TAG, "API Response successful. Total items: " + apiResponse.getTotalItems());
                    Log.d(TAG, "Books found: " + (books != null ? books.size() : 0));

                    if (books != null && !books.isEmpty()) {
                        searchResults.setValue(books);
                        searchError.setValue(null);
                    } else {
                        searchResults.setValue(new ArrayList<>());
                        searchError.setValue("Nessun risultato trovato per: " + query);
                    }
                } else {
                    Log.e(TAG, "API Response error: " + response.code() + " - " + response.message());
                    searchResults.setValue(new ArrayList<>());
                    searchError.setValue("Errore API: " + response.code());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<BookApiResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed", t);
                searchError.setValue("Errore di connessione: " + t.getMessage());
                searchResults.setValue(new ArrayList<>());
                isLoading.setValue(false);
            }
        });
    }

    public LiveData<List<Book>> getSavedBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<Book> getBookById(String id) {
        return bookDao.getBookById(id);
    }

    public void saveBook(Book book) {
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.insert(book));
    }

    public void deleteBook(Book book) {
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.delete(book));
    }
}
