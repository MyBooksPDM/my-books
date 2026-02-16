package it.mybooks.mybooks.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import it.mybooks.mybooks.data.local.AppDatabase;
import it.mybooks.mybooks.data.local.BookDao;

import it.mybooks.mybooks.data.model.Book;
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
    private final BookApiService apiService;
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;
    
    private final MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> searchError = new MutableLiveData<>();
    private final MutableLiveData<String> firestoreError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);


    public BookRepository(Application application) {
        AppDatabase dbLocal = AppDatabase.getInstance(application);
        this.bookDao = dbLocal.bookDao();
        this.apiService = RetrofitClient.getInstance().getGoogleBooksService();
        
        // Inizializzazione Firebase
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public LiveData<List<Book>> getSearchResults() {
        return searchResults;
    }

    public LiveData<String> getSearchError() {
        return searchError;
    }

    public LiveData<String> getFirestoreError() {
        return firestoreError;
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

                    if (books != null && !books.isEmpty()) {
                        searchResults.setValue(books);
                        searchError.setValue(null);
                    } else {
                        searchResults.setValue(new ArrayList<>());
                        searchError.setValue("Nessun risultato trovato per: " + query);
                    }
                } else {
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
        // 1. Salvataggio locale su Room
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.insert(book));
        
        // 2. Salvataggio su Firestore (se l'utente è loggato)
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            db.collection("users")
                    .document(user.getUid())
                    .collection("books")
                    .document(book.getGid()) // Corretto da getId() a getGid()
                    .set(book)
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Errore salvataggio Firestore", e);
                        firestoreError.postValue("Errore salvataggio cloud: " + e.getMessage());
                    });
        }
    }

    public void deleteBook(Book book) {
        // 1. Eliminazione locale da Room
        AppExecutors.getInstance().diskIO().execute(() -> bookDao.delete(book));
        
        // 2. Eliminazione da Firestore
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            db.collection("users")
                    .document(user.getUid())
                    .collection("books")
                    .document(book.getGid()) // Corretto da getId() a getGid()
                    .delete()
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Errore eliminazione Firestore", e);
                        firestoreError.postValue("Errore eliminazione cloud: " + e.getMessage());
                    });
        }
    }

    public void syncBooksWithRoom(List<Book> remoteBooks) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            bookDao.insertAll(remoteBooks);
        });
    }
}
