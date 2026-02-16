package it.mybooks.mybooks.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import it.mybooks.mybooks.data.model.Book;
import it.mybooks.mybooks.utils.Constants;

@Database(entities = {Book.class}, version = Constants.Database.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    Constants.Database.NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
