package pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ManuallyDeletedMedicamentDao {
    @Insert
    void insert(ManuallyDeletedMedicament medicament);

    @Delete
    void delete(ManuallyDeletedMedicament medicament);

    @Query("SELECT * FROM manually_deleted_medicament_table")
    LiveData<List<ManuallyDeletedMedicament>> getManuallyDeletedMedicaments();
    @Query("SELECT * FROM manually_deleted_medicament_table")
    List<ManuallyDeletedMedicament> getManuallyDeletedMedicamentsSync();
}
