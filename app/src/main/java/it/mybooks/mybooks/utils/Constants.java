package it.mybooks.mybooks.utils;

import it.mybooks.mybooks.BuildConfig;

public class Constants {
    public static class Database {
        public static final String NAME = "mybooks_database";
        public static final int VERSION = 2;
    }

    public static class GoogleBooksApi {
        public static final String BASE_URL = BuildConfig.GOOGLE_BOOKS_BASE_URL;
        public static final String API_KEY = BuildConfig.GOOGLE_BOOKS_API_KEY;
    }

    public static class FirestoreCollections {
        public static final String USERS_COLLECTION = "users";
        public static final String BOOKS_COLLECTION = "saved_books";
    }

    public static class FirebaseAuth {
        public static final String SERVER_CLIENT_ID = BuildConfig.FIREBASE_SERVER_CLIENT_ID;
    }
}
