package it.mybooks.mybooks.remote;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit client singleton for Google Books API
 */
public class RetrofitClient {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/";

    // TODO: Add your Google Books API key here for better rate limits
    // Get one at: https://console.cloud.google.com/apis/credentials
    private static final String API_KEY = null; // Set to your API key or keep null for anonymous access

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

        // Add API key interceptor if key is provided
        if (API_KEY != null && !API_KEY.isEmpty()) {
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("key", API_KEY)
                            .build();

                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

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

    /**
     * Get the Retrofit instance (for advanced usage)
     *
     * @return Retrofit instance
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
