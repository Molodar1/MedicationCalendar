package pl.chmielewski.medicationcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class RecylcerShow extends AppCompatActivity
{
    RecyclerView recview;
    AdapterShow adapter;
    SearchView searchViewa;
FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylcer_show);
        setTitle("");
        searchViewa = findViewById(R.id.searchView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationViewEmployee);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.action_dodaj){
                    startActivity(new Intent(RecylcerShow.this, AddMedicament.class));
                    return true;
                } else if (itemId == R.id.action_logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(RecylcerShow.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
            }else
                    return false;
            }
        });
        searchViewa.clearFocus();
        searchViewa.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
    @Override
    public boolean onQueryTextSubmit(String s){
        return false;
    }
    @Override
    public boolean onQueryTextChange(String s){
        filterList(s);
        return true;
    }

    private void filterList(String s) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Medicament");

        String query = s.toUpperCase(); // Konwertujemy zapytanie na małe litery

        Query filteredQuery = reference.orderByChild("medicineName")
                .startAt(query)
                .endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<Medicament> options =
                new FirebaseRecyclerOptions.Builder<Medicament>()
                        .setQuery(filteredQuery, Medicament.class)
                        .build();
        adapter=new AdapterShow(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }
});

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Medicament> options =
                new FirebaseRecyclerOptions.Builder<Medicament>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Medicament"), Medicament.class)
                .build();
        adapter=new AdapterShow(options);
        recview.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}