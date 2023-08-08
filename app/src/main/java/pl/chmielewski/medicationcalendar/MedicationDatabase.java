package pl.chmielewski.medicationcalendar;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Medicament.class},version = 1)
public abstract class MedicationDatabase extends RoomDatabase {

    public abstract MedicamentDAO getMedicamentDAO();

}
