package it.mybooks.mybooks.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.data.repository.BookRepository;
import it.mybooks.mybooks.utils.SampleBookData;

public class BookViewModel extends AndroidViewModel {
    private final BookRepository repository;
    private final LiveData<List<Book>> savedBooks;
    private final LiveData<List<Book>> searchResults;
    private String currentQuery;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        savedBooks = repository.getSavedBooks();
        searchResults = repository.getSearchResults();
        currentQuery = "";
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
        repository.saveBook(book);
    }

    public void deleteBook(Book book) {
        repository.deleteBook(book);
    }
}