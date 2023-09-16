package pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "manually_deleted_medicament_table")
public class ManuallyDeletedMedicament {
    @PrimaryKey
    @NonNull
    private String medicamentId;

    public ManuallyDeletedMedicament(@NonNull String medicamentId) {
        this.medicamentId = medicamentId;
    }

    public ManuallyDeletedMedicament() {
    }

    @NonNull
    public String getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(@NonNull String medicamentId) {
        this.medicamentId = medicamentId;
    }

    @Override
    public String toString() {
        return "ManuallyDeletedMedicament{" +
                "medicamentId='" + medicamentId + '\'' +
                '}';
    }
}
