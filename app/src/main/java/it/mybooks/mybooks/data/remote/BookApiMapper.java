package it.mybooks.mybooks.data.remote;

import java.util.ArrayList;
import java.util.List;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.data.model.BookItem;
import it.mybooks.mybooks.data.model.ImageLinks;
import it.mybooks.mybooks.data.model.IndustryIdentifier;
import it.mybooks.mybooks.data.model.VolumeInfo;

/**
 * Mapper to convert Google Books API response objects to domain Book objects
 */
public class BookApiMapper {

    /**
     * Convert a BookItem from API response to domain Book model
     *
     * @param bookItem API response book item
     * @return Book domain model
     */
    public static Book toDomain(BookItem bookItem) {
        if (bookItem == null || bookItem.getVolumeInfo() == null) {
            return null;
        }

        Book book = new Book();
        VolumeInfo volumeInfo = bookItem.getVolumeInfo();

        // Set ID
        book.setGid(bookItem.getId());

        // Set basic info
        book.setTitle(volumeInfo.getTitle());
        book.setSubtitle(volumeInfo.getSubtitle());
        book.setAuthors(volumeInfo.getAuthors());
        book.setPublisher(volumeInfo.getPublisher());
        book.setPublishedDate(volumeInfo.getPublishedDate());
        book.setDescription(volumeInfo.getDescription());

        // Extract ISBNs
        extractIsbn(volumeInfo.getIndustryIdentifiers(), book);

        // Set additional details
        if (volumeInfo.getPageCount() != null) {
            book.setPageCount(volumeInfo.getPageCount());
        }
        book.setCategories(volumeInfo.getCategories());
        book.setLanguage(volumeInfo.getLanguage());

        // Set image links
        extractImageLinks(volumeInfo.getImageLinks(), book);


        // Set ratings
        if (volumeInfo.getAverageRating() != null) {
            book.setAverageRating(volumeInfo.getAverageRating());
        }
        if (volumeInfo.getRatingsCount() != null) {
            book.setRatingsCount(volumeInfo.getRatingsCount());
        }

        return book;
    }

    /**
     * Convert a list of BookItems to a list of Books
     *
     * @param bookItems List of API response book items
     * @return List of Book domain models
     */
    public static List<Book> toDomainList(List<BookItem> bookItems) {
        List<Book> books = new ArrayList<>();
        if (bookItems != null) {
            for (BookItem bookItem : bookItems) {
                Book book = toDomain(bookItem);
                if (book != null) {
                    books.add(book);
                }
            }
        }
        return books;
    }

    /**
     * Extract ISBN-10 and ISBN-13 from industry identifiers
     */
    private static void extractIsbn(List<IndustryIdentifier> identifiers, Book book) {
        if (identifiers == null) {
            return;
        }

        for (IndustryIdentifier identifier : identifiers) {
            if ("ISBN_10".equals(identifier.getType())) {
                book.setIsbn10(identifier.getIdentifier());
            } else if ("ISBN_13".equals(identifier.getType())) {
                book.setIsbn13(identifier.getIdentifier());
            }
        }
    }

    /**
     * Extract image links (thumbnail and small thumbnail)
     */
    private static void extractImageLinks(ImageLinks imageLinks, Book book) {
        if (imageLinks == null) {
            return;
        }

        // Convert http to https for secure loading
        if (imageLinks.getSmallThumbnail() != null) {
            book.setSmallThumbnail(imageLinks.getSmallThumbnail().replace("http://", "https://"));
        }
        if (imageLinks.getThumbnail() != null) {
            book.setThumbnail(imageLinks.getThumbnail().replace("http://", "https://"));
        }
    }
}
