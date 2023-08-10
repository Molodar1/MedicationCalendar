package pl.chmielewski.medicationcalendar;

public class Medicament
{
    private String medicamentName;

    private String medicamentDose;

    private int medicamentDosingFrequency;
    private String medicamentFrequencyTimeMeasure;

    private String medicamentAdditionalInfo;


    public Medicament(String medicamentName, String medicamentDose, String medicamentFrequencyTimeMeasure, int medicamentDosingFrequency, String medicamentAdditionalInfo) {
        this.medicamentName = medicamentName;
        this.medicamentDose = medicamentDose;
        this.medicamentFrequencyTimeMeasure = medicamentFrequencyTimeMeasure;
        this.medicamentDosingFrequency = medicamentDosingFrequency;
        this.medicamentAdditionalInfo = medicamentAdditionalInfo;
    }

    public Medicament() {

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

    public String getMedicamentFrequencyTimeMeasure() {
        return medicamentFrequencyTimeMeasure;
    }

    public void setMedicamentFrequencyTimeMeasure(String medicamentFrequencyTimeMeasure) {
        this.medicamentFrequencyTimeMeasure = medicamentFrequencyTimeMeasure;
    }

    public int getMedicamentDosingFrequency() {
        return medicamentDosingFrequency;
    }

    public void setMedicamentDosingFrequency(int medicamentDosingFrequency) {
        this.medicamentDosingFrequency = medicamentDosingFrequency;
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
                ", medicamentDoseUnit='" + medicamentFrequencyTimeMeasure + '\'' +
                ", medicamentDailyDosingFrequency=" + medicamentDosingFrequency +
                ", medicamentAdditionalInfo='" + medicamentAdditionalInfo + '\'' +
                '}';
    }
}


