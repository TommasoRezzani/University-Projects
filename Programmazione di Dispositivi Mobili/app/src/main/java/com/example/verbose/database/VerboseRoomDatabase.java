package com.example.verbose.database;

import static android.icu.text.DateFormat.getPatternInstance;
import static com.example.verbose.Constants.DATABASE_NAME;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.verbose.R;
import com.example.verbose.model.Appointment;
import com.example.verbose.model.Category;
import com.example.verbose.model.Notification;
import com.example.verbose.sources.IAppointmentsCallback;
import com.example.verbose.sources.LocalNotificationsDataSource;
import com.example.verbose.ui.mainApp.MainActivity;
import com.example.verbose.util.ServiceLocator;
import com.google.android.material.badge.BadgeDrawable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {Category.class, Notification.class}, version = 1)
public abstract class VerboseRoomDatabase extends RoomDatabase {
    private static final String TAG = VerboseRoomDatabase.class.getSimpleName();

    public abstract CategoriesDAO categoriesDao();
    public abstract NotificationsDAO notificationsDAO();

    private static volatile VerboseRoomDatabase instance;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static VerboseRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (VerboseRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            VerboseRoomDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return instance;
    }
}
