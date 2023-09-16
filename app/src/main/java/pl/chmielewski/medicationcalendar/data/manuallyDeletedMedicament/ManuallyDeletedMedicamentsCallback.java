package pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament;

import java.util.List;

public interface ManuallyDeletedMedicamentsCallback {
    void onSuccess(List<ManuallyDeletedMedicament> medicaments);
    void onFailure(Exception e);
}
