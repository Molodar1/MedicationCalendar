package pl.chmielewski.medicationcalendar.medicamentsList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.alarmsList.AlarmsListActivity;
import pl.chmielewski.medicationcalendar.authentication.LoginActivity;
import pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament.ManuallyDeletedMedicament;
import pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament.ManuallyDeletedMedicamentRepository;
import pl.chmielewski.medicationcalendar.data.medicament.Medicament;
import pl.chmielewski.medicationcalendar.data.medicament.MedicamentRepository;


public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recview;
    private AdapterShow adapter;
    private SearchView searchView;
    private MedicamentRepository medicamentRepository;
    private ManuallyDeletedMedicamentRepository manuallyDeletedMedicamentRepository;
    private List<ManuallyDeletedMedicament> manuallyDeletedMedicaments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylcer_view);
        setTitle("");
        searchView = findViewById(R.id.searchView);
        manuallyDeletedMedicamentRepository = new ManuallyDeletedMedicamentRepository(getApplication());
        try {
            manuallyDeletedMedicaments = manuallyDeletedMedicamentRepository.getManuallyDeletedMedicamentsSync();
            if (manuallyDeletedMedicaments == null) {
                manuallyDeletedMedicamentRepository.insert(new ManuallyDeletedMedicament("initial"));
            }
        } catch (Exception e) {
        }

        medicamentRepository = new MedicamentRepository(getApplication());

        adapter = new AdapterShow(getApplication());
        recview = findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapter);

        medicamentRepository.getMedicamentsLiveData().observe(this, new Observer<List<Medicament>>() {
            @Override
            public void onChanged(List<Medicament> medicaments) {
                adapter.setMedicamentList(medicaments);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("Medicament")
                            .child(userId);

                    for (Medicament medicament : medicaments) {
                        databaseReference.child(medicament.getMedicamentId()).setValue(medicament);
                    }

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String medicamentId = snapshot.getKey();
                                if (!containsMedicament(medicaments, medicamentId)) {
                                    // Sprawdź, czy wpis istnieje w tabeli ManuallyDeletedMedicament
                                    if (!containsManuallyDeletedMedicament(medicamentId)) {
                                        // Jeżeli wpisu nie ma w tabeli ManuallyDeletedMedicament, zapisz go w lokalnej bazie danych
                                        Medicament medicament = snapshot.getValue(Medicament.class);
                                        if (medicamentRepository.getMedicamentByIdSync(medicament.getMedicamentId()) == null) {
                                            medicamentRepository.insert(medicament);
                                        }


                                    } else {
                                        snapshot.getRef().removeValue();
                                    }


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewEmployee);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.bottom_menu_color));
        Menu menu = bottomNavigationView.getMenu();
        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            logoutItem.setTitle("Wyloguj");
        } else {
            logoutItem.setTitle("Zaloguj");
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_dodaj) {
                    startActivity(new Intent(RecyclerViewActivity.this, AddMedicament.class));
                    return true;
                } else if (itemId == R.id.action_alarms) {
                    startActivity(new Intent(RecyclerViewActivity.this, AlarmsListActivity.class));
                    return true;
                } else if (itemId == R.id.action_logout) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(RecyclerViewActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return true;
                    } else {
                        Intent intent = new Intent(RecyclerViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                    }
                } else
                    return false;
            }
        });
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }

            private void filterList(String query) {
                List<Medicament> filteredList = new ArrayList<>();

                for (Medicament medicament : Objects.requireNonNull(medicamentRepository.getMedicamentsLiveData().getValue())) {
                    if (medicament.getMedicamentName().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(medicament);
                    }
                }

                adapter.setMedicamentList(filteredList);
            }
        });
        adapter = new AdapterShow(getApplication());
        recview.setAdapter(adapter);


    }

    private boolean containsMedicament(List<Medicament> medicaments, String medicamentId) {
        for (Medicament medicament : medicaments) {
            if (medicament.getMedicamentId().equals(medicamentId)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsManuallyDeletedMedicament(String medicamentId) {

        manuallyDeletedMedicaments = manuallyDeletedMedicamentRepository.getManuallyDeletedMedicamentsSync();

        for (ManuallyDeletedMedicament manuallyDeletedMedicament : manuallyDeletedMedicaments) {
            if (manuallyDeletedMedicament.getMedicamentId().equals(medicamentId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        recview.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}