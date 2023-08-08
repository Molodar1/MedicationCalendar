package pl.chmielewski.medicationcalendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MEDICAMENT")
public class Medicament {
    @ColumnInfo(name = "MEDICAMENT_ID")
    @PrimaryKey(autoGenerate = true)
    int medicamentId;
    @ColumnInfo(name = "MEDICAMENT_NAME")
    private String medicamentName;
    @ColumnInfo(name = "MEDICAMENT_DOSE")
    private double medicamentDose;
    @ColumnInfo(name = "MEDICAMENT_DOSE_UNIT")
    private String medicamentDoseUnit;
    @ColumnInfo(name = "MEDICAMENT_DAILY_DOSING_FREQUENCY")
    private int medicamentDailyDosingFrequency;
    @ColumnInfo(name = "MEDICAMENT_ADDITIONAL_INFO")
    private String medicamentAdditionalInfo;


    public Medicament(String medicamentName, double medicamentDose, String medicamentDoseUnit, int medicamentDailyDosingFrequency, String medicamentAdditionalInfo) {
        this.medicamentName = medicamentName;
        this.medicamentDose = medicamentDose;
        this.medicamentDoseUnit = medicamentDoseUnit;
        this.medicamentDailyDosingFrequency = medicamentDailyDosingFrequency;
        this.medicamentAdditionalInfo = medicamentAdditionalInfo;
    }

    public int getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(int medicamentId) {
        this.medicamentId = medicamentId;
    }

    public String getMedicamentName() {
        return medicamentName;
    }

    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public double getMedicamentDose() {
        return medicamentDose;
    }

    public void setMedicamentDose(double medicamentDose) {
        this.medicamentDose = medicamentDose;
    }

    public String getMedicamentDoseUnit() {
        return medicamentDoseUnit;
    }

    public void setMedicamentDoseUnit(String medicamentDoseUnit) {
        this.medicamentDoseUnit = medicamentDoseUnit;
    }

    public int getMedicamentDailyDosingFrequency() {
        return medicamentDailyDosingFrequency;
    }

    public void setMedicamentDailyDosingFrequency(int medicamentDailyDosingFrequency) {
        this.medicamentDailyDosingFrequency = medicamentDailyDosingFrequency;
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
                "medicamentId=" + medicamentId +
                ", medicamentName='" + medicamentName + '\'' +
                ", medicamentDose=" + medicamentDose +
                ", medicamentDoseUnit='" + medicamentDoseUnit + '\'' +
                ", medicamentDailyDosingFrequency=" + medicamentDailyDosingFrequency +
                ", medicamentAdditionalInfo='" + medicamentAdditionalInfo + '\'' +
                '}';
    }
}
