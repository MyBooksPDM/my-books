package it.mybooks.mybooks.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.data.repository.BookRepository;

public class BookViewModel extends AndroidViewModel {
    private final BookRepository repository;

    private final LiveData<List<Book>> savedBooks;
    private final LiveData<List<Book>> searchResults;
    private String currentQuery;

    private final MutableLiveData<String> syncError = new MutableLiveData<>();

    public LiveData<String> getSyncError() {
        return syncError;
    }


    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = BookRepository.getInstance(application);
        savedBooks = repository.getSavedBooks();
        searchResults = repository.getSearchResults();
        currentQuery = "";
        repository.syncBooksFromRemote(new BookRepository.OnRepositoryActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String message) {
                syncError.postValue(message);
            }
        });

    }

    public String getCurrentQuery() {
        return currentQuery;
    }

    public void searchBooks(String query) {
        currentQuery = query;
        repository.searchBooks(currentQuery);
    }

    public LiveData<List<Book>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<String> getFirestoreError() {
        return repository.getFirestoreError();
    }

    public LiveData<List<Book>> getSavedBooks() {
        return savedBooks;
    }

    public LiveData<Book> getSavedBookByGid(String gid) {
        return repository.getSavedBookByGid(gid);
    }

    public void saveBook(Book book) {
        long timestamp = System.currentTimeMillis();
        book.setSavedTimestamp(timestamp);

        // Save locally (Room)
        repository.saveBook(book);
    }

    public void deleteBook(Book book) {
        // Delete locally (Room)
        repository.deleteBook(book);
    }
}
