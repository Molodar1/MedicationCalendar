package pl.chmielewski.medicationcalendar;

public class Medicament
{
    private String medicamentId;
    private String medicamentName;

    private String medicamentDose;

    private String medicamentAdditionalInfo;


    public Medicament(String medicamentName, String medicamentDose, String medicamentAdditionalInfo) {
        this.medicamentName = medicamentName;
        this.medicamentDose = medicamentDose;
        this.medicamentAdditionalInfo = medicamentAdditionalInfo;
    }

    public Medicament() {

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
                ", medicamentAdditionalInfo='" + medicamentAdditionalInfo + '\'' +
                '}';
    }
}


