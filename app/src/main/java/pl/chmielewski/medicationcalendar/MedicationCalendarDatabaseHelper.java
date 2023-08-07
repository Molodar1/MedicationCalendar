package pl.chmielewski.medicationcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedicationCalendarDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "medicationCalendar"; // Nazwa bazy danych
    private static final int DB_VERSION = 1;

    public MedicationCalendarDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        //tworzenie bazy danych
        db.execSQL("CREATE TABLE MEDICAMENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "DOSE_OF_MEDICAMENT REAL, "
                + "DOSE_UNIT TEXT, "
                + "DAILY_DOSING_FREQUENCY INTEGER, "
                + "ADDITIONAL_MEDICAMENT_INFO TEXT);");

        db.execSQL("CREATE TABLE EVENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "EVENT_TIME TEXT, "
                + "EVENT_DATE TEXT, "
                + "EVENT_MESSAGE TEXT);");

        //Dodawanie elementów do tabeli w bazie danych
        insertMedicament(db,"lek1",100,"mg",1,"To jest pół tabletki");
        insertMedicament(db,"lek2",0.5,"tabletki",2,"To paskudztwo, na stawy.");
        insertMedicament(db,"lek3",4,"krople",3,"");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public static void insertMedicament(SQLiteDatabase db, String name, double doseOfMedicament, String doseUnit,int dailyDosingFrequency,
                                         String additionalMedicamentInfo) {
        ContentValues medicamentValues = new ContentValues();
        medicamentValues.put("NAME", name);
        medicamentValues.put("DOSE_OF_MEDICAMENT", doseOfMedicament);
        medicamentValues.put("DOSE_UNIT", doseUnit);
        medicamentValues.put("DAILY_DOSING_FREQUENCY", dailyDosingFrequency);
        medicamentValues.put("ADDITIONAL_MEDICAMENT_INFO", additionalMedicamentInfo);
        db.insert("MEDICAMENT", null, medicamentValues);
    }
    public static void insertEvent(SQLiteDatabase db, String name, String time, String date, String eventMessage) {
        ContentValues medicamentValues = new ContentValues();
        medicamentValues.put("NAME", name);
        medicamentValues.put("EVENT_TIME", time);
        medicamentValues.put("EVENT_DATE", date);
        medicamentValues.put("EVENT_MESSAGE", eventMessage);
        db.insert("EVENT", null, medicamentValues);
    }

    public static void editMedicament(SQLiteDatabase db, String name, double doseOfMedicament, String doseUnit,int dailyDosingFrequency,
                                      String additionalMedicamentInfo,int medicamentId) {
        ContentValues medicamentValues = new ContentValues();
        medicamentValues.put("NAME", name);
        medicamentValues.put("DOSE_OF_MEDICAMENT", doseOfMedicament);
        medicamentValues.put("DOSE_UNIT", doseUnit);
        medicamentValues.put("DAILY_DOSING_FREQUENCY", dailyDosingFrequency);
        medicamentValues.put("ADDITIONAL_MEDICAMENT_INFO", additionalMedicamentInfo);
        db.update( "MEDICAMENT",
                medicamentValues,
                "_id = ?",
        new String[] {Integer.toString(medicamentId)});
        db.close();
    }

}
