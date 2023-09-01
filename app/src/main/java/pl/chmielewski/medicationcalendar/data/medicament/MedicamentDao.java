package pl.chmielewski.medicationcalendar.data.medicament;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicamentDao {
    @Insert
    void insert(Medicament medicament);

    @Query("DELETE FROM medicament_table")
    void deleteAll();

    @Query("SELECT * FROM medicament_table ORDER BY medicamentName ASC")
    LiveData<List<Medicament>> getMedicaments();
    @Query("SELECT * FROM medicament_table WHERE medicamentId = :medicamentId LIMIT 1")
    Medicament getMedicamentByIdSync(String medicamentId);

    @Update
    void update(Medicament medicament);

    @Delete
    void delete(Medicament medicament);
}