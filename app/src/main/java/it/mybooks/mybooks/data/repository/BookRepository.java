package it.mybooks.mybooks.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;


import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.data.remote.BookApiResponse;
import it.mybooks.mybooks.data.remote.BookApiService;
import it.mybooks.mybooks.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRepository {
    private static final String TAG = "BookRepository";

    private final BookApiService apiService;

    public BookRepository(Application application) {
        this.apiService = RetrofitClient.getInstance().getGoogleBooksService();
    }
    private final MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> searchError = new MutableLiveData<>();
    public void searchBooks(String query) {
        Log.d(TAG, "Searching for: " + query);

        apiService.searchBooks(query).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<BookApiResponse> call, Response<BookApiResponse> response) {
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
            }

            @Override
            public void onFailure(Call<BookApiResponse> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
                searchError.setValue("Errore di connessione: " + t.getMessage());
                searchResults.setValue(new ArrayList<>());
            }
        });
    }
}
