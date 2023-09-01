package pl.chmielewski.medicationcalendar;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.chmielewski.medicationcalendar.alarmslist.AlarmsListActivity;
import pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament.ManuallyDeletedMedicament;
import pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament.ManuallyDeletedMedicamentDatabase;
import pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament.ManuallyDeletedMedicamentRepository;
import pl.chmielewski.medicationcalendar.data.medicament.Medicament;
import pl.chmielewski.medicationcalendar.data.medicament.MedicamentRepository;


public class RecyclerShow extends AppCompatActivity
{
    RecyclerView recview;
    AdapterShow adapter;
    SearchView searchView;
    DatabaseReference databaseReference;
    FloatingActionButton fb;
    private MedicamentRepository medicamentRepository;
    private ManuallyDeletedMedicamentRepository manuallyDeletedMedicamentRepository;
    List<ManuallyDeletedMedicament> manuallyDeletedMedicaments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylcer_show);
        setTitle("");
        searchView = findViewById(R.id.searchView);
        manuallyDeletedMedicamentRepository=new ManuallyDeletedMedicamentRepository(getApplication());
        try {
            manuallyDeletedMedicaments = manuallyDeletedMedicamentRepository.getManuallyDeletedMedicamentsSync();
            if (manuallyDeletedMedicaments==null){
                manuallyDeletedMedicamentRepository.insert(new ManuallyDeletedMedicament("initial"));
            }
        }catch (Exception e){}

        medicamentRepository = new MedicamentRepository(getApplication());

        adapter = new AdapterShow(getApplication());
        recview=findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setAdapter(adapter);

        medicamentRepository.getMedicamentsLiveData().observe(this, new Observer<List<Medicament>>() {
            @Override
            public void onChanged(List<Medicament> medicaments) {
                adapter.setMedicamentList(medicaments);

                // Zapisz elementy z bazy lokalnej do bazy Firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("Medicament")
                            .child(userId);

                    for (Medicament medicament : medicaments) {
                        databaseReference.child(medicament.getMedicamentId()).setValue(medicament);
                    }
                    // Usuń elementy z bazy Firebase, które nie istnieją w bazie lokalnej
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
                                        if (medicamentRepository.getMedicamentByIdSync(medicament.getMedicamentId())==null) {
                                            medicamentRepository.insert(medicament);
                                        }


                                    }else {
                                        snapshot.getRef().removeValue();
                                    }
                                    // Usuń wpis z bazy Firebase

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Obsługa błędu
                        }
                    });
                }
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewEmployee);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.action_dodaj){
                    startActivity(new Intent(RecyclerShow.this, AddMedicament.class));
                    return true;
                } else if (itemId==R.id.action_alarms) {
                    startActivity(new Intent(RecyclerShow.this, AlarmsListActivity.class));
                    return true;
                } else if (itemId == R.id.action_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(RecyclerShow.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }else
                    return false;
            }
        });
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s){
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s){
                filterList(s);
                return true;
            }

            private void filterList(String query) {
                List<Medicament> filteredList = new ArrayList<>();

                for (Medicament medicament : medicamentRepository.getMedicamentsLiveData().getValue()) {
                    if (medicament.getMedicamentName().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(medicament);
                    }
                }

                adapter.setMedicamentList(filteredList);
            }
        });



        FirebaseRecyclerOptions<Medicament> options =
                new FirebaseRecyclerOptions.Builder<Medicament>()
                        .setQuery(databaseReference, Medicament.class)
                        .build();
        adapter=new AdapterShow(getApplication());
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
       // adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // adapter.stopListening();
    }

}