package pl.chmielewski.medicationcalendar.data.alarm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();
    @Transaction
    @Query("SELECT * FROM alarm_table ORDER BY created ASC")
    LiveData<List<Alarm>> getAlarmsWithMedicamentsLiveData();

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);
}
