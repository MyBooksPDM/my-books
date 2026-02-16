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
import it.mybooks.mybooks.data.remote.firebase.FirestoreDataSource;
import it.mybooks.mybooks.utils.AppExecutors;
import it.mybooks.mybooks.data.remote.api.BookApiResponse;
import it.mybooks.mybooks.data.remote.api.BookApiService;
import it.mybooks.mybooks.data.remote.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static final String TAG = BookRepository.class.getName();

    private final BookDao bookDao;
    private final FirestoreDataSource firestoreDataSource;
    private final BookApiService apiService;
    private final MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> searchError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);


    public BookRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        this.bookDao = db.bookDao();
        this.firestoreDataSource = new FirestoreDataSource();
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
        //get savedbooks from firestore and update local db
        firestoreDataSource.getSavedBooks().observeForever(books -> {
            if (books != null) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    bookDao.insertAll(books);
                });
            }
        });

        return bookDao.getAllBooks();
    }

    public LiveData<Book> getBookById(String id) {
        return bookDao.getBookById(id);
    }

    public void saveBook(Book book) {
        firestoreDataSource.saveBook(book);
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.insert(book));
        book.setSavedTimestamp(System.currentTimeMillis());
    }

    public void deleteBook(Book book) {
        firestoreDataSource.deleteBook(book.getGid());
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.delete(book));
    }
}
