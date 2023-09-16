package pl.chmielewski.medicationcalendar.data.medicament;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "medicament_table")
public class Medicament implements Serializable
{
    @PrimaryKey
    @NonNull
    private String medicamentId;
    private String medicamentName;
    private String medicamentDose;
    private String medicamentAdditionalInfo;
    private int medicamentNumberOfDoses;


    public Medicament(String medicamentName, String medicamentDose, String medicamentAdditionalInfo,int medicamentNumberOfDoses) {
        this.medicamentName = medicamentName;
        this.medicamentDose = medicamentDose;
        this.medicamentAdditionalInfo = medicamentAdditionalInfo;
        this.medicamentNumberOfDoses=medicamentNumberOfDoses;
    }

    public Medicament() {

    }

    public int getMedicamentNumberOfDoses() {
        return medicamentNumberOfDoses;
    }

    public void setMedicamentNumberOfDoses(int medicamentNumberOfDoses) {
        this.medicamentNumberOfDoses = medicamentNumberOfDoses;
    }

    public String getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(String medicamentId) {
        this.medicamentId = medicamentId;
    }

    public String getMedicamentName() {
        return medicamentName;
    }

    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public String getMedicamentDose() {
        return medicamentDose;
    }

    public void setMedicamentDose(String medicamentDose) {
        this.medicamentDose = medicamentDose;
    }

    public String getMedicamentAdditionalInfo() {
        return medicamentAdditionalInfo;
    }

    public void setMedicamentAdditionalInfo(String medicamentAdditionalInfo) {
        this.medicamentAdditionalInfo = medicamentAdditionalInfo;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                ", medicamentName='" + medicamentName + '\'' +
                ", medicamentDose=" + medicamentDose +
                ", medicamentNumberOfDoses=" + medicamentNumberOfDoses +
                ", medicamentAdditionalInfo='" + medicamentAdditionalInfo + '\'' +
                '}';
    }
}


