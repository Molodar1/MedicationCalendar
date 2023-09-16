package pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ManuallyDeletedMedicamentRepository {
    private ManuallyDeletedMedicamentDao manuallyDeletedMedicamentDao;
    private LiveData<List<ManuallyDeletedMedicament>> manuallyDeletedMedicamentsLiveData;

    public ManuallyDeletedMedicamentRepository(Application application) {
        ManuallyDeletedMedicamentDatabase database = ManuallyDeletedMedicamentDatabase.getInstance(application);
        manuallyDeletedMedicamentDao = database.manuallyDeletedMedicamentDao();
        manuallyDeletedMedicamentsLiveData = manuallyDeletedMedicamentDao.getManuallyDeletedMedicaments();
    }

    public void insert(ManuallyDeletedMedicament medicament) {
        ManuallyDeletedMedicamentDatabase.databaseWriteExecutor.execute(() -> {
            manuallyDeletedMedicamentDao.insert(medicament);
        });
    }

    public void delete(ManuallyDeletedMedicament medicament) {
        ManuallyDeletedMedicamentDatabase.databaseWriteExecutor.execute(() -> {
            manuallyDeletedMedicamentDao.delete(medicament);
        });
    }

    public LiveData<List<ManuallyDeletedMedicament>> getManuallyDeletedMedicamentsLiveData() {
        return manuallyDeletedMedicamentsLiveData;
    }
    public List<ManuallyDeletedMedicament> getManuallyDeletedMedicamentsSync() {
        return manuallyDeletedMedicamentDao.getManuallyDeletedMedicamentsSync();
    }
}