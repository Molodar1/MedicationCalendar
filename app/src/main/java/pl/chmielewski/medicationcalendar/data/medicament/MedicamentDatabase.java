package pl.chmielewski.medicationcalendar.data.medicament;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Medicament.class}, version = 1, exportSchema = false)
public abstract class MedicamentDatabase extends RoomDatabase {
    public abstract MedicamentDao medicamentDao();

    private static volatile MedicamentDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MedicamentDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MedicamentDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MedicamentDatabase.class,
                            "medicament_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
