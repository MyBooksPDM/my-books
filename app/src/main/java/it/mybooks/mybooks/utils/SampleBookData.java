package it.mybooks.mybooks.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.mybooks.mybooks.model.Book;

public class SampleBookData {

    public static List<Book> getSampleBooks() {
        List<Book> books = new ArrayList<>();

       /* sampleBooks.add(new Book("1", "The Great Gatsby", List.of("F. Scott Fitzgerald"), "1925"));
        sampleBooks.add(new Book("2", "To Kill a Mockingbird", List.of("Harper Lee"), "1960"));
        sampleBooks.add(new Book("3", "1984", List.of("George Orwell"), "1949"));
        sampleBooks.add(new Book("4", "Pride and Prejudice", List.of("Jane Austen"), "1813"));
        sampleBooks.add(new Book("5", "The Catcher in the Rye", List.of("J.D. Salinger"), "1951"));
        */

        Book book1 = new Book();
        book1.setId("sample-001");
        book1.setTitle("The Name of the Rose");
        book1.setAuthors(Arrays.asList("Umberto Eco"));
        book1.setPublisher("Harcourt");
        book1.setPublishedDate("1980-01-01");
        book1.setIsbn10("0156001314");
        book1.setIsbn13("978-0-156-00131-7");
        book1.setDescription("A historical mystery set in a medieval monastery.");
        book1.setLanguage("en");
        book1.setCategories(Arrays.asList("Fiction", "Mystery", "Historical"));
        book1.setPageCount(536);
        book1.setThumbnail("https://picsum.photos/300/450");
        books.add(book1);

        Book book2 = new Book();
        book2.setId("sample-002");
        book2.setTitle("1984");
        book2.setAuthors(Arrays.asList("George Orwell"));
        book2.setPublisher("Secker & Warburg");
        book2.setPublishedDate("1949-06-08");
        book2.setIsbn10("0452284236");
        book2.setIsbn13("978-0-452-28423-4");
        book2.setDescription("A dystopian social science fiction novel and cautionary tale.");
        book2.setLanguage("en");
        book2.setCategories(Arrays.asList("Fiction", "Dystopian", "Science Fiction"));
        book2.setPageCount(328);
        book2.setAverageRating(4.5);
        book2.setRatingsCount(12543);
        books.add(book2);

        Book book3 = new Book();
        book3.setId("sample-003");
        book3.setTitle("To Kill a Mockingbird");
        book3.setAuthors(Arrays.asList("Harper Lee"));
        book3.setPublisher("J.B. Lippincott & Co.");
        book3.setPublishedDate("1960-07-11");
        book3.setIsbn10("0061120081");
        book3.setIsbn13("978-0-061-12008-4");
        book3.setDescription("A novel about racial injustice and childhood innocence.");
        book3.setLanguage("en");
        book3.setCategories(Arrays.asList("Fiction", "Classic", "Literature"));
        book3.setPageCount(324);
        book3.setAverageRating(4.7);
        book3.setRatingsCount(8921);
        books.add(book3);

        Book book4 = new Book();
        book4.setId("sample-004");
        book4.setTitle("The Great Gatsby");
        book4.setAuthors(Arrays.asList("F. Scott Fitzgerald"));
        book4.setPublisher("Charles Scribner's Sons");
        book4.setPublishedDate("1925-04-10");
        book4.setIsbn10("0743273567");
        book4.setIsbn13("978-0-743-27356-5");
        book4.setDescription("A tale of the American Dream in the Jazz Age.");
        book4.setLanguage("en");
        book4.setCategories(Arrays.asList("Fiction", "Classic", "American Literature"));
        book4.setPageCount(180);
        book4.setAverageRating(4.3);
        book4.setRatingsCount(15632);
        books.add(book4);

        Book book5 = new Book();
        book5.setId("sample-005");
        book5.setTitle("Pride and Prejudice");
        book5.setAuthors(Arrays.asList("Jane Austen"));
        book5.setPublisher("T. Egerton");
        book5.setPublishedDate("1813-01-28");
        book5.setIsbn10("0141439518");
        book5.setIsbn13("978-0-141-43951-8");
        book5.setDescription("A romantic novel of manners set in Georgian England.");
        book5.setLanguage("en");
        book5.setCategories(Arrays.asList("Fiction", "Romance", "Classic"));
        book5.setPageCount(432);
        book5.setAverageRating(4.6);
        book5.setRatingsCount(11247);
        books.add(book5);

        Book book6 = new Book();
        book6.setId("sample-006");
        book6.setTitle("The Catcher in the Rye");
        book6.setAuthors(Arrays.asList("J.D. Salinger"));
        book6.setPublisher("Little, Brown and Company");
        book6.setPublishedDate("1951-07-16");
        book6.setIsbn10("0316769487");
        book6.setIsbn13("978-0-316-76948-0");
        book6.setDescription("A story about teenage rebellion and alienation.");
        book6.setLanguage("en");
        book6.setCategories(Arrays.asList("Fiction", "Coming of Age", "Classic"));
        book6.setPageCount(277);
        book6.setAverageRating(4.0);
        book6.setRatingsCount(9854);
        books.add(book6);

        Book book7 = new Book();
        book7.setId("sample-007");
        book7.setTitle("One Hundred Years of Solitude");
        book7.setAuthors(Arrays.asList("Gabriel García Márquez"));
        book7.setPublisher("Harper & Row");
        book7.setPublishedDate("1967-05-30");
        book7.setIsbn10("0060883286");
        book7.setIsbn13("978-0-060-88328-8");
        book7.setDescription("A magical realist epic about the Buendía family.");
        book7.setLanguage("en");
        book7.setCategories(Arrays.asList("Fiction", "Magical Realism", "Latin American Literature"));
        book7.setPageCount(417);
        book7.setAverageRating(4.4);
        book7.setRatingsCount(7632);
        books.add(book7);

        Book book8 = new Book();
        book8.setId("sample-008");
        book8.setTitle("The Lord of the Rings");
        book8.setAuthors(Arrays.asList("J.R.R. Tolkien"));
        book8.setPublisher("Allen & Unwin");
        book8.setPublishedDate("1954-07-29");
        book8.setIsbn10("0544003411");
        book8.setIsbn13("978-0-544-00341-5");
        book8.setDescription("An epic high-fantasy trilogy.");
        book8.setLanguage("en");
        book8.setCategories(Arrays.asList("Fiction", "Fantasy", "Adventure"));
        book8.setPageCount(1178);
        book8.setAverageRating(4.8);
        book8.setRatingsCount(25431);
        books.add(book8);

        Book book9 = new Book();
        book9.setId("sample-009");
        book9.setTitle("Harry Potter and the Philosopher's Stone");
        book9.setAuthors(Arrays.asList("J.K. Rowling"));
        book9.setPublisher("Bloomsbury");
        book9.setPublishedDate("1997-06-26");
        book9.setIsbn10("0439708184");
        book9.setIsbn13("978-0-439-70818-8");
        book9.setDescription("The first novel in the Harry Potter series.");
        book9.setLanguage("en");
        book9.setCategories(Arrays.asList("Fiction", "Fantasy", "Young Adult"));
        book9.setPageCount(223);
        book9.setAverageRating(4.9);
        book9.setRatingsCount(32145);
        books.add(book9);

        Book book10 = new Book();
        book10.setId("sample-010");
        book10.setTitle("The Hobbit");
        book10.setSubtitle("or There and Back Again");
        book10.setAuthors(Arrays.asList("J.R.R. Tolkien"));
        book10.setPublisher("Allen & Unwin");
        book10.setPublishedDate("1937-09-21");
        book10.setIsbn10("0547928227");
        book10.setIsbn13("978-0-547-92822-7");
        book10.setDescription("A fantasy adventure novel and prequel to The Lord of the Rings.");
        book10.setLanguage("en");
        book10.setCategories(Arrays.asList("Fiction", "Fantasy", "Adventure"));
        book10.setPageCount(310);
        book10.setAverageRating(4.7);
        book10.setRatingsCount(18934);
        books.add(book10);

        return books;

    }


}
