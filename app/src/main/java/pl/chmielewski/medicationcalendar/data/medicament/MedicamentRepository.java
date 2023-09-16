package pl.chmielewski.medicationcalendar.data.medicament;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MedicamentRepository {
    private MedicamentDao medicamentDao;
    private LiveData<List<Medicament>> medicamentsLiveData;

    public MedicamentRepository(Application application) {
        MedicamentDatabase db = MedicamentDatabase.getDatabase(application);
        medicamentDao = db.medicamentDao();
        medicamentsLiveData = medicamentDao.getMedicaments();
    }

    public void insert(Medicament medicament) {
        MedicamentDatabase.databaseWriteExecutor.execute(() -> {
            medicamentDao.insert(medicament);
        });
    }

    public void update(Medicament medicament) {
        MedicamentDatabase.databaseWriteExecutor.execute(() -> {
            medicamentDao.update(medicament);
        });
    }

    public LiveData<List<Medicament>> getMedicamentsLiveData() {
        return medicamentsLiveData;
    }

    public void delete(Medicament medicament) {
        MedicamentDatabase.databaseWriteExecutor.execute(() -> {
            medicamentDao.delete(medicament);
        });
    }
    public Medicament getMedicamentByIdSync(String medicamentId) {
        return medicamentDao.getMedicamentByIdSync(medicamentId);
    }
}
