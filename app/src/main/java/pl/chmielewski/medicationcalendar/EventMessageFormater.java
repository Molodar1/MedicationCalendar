package pl.chmielewski.medicationcalendar;

public class EventMessageFormater {


    public static String formatEventMessage(String doseOfMedicament, String doseUnit, String additionalMedicamentInfo) {
        String message= doseOfMedicament + " " + doseUnit + "\n\n"
               + additionalMedicamentInfo;
        return message;
    }
}
