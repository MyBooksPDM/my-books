package it.mybooks.mybooks.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.mybooks.mybooks.data.model.Book;

/**
 * Gestisce i dati e la logica di ricerca per la schermata Explore.
 */
public class SearchViewModel extends ViewModel {

    // Lista osservabile dei risultati (notifica il fragment quando i dati cambiano)
    private final MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();
    
    // Lista completa dei libri di esempio
    private final List<Book> allBooks = new ArrayList<>();

    public SearchViewModel() {
        // Popola la lista iniziale con alcuni libri mock
        allBooks.add(new Book("1", "Il Signore degli Anelli", Arrays.asList("J.R.R. Tolkien"), "1954"));
        allBooks.add(new Book("2", "Lo Hobbit", Arrays.asList("J.R.R. Tolkien"), "1937"));
        allBooks.add(new Book("3", "1984", Arrays.asList("George Orwell"), "1949"));
        allBooks.add(new Book("4", "Orgoglio e Pregiudizio", Arrays.asList("Jane Austen"), "1813"));
        allBooks.add(new Book("5", "Cronache del ghiaccio e del fuoco", Arrays.asList("George R. R. Martin"), "1996"));
        
        // Mostra tutti i libri all'avvio
        searchResults.setValue(new ArrayList<>(allBooks));
    }

    // Fornisce i risultati al Fragment
    public LiveData<List<Book>> getBooks() {
        return searchResults;
    }

    /**
     * Filtra la lista dei libri in base al testo inserito (titolo o autore).
     */
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
