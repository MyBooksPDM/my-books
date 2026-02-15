package it.mybooks.mybooks.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.data.repository.BookRepository;
import it.mybooks.mybooks.data.repository.FirestoreRepository;

public class BookViewModel extends AndroidViewModel {
    private final BookRepository repository;
    private final FirestoreRepository firestoreRepository;
    private final MediatorLiveData<List<Book>> savedBooks = new MediatorLiveData<>();
    private final LiveData<List<Book>> searchResults;
    private String currentQuery;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        firestoreRepository = new FirestoreRepository();
        
        searchResults = repository.getSearchResults();
        currentQuery = "";

        // Combine Local (Room) and Remote (Firestore) saved books if needed,
        // or just use Firestore as the source of truth for saved books.
        savedBooks.addSource(firestoreRepository.getSavedBooks(), savedBooks::setValue);
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

    public LiveData<List<Book>> getSavedBooks() {
        return savedBooks;
    }

    public LiveData<Book> getBookById(String id) {
        return repository.getBookById(id);
    }

    public void saveBook(Book book) {
        // Save to Local Room
        repository.saveBook(book);
        // Save to Firestore
        book.setSavedTimestamp(System.currentTimeMillis());
        firestoreRepository.saveBook(book);
    }

    public void deleteBook(Book book) {
        // Delete from Local Room
        repository.deleteBook(book);
        // Delete from Firestore
        firestoreRepository.deleteBook(book.getGid());
    }
}
