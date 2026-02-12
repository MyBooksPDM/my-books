package it.mybooks.mybooks.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.utils.SampleBookData;

public class BookViewModel extends ViewModel {


    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();
    private final MutableLiveData<Integer> bookCount = new MutableLiveData<>();

    public LiveData<Integer> getBookCount() {
        return bookCount;
    }

    private final MutableLiveData<Book> book = new MutableLiveData<>();

    public MutableLiveData<Book> getBook() {
        return book;
    }

    private final MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();
    private final List<Book> allBooks = new ArrayList<>();

    public void loadBookDetails(String bookId) {
        if (bookId == null || bookId.isEmpty()) {
            return;
        }
        // Simulate loading book details (in a real app, fetch from repository or database)
        List<Book> books = SampleBookData.getSampleBooks();
        for (Book b : books) {
            if (bookId.equals(b.getGid())) {
                book.setValue(b);
                break;
            }
        }
    }

    public BookViewModel() {
        List<Book> list = SampleBookData.getSampleBooks();
        books.setValue(list);
        bookCount.setValue(list.size());

        // Popola la lista iniziale con alcuni libri mock
        allBooks.add(new Book("1", "Il Signore degli Anelli", Arrays.asList("J.R.R. Tolkien"), "1954"));
        allBooks.add(new Book("2", "Lo Hobbit", Arrays.asList("J.R.R. Tolkien"), "1937"));
        allBooks.add(new Book("3", "1984", Arrays.asList("George Orwell"), "1949"));
        allBooks.add(new Book("4", "Orgoglio e Pregiudizio", Arrays.asList("Jane Austen"), "1813"));
        allBooks.add(new Book("5", "Cronache del ghiaccio e del fuoco", Arrays.asList("George R. R. Martin"), "1996"));

        // Mostra tutti i libri all'avvio
        searchResults.setValue(new ArrayList<>(allBooks));
    }


    public void deleteBook(String bookId) {
        List<Book> currentBook = books.getValue();
        if (currentBook == null) {
            return;
        }
        List<Book> updatedBooks = new ArrayList<>();
        for (Book book : currentBook) {
            if (!book.getGid().equals(bookId)) {
                updatedBooks.add(book);
            }
        }
        books.setValue(updatedBooks);
        bookCount.setValue(updatedBooks.size());


    }

    public LiveData<List<Book>> getBooks() {
        return searchResults;
    }

    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.setValue(new ArrayList<>(allBooks)); // Ripristina lista completa
            return;
        }

        List<Book> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (Book book : allBooks) {
            if (book.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    book.getAuthorsAsString().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(book);
            }
        }
        searchResults.setValue(filteredList); // Aggiorna i risultati
    }
}