package pl.chmielewski.medicationcalendar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicamentDAO {

    @Insert
    public void addMedicament(Medicament medicament);
    @Update
    public void updateMedicament(Medicament medicament);
    @Delete
    public void deleteMedicament(Medicament medicament);
    @Query("select * from MEDICAMENT")
    public List<Medicament> getAllMedicaments();

    @Query("select * from MEDICAMENT where MEDICAMENT_ID==:medicamentId")
    public Medicament getMedicamentFromDB(int medicamentId);
}
