package pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ManuallyDeletedMedicament.class}, version = 1)
public abstract class ManuallyDeletedMedicamentDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static ManuallyDeletedMedicamentDatabase instance;

    public abstract ManuallyDeletedMedicamentDao manuallyDeletedMedicamentDao();

    public static synchronized ManuallyDeletedMedicamentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ManuallyDeletedMedicamentDatabase.class, "manually_deleted_medicament_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}