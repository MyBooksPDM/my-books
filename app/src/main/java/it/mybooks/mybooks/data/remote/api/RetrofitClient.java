package it.mybooks.mybooks.data.remote.api;

import java.util.concurrent.TimeUnit;

import it.mybooks.mybooks.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit client singleton for Google Books API
 */
public class RetrofitClient {

    private static final String BASE_URL = BuildConfig.GOOGLE_BOOKS_BASE_URL;

    private static final String API_KEY = BuildConfig.GOOGLE_BOOKS_API_KEY;

    private static RetrofitClient instance;
    private final Retrofit retrofit;
    private final BookApiService googleBooksService;

    /**
     * Private constructor to create Retrofit instance
     */
    private RetrofitClient() {
        // Create logging interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Changed to BODY for more details

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor);

        OkHttpClient okHttpClient = clientBuilder.build();

        // Create Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create service instance
        googleBooksService = retrofit.create(BookApiService.class);
    }

    /**
     * Get singleton instance of RetrofitClient
     *
     * @return RetrofitClient instance
     */
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    /**
     * Get the GoogleBooksService API interface
     *
     * @return GoogleBooksService instance
     */
    public BookApiService getGoogleBooksService() {
        return googleBooksService;
    }
}
