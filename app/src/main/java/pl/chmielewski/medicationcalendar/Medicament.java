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
}
