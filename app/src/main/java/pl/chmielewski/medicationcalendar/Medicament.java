package pl.chmielewski.medicationcalendar;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Medicament {
    private String name;
    private double doseOfMedicament;
    private String doseUnit;
    private int dailyDosingFrequency;
    private String additionalMedicamentInfo;


//    public String getName() {
//        return name;
//    }
//
//    public double getDoseOfMedicament() {
//        return doseOfMedicament;
//    }
//
//    public String getDoseUnit() {
//        return doseUnit;
//    }
//
//    public int getDailyDosingFrequency() {
//        return dailyDosingFrequency;
//    }
//
//    public String getAdditionalMedicamentInfo() {
//        return additionalMedicamentInfo;
//    }

    @Override
    public String toString() {
        return name;
    }
}
